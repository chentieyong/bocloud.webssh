package com.bocloud.webssh.booter.config;

import com.bocloud.common.utils.MapTools;
import com.bocloud.webssh.entity.MqContent;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;

/**
 * rabbitmq配置类
 *
 * @author wangyu
 * @version 1.0
 * @since 2018年3月30日
 */
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMQConfiguration {

    @Autowired
    private ConnectionFactory connectionFactory;
    private Map<String, Object> arguments = MapTools.simpleMap("x-ha-policy", "all");


    /**
     * 声明任务动作队列
     *
     * @return
     */
    @Bean
    public Queue declareSubscriptionQueue() {
        return new Queue(MqContent.SUBSCRIPTION_MQ_QUEUE, true, false, false, arguments);
    }

    @Bean
    public DirectExchange declareSubscriptionDirectExchange() {
        return new DirectExchange(MqContent.SUBSCRIPTION_MQ_EXCHANGE, true, false, arguments);
    }

    @Bean
    public Binding bindingSubscriptionQueue() {
        return BindingBuilder.bind(declareSubscriptionQueue()).to(declareSubscriptionDirectExchange()).withQueueName();
    }


    @Bean
    public Queue declareSubscriptionHTTPQueue() {
        return new Queue(MqContent.SUBSCRIPTION_MQ_HTTP_QUEUE, true, false, false, arguments);
    }

    @Bean
    public DirectExchange declareSubscriptionDirectHTTPExchange() {
        return new DirectExchange(MqContent.SUBSCRIPTION_MQ_HTTP_EXCHANGE, true, false, arguments);
    }

    @Bean
    public Binding bindingSubscriptionHttpQueue() {
        return BindingBuilder.bind(declareSubscriptionHTTPQueue()).to(declareSubscriptionDirectHTTPExchange()).withQueueName();
    }


    @Bean("customContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(10);  //设置线程数
        factory.setMaxConcurrentConsumers(10); //最大线程数
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate amqpTemplate() {
        RabbitTemplate amqpTemplate = new RabbitTemplate(connectionFactory);
        amqpTemplate.setMessageConverter(messageConverter());
        return amqpTemplate;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        return rabbitAdmin;
    }
}
