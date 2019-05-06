package com.study.project4.com.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//设置swaggerApi的模板
@Controller
@Api("用户模块")
public class helloworld {
    @RequestMapping("hello")
    @ResponseBody
    @ApiOperation(value = "SayHello")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nice()",value = "666"),
            @ApiImplicitParam(name = "test()",value = "777")
    })
    public String hello(){
        return "hello swagger";
    }
}
