package com.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    // 锁的 Key 前缀
    private static final String LOCK_PREFIX = "REDIS_LOCK:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置带过期时间的缓存
     * @param key 键
     * @param value 值
     * @param timeout 过期时间（秒）
     */
    public void setWithExpire(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存值
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     * @param key 键
     * @return 是否成功
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 设置过期时间
     * @param key 键
     * @param timeout 过期时间（秒）
     * @return 是否成功
     */
    public Boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    // ====================== Hash =====================

    /**
     * 判断key是否存在
     * @param key 键
     * @return 是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 自增操作
     * @param key 键
     * @param delta 增量
     * @return 自增后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 设置哈希表中的字段值
     * @param key 哈希表的键
     * @param hashKey 哈希表中的字段
     * @param value 字段值
     */
    public void hPut(String key, String hashKey, Object value) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        ops.put(key, hashKey, value);
    }

    /**
     * 获取哈希表中指定字段的值
     * @param key 哈希表的键
     * @param hashKey 哈希表中的字段
     * @return 字段值
     */
    public Object hGet(String key, String hashKey) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        return ops.get(key, hashKey);
    }

    /**
     * 获取哈希表中所有字段的值
     * @param key 哈希表的键
     * @return 哈希表中的所有字段及其值
     */
    public Map<String, Object> hGetAll(String key) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        return ops.entries(key);
    }

    /**
     * 删除哈希表中的指定字段
     * @param key 哈希表的键
     * @param hashKey 哈希表中的字段
     */
    public void hDelete(String key, String hashKey) {
        HashOperations<String, String, Object> ops = redisTemplate.opsForHash();
        ops.delete(key, hashKey);
    }

    // ====================== List =====================

    /**
     * 向列表头部添加元素
     * @param key 列表的键
     * @param value 列表中的元素
     */
    public void lPush(String key, Object value) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        ops.leftPush(key, value);
    }

    /**
     * 从列表头部弹出元素
     * @param key 列表的键
     * @return 列表头部的元素
     */
    public Object lPop(String key) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        return ops.leftPop(key);
    }

    /**
     * 添加多个元素到列表的尾部, 返回添加的元素数量
     * @param key 列表的键
     * @param values 多个元素
     * @return 添加的元素数量
     */
    public <T> long lRAppendAll(String key, List<T> values){
        ListOperations<String,Object> ops = redisTemplate.opsForList();
        return Optional.ofNullable(ops.rightPushAll(key, values)).orElse(0L);
    }

    /**
     * 添加元素到列表的尾部, 返回添加的元素数量 (如果key不存在，则创建一个空的列表并添加元素)
     * @param key 列表的键
     * @param value 列表中的元素
     * @return 添加的元素数量
     */
    public long lRAppend(String key,Object value){
        ListOperations<String, Object> opsForList = redisTemplate.opsForList();
        return Optional.ofNullable(opsForList.rightPush(key, value)).orElse(0L);
    }

    /**
     * 获取列表中指定区间的元素
     * @param key 列表的键
     * @param start 起始位置（从0开始）
     * @param end 结束位置（从0开始）
     * @return 列表中指定区间的元素
     */
    public List<Object> lRange(String key, long start, long end) {
        ListOperations<String,Object> ops = redisTemplate.opsForList();
        return ops.range(key, start, end);
    }

    /**
     * 获取列表的长度
     * @param key 列表的键
     * @return 列表的长度
     */
    public long lSize(String key) {
        ListOperations<String, Object> ops = redisTemplate.opsForList();
        return Optional.ofNullable(ops.size(key)).orElse(0L);
    }

    // ====================== Set =====================

    /**
     * 向集合中添加元素
     * @param key 集合的键
     * @param value 集合中的元素
     */
    public void sAdd(String key, Object value) {
        SetOperations<String, Object> ops = redisTemplate.opsForSet();
        ops.add(key, value);
    }

    /**
     * 获取集合中的所有元素
     * @param key 集合的键
     * @return 集合中的所有元素
     */
    public Set<Object> sMembers(String key) {
        SetOperations<String, Object> ops = redisTemplate.opsForSet();
        return ops.members(key);
    }

    /**
     * 判断某个元素是否存在于集合中
     * @param key 集合的键
     * @param value 集合中的元素
     * @return true 如果元素在集合中，false 如果元素不在集合中
     */
    public boolean sIsMember(String key, Object value) {
        SetOperations<String, Object> ops = redisTemplate.opsForSet();
        return Boolean.TRUE.equals(ops.isMember(key, value));
    }

    /**
     * 获取集合的大小
     * @param key 集合的键
     * @return 集合的大小
     */
    public long sSize(String key) {
        SetOperations<String, Object> ops = redisTemplate.opsForSet();
        return Optional.ofNullable(ops.size(key)).orElse(0L);
    }

    // ====================== ZSet (Sorted Set) =====================

    /**
     * 向有序集合中添加元素
     * @param key 有序集合的键
     * @param value 元素
     * @param score 元素的分数
     */
    public void zAdd(String key, Object value, double score) {
        ZSetOperations<String, Object> ops = redisTemplate.opsForZSet();
        ops.add(key, value, score);
    }

    /**
     * 获取有序集合中指定区间的元素
     * @param key 有序集合的键
     * @param start 起始位置（从0开始）
     * @param end 结束位置（从0开始）
     * @return 有序集合中指定区间的元素
     */
    public Set<Object> zRange(String key, long start, long end) {
        ZSetOperations<String, Object> ops = redisTemplate.opsForZSet();
        return ops.range(key, start, end);
    }

    /**
     * 根据分数范围获取有序集合中的元素
     * @param key 有序集合的键
     * @param min 最小分数
     * @param max 最大分数
     * @return 符合条件的元素
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        ZSetOperations<String, Object> ops = redisTemplate.opsForZSet();
        return ops.rangeByScore(key, min, max);
    }

    /**
     * 获取有序集合的大小
     * @param key 有序集合的键
     * @return 有序集合的大小
     */
    public long zSize(String key) {
        ZSetOperations<String, Object> ops = redisTemplate.opsForZSet();
        return Optional.ofNullable(ops.size(key)).orElse(0L);
    }

    //============分布式=========

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey 锁的 Key
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey,long lockExpire) {
        String key = LOCK_PREFIX + lockKey;
        String id = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, id, lockExpire, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    /**
     * 尝试获取锁，支持重试
     *
     * @param lockKey 锁的 Key
     * @param expire 锁的过期时间（秒）
     * @param timeout 超时时间（毫秒）
     * @return 是否获取成功
     * @throws InterruptedException 线程中断异常
     */
    public boolean tryLockWithRetry(String lockKey,long expire, long timeout) throws InterruptedException {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            if (tryLock(lockKey,expire)) {
                return true;
            }
            TimeUnit.MILLISECONDS.sleep(100);
        }
        return false;
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁的 Key
     */
    public void releaseLock(String lockKey) {
        String key = LOCK_PREFIX + lockKey;

        // Lua 脚本：直接删除锁，确保 exists 和 del 命令在 Redis 内部原子执行
        String luaScript =
                "if redis.call('exists', KEYS[1]) == 1 then " +
                    "   return redis.call('del', KEYS[1]) " +
                "else " +
                    "   return 0 " +
                    "end";
        // 执行 Lua 脚本
        redisTemplate.execute((RedisCallback<Object>) connection ->
                connection.eval(luaScript.getBytes(), ReturnType.INTEGER, 1, key.getBytes())
        );
    }
}