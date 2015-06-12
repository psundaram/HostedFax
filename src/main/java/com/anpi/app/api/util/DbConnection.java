package com.anpi.app.api.util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class DbConnection {
	
	 private Connection createConnection(){
	        Connection connection_mysql = null;
	        try {  
	            Class.forName("com.mysql.jdbc.Driver");  
	             connection_mysql = (Connection) DriverManager.getConnection("jdbc:mysql://10.5.3.203:3306/fax_db","root","123456");  
	     } catch (Exception e) {  
	           e.printStackTrace();  
	        } 
	        return connection_mysql;
	    }
	 
	 public int insert(String sql) {
	    	System.out.println("entering insert" + sql);
	    	boolean success = false;
	    	int insertId = 0;
	    	DbConnection dbConnect = new DbConnection();
	    	Connection con = dbConnect.createConnection();
	    	Statement stmt;
	    	try {
	    		stmt = con.createStatement();
	    		stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
	    		ResultSet rs = stmt.getGeneratedKeys();
	    		while (rs.next()) {
	    			insertId = rs.getInt(1);
	    		}
	    		success = true;
	    	} catch (SQLException ex) {
	    		System.out.println(ex.getMessage());
	    	} finally {
	    		// Close connections be handled as functions with exception handling.
	    		try {
	    			con.close();
	    		} catch (SQLException ex) {
	    			System.out.println(ex.getMessage());
	    		}
	    	}
	    	System.out.println("exiting insert -->"+insertId);
	    	return insertId;
	    }
	    

}
