package pers.fjl.crawler.pipeline;

import org.springframework.stereotype.Component;
import pers.fjl.crawler.dao.CrawledBlogDao;
import pers.fjl.crawler.po.CrawledBlog;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 将爬取到的数据文件存入数据库
 */
@Component
public class DbPipeline implements Pipeline {

    @Resource
    private CrawledBlogDao crawledBlogDao;

    private Integer imgNum = 1;

    @Override
    public void process(ResultItems resultItems, Task task) {
        String title = resultItems.get("title");
        String content = resultItems.get("content");
        String nickname = resultItems.get("nickname");
        String avatar = resultItems.get("avatar");
        String createTime = resultItems.get("createTime");
        String views = resultItems.get("views");
        String thumbs = resultItems.get("thumbs");
        String firstPicture = "https://unsplash.it/800/450?image=" + imgNum++;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createTimeDb = LocalDateTime.parse(createTime, formatter);
        CrawledBlog crawledBlog = new CrawledBlog();
        crawledBlog.setTitle(title).setContent(content).setNickname(nickname).setCreateTime(createTimeDb).setViews(Integer.parseInt(views))
        .setAvatar(avatar).setThumbs(Integer.parseInt(thumbs.trim())).setFirstPicture(firstPicture);
//        setCreateTime(createTimeDb)   setViews(Integer.parseInt(views))
        crawledBlogDao.insert(crawledBlog);
    }
}
