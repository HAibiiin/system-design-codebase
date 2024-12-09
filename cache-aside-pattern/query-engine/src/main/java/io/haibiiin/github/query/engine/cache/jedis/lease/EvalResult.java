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

import java.util.List;

public class EvalResult {
    
    String value;
    
    boolean effect;
    
    public EvalResult(List<?> args) {
        value = (String) args.get(0);
        if (args.get(1) == null) {
            effect = false;
        } else {
            effect = 1 == (long) args.get(1);
        }
    }
    
    public String getValue() {
        return value;
    }
}
