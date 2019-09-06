package tech.codecozy.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DBUtil {
	
	public DBUtil() {}
	
	boolean checkLoginCredentials(String email, String password){
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select count(*) 'user_count' from cp_user where cp_email = ? and cp_password = ?";			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, new CryptoUtil().encrypt(CryptoUtil.KEY, password));
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
	
	boolean emailExists(String email) {
		
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
	
	boolean usernameExists(String username) {
		
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

	void registerUser(User user) {
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String cryptPass = new CryptoUtil().encrypt(CryptoUtil.KEY,user.getPassword());
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
	
	void updatePassword(String email, String password) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			password = new CryptoUtil().encrypt(CryptoUtil.KEY,password);
			
			String sql = "update cp_user set cp_password=? where "
					+ "cp_email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, password);
			stmt.setString(2, email);
			stmt.execute();
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
	}
	
	ArrayList<String> getUsernames() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<String> result = new ArrayList<>();
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select cp_username from cp_user";
			
			stmt = conn.prepareStatement(sql);
		
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				String username = rs.getString("cp_username");
				result.add(username);
			}
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
		return result;
	}
	
	boolean isVerified(String email) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int verificationStatus = 0;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select cp_isverified from cp_user where cp_email = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				verificationStatus = rs.getInt("cp_isverified");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		return (verificationStatus == 1)? true: false;
	}
	
	void markVerificationMailSentTime(String email) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long timestamp = System.currentTimeMillis();
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "update cp_user set cp_verificationMailSentTime=? where cp_email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, timestamp);
			stmt.setString(2, email);
			
			stmt.execute();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}

	}
	
	boolean isUserVerificationTimedOut(String email) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long timestamp = System.currentTimeMillis();
		boolean res = true;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select cp_verificationMailSentTime from cp_user where cp_email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			
			rs = stmt.executeQuery();
			if(rs.next()) {
				long time = rs.getLong("cp_verificationMailSentTime");
				long till = time + 15L*60L*1000L;
				if(timestamp<=till) {
					res = false;
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		return res;
	}
	
	void markVerified(String email) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "update cp_user set cp_isverified=? where cp_email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, 1);
			stmt.setString(2, email);
			
			stmt.execute();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
	}
}
