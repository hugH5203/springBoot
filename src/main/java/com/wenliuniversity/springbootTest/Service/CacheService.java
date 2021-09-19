package com.wenliuniversity.springbootTest.Service;

import com.wenliuniversity.springbootTest.entity.Department;
import com.wenliuniversity.springbootTest.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *   测试一下springboot的缓存机制，当我们没有导入其他的缓存相关依赖时，默认使用SimpleConfiguration配置类去注册cacheManager,
 * 用它去创建concurrentMapCache缓存组件,它是基于ConcurrentHashMap 实现的缓存，适合单体应用或者开发环境使用，没有持
 * 久性（即当数据过多时不会存储在硬盘里）；
 *   而当我们导入其他的缓存依赖时，就不会使用默认的缓存配置（因为默认的缓存配置类是最低优先级），例如我们如果导入了redis场景则会使用
 * RedisConfiguration去注册redisCacheManager再用它去创建redisCache，通过redisCache操作redis缓存数据，将数据缓存到redis里。
 * redisCache有持久性（当数据过多时会将很久不用的数据存储在硬盘里）。
 * @author HuangHai
 * @date 2021/4/18 20:16
 */
//@CacheConfig(cacheNames = {"department"})//设置缓存属性的一些公共配置，比如说指定缓存组件。用了这个其他cache注解就不用写cacheNames了
@Service//加入到容器里
public class CacheService {

    @Autowired(required = false)
    DepartmentMapper departmentMapper;

    /**
     *    一个应用可以启动多个cacheProvider，一个CacheProvider可以有多个CacheManager，一个CacheManager可以有多个cache，一个cache可以
     * 有多个entry。而entry就是我们单次缓存的数据。entry内部也是一个map结构，key相同，缓存的数据就会被覆盖。每一个entry可以被多个cache
     * 拥有，但是每个cache都是唯一的，只会被一个cacheManager拥有。CacheManager也一样只会被一个CacheProvider拥有。
     *    使用@Cacheable可以对使用的方法进行结果缓存，如果是第一次进行会将结果缓存到用“cacheNames”指定的缓存组件里（这个属性可以是数组，意味
     * 着可以缓存到多组个cache组件里），如果没有这个指定的cache组件，就会在cacheManager里new一个该名字的cache。
     * 每一个@Cacheable必须设置key用来指定是哪一个entry。key相同就会把数据覆盖。
     *    key的默认策略：默认以方法的参数来生成。方法没有参数则会把key设置为0，方法只有一个参数则key为那个唯一的参数，方法有多个参数则key为所有
     * 参数的hashcode码。但是我们可以 使用cacheSpELl表达式来设置：
     * 属性名称	                  描述                         示例
     * methodName              当前方法名                      #root.methodName
     * method                  当前方法                       #root.method.name
     * target                 当前被调用的对象                  #root.target
     * targetClass             当前被调用的对象的class           #root.targetClass
     * args                   当前方法参数组成的数组             #root.args[0]
     * caches                 当前被调用的方法使用的Cache        #root.caches[0].name
     *
     * 示例：  key = "#root.targetClass.simpleName+':'+#root.methodName+':'+#param"
     * 最好还是重写key的生成策略方法：cacheKeyGenerator
     * 其他一些属性：
     * keyGenerator：key的生成策略类，写了这个就不用写key了
     * cacheManager：指定是哪个缓存管理器
     * condition:指定缓存条件，满足什么条件才会进行缓存
     * unless：否定缓存，与condition判断条件刚刚相反。如：#refult==null
     * sync:是否使用异步模式
     *
     * @return
     */

    //@Cacheable注解的key不能用#result.id，因为这个方法会先查缓存，再运行方法，是不可能在查缓存时用得了#result（返回值）的。key与myKeyGenerator二选一
    @Cacheable(cacheNames = {"department"}, keyGenerator ="myKeyGenerator"  /*key = "#root.methodName"*/)
    //请求两次，控制台只打印一次sql语句，说明是从缓存中拿的，记得开启debug的日志，不然看不见打印的sql语句
    public List<Department> findAll() {
        return departmentMapper.findAll();
    }

    //key的命名由我们的自定义生成策略来设置,缓存条件为integer>0
    @Cacheable(cacheNames = {"department"},key = "#integer",condition = "#integer>0")
    public Department findByid(Integer integer) {   //将注解注释掉，测试不缓存，会打印了两次sql语句
        return departmentMapper.findById(integer);
    }

    //@CachePut注解为先调用方法，再更新缓存。被这个注解注释了的方法是一定会调用方法的，而不是只查询缓存
    @CachePut(cacheNames = "department",key = "#department.id")//这里key最好是使用对象的id，或者是#result.id,(返回结果的id)key一定要明确，不然不能覆盖缓存里原来的数据
    public int update( Department department){
//这里应该在mapper接口里写一个update返回值为department的方法的，这里的返回值也应该是department而不是int的，只是我懒得写，毕竟只是测试
        return departmentMapper.updateById(department,department.getId());
    }

    /**
     * 注解@CacheEnvict：清除缓存
     * 其他属性：
     * allEntries=true  指定清除这些缓存组件里的所有key
     * beforeInvocation=false  缓存的清除是否在方法调用前执行，true就是在方法前执行，false在之后执行。默认在方法后执行，意味着出异常就不会清除
     * 最好在方法执行前执行，也就是设置为true
     *
     * @param id
     */
    @CacheEvict(cacheNames = "department",key = "#id") //这里删除指定缓存里指定的key
    public void delete(Integer id){
        departmentMapper.deleteById(id);
    }


    /** @Caching
     *    该注解是一个分组注解，作用是可以同时应用多个其他注解，该注解提供了3个属性cacheable，put，evict分别
     * 用于组合@Cacheable、@CachePut、@CacheEvict三个注解,且都是数组属性，可以配置多个相关注解。
     *    当@Cacheing同时含有CachePut注解和Cacheable注解时，仍然会先执行目标方法。（并不是按@Cacheable的执行过程，
     * 先检查缓存，存在则返回）。
     * @param id
     * @param s
     * @return
     */
    @Caching(
            cacheable = {
                    @Cacheable(cacheNames = "department",key ="#id")
            },
            put = {
                    @CachePut(cacheNames = "department",key = "#id"),
                    @CachePut(cacheNames = "department",key = "#s")
            },
            evict = {
                    @CacheEvict(cacheNames = "department",key = "#s")
            }

    )
    public Department getSome(Integer id,String s){
        return departmentMapper.findById(id);
    }
}
