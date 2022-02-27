package pers.fjl.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.server.service.OssService;

import javax.annotation.Resource;
import java.util.HashMap;

@Api("文件上传")
@RestController
@RequestMapping("/oss")
@CrossOrigin
public class OssController {

    @Resource
    private OssService ossService;

    //上传头像的方法
    @PostMapping("/userAvatar")
    @ApiOperation(value = "用户上传头像")
    public Result userAvatar(MultipartFile file) {
        String url = ossService.uploadImg(file,"userAvatar");
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return new Result(true, MessageConstant.OK, "上传成功", map);
    }

    //上传文章内容图片的方法
    @PostMapping("/articleImage")
    @ApiOperation(value = "用户上传文章图片")
    public Result articleImage(MultipartFile file) {
        String url = ossService.uploadImg(file,"articleImage");
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return new Result(true, MessageConstant.OK, "上传成功", map);
    }

    //上传文章内容图片的方法
    @PostMapping("/chatLogImg")
    @ApiOperation(value = "聊天发送图片")
    public Result chatLogImg(MultipartFile file) {
        String url = ossService.uploadImg(file,"chatLogImg");
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return new Result(true, MessageConstant.OK, "上传成功", map);
    }

}
