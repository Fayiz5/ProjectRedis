package com.redis.RedisRetrieval.Config;

import com.redis.RedisRetrieval.Service.RedisServices.RedisKeyspaceNotificationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisListenerConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            RedisKeyspaceNotificationListener listener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // Subscribe to keyspace notifications
        container.addMessageListener(listener, new ChannelTopic("__keyevent@0__:set"));
        container.addMessageListener(listener, new ChannelTopic("__keyevent@0__:del"));
        container.addMessageListener(listener, new ChannelTopic("__keyevent@0__:expired"));

        return container;
    }
}
