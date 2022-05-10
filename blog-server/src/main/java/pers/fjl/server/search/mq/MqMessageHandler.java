package pers.fjl.server.search.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pers.fjl.common.constant.RabbitMQConst;
import pers.fjl.server.config.RabbitConfig;
import pers.fjl.server.search.index.BlogInfo;
import pers.fjl.server.search.repository.BlogInfoMapper;
import pers.fjl.server.service.BlogInfoService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RabbitListener(queues = RabbitMQConst.esQueue)
public class MqMessageHandler {
    @Resource
    private BlogInfoService blogInfoService;
    @Resource
    private BlogInfoMapper blogInfoMapper;

    @RabbitHandler
    public void handler(PostMqIndexMessage message){
        switch (message.getType()){
            case PostMqIndexMessage.CREATE_OR_UPDATE:
                blogInfoService.createOrUpdate(message);
                log.info("es添加或更新博客数据，id为:{}", message.getBlogId());
                break;
            case PostMqIndexMessage.REMOVE:
                List<BlogInfo> blogInfoList = message.getBlogIdList().stream().map(blogId -> BlogInfo.builder().blogId(blogId).build()
                ).collect(Collectors.toList());
                blogInfoMapper.deleteAll(blogInfoList);
                log.info("es删除博客数据，id为:{}", blogInfoList);
                break;
            default:
                log.error("没找到对应的消息类型，请注意 {}",message.toString());
        }
    }
}
