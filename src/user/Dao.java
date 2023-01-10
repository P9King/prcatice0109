package user;

import java.io.FileReader;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Dao {
	// 싱글톤
	private static Dao dao = new Dao();
	private Dao() {}
	public static Dao getInstance() {
		return dao;
	} // ------------
	
	private Connection conn = getConnect();
	
	private Connection getConnect() {
		try {
			Properties prop = new Properties();
			String path = Dao.class.getResource("db.properties").getPath();
			path = URLDecoder.decode(path,"utf-8");
			prop.load(new FileReader(path));
			String driver = prop.getProperty("driver");
			String dbURL = prop.getProperty("dbURL");
			String dbID = prop.getProperty("dbID");
			String dbPW = prop.getProperty("dbPW");
			Class.forName(driver);
			conn = DriverManager.getConnection(dbURL, dbID, dbPW);
			
			return conn;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("연결 실패");
		}
		return null;
	}
//	post  ---------------------------------------------------------
	// 게시글 전체 목록 불러오기
	public List<Post> selectPostAll(int index_no){
		List<Post> postList = new ArrayList<>();
		
		String sql = "select * from post where onoff=1 order by postNum desc limit "+index_no+",10";
		Post post = null;
		
		try {
			
			PreparedStatement pstm = conn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				int postNum = rs.getInt("postNum");
				int studentNum = rs.getInt("studentNum");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int likeCount = rs.getInt("likeCount");
				int commentCount = rs.getInt("commentCount");
				String date =  rs.getString("date");
				String board =  rs.getString("board");
				String onoff =  rs.getString("onoff");
				post = new Post(postNum, studentNum, title, content, likeCount, commentCount, date, board, onoff); 
				postList.add(post);
			}
			rs.close();
			pstm.close();
			System.out.println("게시글 전체 목록 리턴");
			return postList;
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("selectPostAll() 에러");
		}
		
		return null;
	}
	
	// 해당 아이디가 작성한 게시글 전체 목록 불러오기
	public List<Post> selectPostID(int idStudentNum,int index_no){
		List<Post> postList = new ArrayList<>();
		String sql = "select * from post where onoff=1 and studentNum= ? order by postNum desc limit ?,10";
		Post post = null;
		try {
			
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setInt(1, idStudentNum);
			pstm.setInt(2, index_no);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				int postNum = rs.getInt("postNum");
				int studentNum = rs.getInt("studentNum");
				String title = rs.getString("title");
				String content = rs.getString("content");
				int likeCount = rs.getInt("likeCount");
				int commentCount = rs.getInt("commentCount");
				String date =  rs.getString("date");
				String board =  rs.getString("board");
				String onoff =  rs.getString("onoff");
				post = new Post(postNum, studentNum, title, content, likeCount, commentCount, date, board, onoff); 
				postList.add(post);
			}
			rs.close();
			pstm.close();
			System.out.println("해당 아이디가 작성한 게시글 전체 목록 리턴");
			return postList;
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("selectPostID() 에러");
		}
		
		return null;
	}
	
	// 게시글 번호로 게시글 제목 찾기
	public List <String> selectPostNum(int postNum){
		List<String> titleList = new ArrayList<>();
		String sql = "select title from post where onoff=1 and postNum= ?";
		String title = null;
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setInt(1, postNum);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				title = rs.getString("title");
				titleList.add(title);
			}
			rs.close();
			pstm.close();
			System.out.println("게시물 번호로 제목 검색");
			return titleList;
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("selectPostID() 에러");
		}
		
		return null;
	}
	
	//총 게시물 개수
	public int countPostAll(){
		
		String sql = "select count(*) total from post where onoff=1";
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			
			ResultSet rsTot = pstm.executeQuery();
			rsTot.next();
			int total = rsTot.getInt("total");
			System.out.println("총 게시물 개수 : "+total+"리턴완료");
			return total;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("countPostAll() 에러");
		}
		return  0;
	}
	
	// 해당 아이디가 작성한 총 게시물 개수
		public int countPostID(int idStudentNum){
			
			String sql = "select count(*) total from post where onoff=1 and studentNum = ?";
			try {
				PreparedStatement pstm = conn.prepareStatement(sql);
				pstm.setInt(1, idStudentNum);
				ResultSet rsTot = pstm.executeQuery();
				rsTot.next();
				int total = rsTot.getInt("total");
				System.out.println("해당 아이디 총 게시물 개수 : "+total+"리턴완료");
				return total;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("countPostID() 에러");
			}
			return  0;
		}
		
	
	
