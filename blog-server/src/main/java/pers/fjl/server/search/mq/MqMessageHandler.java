package pers.fjl.server.search.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pers.fjl.server.config.RabbitConfig;
import pers.fjl.server.service.BlogInfoService;

import javax.annotation.Resource;

@Slf4j
@Component
@RabbitListener(queues = RabbitConfig.esQueue)
public class MqMessageHandler {
    @Resource
    private BlogInfoService blogInfoService;

    @RabbitHandler
    public void handler(PostMqIndexMessage message){
        switch (message.getType()){
            case PostMqIndexMessage.CREATE_OR_UPDATE:
                blogInfoService.createOrUpdate(message);
                break;
            default:
                log.error("没找到对应的消息类型，请注意 {}",message.toString());
        }

    }
}
