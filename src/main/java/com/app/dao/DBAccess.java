package com.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.app.model.User;

@Component
public class DBAccess {
	@PostConstruct 
	private void init(){
		try {
			Class.forName("io.crate.client.jdbc.CrateDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	/*
	 *   Table Definition:
	 *   CREATE TABLE user_info (id int primary key, name string);
	 */
	
	public int insertRecord(User user){
		Connection conn = null;
		try{
			conn=DriverManager.getConnection("crate://localhost:4300");
			//conn.setSchema("user_schema");
			
			final String query="insert into user_info(id,name) values(?,?)";
			PreparedStatement preparedStmt=conn.prepareStatement(query);
			preparedStmt.setInt(1,user.getId());
			preparedStmt.setString(2, user.getName()+"-"+user.getNATS_Status());
			preparedStmt.executeUpdate();
			return 1;
		}catch(Exception ex){
			ex.printStackTrace();
			return -1;
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

}