//	--------------------------------------------------------------------
	
//	user  ----------------------------------------------------------------
	// 유저 전체 목록 불러오기
	public List<User> selectUserAll(){
		List<User> userList = new ArrayList<>();
		String sql = "select * from user";
		User user = null;
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				int studentNum = rs.getInt("studentNum");
				String userID =  rs.getString("userID");
				String nickName =  rs.getString("nickName");
				String pw =  rs.getString("pw");
				String email =  rs.getString("email");
				user = new User(studentNum,userID,nickName,pw,email); 
				userList.add(user);
			}
			rs.close();
			pstm.close();
			System.out.println("유저 전체 목록 리턴");
			return userList;
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("selectUserAll() 에러");
		}
		
		return null;
	}
//	--------------------------------------------------------------------
	
//	comment  -------------------------------------------------------------
	// 댓글 전체 목록 불러오기
	public List<Comment> selectCommentAll(){
		List<Comment> commentList = new ArrayList<>();
		String sql = "select * from comment";
		Comment comment = null;
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				int commentNum = rs.getInt("commentNum");
				int postNum =  rs.getInt("postNum");
				int studentNum =  rs.getInt("studentNum");
				String commentContent =  rs.getString("commentContent");
				String date =  rs.getString("date");
				comment = new Comment(commentNum,postNum,studentNum,commentContent,date); 
				commentList.add(comment);
			}
			rs.close();
			pstm.close();
			System.out.println("댓글 전체 목록 리턴");
			return commentList;
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("selectCommentAll() 에러");
		}
		
		return null;
	}
	
	// 해당 아이디가 댓글 단 목록 전체 불러오기(post제목까지)
	public List<Comment> selectCommentID(int idStudentNum, int index_no){
		List<Comment> commentList = new ArrayList<>();
		String sql = "select c.commentNum, c.studentNum, c.postNum, c.commentContent, c.date, p.title " + 
				"from comment c, post p " + 
				"where c.postNum = p.postNum and c.studentNum = ? order by date desc limit ?,10";
		Comment comment = null;
		try {
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setInt(1, idStudentNum);
			pstm.setInt(2, index_no);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				int commentNum =  rs.getInt("commentNum");
				int studentNum =  rs.getInt("studentNum");
				int postNum =  rs.getInt("postNum");
				String commentContent =  rs.getString("commentContent");
				String date =  rs.getString("date");
				String title =  rs.getString("title");
				comment = new Comment(commentNum, postNum, studentNum, commentContent, date, title); 
				commentList.add(comment);
			}
			//System.out.println("해당 아이디가 댓글 단 목록 리턴"+commentList.get(0).getTitle());
			rs.close();
			pstm.close();
			return commentList;
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("selectCommentID() 에러");
		}
		
		return null;
	}
	
	// 해당 아이디가 작성한 총 댓글 개수
		public int countCommentID(int idStudentNum){
			String sql = "select count(*) total from comment where studentNum = ? ";
			try {
				PreparedStatement pstm = conn.prepareStatement(sql);
				pstm.setInt(1, idStudentNum);
				ResultSet rsTot = pstm.executeQuery();
				rsTot.next();
				int total = rsTot.getInt("total");
				System.out.println("해당 아이디 총 댓글 수 : "+total+"리턴완료");
				return total;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("countCommentID() 에러");
			}
			return  0;
		}
	
