package com.bt.ms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bt.ms.common.vo.RespBean;
import com.bt.ms.pojo.User;
import com.bt.ms.vo.LoginVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bt
 * @since 2022-06-22
 */
public interface IUserService extends IService<User> {

    public RespBean doLogin(LoginVo loginVo);
}
