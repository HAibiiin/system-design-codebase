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

import java.io.IOException;
import java.util.Properties;

public class RedisConnectionEnvInfo {
    
    private String host;
    private int port;
    
    public RedisConnectionEnvInfo() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/test-env.properties"));
        host = properties.getProperty("host");
        port = Integer.parseInt(properties.getProperty("port"));
    }
    
    public String host() {
        return host;
    }
    
    public int port() {
        return port;
    }
}
