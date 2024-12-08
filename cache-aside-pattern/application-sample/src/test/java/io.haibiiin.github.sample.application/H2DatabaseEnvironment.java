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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import org.h2.tools.RunScript;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class H2DatabaseEnvironment {
    
    private final Logger log = LoggerFactory.getLogger(H2DatabaseEnvironment.class);
    
    BeanContainer container;
    Connection connection;
    Server server;
    
    final String[] H2ServerArgs = new String[]{"-tcp", "-tcpAllowOthers", "-tcpPort", "8043", "-ifNotExists"};
    final String connectionUrl = "jdbc:h2:tcp://localhost:8043/mem:test;MODE=MYSQL;DB_CLOSE_DELAY=-1";
    final String[] connectionKeys = new String[]{"", ""};
    
    public void startH2Server() {
        try {
            server = Server.createTcpServer(H2ServerArgs).start();
        } catch (SQLException e) {
            log.error("Start H2 server has exception :\n", e);
            throw new RuntimeException(e);
        }
        
        try {
            connection = DriverManager.getConnection(connectionUrl, connectionKeys[0], connectionKeys[1]);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            log.error("Connect H2 database has exception :\n", e);
            throw new RuntimeException(e);
        }
    }
    
    public void initializeSchema(String scriptPath) {
        try {
            String path = Objects.requireNonNull(this.getClass().getResource(scriptPath)).getPath();
            RunScript.execute(connection, new FileReader(path));
        } catch (FileNotFoundException e) {
            log.error("Sql script file not exist :\n", e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            log.error("Sql script execute has exception :\n", e);
            throw new RuntimeException(e);
        }
    }
    
    public void initializeData(String scriptPath) {
        try {
            String path = Objects.requireNonNull(this.getClass().getResource(scriptPath)).getPath();
            RunScript.execute(connection, new FileReader(path));
        } catch (FileNotFoundException e) {
            log.error("Sql script file not exist :\n", e);
            throw new RuntimeException(e);
        } catch (SQLException e) {
            log.error("Sql script execute has exception :\n", e);
            throw new RuntimeException(e);
        }
    }
    
    public void stopH2Server() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        if (server != null) {
            server.stop();
        }
    }
    
}
