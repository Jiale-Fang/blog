package pers.fjl.server;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.AntPathMatcher;
import pers.fjl.common.dto.MenuDTO;
import pers.fjl.common.entity.QueryPageBean;
import pers.fjl.common.po.Blog;
import pers.fjl.common.po.Order;
import pers.fjl.common.po.User;
import pers.fjl.common.po.admin.RoleResource;
import pers.fjl.common.po.admin.UserRole;
import pers.fjl.common.vo.BlogVO;
import pers.fjl.common.vo.CommentVO;
import pers.fjl.common.vo.NotifyParamsVO;
import pers.fjl.common.vo.PayParamsVO;
import pers.fjl.server.dao.OrderDao;
import pers.fjl.server.dao.TypeDao;
import pers.fjl.server.dao.UserDao;
import pers.fjl.server.dao.admin.RoleDao;
import pers.fjl.server.dao.admin.TbRoleResourceDao;
import pers.fjl.server.dao.admin.TbUserRoleDao;
import pers.fjl.server.face.FaceAuthService;
import pers.fjl.server.face.FaceDetectUtils;
import pers.fjl.server.face.view.FaceParaVO;
import pers.fjl.server.face.view.FaceResultVO;
import pers.fjl.server.filter.SensitiveFilter;
import pers.fjl.server.search.index.BlogInfo;
import pers.fjl.server.service.*;
import pers.fjl.server.utils.BeanCopyUtils;
import pers.fjl.server.utils.IpUtils;
import pers.fjl.server.utils.RedisUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static pers.fjl.common.constant.RedisConst.*;

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
    private UserService userService;
    @Resource
    private RoleDao roleDao;
    @Resource
    private TagService tagService;
    @Resource
    private BlogInfoService blogInfoService;
    @Resource
    private ElasticsearchOperations elasticsearchOperations;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SensitiveFilter sensitiveFilter;
    @Resource
    private GoGoPayService goGoPayService;

    //    @Autowired
