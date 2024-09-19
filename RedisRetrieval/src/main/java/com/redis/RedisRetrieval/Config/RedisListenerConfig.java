package com.redis.RedisRetrieval.Config;

import com.redis.RedisRetrieval.Service.RedisServices.RedisKeyspaceNotificationListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisShardInfo;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class RedisListenerConfig {

    @Value("${redis.keyspace.event.set:__keyevent@0__:set}")
    private String setEventChannel;

    @Value("${redis.keyspace.event.del:__keyevent@0__:del}")
    private String delEventChannel;

    @Value("${redis.keyspace.event.expired:__keyevent@0__:expired}")
    private String expiredEventChannel;

    @Autowired
    private Config mconfig;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(mconfig.getRedisurl(), mconfig.getRedisport());
        // config.setPassword("your_password_if_needed"); // Uncomment if password is needed
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            RedisKeyspaceNotificationListener listener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // Subscribe to keyspace notifications
        container.addMessageListener(listener, new ChannelTopic(setEventChannel));
        container.addMessageListener(listener, new ChannelTopic(delEventChannel));
        container.addMessageListener(listener, new ChannelTopic(expiredEventChannel));

        return container;
    }
}
