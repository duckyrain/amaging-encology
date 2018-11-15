package cn.amaging.encology.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;

/**
 * Created by DuQiyu on 2018/8/15 9:31.
 */
@Configuration
@PropertySource("classpath:redis.properties")
public class SimpleRedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.connectionTimeout}")
    private int connectionTimeout;

    @Value("${redis.soTimeout}")
    private int soTimeout;

    @Value("${redis.maxTotal:8}")
    private int maxTotal;

    @Value("${redis.maxIdle:8}")
    private int maxIdle;

    @Value("${redis.minIdle:0}")
    private int minIdle;

    @Value("${redis.testOnBorrow:true}")
    private boolean testOnBorrow;

    @Value("${redis.maxWaitMillis:5000}")
    private int maxWaitMillis;

    @Value("${redis.minEvictableIdleTimeMillis:300000}")
    private int minEvictableIdleTimeMillis;

    @Value("${redis.numTestsPerEvictionRun}")
    private int numTestsPerEvictionRun;

    @Value("${redis.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        return jedisPoolConfig;
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(jedisPoolConfig, URI.create(host), connectionTimeout, soTimeout);
    }

}
