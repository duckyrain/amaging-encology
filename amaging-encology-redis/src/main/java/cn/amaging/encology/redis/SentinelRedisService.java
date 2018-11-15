package cn.amaging.encology.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by DuQiyu on 2018/8/24 14:30.
 */
@Component
public class SentinelRedisService implements RedisService {

    private static final Logger log = LoggerFactory.getLogger(SentinelRedisService.class);

    private final JedisSentinelPool jedisSentinelPool;

    @Autowired
    public SentinelRedisService(JedisSentinelPool jedisSentinelPool) {
        this.jedisSentinelPool = jedisSentinelPool;
    }

    @Override
    public String set(String key, String value) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.set(key, value);
            log.debug("set, key:{}, value:{}.", key, value);
        } catch (Exception e) {
            log.error("set error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String set(String key, String value, int seconds) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.setex(key, seconds, value);
            log.debug("set, key:{}, value:{}, seconds:{}.", key, value, seconds);
        } catch (Exception e) {
            log.error("set error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String set(String key, String value, long milliseconds) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.psetex(key, milliseconds, value);
            log.debug("set, key:{}, value:{}, milliseconds:{}.", key, value, milliseconds);
        } catch (Exception e) {
            log.error("set error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String get(String key) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("get error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> keys(String pattern) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.keys(pattern);
            log.debug("keys, pattern:{}", pattern);
        } catch (Exception e) {
            log.error("get error. pattern:" + pattern, e);
        }
        return result;
    }

    @Override
    public Boolean exists(String key) {
        Boolean result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.exists(key);
            log.debug("exists, key:{}.", key);
        } catch (Exception e) {
            log.error("exists error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long persist(String key) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.persist(key);
            log.debug("persist, key:{}.", key);
        } catch (Exception e) {
            log.error("persist error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String type(String key) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.type(key);
            log.debug("type, key:{}.", key);
        } catch (Exception e) {
            log.error("type error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long expire(String key, int seconds) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.expire(key, seconds);
            log.debug("expire, key:{}, seconds:{}", key, seconds);
        } catch (Exception e) {
            log.error("expire error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long pexpire(String key, long milliseconds) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.pexpire(key, milliseconds);
            log.debug("pexpire, key:{}, seconds:{}", key, milliseconds);
        } catch (Exception e) {
            log.error("pexpire error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.expireAt(key, unixTime);
            log.debug("expireAt, key:{}, seconds:{}", key, unixTime);
        } catch (Exception e) {
            log.error("expireAt error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.pexpireAt(key, millisecondsTimestamp);
            log.debug("pexpireAt, key:{}, seconds:{}", key, millisecondsTimestamp);
        } catch (Exception e) {
            log.error("pexpireAt error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long ttl(String key) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.ttl(key);
            log.debug("ttl, key:{}", key);
        } catch (Exception e) {
            log.error("ttl error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long pttl(String key) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.pttl(key);
            log.debug("pttl, key:{}", key);
        } catch (Exception e) {
            log.error("pttl error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long setrange(String key, long offset, String value) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.setrange(key, offset, value);
            log.debug("setrange, key:{}, offset:{}, value:{}", key, offset, value);
        } catch (Exception e) {
            log.error("setrange error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.getrange(key, startOffset, endOffset);
            log.debug("getrange, key:{}, startOffset:{}, endOffset:{}", key, startOffset, endOffset);
        } catch (Exception e) {
            log.error("getrange error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String getSet(String key, String value) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.getSet(key, value);
            log.debug("getSet, key:{}, value:{}", key, value);
        } catch (Exception e) {
            log.error("getSet error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long decrBy(String key, long value) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.decrBy(key, value);
            log.debug("decrBy, key:{}, value:{}", key, value);
        } catch (Exception e) {
            log.error("decrBy error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long decr(String key) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.decr(key);
            log.debug("decr, key:{}", key);
        } catch (Exception e) {
            log.error("decr error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long incrBy(String key, long integer) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.incrBy(key, integer);
            log.debug("incrBy, key:{}, integer:{}", key, integer);
        } catch (Exception e) {
            log.error("incrBy error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Double incrByFloat(String key, double value) {
        Double result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.incrByFloat(key, value);
            log.debug("incrByFloat, key:{}, value:{}", key, value);
        } catch (Exception e) {
            log.error("incrByFloat error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long incr(String key) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.incr(key);
            log.debug("incr, key:{}", key);
        } catch (Exception e) {
            log.error("incr error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long append(String key, String value) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.append(key, value);
            log.debug("append, key:{}", key);
        } catch (Exception e) {
            log.error("append error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String substr(String key, int start, int end) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.substr(key, start, end);
            log.debug("substr, key:{}, start:{}, end:{}", key, start, end);
        } catch (Exception e) {
            log.error("substr error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long hset(String key, String field, String value) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hset(key, field, value);
            log.debug("hset, key:{}, field:{}, value:{}", key, field, value);
        } catch (Exception e) {
            log.error("hset error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String hget(String key, String field) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hget(key, field);
            log.debug("hget, key:{}, field:{}", key, field);
        } catch (Exception e) {
            log.error("hget error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hsetnx(key, field, value);
            log.debug("hsetnx, key:{}, field:{}, value:{}", key, field, value);
        } catch (Exception e) {
            log.error("hsetnx error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hmset(key, hash);
            log.debug("hmset, key:{}", key);
        } catch (Exception e) {
            log.error("hmset error. key:" + key, e);
        }
        return result;
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        List<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hmget(key, fields);
            log.debug("hmget, key:{}, fields:{}", key, fields);
        } catch (Exception e) {
            log.error("hmget error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hincrBy(key, field, value);
            log.debug("hincrBy, key:{}, field:{}, value:{}", key, field, value);
        } catch (Exception e) {
            log.error("hincrBy error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Double hincrByFloat(String key, String field, double value) {
        Double result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hincrByFloat(key, field, value);
            log.debug("hincrByFloat, key:{}, field:{}, value:{}", key, field, value);
        } catch (Exception e) {
            log.error("hincrByFloat error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Boolean hexists(String key, String field) {
        Boolean result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hexists(key, field);
            log.debug("hexists, key:{}, field:{}", key, field);
        } catch (Exception e) {
            log.error("hexists error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long hdel(String key, String... fields) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hdel(key, fields);
            log.debug("hdel, key:{}, fields:{}", key, fields);
        } catch (Exception e) {
            log.error("hdel error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long hlen(String key) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hlen(key);
            log.debug("hlen, key:{}, fields:{}", key);
        } catch (Exception e) {
            log.error("hlen error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> hkeys(String key) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hkeys(key);
            log.debug("hkeys, key:{}, fields:{}");
        } catch (Exception e) {
            log.error("hkeys error. key:" + key, e);
        }
        return result;
    }

    @Override
    public List<String> hvals(String key) {
        List<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hvals(key);
            log.debug("hvals, key:{}");
        } catch (Exception e) {
            log.error("hvals error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Map<String, String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hgetAll(key);
            log.debug("hgetAll, key:{}");
        } catch (Exception e) {
            log.error("hgetAll error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long rpush(String key, String... string) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.rpush(key, string);
            log.debug("rpush, key:{}");
        } catch (Exception e) {
            log.error("rpush error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long lpush(String key, String... string) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.lpush(key, string);
            log.debug("lpush, key:{}");
        } catch (Exception e) {
            log.error("lpush error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long llen(String key) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.llen(key);
            log.debug("llen, key:{}");
        } catch (Exception e) {
            log.error("llen error. key:" + key, e);
        }
        return result;
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        List<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.lrange(key, start, end);
            log.debug("lrange, key:{}, start:{}, end:{}", key, start, end);
        } catch (Exception e) {
            log.error("lrange error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String ltrim(String key, long start, long end) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.ltrim(key, start, end);
            log.debug("ltrim, key:{}, start:{}, end:{}", key, start, end);
        } catch (Exception e) {
            log.error("ltrim error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String lindex(String key, long index) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.lindex(key, index);
            log.debug("lindex, key:{}, index:{}", key, index);
        } catch (Exception e) {
            log.error("lindex error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String lset(String key, long index, String value) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.lset(key, index, value);
            log.debug("lset, key:{}, index:{}, value:{}", key, index, value);
        } catch (Exception e) {
            log.error("lset error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long lrem(String key, long count, String value) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.lrem(key, count, value);
            log.debug("lrem, key:{}, count:{}, value:{}", key, count, value);
        } catch (Exception e) {
            log.error("lrem error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String lpop(String key) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.lpop(key);
            log.debug("lpop, key:{}", key);
        } catch (Exception e) {
            log.error("lpop error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String rpop(String key) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.rpop(key);
            log.debug("rpop, key:{}", key);
        } catch (Exception e) {
            log.error("rpop error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long sadd(String key, String... member) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.sadd(key, member);
            log.debug("sadd, key:{}, member:{}", key, member);
        } catch (Exception e) {
            log.error("sadd error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> smembers(String key) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.smembers(key);
            log.debug("smembers, key:{}, member:{}", key);
        } catch (Exception e) {
            log.error("smembers error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long srem(String key, String... member) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.srem(key, member);
            log.debug("srem, key:{}, member:{}", key);
        } catch (Exception e) {
            log.error("srem error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String spop(String key) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.spop(key);
            log.debug("spop, key:{}", key);
        } catch (Exception e) {
            log.error("spop error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> spop(String key, long count) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.spop(key, count);
            log.debug("spop, key:{}, count:{}", key, count);
        } catch (Exception e) {
            log.error("spop error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long scard(String key) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.scard(key);
            log.debug("scard, key:{}", key);
        } catch (Exception e) {
            log.error("scard error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Boolean sismember(String key, String member) {
        Boolean result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.sismember(key, member);
            log.debug("sismember, key:{}, member:{}", key, member);
        } catch (Exception e) {
            log.error("sismember error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String srandmember(String key) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.srandmember(key);
            log.debug("srandmember, key:{}", key);
        } catch (Exception e) {
            log.error("srandmember error. key:" + key, e);
        }
        return result;
    }

    @Override
    public List<String> srandmember(String key, int count) {
        List<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.srandmember(key, count);
            log.debug("srandmember, key:{}, count:{}", key, count);
        } catch (Exception e) {
            log.error("srandmember error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long strlen(String key) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.strlen(key);
            log.debug("strlen, key:{}", key);
        } catch (Exception e) {
            log.error("strlen error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zadd(String key, double score, String member) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zadd(key, score, member);
            log.debug("zadd, key:{}, score:{}, member:{}", key, score, member);
        } catch (Exception e) {
            log.error("zadd error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zadd(String key, double score, String member, ZAddParams params) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zadd(key, score, member, params);
            log.debug("zadd, key:{}, score:{}, member:{}", key, score, member);
        } catch (Exception e) {
            log.error("zadd error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zadd(key, scoreMembers);
            log.debug("zadd, key:{}", key);
        } catch (Exception e) {
            log.error("zadd error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zadd(key, scoreMembers, params);
            log.debug("zadd, key:{}", key);
        } catch (Exception e) {
            log.error("zadd error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrange(key, start, end);
            log.debug("zrange, key:{}, start:{}, end:{}", key, start, end);
        } catch (Exception e) {
            log.error("zrange error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zrem(String key, String... member) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrem(key, member);
            log.debug("zrem, key:{}, member:{}", key, member);
        } catch (Exception e) {
            log.error("zrem error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        Double result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zincrby(key, score, member);
            log.debug("zincrby, key:{}, score:{}, member:{}", key, score, member);
        } catch (Exception e) {
            log.error("zincrby error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Double zincrby(String key, double score, String member, ZIncrByParams params) {
        Double result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zincrby(key, score, member, params);
            log.debug("zincrby, key:{}, score:{}, member:{}", key, score, member);
        } catch (Exception e) {
            log.error("zincrby error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zrank(String key, String member) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrank(key, member);
            log.debug("zrank, key:{}, member:{}", key, member);
        } catch (Exception e) {
            log.error("zrank error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zrevrank(String key, String member) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrank(key, member);
            log.debug("zrevrank, key:{}, member:{}", key, member);
        } catch (Exception e) {
            log.error("zrevrank error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrange(key, start, end);
            log.debug("zrevrange, key:{}, start:{}, end:{}", key, start, end);
        } catch (Exception e) {
            log.error("zrevrange error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        Set<Tuple> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrangeWithScores(key, start, end);
            log.debug("zrangeWithScores, key:{}, start:{}, end:{}", key, start, end);
        } catch (Exception e) {
            log.error("zrangeWithScores error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        Set<Tuple> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrangeWithScores(key, start, end);
            log.debug("zrevrangeWithScores, key:{}, start:{}, end:{}", key, start, end);
        } catch (Exception e) {
            log.error("zrevrangeWithScores error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zcard(String key) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zcard(key);
            log.debug("zcard, key:{}", key);
        } catch (Exception e) {
            log.error("zcard error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Double zscore(String key, String member) {
        Double result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zscore(key, member);
            log.debug("zscore, key:{}, member:{}", key, member);
        } catch (Exception e) {
            log.error("zscore error. key:" + key, e);
        }
        return result;
    }

    @Override
    public List<String> sort(String key) {
        List<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.sort(key);
            log.debug("sort, key:{}", key);
        } catch (Exception e) {
            log.error("sort error. key:" + key, e);
        }
        return result;
    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {
        List<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.sort(key, sortingParameters);
            log.debug("sort, key:{}", key);
        } catch (Exception e) {
            log.error("sort error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zcount(String key, double min, double max) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zcount(key, min, max);
            log.debug("zcount, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zcount error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zcount(String key, String min, String max) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zcount(key, min, max);
            log.debug("zcount, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zcount error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrangeByScore(key, min, max);
            log.debug("zrangeByScore, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zrangeByScore error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrangeByScore(key, min, max);
            log.debug("zrangeByScore, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zrangeByScore error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrangeByScore(key, min, max);
            log.debug("zrevrangeByScore, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zrevrangeByScore error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrangeByScore(key, min, max, offset, count);
            log.debug("zrangeByScore, key:{}, min:{}, max:{}, offset:{}, count:{}", key, min, max, offset, count);
        } catch (Exception e) {
            log.error("zrangeByScore error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrangeByScore(key, min, max);
            log.debug("zrevrangeByScore, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zrevrangeByScore error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrangeByScore(key, min, max, offset, count);
            log.debug("zrangeByScore, key:{}, min:{}, max:{}, offset:{}, count:{}", key, min, max, offset, count);
        } catch (Exception e) {
            log.error("zrangeByScore error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrangeByScore(key, min, max, offset, count);
            log.debug("zrevrangeByScore, key:{}, min:{}, max:{}, offset:{}, count:{}", key, min, max, offset, count);
        } catch (Exception e) {
            log.error("zrevrangeByScore error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        Set<Tuple> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrangeByScoreWithScores(key, min, max);
            log.debug("zrangeByScoreWithScores, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zrangeByScoreWithScores error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        Set<Tuple> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrangeByScoreWithScores(key, min, max);
            log.debug("zrevrangeByScoreWithScores, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zrevrangeByScoreWithScores error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        Set<Tuple> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrangeByScoreWithScores(key, min, max, offset, count);
            log.debug("zrevrangeByScoreWithScores, key:{}, min:{}, max:{}, offset:{}, count:{}", key, min, max, offset, count);
        } catch (Exception e) {
            log.error("zrevrangeByScoreWithScores error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrangeByScore(key, min, max, offset, count);
            log.debug("zrevrangeByScore, key:{}, min:{}, max:{}, offset:{}, count:{}", key, min, max, offset, count);
        } catch (Exception e) {
            log.error("zrevrangeByScore error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        Set<Tuple> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrangeByScoreWithScores(key, min, max);
            log.debug("zrangeByScoreWithScores, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zrangeByScoreWithScores error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        Set<Tuple> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrangeByScoreWithScores(key, min, max);
            log.debug("zrevrangeByScoreWithScores, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zrevrangeByScoreWithScores error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        Set<Tuple> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrangeByScoreWithScores(key, min, max, offset, count);
            log.debug("zrangeByScoreWithScores, key:{}, min:{}, max:{}, offset:{}, count:{}", key, min, max, offset, count);
        } catch (Exception e) {
            log.error("zrangeByScoreWithScores error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        Set<Tuple> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrangeByScoreWithScores(key, min, max, offset, count);
            log.debug("zrevrangeByScoreWithScores, key:{}, min:{}, max:{}, offset:{}, count:{}", key, min, max, offset, count);
        } catch (Exception e) {
            log.error("zrevrangeByScoreWithScores error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        Set<Tuple> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrangeByScoreWithScores(key, min, max, offset, count);
            log.debug("zrevrangeByScoreWithScores, key:{}, min:{}, max:{}, offset:{}, count:{}", key, min, max, offset, count);
        } catch (Exception e) {
            log.error("zrevrangeByScoreWithScores error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zremrangeByRank(String key, long start, long end) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zremrangeByRank(key, start, end);
            log.debug("zremrangeByRank, key:{}, start:{}, end:{}", key, start, end);
        } catch (Exception e) {
            log.error("zremrangeByRank error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zremrangeByScore(key, start, end);
            log.debug("zremrangeByScore, key:{}, start:{}, end:{}", key, start, end);
        } catch (Exception e) {
            log.error("zremrangeByScore error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zremrangeByScore(String key, String start, String end) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zremrangeByScore(key, start, end);
            log.debug("zremrangeByScore, key:{}, start:{}, end:{}", key, start, end);
        } catch (Exception e) {
            log.error("zremrangeByScore error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zlexcount(String key, String min, String max) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zlexcount(key, min, max);
            log.debug("zlexcount, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zlexcount error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrangeByLex(key, min, max);
            log.debug("zrangeByLex, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zrangeByLex error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrangeByLex(key, min, max, offset, count);
            log.debug("zrangeByLex, key:{}, min:{}, max:{}, offset:{}, count:{}", key, min, max, offset, count);
        } catch (Exception e) {
            log.error("zrangeByLex error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrangeByLex(key, min, max);
            log.debug("zrevrangeByLex, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zrevrangeByLex error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        Set<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zrevrangeByLex(key, min, max, offset, count);
            log.debug("zrevrangeByLex, key:{}, min:{}, max:{}, offset:{}, count:{}", key, min, max, offset, count);
        } catch (Exception e) {
            log.error("zrevrangeByLex error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long zremrangeByLex(String key, String min, String max) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zremrangeByLex(key, min, max);
            log.debug("zremrangeByLex, key:{}, min:{}, max:{}", key, min, max);
        } catch (Exception e) {
            log.error("zremrangeByLex error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long linsert(String key, Client.LIST_POSITION where, String pivot, String value) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.linsert(key, where, pivot, value);
            log.debug("linsert, key:{}, where:{}, pivot:{}, value:{}", key, where, pivot, value);
        } catch (Exception e) {
            log.error("linsert error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long lpushx(String key, String... string) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.lpushx(key, string);
            log.debug("lpushx, key:{}, string:{}", key, string);
        } catch (Exception e) {
            log.error("lpushx error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long rpushx(String key, String... string) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.rpushx(key, string);
            log.debug("rpushx, key:{}, string:{}", key, string);
        } catch (Exception e) {
            log.error("rpushx error. key:" + key, e);
        }
        return result;
    }

    @Override
    public List<String> blpop(int timeout, String key) {
        List<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.blpop(timeout, key);
            log.debug("blpop, timeout:{}, key:{}", timeout, key);
        } catch (Exception e) {
            log.error("blpop error. key:" + key, e);
        }
        return result;
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        List<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.brpop(timeout, key);
            log.debug("brpop, timeout:{}, key:{}", timeout, key);
        } catch (Exception e) {
            log.error("brpop error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long del(String key) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.del(key);
            log.debug("del, key:{}", key);
        } catch (Exception e) {
            log.error("del error. key:" + key, e);
        }
        return result;
    }

    @Override
    public String flushDB() {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.flushDB();
            log.warn("flushDB.");
        } catch (Exception e) {
            log.error("flushDB.", e);
        }
        return result;
    }

    @Override
    public String echo(String string) {
        String result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.echo(string);
            log.debug("echo, string:{}", string);
        } catch (Exception e) {
            log.error("echo error. echo:" + string, e);
        }
        return result;
    }

    @Override
    public Long move(String key, int dbIndex) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.move(key, dbIndex);
            log.debug("move, key:{}, dbIndex:{}", key, dbIndex);
        } catch (Exception e) {
            log.error("move error. key:" + key, e);
        }
        return result;
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        ScanResult<Map.Entry<String, String>> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hscan(key, cursor);
            log.debug("hscan, key:{}, cursor:{}", key, cursor);
        } catch (Exception e) {
            log.error("hscan error. key:" + key, e);
        }
        return result;
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        ScanResult<Map.Entry<String, String>> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.hscan(key, cursor, params);
            log.debug("hscan, key:{}, cursor:{}", key, cursor);
        } catch (Exception e) {
            log.error("hscan error. key:" + key, e);
        }
        return result;
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) {
        ScanResult<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.sscan(key, cursor);
            log.debug("sscan, key:{}, cursor:{}", key, cursor);
        } catch (Exception e) {
            log.error("sscan error. key:" + key, e);
        }
        return result;
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        ScanResult<String> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.sscan(key, cursor, params);
            log.debug("sscan, key:{}, cursor:{}", key, cursor, params);
        } catch (Exception e) {
            log.error("sscan error. key:" + key, e);
        }
        return result;
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) {
        ScanResult<Tuple> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zscan(key, cursor);
            log.debug("zscan, key:{}, cursor:{}", key, cursor);
        } catch (Exception e) {
            log.error("zscan error. key:" + key, e);
        }
        return result;
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        ScanResult<Tuple> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.zscan(key, cursor, params);
            log.debug("zscan, key:{}, cursor:{}", key, cursor);
        } catch (Exception e) {
            log.error("zscan error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long pfadd(String key, String... elements) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.pfadd(key, elements);
            log.debug("pfadd, key:{}, elements:{}", key, elements);
        } catch (Exception e) {
            log.error("pfadd error. key:" + key, e);
        }
        return result;
    }

    @Override
    public long pfcount(String key) {
        long result = 0;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.pfcount(key);
            log.debug("pfcount, key:{}", key);
        } catch (Exception e) {
            log.error("pfcount error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Boolean setbit(String key, long offset, boolean value) {
        Boolean result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.setbit(key, offset, value);
            log.debug("setbit, key:{}, offset:{}, value:{}", key, offset, value);
        } catch (Exception e) {
            log.error("setbit error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Boolean setbit(String key, long offset, String value) {
        Boolean result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.setbit(key, offset, value);
            log.debug("setbit, key:{}, offset:{}, value:{}", key, offset, value);
        } catch (Exception e) {
            log.error("setbit error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Boolean getbit(String key, long offset) {
        Boolean result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.getbit(key, offset);
            log.debug("getbit, key:{}, offset:{}", key, offset);
        } catch (Exception e) {
            log.error("getbit error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long bitcount(String key) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.bitcount(key);
            log.debug("bitcount, key:{}", key);
        } catch (Exception e) {
            log.error("bitcount error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long bitcount(String key, long start, long end) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.bitcount(key, start, end);
            log.debug("bitcount, key:{}, start:{}, end:{}", key, start, end);
        } catch (Exception e) {
            log.error("bitcount error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long bitpos(String key, boolean value) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.bitpos(key, value);
            log.debug("bitpos, key:{}, value:{}", key, value);
        } catch (Exception e) {
            log.error("bitpos error. key:" + key, e);
        }
        return result;
    }

    @Override
    public Long bitpos(String key, boolean value, BitPosParams params) {
        Long result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.bitpos(key, value, params);
            log.debug("bitpos, key:{}, value:{}", key, value);
        } catch (Exception e) {
            log.error("bitpos error. key:" + key, e);
        }
        return result;
    }

    @Override
    public List<Long> bitfield(String key, String... arguments) {
        List<Long> result = null;
        try (Jedis jedis = jedisSentinelPool.getResource()) {
            result = jedis.bitfield(key, arguments);
            log.debug("bitfield, key:{}, arguments:{}", key, arguments);
        } catch (Exception e) {
            log.error("bitfield error. key:" + key, e);
        }
        return result;
    }
}
