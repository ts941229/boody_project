package boody.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import boody.member.JoinForm;
import boody.member.LoginForm;

public class BoodyMain {
   //JoinForm joinForm;
   LoginForm loginForm;
   
   // 데이터베이스 관련
   String driver = "com.mysql.cj.jdbc.Driver";
   String url = "jdbc:mysql://localhost:3306/boody?characterEncoding=UTF-8";
   String user = "root";
   String password = "1234";
   private Connection con;
   
   public BoodyMain() {
      connect();
      
      //joinForm=new JoinForm(this);
      loginForm=new LoginForm(this);
      
   }
   
//=====================메서드 영역=========================
   
   public void connect() {
      try {
         Class.forName(driver);
         con = DriverManager.getConnection(url, user, password);
               if (con != null) {
                  System.out.println("접속성공");
                  
                  //status창 떠야함
                  
                  
               } else {
                  System.out.println("접속실패");
               }
            } catch (ClassNotFoundException e1) {
               e1.printStackTrace();
            } catch (SQLException e) {
               e.printStackTrace();
            }
         }
   

   public void disConnect() {
      if (con != null) {
         try {
            con.close();
         } catch (SQLException e) {
               e.printStackTrace();
            }
         }
   }
   
   
   
   // 쿼리문이 DML인 경우
   public void release(PreparedStatement pstmt) {
      if (pstmt != null) {
         try {
            pstmt.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

      // 쿼리문이 Select 인경우
   public void release(PreparedStatement pstmt, ResultSet rs) {
      if (rs != null) {
         try {
            rs.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
      if (pstmt != null) {
         try {
            pstmt.close();
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

   // con getter
   public Connection getCon() {
      return con;
   }
   
   
   public static void main(String[] args) {
      new BoodyMain();
   }

}