package com.wenliuniversity.springbootTest.myOldWeb;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**    servlet
 * 由于springboot是以jar包的形式启动它内置的servlet容器，然后由此来启动springboot的web应用，所以他是没有web.xml与三大组件的，
 * 但是我们可以自己注册回来，springboot默认有相关的自动配置类，我们只需要注册相关的bean到spring容器即可
 * @author HuangHai
 * @date 2021/4/15 15:19
 */
@WebServlet("/myServlet")//（注解注册可用，即在入口类上写@ServletComponentScan就可以不需代码注册）由于老式的servlet在这里是注册的，所以映射这个servlet的虚拟路径也必须到注册Bean的哪个方法与类里去配置
public class MyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().write("hello,我自己注册的servlet");
    }
}
