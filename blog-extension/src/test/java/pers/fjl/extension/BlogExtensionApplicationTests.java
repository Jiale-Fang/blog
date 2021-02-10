package pers.fjl.extension;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.fjl.extension.po.Link;
import pers.fjl.extension.service.LinkService;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class BlogExtensionApplicationTests {
    @Resource
    private LinkService linkservice;

    @Test
    public void contextLoads() {
    }

    @Test
    public void getLinkList(){
        System.out.println(linkservice.getLinkList());
    }

    /**
     * 测试添加友链
     */
    @Test
    public void addLink(){
        Link link = new Link();
        link.setAvatarLink("1");
        link.setBlogLink("2");
        link.setLinkName("3");
        link.setDescription("desc");
        link.setDataStatus(false);
        linkservice.addLink(link);
    }


}
