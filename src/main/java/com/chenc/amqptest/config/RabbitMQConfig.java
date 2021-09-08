package com.chenc.amqptest.config;


import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnsCallback;

import com.chenc.amqptest.receiver.TestReceiver;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.msgpack.jackson.dataformat.MessagePackFactory;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String user;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.template.receive-timeout}")
    private long receiveTimeout;

    @Value("${spring.rabbitmq.template.reply-timeout}")
    private long replyTimeout;


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host, port);
        cachingConnectionFactory.setUsername(user);
        cachingConnectionFactory.setPassword(password);
        cachingConnectionFactory.setChannelCacheSize(50);
        
        // 消息到broke确认回调
        cachingConnectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);
        
        return cachingConnectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
        return rabbitAdmin;
    }

    @Bean(name = "rabbit")
    @Primary
    public AmqpTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setReceiveTimeout(receiveTimeout);
        rabbitTemplate.setReplyTimeout(replyTimeout);
        return rabbitTemplate;
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper(){
        return new ObjectMapper();
    }

    @Bean("msgpackObjectMapper")
    public ObjectMapper messagePackObjectMapper(){
        return new ObjectMapper(new MessagePackFactory());
    }

}
