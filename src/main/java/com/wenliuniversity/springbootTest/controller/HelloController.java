package com.wenliuniversity.springbootTest.controller;

import com.wenliuniversity.springbootTest.entity.Man;
import com.wenliuniversity.springbootTest.exception.MyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HuangHai
 * @date 2021/4/6 12:05
 */
@Controller
@SessionAttributes( value = {"language","status"})
public class HelloController {

    @ResponseBody
    @RequestMapping("man")
    public Man hello(Man man){
        return man;
    }

    @RequestMapping("success")
    public String success(){
        return "success";
    }

    @RequestMapping("test")
    public String testPage(Map<String ,List> map){
        List<String> mans = Arrays.asList("胖虎", "大雄", "小夫");
        map.put("mans",mans);
        return "testpage";
    }

    /**
     * 用于登录注册问题
     * @param username
     * @param password
     * @param map
     * @return
     */
    @RequestMapping(value = "login",method = {RequestMethod.GET,RequestMethod.POST})
    public String login(@RequestParam(name = "userName") String username,
                        @RequestParam(name="passWord") String password, Map<String,Object> map) {
        if (username!=null && "123".equals(password)){
            map.put("status","登录成功！");
            return "redirect:/goThere";
        }else {
            map.put("tips","密码或用户名错误");
        }
        return "index";
    }

    /**
     * 用于国际化处理
     * @param language
     * @param map
     * @return
     */
    @RequestMapping("index")
    public String index(@RequestParam(name = "language",required = false) String language,Map<String,Object> map){
        map.put("language",language);
        return "index";
    }


    @RequestMapping("todashboard")
    public String todashboard(){
        return "dashboard";
    }

    /**
     * 测试一下ModelAndView
     * @return
     */
    @RequestMapping("goThere")
    public ModelAndView goThere(){
        ModelAndView modelAndView = new ModelAndView("dashboard");
        return modelAndView ;
    }

    /**
     * 测试异常类与异常页面
     * @return
     */
    @RequestMapping("exception")
    @ResponseBody
    public String exception(@RequestParam(name = "name") String name){
        if (!"huang".equals(name)){
            throw new MyException();  //抛出异常
        }
        return "No Problem!";
    }


    /**
     * 测试map在方法参数能不能作为request域传递值。（模板中获取到该map的值）
     * 由测试得出方法参数的map是完全可以作为request域对象的
     * @param map
     * @return
     */
    @GetMapping("mapInParams")
    public String testMethodParams(Map<String,Object> map){
        map.put("mapAttribute","方法参数中可获取");
        return "testMapAttribute";
    }

    /**
     * 测试map在方法体能不能作为request域传递值。（模板中获取不到map的该值）
     * 由测试得出方法体的map是完全不能作为request域对象的
     * @param
     * @return
     */
    @GetMapping("mapInMethod")
    public String testInMethod(){
        Map<String, Object> map = new HashMap
                <String, Object>();
        map.put("mapAttribute","方法体中可获取");
        return "testMapAttribute";
    }


    /**
     * 测试request与方法参数里的map同时存在时会不会有其他情况出现，
     * 没什么其他问题，谁在后面谁覆盖前者的值
     * @param request
     * @param map
     * @return
     */
    @GetMapping("mapAndRequest")
    public String mapAndRequest(HttpServletRequest request,Map<String,Object> map ){
        request.setAttribute("mapAttribute","有request则map域无作用");
        map.put("mapAttribute","有request但是map域照样覆盖");
        return "testMapAttribute";
    }
}
