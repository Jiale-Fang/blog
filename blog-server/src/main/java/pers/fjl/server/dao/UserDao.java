package pers.fjl.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import pers.fjl.common.po.User;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fangjiale
 * @since 2021-01-26
 */
@Repository
public interface UserDao extends BaseMapper<User> {

    @Select("UPDATE user set password = #{password} where username = 'test1' ")
    void updatePassword(Long uid, String password);
}
