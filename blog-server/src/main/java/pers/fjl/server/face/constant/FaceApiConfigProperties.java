package pers.fjl.server.face.constant;

import lombok.Data;

/**
 * 人脸配置属性
 *
 */
@Data
public class FaceApiConfigProperties {
    // 人脸识别API验证信息
    public static final String API_ID = "25932549";

    public static final String API_KEY = "hy7clCr9NsuZp1pHXCMQEEXH";

    public static final String SECRET_KEY = "0b9qcIFHyfUQg4VTMSGzFvwsGDXMifdU";

    public static String TOKEN = "";

    public static final String GROUP_ID = "tcefrep";

    // (value = "请求类型")
    public static final String CONTENT_TYPE = "application/json";

    // 人脸识别API
    // (value = "人脸添加API")
    public static final String ADD_URL = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
    // (value = "人脸更新API")
    public static final String UPDATE_URL = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/update";
    // (value = "人脸删除API")
    public static final String DELETE_URL = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/face/delete";
    // (value = "人脸搜索API")
    public static final String SEARCH_URL = "https://aip.baidubce.com/rest/2.0/face/v3/search";
    // (value = "人脸检测API")
    public static final String DETECT_URL = "https://aip.baidubce.com/rest/2.0/face/v3/detect";
}
