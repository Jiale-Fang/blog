package pers.fjl.server.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    public final static String esQueue = "es_queue";
    public final static String esExchange = "es_exchange";
    public final static String esBingKey = "es_exchange";

    @Bean
    public Queue exQueue(){
        return new Queue(esQueue);
    }

    @Bean
    DirectExchange exchange(){
        return new DirectExchange(esExchange);
    }

    @Bean
    Binding binding(Queue exQueue, DirectExchange exchange){
        return BindingBuilder.bind(exQueue).to(exchange).with(esBingKey);
    }

}
