package com.getusroi.ui.test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import play.Logger;

import com.getusroi.config.persistence.dao.DataBaseUtil;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class InsertUserDetailsWithPasswordInMD5 {
	
	public static final String INSERTUSERDETAILS = "INSERT INTO login (nodeid, username, password, tenant) VALUES (?, ?, ?, ?)";
	
	@Test
	public void testInsertUserDetailsWithPasswordInMD5(){
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String generatedHashPassword;
		try {
			conn = DataBaseUtil.getConnection();
			ps = (PreparedStatement) conn.prepareStatement(INSERTUSERDETAILS);
			ps.setInt(1, 111);
			ps.setString(2, "joydeep");
			ps.setString(3, DigestUtils.md5Hex("paul"));
			ps.setString(4, "gap2");
			
			ps.executeUpdate();
			rs=ps.getGeneratedKeys();
			
			if (rs.next()) {
				generatedHashPassword=rs.getString("password"); 
				Logger.debug("generatedHashPassword :: "+generatedHashPassword);
			}else{
				Logger.debug("generatedHashPassword Resultset not found");
			}
			
		}catch(ClassNotFoundException | IOException | SQLException exp){
			Logger.debug("Unable to insert data into table.. ");
		}finally {
			DataBaseUtil.dbCleanup(conn, ps, rs);
		}
	}
	
}
