package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BoardDAO {
	
	
	private Connection conn; //자바와 데이터베이스 연결
	private PreparedStatement pstmt; //쿼리문 대기 및 설정
	private ResultSet rs; //결과값 받아오기
	
	//기본생성자 BoardDAO가 실행되면 자동으로 생성된다
	//메소드마다 반복되는 코드를 이곳에 넣으면 코드가 간소화된다
	public BoardDAO() {
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
	

	//작성일자 메소드
	public String getDate() {
		//mysql 지금 시간 구하는 쿼리문
		String sql = "select now()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql); //미리 설정한 (변수에담아둔) sql 쿼리문을 대기시킨다.
			rs = pstmt.executeQuery(); //쿼리 실행하고 나온 결과값을 rs에저장
			if(rs.next()) {  //rs.next()를 실행했을때 결과값이 존재한다면 해당 결과값을 얻을수있다.
				return rs.getString(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ""; //데이터베이스 오류
	}
	
	//게시글 번호 부여 메소드
	public int getNext() {
		//현재 게시글을 내림차순으로 조회하여 가장 마지막 글의 번호를 구한다
		String sql = "select bbsID from board order by bbsID desc";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) { //rs.next()를 실행했을때 결과값이 존재한다면 해당 결과값을 얻을수있다.
				return rs.getInt(1) + 1; //+1을 하는 이유는 다음에 작성될 게시물의 번호를 결정하기 위해서입니다.
			}
			return 1; //첫 번째 게시물인 경우
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
    
	//글쓰기 메소드
	public int write(String bbsTitle, String userID, String bbsContent) {
		String sql = "insert into board values(?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext()); //게시물번호부여메소드
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate()); //작성일자메소드
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1); //글의 유효번호
			return pstmt.executeUpdate(); //실제 db에서 insert를 정상적으로 입력하면 Query 1 OK라고 뜨는데 그 숫자를 받아온다
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
	
	//게시글 리스트 메소드
	public ArrayList<Board> getList(int pageNumber){
		
		/*select * from board -- 게시판 table에서 선택합니다 
		where bbsID < ?  -- 새롭게 작성될 게시글 번호보다 작은 
		and bbsAvailable = 1  -- 현재 유효번호가 존재하는 컬럼들을
		order by bbsID desc limit 10  -- 게시글번호를 내림차순 정렬하고, 최대 10개까지 조회합니다*/
		String sql = "select * from board where bbsID < ? and bbsAvailable = 1 order by bbsID desc limit 10";
		ArrayList<Board> list = new ArrayList<Board>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql); //미리 설정한 (변수에담아둔) sql 쿼리문을 대기시킨다.
			
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10); 
			/* 만약 현재 글이 5개라면 getNext()=6, 1페이지이기 때문에 결과값은 6이 나온다, 6보다 작은 5개의 게시글이 출력
			 * 만약 현재 글이 23개라면 getNext()=24, 3페이지이기 때문에 결과값은 4가 나온다, 4보다 작은 3개의 게시글이 출력*/

			rs = pstmt.executeQuery(); //쿼리 실행하고 나온 결과값을 rs에저장
			while(rs.next()) { //결과값이 존재하는 동안 각각의 요소를 각각 담아 하나의 리스트를 완성하여 'getList' 로 'return'
				Board board = new Board();
				board.setBbsID(rs.getInt(1));
				board.setBbsTitle(rs.getString(2));
				board.setUserID(rs.getString(3));
				board.setBbsDate(rs.getString(4));
				board.setBbsContent(rs.getString(5));
				board.setBbsAvailable(rs.getInt(6));
				list.add(board);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//페이징 처리 메소드 //특정한 페이지가 존재하는지 조회하는 메소드 //게시글이 10개에서 11개로 넘어갈 때 '다음'버튼을 만들어 페이징 처리를 위한 기능
	public boolean nextPage(int pageNumber) {
		String sql = "select * from board where bbsID < ? and bbsAvailable = 1";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
