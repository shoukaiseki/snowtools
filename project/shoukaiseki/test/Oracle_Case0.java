/*
 * Copyright 1999-2011 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;

import javax.sql.DataSource;


import org.apache.commons.dbcp.BasicDataSource;

import com.alibaba.druid.pool.DruidDataSource;


public class Oracle_Case0 {

    private String   jdbcUrl;
    private String   user;
    private String   password;
    private String   driverClass;
    private int      initialSize     = 10;//10
    private int      minPoolSize     = 15;//1//10
    private int      maxPoolSize     = 30;//2//20
    private int      maxActive       = 25;//2//11
    private String   validationQuery = "SELECT 'asus' FROM DUAL";

    public final int LOOP_COUNT      = 5;

    protected void setUp() throws Exception {
    	jdbcUrl = "jdbc:oracle:thin:@172.22.48.101:1521:orcl";
    	user = "maximo7001";
        password = "maximo";
        driverClass = "oracle.jdbc.driver.OracleDriver";
//    	jdbcUrl = "jdbc:oracle:thin:@172.22.48.254:1522:eamcrpjs";
//    	user = "maxdb";
//        password = "maximo";
//        driverClass = "oracle.jdbc.driver.OracleDriver";
        
    }

    public void test_0() throws Exception {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minPoolSize);
        dataSource.setMaxIdle(maxPoolSize);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(jdbcUrl);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestOnBorrow(true);

        for (int i = 0; i < LOOP_COUNT; ++i) {
            p0(dataSource, "druid");
        }
        System.out.println();
    }

    public void test_1() throws Exception {
        final BasicDataSource dataSource = new BasicDataSource();

        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minPoolSize);
        dataSource.setMaxIdle(maxPoolSize);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(jdbcUrl);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestOnBorrow(true);

        for (int i = 0; i < LOOP_COUNT; ++i) {
            p0(dataSource, "dbcp");
        }
        System.out.println();
    }

    private void p0(DataSource dataSource, String name) throws SQLException {
        long startMillis = System.currentTimeMillis();

        final int COUNT = 1000 * 1;
        for (int i = 0; i < COUNT; ++i) {
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT program FROM V$SESSION WHERE AUDSID = USERENV('SESSIONID')";
            			sql="select * from shoukaiseki_insert_kks";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
//            System.out.println("program="+rs.getString(1));
            rs.close();
            stmt.close();
            conn.close();
        }
        long millis = System.currentTimeMillis() - startMillis;

        System.out.println(name + " millis : " + NumberFormat.getInstance().format(millis) );
    }
    
    public static void main(String[] args) throws Exception {
		Oracle_Case0 o = new Oracle_Case0();
		o.setUp();
		o.test_0();
		o.test_1();
	}
}
