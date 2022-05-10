package pers.fjl.server.controller.admapi;

import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.dto.UserInfoDTO;
import pers.fjl.common.entity.Result;
import pers.fjl.common.po.User;
import pers.fjl.common.utils.JWTUtils;
import pers.fjl.common.vo.QQLoginVO;
import pers.fjl.common.vo.UserVO;
import pers.fjl.common.vo.WeiboLoginVO;
import pers.fjl.server.annotation.IpRequired;
import pers.fjl.server.face.view.FaceRegisterVO;
import pers.fjl.server.service.ResourceService;
import pers.fjl.server.service.UserService;
import pers.fjl.server.utils.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static pers.fjl.common.constant.RedisConst.*;


/**
 * <p>
 * 信息管理控制器
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-27
 */
@CrossOrigin
@RestController
@Slf4j
@Api(value = "信息管理模块", description = "管理用户信息验证")
public class IndexAdminController {
    @Resource
    private UserService userService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ResourceService resourceService;

    @ApiOperation(value = "普通用户登录接口")
    @PostMapping("/admapi/login")
    @IpRequired
    public Result login(@RequestBody UserVO userVO, HttpServletRequest request) {
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        userService.verifyCode(userVO.getVerKey(), userVO.getCode()); // 验证如果不通过，后台直接抛异常
        log.info("用户名:[{}]", user.getUsername());
        request.getSession().setAttribute("username", user.getUsername());   // 给websocket取出
        log.info("密码:[{}]", user.getPassword());
        try {
            //认证成功，生成jwt令牌
            user.setLastIp((String) request.getAttribute("host"));
            User userDB = userService.login(user);
            HashMap<String, String> payload = new HashMap<>();
            payload.put("id", String.valueOf(userDB.getUid()));
            payload.put("lastIp", userDB.getLastIp());
            payload.put("username", userDB.getUsername());
            String token = JWTUtils.getToken(payload);
            redisUtil.set(TOKEN_ALLOW_LIST + userDB.getUid(), token, HOUR);   // token设置白名单，因此可以管理token的有效期
            HashMap<String, Object> userInfo = new HashMap<>();
            userInfo.put("token", token);
            userInfo.put("user", userDB);
            return Result.ok("token生成成功", userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("token生成失败,请检查你的账号与密码是否匹配");
    }

    @ApiOperation(value = "验证码")
    @RequestMapping("/admapi/captcha")
    public Result captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        // 存入redis并设置过期时间为10分钟
        redisUtil.set(USER_CODE_KEY + key, verCode, 600);
        request.getSession().setAttribute("CAPTCHA", verCode);  //存入session
        HashMap<String, Object> code = new HashMap<>();
        code.put("verKey", key);
        code.put("verCode", specCaptcha.toBase64());
        // 将key和base64返回给前端
        return Result.ok(MessageConstant.VERIFICATION_CODE_SUCCESS, code);
    }

    @ApiOperation(value = "注销登录接口")
    @PostMapping("/admapi/logout")
    public Result logout() {
        return Result.ok("登出成功");
    }

    /**
     * qq登录
     *
     * @param qqLoginVO qq登录信息
     * @return {@link Result} 用户信息
     */
    @ApiOperation(value = "qq登录")
    @PostMapping("/user/oauth/qq")
    public Result qqLogin(@Valid @RequestBody QQLoginVO qqLoginVO) {
        UserInfoDTO userInfoDTO = userService.qqLogin(qqLoginVO);
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", userService.getToken(userInfoDTO));
        userInfo.put("user", userInfoDTO);
        redisUtil.set(TOKEN_ALLOW_LIST + userInfoDTO.getUid(), userService.getToken(userInfoDTO), HOUR);   // token设置白名单，因此可以管理token的有效期
        return Result.ok("qq登录成功", userInfo);
    }

    /**
     * 微博登录
     *
     * @param weiBoLoginVO 微博登录信息
     * @return {@link Result} 用户信息
     */
    @ApiOperation(value = "微博登录")
    @PostMapping("/user/oauth/weibo")
    public Result weiboLogin(@Valid @RequestBody WeiboLoginVO weiBoLoginVO) {
        UserInfoDTO userInfoDTO = userService.weiboLogin(weiBoLoginVO);
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", userService.getToken(userInfoDTO));
        userInfo.put("user", userInfoDTO);
        redisUtil.set(TOKEN_ALLOW_LIST + userInfoDTO.getUid(), userService.getToken(userInfoDTO), HOUR);   // token设置白名单，因此可以管理token的有效期
        return Result.ok("微博登录成功", userInfo);
    }

    /**
     * 人脸识别登录
     *
     * @param faceRegisterVO 人脸图片信息
     * @return {@link Result} 用户信息
     */
    @ApiOperation(value = "上传人脸图片登录")
    @PostMapping("/user/oauth/face")
    public Result faceLogin(@Valid @RequestBody FaceRegisterVO faceRegisterVO, HttpServletRequest request) throws IOException {
        UserInfoDTO userInfoDTO = userService.faceLogin(faceRegisterVO, request);
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", userService.getToken(userInfoDTO));
        userInfo.put("user", userInfoDTO);
        redisUtil.set(TOKEN_ALLOW_LIST + userInfoDTO.getUid(), userService.getToken(userInfoDTO), HOUR);   // token设置白名单，因此可以管理token的有效期
        return Result.ok("人脸识别登录成功", userInfo);
    }

    @PostMapping("/user/oauth/facePhoto")
    @ApiOperation(value = "人脸识别拍照登录")
    public Result facePhotoLogin(MultipartFile file, HttpServletRequest request) throws IOException {
        UserInfoDTO userInfoDTO = userService.facePhotoLogin(file, request);
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", userService.getToken(userInfoDTO));
        userInfo.put("user", userInfoDTO);
        redisUtil.set(TOKEN_ALLOW_LIST + userInfoDTO.getUid(), userService.getToken(userInfoDTO), HOUR);   // token设置白名单，因此可以管理token的有效期
        return Result.ok("人脸识别登录成功", userInfo);
    }
}

