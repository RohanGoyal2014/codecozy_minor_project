package tech.codecozy.app;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
	
	ArrayList<Contest> fetchContests() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Contest> result = new ArrayList<>();
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select * from contest order by ct_start";
			
			stmt = conn.prepareStatement(sql);
		
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				long id = rs.getLong("ct_id");
				String name = rs.getString("ct_name");
				long start = rs.getLong("ct_start");
				long end = rs.getLong("ct_end");
				result.add(new Contest(id, name , start, end,
						new Date(new Timestamp(start).getTime()), new Date(new Timestamp(end).getTime()),
						rs.getString("ct_editorial_link")));
			}
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
		return result;
	}
	
	boolean isAdmin(String email) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean res =  false;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select cp_userlevel from cp_user where cp_email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
		
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getInt("cp_userlevel")!=0) res=true;
			}
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
		return res;
	}
	
	User getUserByUsername(String username) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		User user = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select * from cp_user where cp_username=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
		
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				user = new User(rs.getString("cp_fname"),rs.getString("cp_lname"),
						rs.getString("cp_email"),username,null,rs.getString("cp_gender").charAt(0));
			}
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		return user;
	}
	
	User getUserByEmail(String email) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		User user = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select * from cp_user where cp_email=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
		
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				user = new User(rs.getString("cp_fname"),rs.getString("cp_lname"),
						rs.getString("cp_email"),rs.getString("cp_username"),null,rs.getString("cp_gender").charAt(0));
			}
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		return user;
	}
	
	long createContestAndGetID(Contest contest) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "insert into contest(ct_name,ct_start,ct_end,ct_editorial_link) values(?,?,?,?)";
			
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, contest.getName());
			stmt.setLong(2, contest.getStart());
			stmt.setLong(3, contest.getEnd());
			stmt.setString(4, contest.getEditorialLink());
		
			stmt.executeUpdate();
			
			rs = stmt.getGeneratedKeys();
			
			if(rs!=null && rs.next()) {
				int id = rs.getInt(1);
				return id;
			}
			
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
		return -1;
	}
	
	long addProblemToContest(Problem problem) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "insert into problem(pb_name,pb_description,ct_id) values(?,?,?)";
			
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, problem.getName());
			stmt.setString(2, problem.getLink());
			stmt.setLong(3, problem.getContestId());
			
			stmt.executeUpdate();
			
			rs = stmt.getGeneratedKeys();
			
			if(rs!=null && rs.next()) {
				return rs.getInt(1);
			}
			
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
		return -1;
	}
	
	void addTestCase(TestCase tc) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "insert into test_case(pb_id,tc_input,tc_output) values(?,?,?)";
			
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, tc.getProblemID());
			stmt.setString(2, tc.getInputPath());
			stmt.setString(3, tc.getOutputPath());
			
			stmt.execute();
			
			
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
	}
	
	boolean isSuperAdmin(String email) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select cp_userlevel from cp_user where cp_email = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getInt("cp_userlevel")==2) {
					return true;
				}
			}
			
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		return false;
	}
	
	void removeContest(long id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select pb_id from problem where ct_id=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1,id);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				long pbID = rs.getLong("pb_id");
				removeProblem(pbID);
			}
			sql = "delete from contest where ct_id=?";
			PreparedStatement stmt2 = conn.prepareStatement(sql);
			stmt2.setLong(1, id);
			stmt2.execute();
			CloudConfig.close(null, stmt2, null);
			
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
	}
	
	private void removeProblem(long id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select tc_id from test_case where pb_id=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1,id);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				long tcID = rs.getLong("tc_id");
				removeTestCase(tcID);
			}
			sql = "delete from problem where pb_id=?";
			PreparedStatement stmt2 = conn.prepareStatement(sql);
			stmt2.setLong(1, id);
			stmt2.execute();
			CloudConfig.close(null, stmt2, null);
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
	}
	
	private void removeTestCase(long id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select * from test_case where tc_id=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1,id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				String inputPath = rs.getString("tc_input");
				String outputPath = rs.getString("tc_output");
				File inputFile = new File(inputPath);
				File outputFile = new File(outputPath);
				inputFile.delete();
				outputFile.delete();
				
				int ipos = inputPath.indexOf("/input");
				int opos = outputPath.indexOf("/output");
				inputPath = inputPath.substring(0, ipos);
				outputPath = outputPath.substring(0, opos);
				inputFile = new File(inputPath);
				outputFile = new File(outputPath);
//				System.out.println(inputPath);
//				System.out.println(outputPath);
				if(inputFile.exists()) {
					inputFile.delete();
				}
				if(outputFile.exists()) {
					outputFile.delete();
				}
				sql = "delete from test_case where tc_id=?";
				PreparedStatement stmt2 = conn.prepareStatement(sql);
				stmt2.setLong(1, id);
				stmt2.execute();
				CloudConfig.close(null, stmt2, null);
			}
				
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
	}
	
	ArrayList<User> getAdmins() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<User> admins = new ArrayList<>();
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select * from cp_user where cp_userlevel=1";
			
			stmt = conn.prepareStatement(sql);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				admins.add(new User(
						rs.getString("cp_fname"),
						rs.getString("cp_lname"),
						rs.getString("cp_email"),
						rs.getString("cp_username"),
						null,'\0'
						));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
		return admins;
	}
	
	void removeAdmin(String username) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "update cp_user set cp_userlevel=0 where cp_username=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			
			stmt.execute();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
	}
	
	void addAdmin(String username) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "update cp_user set cp_userlevel=1 where cp_username=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			
			stmt.execute();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
	}
	
	boolean contestExists(String id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select count(*) 'count' from contest where ct_id=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getInt("count")>0) return true;
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
		return false;
	}
	
	ArrayList<Problem> getProblems(String id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Problem> problems = new ArrayList<>();
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select * from problem where ct_id=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				long pbID = rs.getLong("pb_id");
				String name = rs.getString("pb_name");
				String link = rs.getString("pb_description");
				problems.add(new Problem(pbID,name,link,Long.parseLong(id)));
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
		return problems;
	}
	
	Contest getContest(String id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select * from contest where ct_id=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				String name = rs.getString("ct_name");
				long start = rs.getLong("ct_start");
				long end = rs.getLong("ct_end");
				String editorialLink = rs.getString("ct_editorial_link");
				return new Contest(Long.parseLong(id),name,start,end,null,null,editorialLink);
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
		return null;
	}
	
	boolean problemExists(String id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select count(*) 'count' from problem where pb_id=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getInt("count")>0) return true;
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
		return false;
	}
	
	Problem getProblem(String id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select * from problem where pb_id=?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				String name = rs.getString("pb_name");
				String link = rs.getString("pb_description");
				long ctID = rs.getLong("ct_id");
				return new Problem(Long.parseLong(id),name,link,ctID);
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
		return null;
	}
	
	ArrayList<TestCase> getTestCasesByProblem(long problemId) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<TestCase> testCases = new ArrayList<>();
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select * from test_case where pb_id=? order by tc_id";
			
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, problemId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				long tcId = rs.getLong("tc_id");
				String inputPath = rs.getString("tc_input");
				String outputPath = rs.getString("tc_output");
				testCases.add(new TestCase(tcId,problemId,inputPath,outputPath));
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		
		return testCases;

	}
	
	void makeSubmission(Submission submission) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "insert into submission(pb_id,sb_time,sb_link,cp_email,score) values(?,?,?,?,?)";
			
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, submission.getProblemId());
			stmt.setLong(2, submission.getTime());
			stmt.setString(3, submission.getLink());
			stmt.setString(4, submission.getEmail());
			stmt.setInt(5, submission.getScore());
			
			stmt.execute();
			rs = stmt.getGeneratedKeys();
			
			if(rs.next()) {
				for(int i=0;i<submission.getTcId().size();++i) {
					sql = "insert into submission_breakup(sb_id,tc_id,verdict) values(?,?,?)";
					PreparedStatement stmt2 = conn.prepareStatement(sql);
					stmt2.setLong(1, rs.getLong(1));
					stmt2.setLong(2, submission.getTcId().get(i));
					stmt2.setString(3, submission.getVerdict().get(i));
					stmt2.execute();
					stmt2.close();
				}
				
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
	}
	
	ArrayList<Submission> getSubmissions(long problemId, String email) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Submission> submissions = new ArrayList<>();
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select * from submission where pb_id=? and cp_email=? order by sb_time desc";
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, problemId);
			stmt.setString(2, email);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				String link = rs.getString("sb_link");
				long id = rs.getLong("sb_id");
				long time = rs.getLong("sb_time");
				int score = rs.getInt("score");
				Submission sm = new Submission(id,problemId,time,link,email,null,null);
				sm.setScore(score);
				submissions.add(sm);
				
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		return submissions;
	}
	
	
	ArrayList<Submission> getAllSubmissions(long problemId) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Submission> submissions = new ArrayList<>();
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select * from submission where pb_id=? order by cp_email,sb_time desc";
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, problemId);
			
			rs = stmt.executeQuery();
			
			while(rs.next()) {
				String link = rs.getString("sb_link");
				long id = rs.getLong("sb_id");
				long time = rs.getLong("sb_time");
				int score = rs.getInt("score");
				String email = rs.getString("cp_email");
				Submission sm = new Submission(id,problemId,time,link,email,null,null);
				sm.setScore(score);
				submissions.add(sm);
				
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		return submissions;
	}
	
	Contest getContestByProblemId(long problemId) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = CloudConfig.getConnection();
			
			String sql = "select ct_id from problem where pb_id=?";
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, problemId);
			
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				long contestId = rs.getLong("ct_id");
				return this.getContest(String.valueOf(contestId));
				
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		return null;
	}
	
	HashMap<Long,Integer> getParticipatedContestsByUsername(String username) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap<Long,Integer> map = new HashMap<>();
		
		try {
			conn = CloudConfig.getConnection();
			User user = this.getUserByUsername(username);
			
			String sql = "select pb_id,score from submission where cp_email=?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getEmail());
			
			rs = stmt.executeQuery();
			
			HashMap<Long,Integer> problemToScoreMap = new HashMap<>();
			
			while(rs.next()) {
				long problemId = rs.getLong("pb_id");
				if(!problemToScoreMap.containsKey(problemId)) {
					problemToScoreMap.put(problemId, 0);
				}
				
				int curr = problemToScoreMap.get(problemId);
				problemToScoreMap.replace(problemId, Integer.max(curr, rs.getInt("score")));
			}
			
			for(Map.Entry<Long, Integer> mp: problemToScoreMap.entrySet()) {
				Contest contest = this.getContestByProblemId(mp.getKey());
				if(!map.containsKey(contest.getId())) {
					map.put(contest.getId(), 0);
				}
				int curr = map.get(contest.getId());
				map.replace(contest.getId(), curr+mp.getValue());
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CloudConfig.close(conn, stmt, rs);
		}
		return map;
	}
}
