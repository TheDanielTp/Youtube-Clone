package com.wetube.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection
{
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/WeTube-DataBase";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "123456789";

    public static Connection getConnection () throws SQLException
    {
        return DriverManager.getConnection (JDBC_URL, USER, PASSWORD);
    }
}
