package com.semester.tinder.DbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static Connection connection = null;

    public static Connection getConnection(){

        if( connection != null ){
            return connection;
        }else{
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/semester4";
            String user = "root";
            String password = "123456";

            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url,user, password);
                connection.setReadOnly(false); // set read-only to false
            }catch ( ClassNotFoundException | SQLException e){
                throw new RuntimeException(e);
            }
        }

        return connection;
    }
}