//    JavaMailSenderImpl mailSender;
//
    @Autowired
    private JavaMailSender javaMailSender;

    private String emailServiceCode;

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private FaceAuthService faceAuthService;
    @Resource
    private OrderDao orderDao;

    /**
     * 邮箱号
     */
    @Value("${spring.mail.username}")
    private String email;

    @Resource
    private HttpServletResponse response;

    @Test
    public void testGoGoPay() {
        Order order = new Order();
        order.setPayId("123132135");
        orderDao.insert(order);
//       NotifyParamsVO notifyParamsVO = new NotifyParamsVO();
//        notifyParamsVO.setPayId("1523677131324878849");
//        notifyParamsVO.setParam("1354330782506385410");
//        notifyParamsVO.setType("1");
//        notifyParamsVO.setPrice("2.88");
//        notifyParamsVO.setReallyPrice("2.88");
//        notifyParamsVO.setSign("2e011761717f4004692d8922a2ce8634");
////        goGoPayService.pay(new PayParamsVO(), response);
//        goGoPayService.callBackNotify(notifyParamsVO,response);
    }

    @Test
    public void testSensitiveWords(){
        String word = "我知道习近平是中国国家主席";
        System.out.println(SensitiveFilter.filter(word));
    }

    @Test
    public void testFace() throws IOException {
        FaceParaVO faceParaVO = new FaceParaVO();
        faceParaVO.setGroupId("tcefrep");
//        faceParaVO.setUserId("3");
        faceParaVO.setFaceImgUrl("https://tcefrep.oss-cn-beijing.aliyuncs.com/face/gem4.jpg");
        FaceResultVO faceResultVO = FaceDetectUtils.faceSearch(faceParaVO);
//        FaceResultVO faceResultVO = FaceDetectUtils.faceAdd(faceParaVO);
        System.out.println("======================-");
        System.out.println(faceResultVO);
    }

    @Test
    public void testSaveViews() {
        blogInfoService.saveViews();
    }

    @Test
    public void testRedis() {
        // 浏览量+1
        redisUtil.zIncr(BLOG_VIEWS_COUNT, 1354747628447477762L, 1D);
        redisUtil.zIncr(BLOG_VIEWS_COUNT, 1354747628447477765L, 1D);
        redisUtil.zIncr(BLOG_VIEWS_COUNT, 1354747628447477762L, 1D);
        // 查询浏览量
        Map<Object, Double> viewsCountMap = redisUtil.zAllScore(BLOG_VIEWS_COUNT);
        System.out.println(viewsCountMap);
        blogInfoService.saveViews();
    }

    @Test
    public void updateDocument() {
        Blog blog = new Blog();
        blog.setBlogId(1502988598260113409L);
        blog.setTitle("333333333333");
        blog.setContent("eeeeeeeeeeeeeee");
        blog.setDescription("ccccccccccccccccc");
        BlogInfo blogInfo = BeanCopyUtils.copyObject(blog, BlogInfo.class);
        elasticsearchOperations.save(blogInfo);
    }

    /**
     * 删除文档信息
     */
    @Test
    public void deleteDocument() {
        try {
            // 创建删除请求对象
            DeleteRequest deleteRequest = new DeleteRequest("blog-search", "_doc", "OvKEgn8Btm9EUK0M8ZZq");
            // 执行删除文档
            DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            log.info("删除状态：{}", response.status());
        } catch (IOException e) {
            log.error("", e);
        }
    }

    @Test
    public void testTags() {
//        List<String> tagNameList = new ArrayList<>();
//        tagNameList.add("Java");
//        tagNameList.add("ccc");
//        List<Tag> tagList = tagNameList.stream().map(item -> Tag.builder()
//                .tagName(item)
//                .build())
//                .collect(Collectors.toList());
//        tagService.saveBatch(tagList);
//        List<Integer> tagIdList = tagList.stream()
//                .map(Tag::getTagId)
//                .collect(Collectors.toList());
//        System.out.println(tagIdList.toString());
//        System.out.println(typeDao.selectList(new LambdaQueryWrapper<Type>()
//                .like(StringUtils.isNotBlank(""), Type::getTypeName, "J")
//                .orderByDesc(Type::getTypeId)));
    }

    @Test
    public void testEmail() {
//        emailServiceCode = "1234";
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(email);
//        message.setTo("zxz1626680@163.com");
//        message.setSubject("验证码");
//        message.setText(emailServiceCode);
//        javaMailSender.send(message);
        userService.sendCode("zxz1626680@163.com");
    }

    @Test
    public void testPermission() {
        AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
        String permission = "GET/comment/1468852694897729537";
        String resource = "GET/comment/**";
        System.out.println(permission.startsWith(resource));
        System.out.println(ANT_PATH_MATCHER.match(permission, resource));
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
            UserRole userRole = new UserRole();
            userRole.setRid(2);
            userRole.setUid(uid);
            tbUserRoleDao.insert(userRole);
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
        Page<BlogVO> homePage = blogService.findHomePage(queryPageBean);
        System.out.println(homePage.getRecords());
    }

    @Test
    public void insertUser() {
        String pass = "123456";
        String hashPass = encoder.encode(pass);
        long uid = IdWorker.getId(User.class);
        User user = new User();
        user.setUid(uid);
        user.setUsername("admin");
        user.setNickname("admin");
        user.setAvatar("https://r.photo.store.qq.com/psc?/V53KcXfb1umonn4HbITu3rINxs43TczD/45NBuzDIW489QBoVep5mccJUo7*q6gaMPZmbFDSW8tjmAm4XwuoUZmMKw3asmvn1mxsE*Tf0fj.VOh2G6OX7v4duFOfedV2oGNQ*GrJEPkA!/r");
        user.setLoginType(1);
        user.setPassword(hashPass);
        user.setStatus(true);
        userDao.insert(user);
        UserRole userRole = new UserRole();
        userRole.setUid(uid);
        userRole.setRid(1);
        tbUserRoleDao.insert(userRole);
    }

    @Test
    public void updateUser() {
        String password = "123456";
        User user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, "aaac"));
        System.out.println(encoder.matches(user.getPassword(), password));
//        userDao.updatePassword(1L, encoder.encode(password));
//        QueryWrapper<User> wrapper = new QueryWrapper<>();
//        wrapper.eq("username", "rosie");
//        User userDB = userDao.selectOne(wrapper);
//        userDB.setNickname("朴彩英2");
//        userDB.setPassword(encoder.encode(password));
//        userDao.updateById(userDB);
    }

    @Test
    public void getCommentList() {
        List<CommentVO> commentList = commentService.getCommentList(1L);
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


