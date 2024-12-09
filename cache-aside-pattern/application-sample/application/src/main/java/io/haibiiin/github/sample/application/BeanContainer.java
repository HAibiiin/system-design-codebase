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
package io.haibiiin.github.sample.application;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BeanContainer {
    
    ConcurrentMap<String, Object> container;
    
    public BeanContainer() {
        this.container = new ConcurrentHashMap<>();
    }
    
    void put(String name, Object bean) {
        this.container.put(name, bean);
    }
    
    public Object get(String name) {
        return this.container.getOrDefault(name, null);
    }
}
