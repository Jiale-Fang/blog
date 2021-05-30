package pers.fjl.crawler.controller;

import org.springframework.web.bind.annotation.*;
import pers.fjl.crawler.constant.MessageConstant;
import pers.fjl.crawler.entity.Result;
import pers.fjl.crawler.po.CrawledBlog;
import pers.fjl.crawler.service.CnnService;

import javax.annotation.Resource;

/**
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/cnn")
public class CnnController {
    @Resource
    private CnnService cnnService;

    @PostMapping(value = "/textClassify")
    public Result textClassify(@RequestBody CrawledBlog crawledBlog) {
        System.out.println(crawledBlog.getDescription());
        return new Result(true, MessageConstant.OK, "请求成功", cnnService.textClassify(crawledBlog.getDescription()));
    }

    @GetMapping(value = "/test")
    public Result test() {
        return new Result(true, MessageConstant.OK, "请求成功","这是一个测试接口");
    }

}
