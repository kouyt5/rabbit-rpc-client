package com.chenc.amqptest.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@Configuration
@PropertySource("classpath:queues.properties")
public class RabbitMQConstant {
    @Value("${rabbitmq.queue.cc1}")
    public String queue1;

    @Value("${rabbitmq.queue.cc2}")
    public String queue2;

    @Value("${rabbitmq.queue.timeoutQuene}")
    public String timeoutQuene;

    @Value("${rabbitmq.exchange.asr}")
    public String asrExchange;
    @Value("${rabbitmq.bindingkey.asr}")
    public String bindingKey;

    @Value("${rabbitmq.bindingkey.asr-en}")
    public String bindingKeyEn;

    @Value("${rabbitmq.bindingkey.se}")
    public String bindingKeySe;

    @Value("${rabbitmq.exchange.se}")
    public String seExchange;

    @Value("${rabbitmq.queue.seBase}")
    public String se;

    @Bean(name = "queue1")
    @Primary
    // @Qualifier("queue.cc1")
    public Queue queue1(){
        return new Queue(queue1);
    }

    @Bean(name = "queue2")
    // @Qualifier("queue.cc2")
    public Queue queue2(){
        return new Queue(queue2);
    }

    @Bean(name = "queueTimeOut")
    // @Qualifier("queue.timeoutQuene")
    public Queue queueTimeOut(){
        return new Queue(timeoutQuene);
    }

    @Bean
    public Exchange asrExchange(){
        return new TopicExchange(asrExchange);
    }

    /**
     * 绑定路由键
     * @return
     */
    @Bean
    public Binding binging(@Qualifier("queue1") Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue)
        .to(exchange).with(bindingKey);
    }
        /**
     * 绑定路由键
     * @return
     */
    @Bean("asren")
    public Binding bingingEn(@Qualifier("queue2") Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue)
        .to(exchange).with(bindingKeyEn);
    }
}
