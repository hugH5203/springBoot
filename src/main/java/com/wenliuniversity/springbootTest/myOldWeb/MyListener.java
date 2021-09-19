package com.wenliuniversity.springbootTest.myOldWeb;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**  listener，web应用的启动与销毁时会触发相应的方法
 * 由于springboot是以jar包的形式启动它内置的servlet容器，然后由此来启动springboot的web应用，所以他是没有web.xml与三大组件的，
 *  * 但是我们可以自己注册回来，springboot默认有相关的自动配置类，我们只需要注册相关的bean到spring容器即可
 * @author HuangHai
 * @date 2021/4/15 17:49
 */
@WebListener
public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("web应用启动");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("web应用销毁");

    }
}
