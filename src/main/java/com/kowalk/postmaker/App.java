package com.kowalk.postmaker;

import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SQLException
    {
        System.out.println( "Hello World!" );

        String url = "jdbc:postgresql://localhost:5432/postgres";

        DbService db = new DbService("postgres", "", url);
        db.getAll();
    }
}
