package pers.fjl.server.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户模块
 *
 * @author fangjiale 2021年01月26日
 */

@Api(value = "用户模块", description = "用户模块的接口信息")
@RequestMapping("/user")
@RestController
@CrossOrigin
public class UserController {

}

