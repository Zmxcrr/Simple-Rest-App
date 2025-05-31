package Zmxcrr.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    private static final String HOST = "localhost";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String DIRECT_EXCHANGE = "owner-direct-exchange";
    public static final String CREATE_OWNER_QUEUE = "create-owner-queue";
    public static final String DELETE_OWNER_QUEUE = "delete-owner-queue";
    public static final String CREATE_CAT_QUEUE = "create_cat_queue";
    public static final String DELETE_CAT_QUEUE = "delete_cat_queue";
    public static final String CREATE_FRIEND_QUEUE = "create_friend_queue";
    public static final String DELETE_FRIEND_QUEUE = "delete_friend_queue";

    @Bean
    public Queue createOwnerQueue() {
        return new Queue(CREATE_OWNER_QUEUE, false, false, true);
    }
    @Bean
    public Queue deleteOwnerQueue() {
        return new Queue(DELETE_OWNER_QUEUE, false, false, true);
    }
    @Bean
    public Queue createCatQueue() {
        return new Queue(CREATE_CAT_QUEUE, false, false, true);
    }
    @Bean
    public Queue deleteCatQueue() {
        return new Queue(DELETE_CAT_QUEUE, false, false, true);
    }
    @Bean
    public Queue createFriendQueue() {
        return new Queue(CREATE_FRIEND_QUEUE, false, false, true);
    }
    @Bean
    public Queue deleteFriendQueue() {
        return new Queue(DELETE_FRIEND_QUEUE, false, false, true);
    }
    @Bean
    public Exchange exchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public Binding createOwnerBinding() {
        return BindingBuilder
                .bind(createOwnerQueue())
                .to(exchange())
                .with(CREATE_OWNER_QUEUE)
                .noargs();
    }

    @Bean
    public Binding deleteOwnerBinding() {
        return BindingBuilder
                .bind(deleteOwnerQueue())
                .to(exchange())
                .with(DELETE_OWNER_QUEUE)
                .noargs();
    }

    @Bean
    public Binding createCatBinding() {
        return BindingBuilder
                .bind(createCatQueue())
                .to(exchange())
                .with(CREATE_CAT_QUEUE)
                .noargs();
    }

    @Bean
    public Binding deleteCatBinding() {
        return BindingBuilder
                .bind(deleteCatQueue())
                .to(exchange())
                .with(DELETE_CAT_QUEUE)
                .noargs();
    }

    @Bean
    public Binding createFriendBinding() {
        return BindingBuilder
                .bind(createFriendQueue())
                .to(exchange())
                .with(CREATE_FRIEND_QUEUE)
                .noargs();
    }

    @Bean
    public Binding deleteFriendBinding() {
        return BindingBuilder
                .bind(deleteFriendQueue())
                .to(exchange())
                .with(DELETE_FRIEND_QUEUE)
                .noargs();
    }

    @Bean
    ConnectionFactory connectionFactory() {
        var factory = new CachingConnectionFactory(HOST);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        return factory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}

