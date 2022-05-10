package pers.fjl.server.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static pers.fjl.common.constant.RabbitMQConst.*;


@Configuration
public class RabbitConfig {

    @Bean
    public Queue exQueue(){
        return new Queue(esQueue,true);
    }

    @Bean
    DirectExchange exchange(){
        return new DirectExchange(esExchange,true,false);
    }

    @Bean
    Binding binding(Queue exQueue, DirectExchange exchange){
        return BindingBuilder.bind(exQueue).to(exchange).with(esBingKey);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public FanoutExchange emailExchange() {
        return new FanoutExchange(EMAIL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingEmailDirect() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange());
    }

}
