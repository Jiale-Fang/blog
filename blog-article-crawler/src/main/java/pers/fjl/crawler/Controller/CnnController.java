package pers.fjl.crawler.Controller;

import org.springframework.web.bind.annotation.*;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.crawler.po.CrawledBlog;
import pers.fjl.crawler.service.CnnService;

import javax.annotation.Resource;
import java.util.Map;

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
}
