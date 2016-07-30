package com.getusroi.ui.tenantconfig;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.Context;

import com.getusroi.config.persistence.dao.ConfigNodeDataDAO;
import com.getusroi.config.persistence.dao.DataBaseUtil;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class CheckCredentialsDAO {
	Logger logger = LoggerFactory.getLogger(ConfigNodeDataDAO.class);

	public static final String CHECKCREDENTIALS = "SELECT * FROM login WHERE username=? and password=?";

	/**
	 * mmethod to fetch login user details from DB and if matches with the
	 * inputed username, password it returns the corresponding tenant
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws AuthenticationFailedException 
	 */
	public String verifyLoginDetails(String username, String password) throws AuthenticationFailedException {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = DataBaseUtil.getConnection();
			ps = (PreparedStatement) conn.prepareStatement(CHECKCREDENTIALS);
			ps.setString(1, username);
			ps.setString(2, password);

			rs = ps.executeQuery();

			while (rs.next()) {
				String Username = rs.getString("username");
				String Password = rs.getString("password");
				String tenant = rs.getString("tenant");
				String nodeid = rs.getString("nodeid");

				if (Username.equals(username) && Password.equals(password))
					return tenant;
			}
		} catch (ClassNotFoundException | SQLException | IOException exp) {
			/*logger.error("Unabel to authenticate username and password..");*/
			throw new AuthenticationFailedException("Unabel to authenticate username and password..",exp);
		} finally {
			DataBaseUtil.dbCleanup(conn, ps, rs);
		}
		return "";
	}

}
