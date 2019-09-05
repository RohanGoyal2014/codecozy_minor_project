package tech.codecozy.app;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtil {
	
	public DBUtil() {}
	
	public static String cryptWithMD5(String pass) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
	    byte[] passBytes = pass.getBytes();
	    md.reset();
	    byte[] digested = md.digest(passBytes);
	    StringBuffer sb = new StringBuffer();
	    for(int i=0;i<digested.length;i++){
	        sb.append(Integer.toHexString(0xff & digested[i]));
	    }
	    return sb.toString();
	}
	
	public boolean checkLoginCredentials(String email, String password){
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select count(*) 'user_count' from cp_user where cp_email = ? and cp_password = ?";			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, cryptWithMD5(password));
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getInt("user_count") == 1) return true;
				return false;
			}
			
			return false;
			
				
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
	}
	
	public boolean emailExists(String email) {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select count(*) 'user_count' from cp_user where cp_email = ?";			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getInt("user_count") == 1) return true;
				return false;
			}
			
			return false;
			
				
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
	}
	
public boolean usernameExists(String username) {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select count(*) 'user_count' from cp_user where cp_username = ?";			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getInt("user_count") == 1) return true;
				return false;
			}
			
			return false;
			
				
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
	}

	public void registerUser(User user) {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String cryptPass = cryptWithMD5(user.getPassword());
			user.setPassword(cryptPass);
			
			String sql = "insert into "
					+ "cp_user(cp_username,cp_email,cp_fname,cp_lname,cp_gender,cp_password) "
					+ "values(?,?,?,?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getFname());
			stmt.setString(4, user.getLname());
			stmt.setString(5, String.valueOf(user.getGender()));
			stmt.setString(6, user.getPassword());
			stmt.execute();
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
	}
}
