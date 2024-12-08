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

import io.haibiiin.github.sku.adaptor.SkuMapper;
import io.haibiiin.github.sku.adaptor.SkuRepositoryAdaptor;
import java.io.IOException;
import java.util.Properties;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Initializer {
    
    private BeanContainer container;
    
    public Initializer(String config) throws IllegalArgumentException, IOException {
        if (config == null || config.isEmpty()) {
            throw new IllegalArgumentException("Initializer must need a properties file");
        }
        this.container = new BeanContainer();
        
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/" + config));
        String mybatisConfig = properties.getProperty("mybatis");
        if (mybatisConfig == null || mybatisConfig.isEmpty()) {
            throw new IllegalArgumentException("Initializer properties need value of mybatis");
        }
        
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(mybatisConfig));
        SqlSession session = sqlSessionFactory.openSession();
        SkuMapper skuMapper = session.getMapper(SkuMapper.class);
        
        String beanName = properties.getProperty("SkuRepositoryAdaptor");
        if (beanName == null || beanName.isEmpty()) {
            beanName = "skuRepositoryAdaptor";
        }
        SkuRepositoryAdaptor skuRepositoryAdaptor = new SkuRepositoryAdaptor(skuMapper);
        this.container.put(beanName, skuRepositoryAdaptor);
    }
    
    public BeanContainer container() {
        return container;
    }
}
