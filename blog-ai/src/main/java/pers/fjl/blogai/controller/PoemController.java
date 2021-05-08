package pers.fjl.blogai.controller;

import org.springframework.web.bind.annotation.*;
import pers.fjl.blogai.constant.PoemConstant;
import pers.fjl.blogai.service.PoemService;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/poem")
public class PoemController {
    @Resource
    private PoemService poemService;

    @GetMapping("/randomPoem")
    public Result randomPoem() {
        return new Result(true, MessageConstant.OK, "生成随机古诗成功", poemService.randomPoem(PoemConstant.RandomPoem));
    }

    @GetMapping("/acrosticPoem")
    public Result acrosticPoem(@RequestParam("words") String words) {
        return new Result(true, MessageConstant.OK, "生成藏头古诗成功", poemService.acrosticPoem(words, PoemConstant.AcrosticPoem));
    }

    @GetMapping("/randomPoem2")
    public Result randomPoem2(@RequestParam("words") String words) {
        return new Result(true, MessageConstant.OK, "给出部分信息的情况下，随机生成剩余部分古诗成功", poemService.randomPoem2(words,PoemConstant.RandomPoem2));
    }

}
