package pers.fjl.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.fjl.common.po.Comment;
import pers.fjl.common.po.User;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.service.CommentService;
import pers.fjl.server.service.UserService;
import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class MybatisPlusApplicationTest {
    @Resource
    private UserDao userDao;
    @Resource
    private CommentService commentService;
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

    @Test
    public void getCommentList(){
        List<Comment> commentList = commentService.getCommentList(1L);
        System.out.println(commentList);
    }

    /**
     * 递归删除评论
     */
    @Test
    public void delComment(){
        commentService.delComment(1354977782910414850L,1355503370591002626L,84351321231233L);
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


