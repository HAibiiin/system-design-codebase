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
package io.haibiiin.github.query.engine;

public class SimpleQueryEngine<T, R> implements QueryCommands<T, R> {
    
    CachePhase<T, R> cachePhase;
    
    DatabasePhase<T, R> databasePhase;
    
    public SimpleQueryEngine(CachePhase<T, R> cachePhase, DatabasePhase<T, R> databasePhase) {
        this.cachePhase = cachePhase;
        this.databasePhase = databasePhase;
    }
    
    public T get(R r) {
        T result = cachePhase.get(r);
        if (result == null) {
            result = databasePhase.get(r);
            cachePhase.set(r, result);
        }
        return result;
    }
    
}
