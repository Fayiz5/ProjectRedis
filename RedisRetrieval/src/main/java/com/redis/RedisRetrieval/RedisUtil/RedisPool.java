package com.redis.RedisRetrieval.RedisUtil;

package tss.wicp;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

import org.apache.log4j.Logger;

public class RedisPool {

    //address of your redis server
    private static String redisUri = "redis://localhost:6379";

    private static final Logger mLogger = Logger.getLogger(RedisPool.class);
    //the jedis connection pool..
    private static JedisPool pool = null;

    public RedisPool() {
        //configure our pool connection
        //pool = new JedisPool(redisHost, redisPort);
    }

    public RedisPool(String host, Integer port) {
        redisUri = "rediss://" + host + ":" + Integer.toString(port);
        //configure our pool connection
        //pool = new JedisPool(redisHost, redisPort);
    }

    public RedisPool(String uri) {
        redisUri = uri;
        //configure our pool connection
        //      JedisPoolConfig poolConfig=new JedisPoolConfig();
        //    poolConfig.setMaxWaitMillis(10000);
        //      poolConfig.setMaxTotal(20);
        //      poolConfig.setMaxIdle(5);
        //      poolConfig.setMinIdle(1);
        //      poolConfig.setTestOnBorrow(true);
        //      poolConfig.setTestOnReturn(true);
        //      poolConfig.setTestWhileIdle(true);
        //      poolConfig.setNumTestsPerEvictionRun(10);
        //      poolConfig.setTimeBetweenEvictionRunsMillis(60000);
        //pool = new JedisPool(new JedisPoolConfig(), uri);
    }

