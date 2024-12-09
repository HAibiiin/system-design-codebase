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
package io.haibiiin.github.query.engine.cache.map;

import io.haibiiin.github.query.engine.cache.CacheCommands;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapWrapper implements CacheCommands {
    
    ConcurrentMap<String, String> map;
    
    public MapWrapper() {
        this.map = new ConcurrentHashMap<>();
    }
    
    @Override
    public String get(String key) {
        return this.map.getOrDefault(key, null);
    }
    
    @Override
    public String set(String key, String value) {
        return this.map.put(key, value + " from cache");
    }
}
