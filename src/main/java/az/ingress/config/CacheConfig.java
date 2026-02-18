package az.ingress.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Value("${redis.server.url}")
    private String url;

    @Bean
    public RedissonClient redisson() {
        var config = new Config();
        config
                .setCodec(new SerializationCodec())
                .useSingleServer()
                .setAddress(url);
        return Redisson.create(config);
    }
}   
