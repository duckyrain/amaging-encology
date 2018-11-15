package cn.amaging.encology.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DuQiyu on 2018/8/15 9:31.
 */
@Configuration
@PropertySource("classpath:redis.properties")
public class SentinelRedisConfig {

    @Value("${redis.sentinel.masterName:masterName}")
    private String master;

    @Value("${redis.sentinel.hosts}")
    private String hosts;

    @Value("${redis.sentinel.password}")
    private String password;

    @Value("${redis.sentinel.database:0}")
    private int database;

    @Value("${redis.sentinel.clientName}")
    private String clientName;

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
    public JedisSentinelPool jedisSentinelPool() {
        String[] host = hosts.split(",");
        Set<String> sentinels = new HashSet<>(Arrays.asList(host));
        return new JedisSentinelPool(master, sentinels, jedisPoolConfig, connectionTimeout, soTimeout, password,
                database,  clientName);
    }

}
