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
package io.haibiiin.github.sku.domain.support;

public class Name {
    
    private String name;
    
    public Name(String name) {
        if (null == name || name.isEmpty()) {
            throw new IllegalArgumentException("The field of name must not be null or empty");
        }
        if (name.length() > 20) {
            throw new IllegalArgumentException("The field of name's length must be less than 20");
        }
        this.name = name;
    }
    
    public String name() {
        return name;
    }
}