//	--------------------------------------------------------------------
//		like_table  ----------------------------------------------------------
		public List<Post> selectLikeID(int idStudentNum,int index_no){
			List<Post> likeList = new ArrayList<>();
			String sql = "select l.postNum, p.title, p.content, p.date " + 
					"from like_table l, post p " + 
					"where l.postNum = p.postNum and l.studentNum = ? order by postNum desc limit ?,10";
			Post like = null;
			try {
				PreparedStatement pstm = conn.prepareStatement(sql);
				pstm.setInt(1, idStudentNum);
				pstm.setInt(2, index_no);
				ResultSet rs = pstm.executeQuery();
				while(rs.next()) {
					int postNum = rs.getInt("postNum");
					String title = rs.getString("title");
					String content = rs.getString("content");
					String date = rs.getString("date");
					like = new Post(postNum,title, content,date); 
					likeList.add(like);
				}
				rs.close();
				pstm.close();
				System.out.println(" 리턴");
				return likeList;
				
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("selectLikeID() 에러");
			}
			
			return null;
		}
			
		public int LikeOnOff(int likePostNum, int likeStudentNum){
			String sql = "select count(*) total from like_table where postNum = ? and studentNum = ?";
			try {
				PreparedStatement pstm = conn.prepareStatement(sql);
				pstm.setInt(1, likePostNum);
				pstm.setInt(2, likeStudentNum);
				ResultSet rsTot = pstm.executeQuery();
				rsTot.next();
				int total = rsTot.getInt("total");
				System.out.println("게시판 좋아요표시 : "+total+"리턴완료");
				return total;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("LikeOnOff() 에러");
			}
			return  0;
		}
			
	// 해당 아이디가 작성한 총 댓글 개수
			public int countLikeID(int idStudentNum){
				String sql = "select count(*) total from like_table where studentNum = ?";
				try {
					PreparedStatement pstm = conn.prepareStatement(sql);
					pstm.setInt(1, idStudentNum);
					ResultSet rsTot = pstm.executeQuery();
					rsTot.next();
					int total = rsTot.getInt("total");
					System.out.println("해당 아이디 총 댓글 수 : "+total+"리턴완료");
					return total;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("countCommentID() 에러");
				}
				return  0;
			}	
			
			
//-------------------------------------------------------------------------
	public static void main(String[] args) {
		//게시글 전체 목록 불러오기 - 내림차순 정렬 check
		System.out.println("게시글 전체 목록 불러오기 - 내림차순 정렬 check");
		Dao dao = Dao.getInstance();
//		List<Post> postCheck = dao.selectPostAll();
//		for(Post a : postCheck) {
//			System.out.println(a);
//		}
		
		//게시글 총 수
		System.out.println("총 게시글 수: "+dao.countPostAll());
		
//		//해당 아이디가쓴 글
//		List<Post> idPostCheck = dao.selectPostID(1001, 1);
//		for(Post a : idPostCheck) {
//			System.out.println(a);
//		}
		
		//해당아이디가 댓글 단 글
		List<Comment> idCommentCheck = dao.selectCommentID(1001,1);
		for(Comment a : idCommentCheck) {
			System.out.println("댓"+a);
		}
		
		//
		List<Post> likeCheck = dao.selectLikeID(1001,1);
		for(Post a : likeCheck) {
			System.out.println("좋"+a.getTitle());
		}

		
		//해당아이디가 쓴 글 수
		System.out.println("아이디 총 게시글 수: "+dao.countPostID(1001));
		
		//해당아이디가 쓴 댓글 수
		System.out.println("아이디 총 댓글 수: "+dao.countCommentID(1001));
		
		//해당글 아이디가 해당글 좋아요 여부
		System.out.println("좋아요: "+dao.LikeOnOff(2001,1002));
		
		//해당아이디가 좋아요한 글 수
		System.out.println("아이디 총 좋아요 수: "+dao.countLikeID(1001));
		
		
		// stream
//		postCheck.stream().sorted((n1,n2)->n2.compareTo(n1)).forEach(n->System.out.println(n));
//		System.out.println("게시글 전체 목록 불러오기 완료");
//		System.out.println();
//		
//		//게시글 전체 목록 불러오기 - 내림차순 정렬 check
//		System.out.println("게시글 전체 목록 불러오기 - 내림차순 정렬 check");
//		List<User> userCheck = dao.selectUserAll();
//		userCheck.stream().sorted((n1,n2)->n2.compareTo(n1)).forEach(n->System.out.println(n));
//		System.out.println("게시글 전체 목록 불러오기 완료");
//		System.out.println();
//		
//		//게시글 전체 목록 불러오기 - 내림차순 정렬 check
//		System.out.println("게시글 전체 목록 불러오기 - 내림차순 정렬 check");
//		List<Comment> commentCheck = dao.selectCommentAll();
//		commentCheck.stream().sorted((n1,n2)->n2.compareTo(n1)).forEach(n->System.out.println(n));
//		System.out.println("게시글 전체 목록 불러오기 완료");
	
	
	
	
	}
	


}