    private JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }


    final JedisPoolConfig poolConfig = buildPoolConfig();

    public boolean init() {
        try {
            //pool = new JedisPool(poolConfig, "10.0.5.77", 6379);

            pool = new JedisPool(poolConfig, new URI(redisUri));
        } catch (Exception e) {
            mLogger.error("Exception occured :" + e.toString());
            return false;
        }
        return isConnected();
    }

    public boolean isConnected() {
        //get a jedis connection jedis connection pool
        boolean lRetVal = false;
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            lRetVal = jedis.isConnected();
        } catch (JedisConnectionException e) {
            mLogger.error("Exception occured :" + e.toString());
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return lRetVal;
    }

    public boolean setKey(String key, String val) {
        //get a jedis connection jedis connection pool
        try {
            Jedis jedis = pool.getResource();
            boolean retVal = false;
            if (jedis.isConnected()) {
                try {
                    //save to redis
                    retVal = (jedis.set(key, val) != null);
                } catch (JedisException e) {
                    //if something wrong happen, return it back to the pool
                    if (null != jedis) {
                        jedis.close();
                        return false;
                    }
                } finally {
                    ///it's important to return the Jedis instance to the pool once you've finished using it
                    if (null != jedis)
                        jedis.close();
                }
            }
            return retVal;
        } catch (JedisConnectionException e) {
            mLogger.error("Exception occured :" + e.toString());
        }
        return false;
    }

    public boolean setKey(String key, String val, int expiry) {
        //get a jedis connection jedis connection pool
        try {
            Jedis jedis = pool.getResource();
            boolean retVal = false;
            if (jedis.isConnected()) {
                try {
                    //save to redis
                    retVal = (jedis.set(key, val) != null);
                    if (retVal & expiry > 0) {
                        jedis.expire(key, expiry);
                    }
                } catch (JedisException e) {
                    //if something wrong happen, return it back to the pool
                    if (null != jedis) {
                        jedis.close();
                        return false;
                    }
                } finally {
                    ///it's important to return the Jedis instance to the pool once you've finished using it
                    if (null != jedis)
                        jedis.close();
                }
            }
            return retVal;
        } catch (JedisConnectionException e) {
            mLogger.error("Exception occured :" + e.toString());
        }
        return false;
    }

    public boolean pushReq(String listName, String val, int expiry) {
        //get a jedis connection jedis connection pool
        //int expiry = 60;
        try {
            Jedis jedis = pool.getResource();
            boolean retVal = false;
            if (jedis.isConnected()) {
                try {
                    //save to redis
                    retVal = (jedis.rpush(listName, val) > 0);

                    // Expiry can't be set to List Item, instead it can only be set to List
                    if (retVal & expiry > 0) {
                        jedis.expire(listName, expiry);
                    }
                } catch (JedisException e) {
                    //if something wrong happen, return it back to the pool
                    if (null != jedis) {
                        jedis.close();
                        return false;
                    }
                } finally {
                    ///it's important to return the Jedis instance to the pool once you've finished using it
                    if (null != jedis)
                        jedis.close();
                }
            }
            return retVal;
        } catch (JedisConnectionException e) {
            mLogger.error("Exception occured :" + e.toString());
        }
        return false;
    }

    public String sendAndReceive(String listName, String txnId, String val, int timeout) {
        String lResponseKey;
        long startTime = System.currentTimeMillis();

        if (!pushReq(listName, val, timeout - 2)) {
            mLogger.error("Txn:" + txnId + " Request Send Failed, Req:" + val);
            return null;
        }
        lResponseKey = "EAPIRESP:" + txnId;
        mLogger.info("Txn:" + txnId + " Request Send to " + listName + " Timeout:" + timeout + " ResponseKey:" + lResponseKey);
        String lResponse = getKey(lResponseKey, timeout);
        if ((lResponse == null || lResponse == "") && ((System.currentTimeMillis() - startTime) >= (timeout * 1000)))
            return "3001:2074: READ_RESPONSE_TIME_OUT:" + txnId;

        mLogger.info("Txn:" + txnId + " Response Received:" + lResponse);
        return lResponse;
    }

    public String getKey(String key) {
        //get a jedis connection jedis connection pool
        try {
            Jedis jedis = pool.getResource();
            String retVal = "";
            if (jedis.isConnected()) {
                try {
                    //save to redis
                    retVal = jedis.get(key);
                    //System.out.println("GetKey :"+retVal);
                } catch (JedisException e) {
                    //if something wrong happen, return it back to the pool
                    mLogger.error("getKey JedisException :" + e.toString());
                    if (null != jedis) {
                        jedis.close();
                        return null;
                    }
                } finally {
                    ///it's important to return the Jedis instance to the pool once you've finished using it
                    if (null != jedis)
                        jedis.close();
                }
            }
            return retVal;
        } catch (JedisConnectionException e) {
            mLogger.error("Exception occured :" + e.toString());
        }
        return null;
    }

    public String getKey(String key, int timeout) {
        //get a jedis connection jedis connection pool
        try {
            Jedis jedis = pool.getResource();
            String retVal = "";
            if (jedis.isConnected()) {
                try {
                    //save to redis
                    long startTime = System.currentTimeMillis();
                    while ((System.currentTimeMillis() - startTime) < (timeout * 1000)) {
                        if (jedis.exists(key))
                            break;
                        else {
                            try {
                                Thread.sleep(2);
                            } catch (java.lang.InterruptedException e) {
                                mLogger.error("Exception occured : " + e.toString());
                            }
                        }
                    }
                    retVal = jedis.get(key);
                    //System.out.println("GetKey :"+retVal);
                } catch (JedisException e) {
                    //if something wrong happen, return it back to the pool
                    mLogger.error("getKey JedisException :" + e.toString());
                    if (null != jedis) {
                        jedis.close();
                        return null;
                    }
                } finally {
                    ///it's important to return the Jedis instance to the pool once you've finished using it
                    if (null != jedis)
                        jedis.close();
                }
            }
            return retVal;
        } catch (JedisConnectionException e) {
            mLogger.error("Exception occured :" + e.toString());
        }
        return null;
    }

    public boolean deleteKey(String key) {
        try (Jedis jedis = pool.getResource()) {
            if (jedis.isConnected()) {
                try {
                    // Delete the key from Redis
                    Long deletedKeys = jedis.del(key);
                    if (deletedKeys == 1) {

                        return true;
                    } else {

                        return false;
                    }
                } catch (JedisException e) {
                    mLogger.error("deleteKey JedisException: " + e.toString());
                    // Handle JedisException
                }
            }
        } catch (JedisConnectionException e) {
            mLogger.error("Exception occurred: " + e.toString());
            // Handle JedisConnectionException
        }
        return false;
    }


    public String getClientList() {
        String lClientsList = null;
        //get a jedis connection jedis connection pool
        try {
            Jedis jedis = pool.getResource();
            if (jedis.isConnected()) {
                try {
                    lClientsList = jedis.clientList();
                } catch (JedisException e) {
                    //if something wrong happen, return it back to the pool
                    mLogger.error("clientList JedisException :" + e.toString());
                    if (null != jedis) {
                        jedis.close();
                        return null;
                    }
                } finally {
                    ///it's important to return the Jedis instance to the pool once you've finished using it
                    if (null != jedis)
                        jedis.close();
                }
            }
            return lClientsList;
        } catch (JedisConnectionException e) {
            mLogger.error("Exception occured :" + e.toString());
        }
        return null;
    }

    public static void main(String[] args) {
        RedisPool main = new RedisPool(args[0]);
        main.init();
        System.out.println("Jedis Connectced : " + main.isConnected());
        //main.setKey("TEST","KOMAL");
        //System.out.println("KEY Value from Redis:"+main.getKey(args[1]));
        //System.out.println("KEY Value from Redis:"+main.getKey(args[1], 20));
        long nano_startTime = System.nanoTime();
        long millis_startTime = System.currentTimeMillis();
        String lClientList = main.getClientList();
        System.out.println("REDIS Clients list: " + lClientList);
        long nano_endTime = System.nanoTime();
        long millis_endTime = System.currentTimeMillis();
        System.out.println("Time taken in nano seconds: " + (nano_endTime - nano_startTime));
        System.out.println("Time taken in milli seconds: " + (millis_endTime - millis_startTime));

        if (lClientList.contains(" name=" + args[1] + " ")) {
            System.out.println("Client: " + args[1] + " is Connected to REDIS");
        } else {
            System.out.println("Client: " + args[1] + " is Not Connected to REDIS");

        }
        nano_endTime = System.nanoTime();
        millis_endTime = System.currentTimeMillis();
        System.out.println("Time taken in nano seconds: " + (nano_endTime - nano_startTime));
        System.out.println("Time taken in milli seconds: " + (millis_endTime - millis_startTime));
    }
}


