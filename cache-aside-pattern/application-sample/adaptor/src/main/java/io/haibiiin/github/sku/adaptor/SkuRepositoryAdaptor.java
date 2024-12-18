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

import io.haibiiin.github.query.engine.DatabasePhase;
import io.haibiiin.github.sku.SkuRepository;
import io.haibiiin.github.sku.domain.Sku;

public class SkuRepositoryAdaptor implements SkuRepository, DatabasePhase<Sku, Long> {
    
    SkuMapper skuMapper;
    
    public SkuRepositoryAdaptor(SkuMapper skuMapper) {
        this.skuMapper = skuMapper;
    }
    
    @Override
    public Sku get(long id) {
        SkuEntity entity = this.skuMapper.getById(id);
        if (entity == null) {
            return null;
        }
        return entity.convert();
    }
    
    @Override
    public Sku get(Long aLong) {
        return get((long) aLong);
    }
}
