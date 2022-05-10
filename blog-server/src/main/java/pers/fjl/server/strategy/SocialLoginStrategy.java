package pers.fjl.server.strategy;

import pers.fjl.common.dto.UserInfoDTO;

/**
 * 第三方登录策略
 *
 */
public interface SocialLoginStrategy {

    /**
     * 登录
     *
     * @param data 数据
     * @return {@link UserInfoDTO} 用户信息
     */
    UserInfoDTO login(String data);

}
