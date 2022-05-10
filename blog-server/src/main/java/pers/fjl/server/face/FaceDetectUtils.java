package pers.fjl.server.face;

import com.baidu.aip.util.Base64Util;
import org.json.JSONObject;
import pers.fjl.server.exception.BizException;
import pers.fjl.server.face.constant.FaceApiConfigProperties;
import pers.fjl.server.face.utils.FaceCommonUtils;
import pers.fjl.server.face.utils.FileUtil;
import pers.fjl.server.face.utils.GsonUtils;
import pers.fjl.server.face.utils.HttpUtil;
import pers.fjl.server.face.view.FaceParaVO;
import pers.fjl.server.face.view.FaceResultVO;
import pers.fjl.server.face.view.FaceReturnVO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 类说明： 人脸识别方法类
 */
public class FaceDetectUtils {
    /**
     * @description 人脸注册
     */
    public static FaceResultVO faceAdd(FaceParaVO dto) throws IOException {
        try {
//            String image = Base64Util.encode(FileUtil.readFileByBytes(dto.getFaceImgUrl()));
            Map<String, Object> map = new HashMap<>();
//            map.put("image", image);
            map.put("image", dto.getFaceImgUrl());
            map.put("image_type", "URL");
            map.put("user_id", dto.getUserId());
            map.put("group_id", dto.getGroupId());
            map.put("user_info", dto.getUserInfo());
            String param = GsonUtils.toJson(map);

            // 获取access_token
            FaceAuthService.setFaceAuth();
            String accessToken = FaceApiConfigProperties.TOKEN;

            String result = HttpUtil.post(FaceApiConfigProperties.ADD_URL, accessToken, FaceApiConfigProperties.CONTENT_TYPE, param);

            // 结果处理
            FaceReturnVO resultVO = FaceCommonUtils.getReturnObj(result);
            if ("0".equals(resultVO.getErrorCode())) {  // 没有报错
                return resultVO.getResult();
            } else {
                throw new BizException(resultVO.getErrorMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("上传失败");
        }
    }

    /**
     * @return faceResultVO
     * @description 人脸搜索
     */
    public static FaceResultVO faceSearch(FaceParaVO dto) throws IOException {
        // 比对图片
//        String image = Base64Util.encode(FileUtil.readFileByBytes(dto.getFaceImgUrl()));
        try {
            Map<String, Object> map = new HashMap<>();
//            map.put("image", image);
            map.put("image", dto.getFaceImgUrl());
            map.put("image_type", "URL");
            map.put("group_id_list", dto.getGroupId());
            String param = GsonUtils.toJson(map);

            // 获取access_token
            FaceAuthService.setFaceAuth();
            String accessToken = FaceApiConfigProperties.TOKEN;

            String result = HttpUtil.post(FaceApiConfigProperties.SEARCH_URL, accessToken, FaceApiConfigProperties.CONTENT_TYPE, param);

            // 结果处理
            FaceReturnVO resultVO = FaceCommonUtils.getReturnObj(result);
            if ("0".equals(resultVO.getErrorCode())) {
                return resultVO.getResult();
            } else {
                throw new BizException(resultVO.getErrorMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("搜索人脸失败");
        }
    }

    /**
     * @description 人脸更新
     */
    public static FaceResultVO faceUpdate(FaceParaVO dto) throws IOException {
        String image = Base64Util.encode(FileUtil.readFileByBytes(dto.getFaceImgUrl()));
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", image);
            map.put("image_type", "URL");
            map.put("user_id", dto.getUserId());
            map.put("group_id", dto.getGroupId());
            map.put("user_info", dto.getUserInfo());
            String param = GsonUtils.toJson(map);

            // 获取access_token
            FaceAuthService.setFaceAuth();
            String accessToken = FaceApiConfigProperties.TOKEN;

            String result = HttpUtil.post(FaceApiConfigProperties.UPDATE_URL, accessToken, FaceApiConfigProperties.CONTENT_TYPE, param);

            // 结果处理
            FaceReturnVO resultVO = FaceCommonUtils.getReturnObj(result);
            if ("0".equals(resultVO.getErrorCode())) {
                return resultVO.getResult();
            } else {
                throw new BizException(resultVO.getErrorMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    /**
     * @description 人脸删除
     */
    public static FaceResultVO faceDelete(FaceParaVO dto) throws IOException {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", dto.getUserId());
            map.put("group_id", dto.getGroupId());
            map.put("face_token", faceSearch(dto).getFaceToken());
            String param = GsonUtils.toJson(map);

            // 获取access_token
            FaceAuthService.setFaceAuth();
            String accessToken = FaceApiConfigProperties.TOKEN;

            String result = HttpUtil.post(FaceApiConfigProperties.DELETE_URL, accessToken, FaceApiConfigProperties.CONTENT_TYPE, param);

            // 结果处理
            FaceReturnVO resultVO = FaceCommonUtils.getReturnObj(result);
            if ("0".equals(resultVO.getErrorCode())) {
                return resultVO.getResult();
            } else {
                throw new BizException(resultVO.getErrorMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    /**
     * @description 人脸检测
     */
    public static JSONObject detect(FaceParaVO dto) throws IOException {
        String image = Base64Util.encode(FileUtil.readFileByBytes(dto.getFaceImgUrl()));
        try {
            // 参数字段
            Map<String, Object> map = new HashMap<>();
            map.put("image", image);
            map.put("image_type", "URL");
            map.put("face_field", "faceshape,facetype,age,beauty");
            String param = GsonUtils.toJson(map);

            // 获取access_token
            FaceAuthService.setFaceAuth();
            String accessToken = FaceApiConfigProperties.TOKEN;

            String result = HttpUtil.post(FaceApiConfigProperties.DETECT_URL, accessToken, FaceApiConfigProperties.CONTENT_TYPE, param);

            // 结果处理
            FaceReturnVO resultVO = FaceCommonUtils.getReturnObj(result);
            if ("0".equals(resultVO.getErrorCode())) {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject object = jsonObject.getJSONObject("result");
                return object;
            } else {
                throw new BizException(resultVO.getErrorMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }
}
