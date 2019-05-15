package com.study.project4.config;

import com.study.project4.Until.HandlerInterceptor.AdminLoginHandlerInterceptor;
import com.study.project4.Until.HandlerInterceptor.LoginHandlerInterceptor;
import com.study.project4.Until.HandlerInterceptor.StudentLoginHandlerInterceptor;
import com.study.project4.Until.HandlerInterceptor.TeacherLoginHandlerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@Slf4j //新版本似乎可有可无
@EnableWebMvc  //新版本似乎可有可无
class WebMvcConfig implements WebMvcConfigurer {


    @Override
    // 这个方法是用来配置静态资源的，比如html，js，css，等等
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        //配置静态资源，不写css就无法访问到
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }


    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //SpringBoot2.0以上需要排除静态资源，不然静态资源会被拦截。
        //excludePathPatterns是排除的请求，addPathPatterns是要被拦截的请求。

        //拦截未登录用户
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/static/**","/studentLogin","/index.html","/",
                        "/teacherLogin","/adminLogin","/getKaptchaImage",
                        "/ifLoginStudent","/ifLoginTeacher","/ifLoginAdmin",
                        "/student/login","/teacher/login","/admin/login");

        //拦截非学生用户
        registry.addInterceptor(new StudentLoginHandlerInterceptor()).addPathPatterns("/students/**","/studentMain");

        //拦截非教师用户
        registry.addInterceptor(new TeacherLoginHandlerInterceptor()).addPathPatterns("/teachers/**","/teacherMain");

        //拦截非管理员用户
        registry.addInterceptor(new AdminLoginHandlerInterceptor()).addPathPatterns("/adminMain","/Admin/**");

    }



    @Override
    //使用其他链接访问页面
    public void addViewControllers(ViewControllerRegistry registry) {
        //学生登录界面
        registry.addViewController("/studentLogin").setViewName("studentlogin");
        //主页
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        //教师登录界面
        registry.addViewController("/teacherLogin").setViewName("teacherlogin");
        //管理员登录界面
        registry.addViewController("/adminLogin").setViewName("managementlogin");
        //成绩表
        registry.addViewController("/students/grade").setViewName("students/grade");
    }

}
