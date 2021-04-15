package pers.fjl.server;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Comment;
import pers.fjl.common.po.User;
import pers.fjl.common.vo.BlogVo;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.service.BlogService;
import pers.fjl.server.service.CommentService;
import pers.fjl.server.service.ReportService;
import pers.fjl.server.service.UserService;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
@Slf4j
public class MybatisPlusApplicationTest {
    @Resource
    private UserDao userDao;
    @Resource
    private CommentService commentService;
    @Resource
    private BCryptPasswordEncoder encoder;
    @Resource
    private BlogService blogService;
    @Resource
    private ReportService reportService;

    @Test
    public void getReport() throws Exception {
        Map<String, Object> report = reportService.getReport(1354747628447477762L);
        System.out.println(report);
    }

    @Test
    public void getBlogHomePage() {
        QueryPageBean queryPageBean = new QueryPageBean();
        queryPageBean.setCurrentPage(1);
        queryPageBean.setPageSize(3);
        queryPageBean.setQueryString("页面展示如下");
        Page<BlogVo> homePage = blogService.findHomePage(queryPageBean);
        System.out.println(homePage.getRecords());
    }

    @Test
    public void insertUser() {
        String pass = "aa";
        String hashPass = encoder.encode(pass);
        User user = new User();
        user.setUsername("fjl");
        user.setNickname("方嘉乐");
        user.setPassword(hashPass);
        user.setDataStatus(true);
        userDao.insert(user);
    }

    @Test
    public void updateUser() {
        String password = "123456";
        userDao.updatePassword(1L,encoder.encode(password));
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.eq("username", "rosie");
//        User userDB = userDao.selectOne(wrapper);
//        userDB.setNickname("朴彩英2");
//        userDB.setPassword(encoder.encode(password));
//        userDao.updateById(userDB);
    }

    @Test
    public void getCommentList() {
        List<Comment> commentList = commentService.getCommentList(1L);
        System.out.println(commentList);
    }

    /**
     * 递归删除评论
     */
    @Test
    public void delComment() {
        commentService.delComment(1354977782910414850L, 1355503370591002626L, 84351321231233L);
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

    @Test
    public void isImagesTrue() throws IOException {
        int max=1000;
        int min=1;
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        String picUrl = "https://unsplash.it/100/100?image="+s;
        System.out.println(picUrl);
    }

}


