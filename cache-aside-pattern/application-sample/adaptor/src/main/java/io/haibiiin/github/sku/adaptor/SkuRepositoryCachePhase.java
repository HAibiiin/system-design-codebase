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

import io.haibiiin.github.query.engine.CachePhase;
import io.haibiiin.github.query.engine.cache.CacheCommands;
import io.haibiiin.github.sku.domain.Sku;
import io.haibiiin.github.sku.domain.support.Name;

public class SkuRepositoryCachePhase implements CachePhase<Sku, Long> {
    
    CacheCommands cacheCommands;
    
    public SkuRepositoryCachePhase(CacheCommands cacheCommands) {
        this.cacheCommands = cacheCommands;
    }
    
    @Override
    public Sku get(Long aLong) {
        String skustr = cacheCommands.get(aLong.toString());
        return convert(skustr);
    }
    
    @Override
    public void set(Long aLong, Sku sku) {
        String skustr = convert(sku);
        if (skustr == null) {
            return;
        }
        cacheCommands.set(aLong.toString(), skustr);
    }
    
    private String convert(Sku sku) {
        if (sku == null) {
            return null;
        }
        return sku.id() + "," + sku.name();
    }
    
    private Sku convert(String skustr) {
        if (skustr == null || skustr.isEmpty()) {
            return null;
        }
        String[] items = skustr.split(",");
        return new Sku(Long.getLong(items[0]), new Name(items[1]));
    }
}
