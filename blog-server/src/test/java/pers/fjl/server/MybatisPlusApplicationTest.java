package pers.fjl.server;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import pers.fjl.common.dto.MenuDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.User;
import pers.fjl.common.po.admin.RoleResource;
import pers.fjl.common.po.admin.UserRole;
import pers.fjl.common.vo.BlogVo;
import pers.fjl.common.vo.CommentVo;
import pers.fjl.server.dao.TypeDao;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.dao.admin.RoleDao;
import pers.fjl.server.dao.admin.TbRoleResourceDao;
import pers.fjl.server.dao.admin.TbUserRoleDao;
import pers.fjl.server.service.*;
import pers.fjl.server.utils.IpUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

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
    @Resource
    private BlogService blogService;
    @Resource
    private ReportService reportService;
    @Resource
    private TbRoleResourceDao tbRoleResourceDao;
    @Resource
    private TbUserRoleDao tbUserRoleDao;
    @Resource
    private TypeDao typeDao;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleDao roleDao;
//    @Autowired
//    JavaMailSenderImpl mailSender;
//
//    @Autowired
//    private JavaMailSender javaMailSender;

    private String emailServiceCode;

    /**
     * 邮箱号
     */
    @Value("${spring.mail.username}")
    private String email;

    @Test
    public void testEmail(){
        emailServiceCode = "1234";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo("1901582757@qq.com");
        message.setSubject("验证码");
        message.setText(emailServiceCode);
//        javaMailSender.send(message);
    }

    @Test
    public void testPermission(){
        String permission = "GET/comment/1468852694897729537";
        String resource= "GET/comment/";
        System.out.println(permission.startsWith(resource));
    }

    /**
     * 解析ip地址
     *
     * @return 解析后的ip地址
     */
    @Test
    public void getIpSource() {

        List<User> userList = userDao.selectList(null);
        userList.forEach(user -> {
            String ipSource = IpUtils.getIpSource(user.getLastIp());
            user.setIpSource(ipSource);
            userDao.updateById(user);
        });
    }

    @Test
    public void testListMenus() {
        List<MenuDTO> menuDTOS = menuService.listMenus();
        System.out.println(menuDTOS.toString());
    }

    /**
     * 插入角色拥有的权限
     */
    @Test
    public void insertRoleResource() {
        for (int i = 276; i <= 315; i++) {
//            if (i=)
//                continue;
            RoleResource roleResource = new RoleResource();
            roleResource.setRid(3);
            roleResource.setResourceId(i);
            tbRoleResourceDao.insert(roleResource);
        }
    }

    /**
     * 插入用户拥有的角色
     */
    @Test
    public void insertUserRole() {
        List<User> userList = userDao.selectList(null);
        for (User user : userList) {
            Long uid = user.getUid();
            if (uid != 1354747628447477762L && uid != 1482631120095899649L) {
                UserRole userRole = new UserRole();
                userRole.setRid(2);
                userRole.setUid(uid);
                tbUserRoleDao.insert(userRole);
            }
        }
    }

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
        userDao.updatePassword(1L, encoder.encode(password));
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.eq("username", "rosie");
//        User userDB = userDao.selectOne(wrapper);
//        userDB.setNickname("朴彩英2");
//        userDB.setPassword(encoder.encode(password));
//        userDao.updateById(userDB);
    }

    @Test
    public void getCommentList() {
        List<CommentVo> commentList = commentService.getCommentList(1L);
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
        int max = 1000;
        int min = 1;
        Random random = new Random();

        int s = random.nextInt(max) % (max - min + 1) + min;
        String picUrl = "https://unsplash.it/100/100?image=" + s;
        System.out.println(picUrl);
    }

    @Test
    public void testTimeUtils() {
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime ldt1 = LocalDateTime.parse("2021-06-07 22:27:06", df);
//        LocalDateTime ldt2 = LocalDateTime.parse("2021-06-07 22:28:06", df);
////        String res = formatTime(ldt);
////        System.out.println(res);
//        Long currentTime1 = ldt1.toInstant(ZoneOffset.of("+8")).toEpochMilli();
//        Long currentTime2 = ldt2.toInstant(ZoneOffset.of("+8")).toEpochMilli();
//        System.out.println(currentTime1);
//        System.out.println(currentTime2);

        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("b")) {
                list.add(i, "找到了");
                i++;
            }
            System.out.println(list.get(i));
        }
//        list.add(2, "f");
        System.out.println("==" + list);
    }

    @Test
    public void testUrl() {
        String uri = "/server/user/getUserList";
        String result = uri.substring(uri.indexOf("/server") + 7);
        System.out.println(result);
    }
}


