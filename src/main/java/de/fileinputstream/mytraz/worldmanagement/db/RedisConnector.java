package de.fileinputstream.mytraz.worldmanagement.db;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;


public class RedisConnector {

    public JedisPool jedisPool;

    public RedisConnector() {
    }

    /**
     * Connects to the redis database with host and port.
     *
     * @param host
     * @param port
     */
    public void connectToRedis(String host, int port) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(0);
        config.setMinIdle(1);
        config.setMaxWaitMillis(30000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        config.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        config.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        config.setNumTestsPerEvictionRun(3);
        config.setBlockWhenExhausted(true);
        jedisPool = new JedisPool(config, host, port);
        System.out.println("Connected to redis server at " + host + ":" + port + " using password: no");
    }

    /**
     * Connects to the redis database with host, port and password.
     *
     * @param host
     * @param port
     * @param password
     */
    public void connectToRedis(String host, int port, String password) {
        jedisPool = new JedisPool(host, port);
        jedisPool.getResource().auth(password);
        System.out.println("Connected to redis server at " + host + ":" + port + " using password: yes");
    }

    /**
     * Returns a @{@link Jedis} instance from the Jedis pool.
     *
     * @return
     */
    public Jedis getJedis() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis;
        }
    }
}
