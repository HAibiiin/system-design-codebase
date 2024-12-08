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

import io.haibiiin.github.sample.application.sku.SkuAppService;
import io.haibiiin.github.sku.adaptor.SkuRepositoryAdaptor;
import io.haibiiin.github.sku.domain.Sku;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkuApplicationServiceTests extends H2DatabaseEnvironment {
    
    private final Logger log = LoggerFactory.getLogger(SkuApplicationServiceTests.class);

    @BeforeEach
    public void setUp() {
        super.startH2Server();
        super.initializeSchema("/sql/schema.sql");
        super.initializeData("/sql/data.sql");

        Initializer initializer;
        try {
            initializer = new Initializer("test.properties");
        } catch (IOException e) {
            log.error("Initializer failure :\n", e);
            throw new RuntimeException(e);
        }
        container = initializer.container();
    }

    @DisplayName("Base on database get data")
    @Test
    public void testGet() {
        SkuRepositoryAdaptor adaptor = (SkuRepositoryAdaptor) container.get("skuRepositoryAdaptor");
        SkuAppService service = new SkuAppService(adaptor);

        Sku sku = service.get(1L);
        Assertions.assertNotNull(sku);
        Assertions.assertEquals(1, sku.id());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        super.stopH2Server();
    }
}
