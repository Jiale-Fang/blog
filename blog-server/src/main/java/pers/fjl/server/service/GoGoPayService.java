package pers.fjl.server.service;

import pers.fjl.common.vo.NotifyParamsVO;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 支付服务类
 * </p>
 *
 * @author fangjiale
 * @since 2022-05-09
 */
public interface GoGoPayService{

    /**
     *
     * @param notifyParamsVO
     * @param response
     * @return
     */
    String callBackNotify(NotifyParamsVO notifyParamsVO, HttpServletResponse response);

    void pay(String uid, HttpServletResponse response);

}
