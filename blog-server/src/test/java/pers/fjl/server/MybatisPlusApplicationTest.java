package pers.fjl.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.fjl.common.po.User;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.service.UserService;
import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class MybatisPlusApplicationTest {
    @Resource
    private UserDao userDao;
    @Resource
    private UserService userService;
    @Resource
    private BCryptPasswordEncoder encoder;

    @Test
    public void insertUser() {
        String pass = "kllxdwh";
        String hashPass = encoder.encode(pass);
        User user = new User();
        user.setUsername("fjl");
        user.setNickname("方嘉乐");
        user.setPassword(hashPass);
        user.setDataStatus(true);
        userDao.insert(user);
    }


    /**
     * 更新用户表密码
     */
//    @Test
//    public void updatePassword() {
//        String pass = "123456";
//        String hashPass = encoder.encode(pass);
////        for (int i = 24; i <= 25; i++) {
//            userDao.updatePassword("145373837323121238", hashPass);
////        }
//    }
}


