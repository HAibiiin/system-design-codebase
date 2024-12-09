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
package io.haibiiin.github.sku.adaptor;

import io.haibiiin.github.sku.domain.Sku;
import io.haibiiin.github.sku.domain.support.Name;
import java.sql.Timestamp;

public class SkuEntity {
    
    Long id;
    
    String name;
    
    Timestamp dataTimestamp;
    
    public SkuEntity(Long id, String name, Timestamp timestamp) {
        this.id = id;
        this.name = name;
        this.dataTimestamp = timestamp;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Timestamp getDataTimestamp() {
        return dataTimestamp;
    }
    
    public void setDataTimestamp(Timestamp dataTimestamp) {
        this.dataTimestamp = dataTimestamp;
    }
    
    public Sku convert() {
        return new Sku(this.id, new Name(this.name));
    }
}
