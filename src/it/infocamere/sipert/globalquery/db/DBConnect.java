package it.infocamere.sipert.globalquery.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import it.infocamere.sipert.globalquery.db.dto.SchemaDTO;

public class DBConnect {
	
	static private final String jdbcUrlFirstBlock = "jdbc:oracle:thin:@//";

	public static Connection getConnection(SchemaDTO schema) {
		
		String url = jdbcUrlFirstBlock + schema.getHostServerURL() + ":" + schema.getPort() + "/" + schema.getDbName();
		
        String username = schema.getSchemaUserName();
        String password = schema.getPassword();

        try {
        	
        	return DriverManager.getConnection(url, username, password);
        	
        } catch (SQLException e) {
			//e.printStackTrace();
			throw new RuntimeException("SCHEMA " + schema.getSchemaUserName() + " " + e.toString());
        }
		
	}

}
