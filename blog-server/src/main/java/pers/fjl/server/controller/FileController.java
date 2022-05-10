package pers.fjl.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.common.vo.VoiceVO;
import pers.fjl.server.chatroom.ChatEndpoint;
import pers.fjl.server.service.OssService;
import pers.fjl.server.strategy.context.UploadStrategyContext;

import javax.annotation.Resource;
import java.util.HashMap;

@Api("文件上传")
@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {

    @Autowired
    private UploadStrategyContext uploadStrategyContext;
    @Resource
    private ChatEndpoint chatEndpoint;

    //上传头像的方法
    @PostMapping("/userAvatar")
    @ApiOperation(value = "用户上传头像")
    public Result userAvatar(MultipartFile file) {
        String url = uploadStrategyContext.executeUploadStrategy(file,"userAvatar/");
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return Result.ok("上传成功", map);
    }

    @PostMapping("/articleImage")
    @ApiOperation(value = "用户上传文章图片")
    public Result articleImage(MultipartFile file) {
        String url = uploadStrategyContext.executeUploadStrategy(file,"articleImage/");
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return Result.ok("上传成功", map);
    }

    @PostMapping("/chatLogImg")
    @ApiOperation(value = "聊天发送图片")
    public Result chatLogImg(MultipartFile file) {
        String url = uploadStrategyContext.executeUploadStrategy(file,"chatLogImg/");
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return Result.ok("上传成功", map);
    }

    @PostMapping("/face/local")
    @ApiOperation(value = "人脸识别上传本地图片")
    public Result faceLocal(MultipartFile file) {
        String url = uploadStrategyContext.executeUploadStrategy(file,"face/local/");
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return Result.ok("上传成功", map);
    }

    @PostMapping("/face/camera")
    @ApiOperation(value = "人脸识别上传本地图片")
    public Result faceCamera(MultipartFile file) {
        String url = uploadStrategyContext.executeUploadStrategy(file, "face/camera/");
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return Result.ok("上传成功", map);
    }

    /**
     * 保存语音信息
     *
     * @param voiceVO 语音信息
     * @return {@link Result<String>} 语音地址
     */
    @ApiOperation(value = "上传语音")
    @PostMapping("/admin/voice")
    public Result sendVoice(VoiceVO voiceVO) {
        return Result.ok("上传成功", chatEndpoint.sendVoice(voiceVO));
    }
}
