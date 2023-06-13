package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	
	
	private Connection conn; //자바와 데이터베이스 연결
	private PreparedStatement pstmt; //쿼리문 대기 및 설정
	private ResultSet rs; //결과값 받아오기
	
	//기본생성자 userDAO가 실행되면 자동으로 생성된다
	//메소드마다 반복되는 코드를 이곳에 넣으면 코드가 간소화된다
	public UserDAO() {
		 try {
			String dbURL = "jdbc:mariadb://localhost:3306/bbs"; //mariaDB와 연결시켜주는 주소
			String dbID = "root"; //mariaDB계정
			String dbPassword = "skfodi"; //mariaDB비밀번호
			Class.forName("org.mariadb.jdbc.Driver"); //JDBC연결클래스를 string타입으로 불러온다
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword); 
			//드라이버 매니저에, 미리 저장했던 연결URL과 DB계정 정보를 담아 연결 설정을한다
		 }catch(Exception e) {
			e.printStackTrace();
		 }
	}
	
	//로그인 구현 메소드
    public int login(String userID, String userPassword) {
        String SQL = "SELECT userPassword FROM USER WHERE userID = ?"; //db에입력할 쿼리문을 미리담아둠
        try {
            pstmt = conn.prepareStatement(SQL); //미리 설정한 (변수에담아둔) SQL 쿼리문을 대기시킨다.
            pstmt.setString(1, userID); //쿼리문 1번째 물음표?에 매개변수로 받아온 userID를 대입
            rs = pstmt.executeQuery(); //쿼리 실행하고 나온 결과값을 rs에저장
            
            //rs.next()를 실행했을때 결과값이 존재한다면 해당 결과값을 얻을수있다.
            if(rs.next()){
            	//SQL쿼리무을 실행하고 나온 결과값이 매개변수로 들어온 userPassword와 같다면?
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
    	    pstmt = conn.prepareStatement(sql); //미리 설정한 (변수에담아둔) sql 쿼리문을 대기시킨다.
    	    pstmt.setString(1, user.getUserID()); //1번째 물음표
    	    pstmt.setString(2, user.getUserPassword()); //2번째 물음표...
    	    pstmt.setString(3, user.getUserName());
    	    pstmt.setString(4, user.getUserGender());
    	    pstmt.setString(5, user.getUserEmail());
    	    return pstmt.executeUpdate();//실제 db에서 insert를 정상적으로 입력하면 Query 1 OK라고 뜨는데 그 숫자를 받아온다
    	  }catch (Exception e) {
    	 	e.printStackTrace();
    	  }
    	  return -1;
    	}
    
    
}
