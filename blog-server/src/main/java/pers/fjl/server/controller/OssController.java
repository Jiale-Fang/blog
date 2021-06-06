package pers.fjl.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Result UploadOssFile(MultipartFile file) {
        String url = ossService.uploadFileAvatar(file);
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return new Result(true, MessageConstant.OK, "上传成功", map);
    }
}
