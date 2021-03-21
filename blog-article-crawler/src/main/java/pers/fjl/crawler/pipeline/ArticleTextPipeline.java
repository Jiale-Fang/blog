package pers.fjl.crawler.pipeline;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pers.fjl.common.utils.HTMLUtil;
import pers.fjl.common.utils.IKUtil;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@Component
public class ArticleTextPipeline implements Pipeline {

    @Value("${ai.dataPath}")
    private String dataPath;

    private String channelId;   //频道ID

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String title = resultItems.get("title");    //获取标题
        String content = HTMLUtil.delHTMLTag(resultItems.get("content"));  //获取正文并删除html标签

        try {
            PrintWriter printWriter = new PrintWriter(new File(dataPath + "/" + channelId + "/" + UUID.randomUUID() + ".txt"));
            printWriter.print(IKUtil.split(title + " " + content, " "));
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

