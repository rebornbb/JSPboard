package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	
	
	private Connection conn; //자바와 데이터베이스 연결
	private PreparedStatement pstmt; //쿼리문 대기 및 설정
	private ResultSet rs; //결과값 받아오기
	
	public UserDAO() {
		 try {
			String dbURL = "jdbc:mariadb://localhost:3307/bbs"; //mariaDB와 연결시켜주는 주소
			String dbID = "root"; //mariaDB계정
			String dbPassword = "4688"; //mariaDB비밀번호
			Class.forName("org.mariadb.jdbc.Driver"); //JDBC연결클래스를 string타입으로 불러온다
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword); 
			//드라이버 매니저에, 미리 저장했던 연결URL과 DB계정 정보를 담아 연결 설정을한다
		 }catch(Exception e) {
			e.printStackTrace();
		 }
	}
	
    public int login(String userID, String userPassword) {
        String SQL = "SELECT userPassword FROM USER WHERE userID = ?";
        try {
            pstmt = conn.prepareStatement(SQL); //sql쿼리문을 대기시킨다.
            pstmt.setString(1, userID); //첫번째 ?에 매개변수로 받아온 userID를 대입
            rs = pstmt.executeQuery(); //쿼리 실행한결과
            if(rs.next()){
                if(rs.getString(1).equals(userPassword))
                    return 1;  // 로그인 성공
                else
                    return 0; // 비밀번호 불일치
            }
            return -1; // ID가 없음
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2; // DB 오류 
    }
    
    public int join(User user) {
    	  String sql = "insert into user values(?, ?, ?, ?, ?)";
    	  try {
    	    pstmt = conn.prepareStatement(sql);
    	    pstmt.setString(1, user.getUserID());
    	    pstmt.setString(2, user.getUserPassword());
    	    pstmt.setString(3, user.getUserName());
    	    pstmt.setString(4, user.getUserGender());
    	    pstmt.setString(5, user.getUserEmail());
    	    return pstmt.executeUpdate();
    	  }catch (Exception e) {
    	 	e.printStackTrace();
    	  }
    	  return -1;
    	}
    
    
}
