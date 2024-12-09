/*
 * Copyright 2024 HAibiiin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.haibiiin.github.cache;

import io.haibiiin.github.query.engine.CachePhase;
import io.haibiiin.github.query.engine.DatabasePhase;
import io.haibiiin.github.query.engine.SimpleQueryEngine;
import io.haibiiin.github.query.engine.cache.CacheCommands;
import io.haibiiin.github.query.engine.cache.jedis.JedisWrapper;
import io.haibiiin.github.query.engine.cache.jedis.lease.LeaseWrapper;
import io.haibiiin.github.query.engine.cache.map.MapWrapper;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

public class SimpleQueryEngineTests {
    
    @Disabled("Disabled until redis server up!")
    @DisplayName("Operate Redis based on Jedis use lease approach")
    @Test
    public void testLeaseWrapper() throws IOException {
        RedisConnectionEnvInfo redisInfo = new RedisConnectionEnvInfo();
        Jedis jedis = new Jedis(redisInfo.host(), redisInfo.port());
        SimpleQueryEngine<String, String> engine = new SimpleQueryEngine<>(
                new SampleCachePhase(
                        new LeaseWrapper(new JedisWrapper(jedis))),
                new SampleDatabasePhase());
        String result = engine.get("test");
        Assertions.assertEquals("key:test, value:sample value", result);
        jedis.del("test", "lease:test");
        jedis.close();
    }
    
    @Disabled("Disabled until redis server up!")
    @DisplayName("Operate Redis based on Jedis")
    @Test
    public void testJedisWrapper() throws IOException {
        RedisConnectionEnvInfo redisInfo = new RedisConnectionEnvInfo();
        Jedis jedis = new Jedis(redisInfo.host(), redisInfo.port());
        SimpleQueryEngine<String, String> engine = new SimpleQueryEngine<>(
                new SampleCachePhase(
                        new JedisWrapper(jedis)),
                new SampleDatabasePhase());
        String result = engine.get("test");
        Assertions.assertEquals("key:test, value:sample value", result);
        jedis.del("test");
        jedis.close();
    }
    
    @DisplayName("Base on ConcurrentMap cache")
    @Test
    public void testMapWrapper() {
        SimpleQueryEngine<String, String> engine = new SimpleQueryEngine<>(
                new SampleCachePhase(new MapWrapper()),
                new SampleDatabasePhase());
        
        String result = engine.get("test");
        Assertions.assertEquals("key:test, value:sample value", result);
        result = engine.get("test");
        Assertions.assertEquals("key:test, value:sample value from cache", result);
    }
    
    class SampleCachePhase implements CachePhase<String, String> {
        
        CacheCommands cache;
        
        public SampleCachePhase(CacheCommands cache) {
            this.cache = cache;
        }
        
        @Override
        public String get(String key) {
            return cache.get(key);
        }
        
        @Override
        public void set(String s, String s2) {
            this.cache.set(s, s2);
        }
    }
    
    class SampleDatabasePhase implements DatabasePhase<String, String> {
        
        @Override
        public String get(String s) {
            return "key:" + s + ", value:sample value";
        }
    }
}
