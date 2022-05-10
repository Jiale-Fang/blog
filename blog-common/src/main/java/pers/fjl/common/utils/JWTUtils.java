package pers.fjl.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;


public class JWTUtils {
    private static final String SING = "!@*(^*#sfdf&*$asdh$F&^";

    /**
     * 生成token
     *
     * @param map
     * @return
     */
    public static String getToken(Map<String, String> map) {

        //设置过期时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);//默认七天过期

        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();

        //payload键值
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

        String token =builder.withExpiresAt(instance.getTime())//令牌过期时间
                .sign(Algorithm.HMAC256(SING));//签名

        return token;
    }

    /**
     * 验证token
     */
    public static void verify(String token) {
        try{
            JWT.require(Algorithm.HMAC256(SING)).build().verify(token);//没抛出异常就通过了
        }catch (Exception e){
            throw new RuntimeException("token生成失败，您的账号密码输入错误");
        }
    }

    /**
     * 获取token信息方法
     */
    public static DecodedJWT getTokenInfo(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
        return verify;
    }

}
