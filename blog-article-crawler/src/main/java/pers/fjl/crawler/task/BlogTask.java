package pers.fjl.crawler.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pers.fjl.crawler.pipeline.ArticleTextPipeline;
import pers.fjl.crawler.pipeline.DbPipeline;
import pers.fjl.crawler.processor.BlogProcessor;
import us.codecraft.webmagic.Spider;
//import us.codecraft.webmagic.scheduler.RedisScheduler;

import javax.annotation.Resource;

/**
 * 博客任务类
 */
@Component
public class BlogTask {

    @Resource
    private BlogProcessor blogProcessor;

    @Resource
    private DbPipeline dbPipeline;

    @Resource
    private ArticleTextPipeline articleTextPipeline;

//    @Resource
//    private RedisScheduler redisScheduler;

//    /**
//     * 秒 分 时 日 月 星期
//     */
//    @Scheduled(cron = "30 05 20 * * ?")
//    public void javaTask() {
//        System.out.println("爬取java模块的文章");
//        Spider spider = Spider.create(blogProcessor);
//        spider.addUrl("https://www.csdn.net/nav/java");
//        articleTextPipeline.setChannelId("java");
//        spider.addPipeline(articleTextPipeline);
//        spider.addPipeline(dbPipeline);
////        spider.setScheduler(redisScheduler);
//        spider.start();
//    }

//    /**
//     * 秒 分 时 日 月 星期
//     */
//    @Scheduled(cron = "0 0 23 * * ?")
//    public void springTask() {
//        System.out.println("爬取spring模块的文章");
//        Spider spider = Spider.create(blogProcessor);
//        spider.addUrl("https://www.csdn.net/tags/MtTaEg0sMDg2NTAtYmxvZwO0O0OO0O0O.html");
//        articleTextPipeline.setChannelId("java");
//        spider.addPipeline(articleTextPipeline);
//        spider.addPipeline(dbPipeline);
//        spider.setScheduler(redisScheduler);
//        spider.start();
//    }
//
//    /**
//     * 秒 分 时 日 月 星期
//     */
//    @Scheduled(cron = "0 0 23 * * ?")
//    public void mpTask() {
//        System.out.println("爬取mp模块的文章");
//        Spider spider = Spider.create(blogProcessor);
//        spider.addUrl("https://www.csdn.net/tags/MtTaEg0sMjEzMjItYmxvZwO0O0OO0O0O.html");
//        articleTextPipeline.setChannelId("java");
//        spider.addPipeline(articleTextPipeline);
//        spider.addPipeline(dbPipeline);
//        spider.setScheduler(redisScheduler);
//        spider.start();
//    }
//
//    /**
//     * 秒 分 时 日 月 星期
//     */
//    @Scheduled(cron = "30 44 19 * * ?")
//    public void pythonTask() {
//        System.out.println("爬取python模块的文章");
//        Spider spider = Spider.create(blogProcessor);
//        spider.addUrl("https://www.csdn.net/nav/python");
//        articleTextPipeline.setChannelId("python");
//        spider.addPipeline(articleTextPipeline);
//        spider.addPipeline(dbPipeline);
////        spider.setScheduler(redisScheduler);
//        spider.start();
//    }
//
//    @Scheduled(cron = "30 52 19 * * ?")
//    public void opsTask() {
//        System.out.println("爬取db模块的文章");
//        Spider spider = Spider.create(blogProcessor);
//        spider.addUrl("https://www.csdn.net/nav/db");
//        articleTextPipeline.setChannelId("db");
//        spider.addPipeline(articleTextPipeline);
//        spider.addPipeline(dbPipeline);
////        spider.setScheduler(redisScheduler);
//        spider.start();
//    }
//
//    @Scheduled(cron = "0 20 23 * * ?")
//    public void ops2Task() {
//        System.out.println("爬取运维模块的文章");
//        Spider spider = Spider.create(blogProcessor);
//        spider.addUrl("https://www.csdn.net/tags/MtjaQg5sMDY0MC1ibG9n.html");
//        articleTextPipeline.setChannelId("ops");
//        spider.addPipeline(articleTextPipeline);
//        spider.addPipeline(dbPipeline);
//        spider.setScheduler(redisScheduler);
//        spider.start();
//    }
}
