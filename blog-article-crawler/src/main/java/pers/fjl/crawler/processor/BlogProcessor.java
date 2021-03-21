package pers.fjl.crawler.processor;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 博客爬取类
 */
@Component
public class BlogProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("https://blog.csdn.net/[a-z 0-9 -]+/article/details/[0-9]{9}").all());
        String title = page.getHtml().xpath("//*[@id=\"articleContentId\"]/text()").get();
        String content = page.getHtml().xpath("//*[@id=\"content_views\"]").get();
        String nickname = page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[2]/div[1]/div/a[1]/text()").get();
        String createTime = page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[2]/div[1]/div/span[1]/text()").get();
        String views = page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[2]/div[1]/div/span[2]/text()").get();
        String avatar = page.getHtml().xpath("//*[@id=\"asideProfile\"]/div[1]/div[1]/a/").css("img", "src").get();  // 图片在img标签下的src里
        String thumbs = page.getHtml().xpath("//*[@id=\"spanCount\"]/text()").get();

        System.out.println("title:" + title);
//        System.out.println("content:"+content);
        System.out.println("nickname:" + nickname);
        System.out.println("createTime:" + createTime);
        System.out.println("views:" + views);
        System.out.println("avatar:" + avatar);
        if (thumbs == null || thumbs.length()==0 || thumbs.equals(" ") || thumbs.trim().isEmpty()) {
            thumbs = "0";
        }
        System.out.println("thumbs:" + thumbs);

        if (title != null || content != null || nickname != null || createTime != null || views != null || avatar != null) {
            page.putField("title", title);
            page.putField("content", content);
            page.putField("nickname", nickname);
            page.putField("createTime", createTime);
            page.putField("views", views);
            page.putField("avatar", avatar);
            page.putField("thumbs", thumbs);
        } else {
            page.setSkip(true); // 跳过
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3000).setSleepTime(100);
    }
}
