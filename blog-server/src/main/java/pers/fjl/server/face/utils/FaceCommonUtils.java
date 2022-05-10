package pers.fjl.server.face.utils;

import com.alibaba.fastjson.JSONObject;
import pers.fjl.server.face.view.FaceReturnVO;

/**
 * @author -china.com
 * @version 1.0
 * @ClassName FaceCommonUtils
 * 创建时间：2021/8/19 16:20
 * 类说明： 人脸识别工具类
 */
public class FaceCommonUtils {
    /**
     * @return
     * @description 人脸识别结果处理方法
     * @author
     * @date 2021/8/19 17:02
     * @params
     * @version 1.0
     */
    public static FaceReturnVO getReturnObj(String resultStr) {
        resultStr = resultStr.replaceAll("_c", "C").replaceAll("_m", "M")
                .replaceAll("_l", "L").replaceAll("_i", "I")
                .replaceAll("_t", "T");

        // json字符串转对象
        return JSONObject.parseObject(resultStr, FaceReturnVO.class);
    }
}
