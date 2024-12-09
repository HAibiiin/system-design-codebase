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
package io.haibiiin.github.query.engine.cache.jedis.lease;

import io.haibiiin.github.query.engine.cache.CacheCommands;
import io.haibiiin.github.query.engine.cache.LuaScripts;
import io.haibiiin.github.query.engine.cache.jedis.EvalResult;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

public class LeaseWrapper extends Jedis implements CacheCommands {
    
    private final Logger log = LoggerFactory.getLogger(LeaseWrapper.class);
    
    private final Jedis jedis;
    private final TokenGenerator tokenGenerator;
    private final ThreadLocal<String> tokenHolder;
    
    public LeaseWrapper(Jedis jedis) {
        this.jedis = jedis;
        this.tokenHolder = new ThreadLocal<>();
        this.tokenGenerator = () -> UUID.randomUUID().toString();
    }
    
    public LeaseWrapper(Jedis jedis, TokenGenerator tokenGenerator) {
        this.jedis = jedis;
        tokenHolder = new ThreadLocal<>();
        this.tokenGenerator = tokenGenerator;
    }
    
    @Override
    public String get(String key) {
        String token = this.tokenGenerator.get();
        tokenHolder.set(token);
        log.debug("Call redis eval command, key:{}, token:{}", key, token);
        Object result = this.jedis.eval(LuaScripts.leaseGet(), List.of(key), List.of(token));
        EvalResult er = new EvalResult((List<?>) result);
        if (er.effect()) {
            return er.value();
        }
        return null;
    }
    
    @Override
    public String set(String key, String value) {
        String token = tokenHolder.get();
        tokenHolder.remove();
        log.debug("Call redis eval command, key:{}, token:{}, value:{}", key, token, value);
        Object result = this.jedis.eval(LuaScripts.leaseSet(), List.of(key), List.of(token, value));
        EvalResult er = new EvalResult((List<?>) result);
        if (er.effect()) {
            return er.value();
        }
        return null;
    }
    
}
