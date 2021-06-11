package boody.member;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import boody.chat.BoodyClient;
import boody.main.BoodyMain;
import boody.vo.Member;
import util.ImageManager;
import util.ShaPassword;


public class LoginForm extends JFrame{
   BoodyMain boodyMain;
   Member member;
   
   
   //북쪽 영역
   XCanvas can;
   
   //남쪽 영역
   JPanel p_south;
   JLabel la_id, la_pass;
   JTextField t_id;
   JPasswordField t_pass;
   JButton bt_login, bt_join;
   
   Toolkit toolkit = Toolkit.getDefaultToolkit();
   Image img = toolkit.getImage(this.getClass().getClassLoader().getResource("logo.png").getFile());
   
   
   
   
   public LoginForm(BoodyMain boodyMain) {
      this.boodyMain=boodyMain;
      
      
   
      can=new XCanvas();
      
      p_south=new JPanel();
      la_id=new JLabel("ID");
      la_pass=new JLabel("Password");
      t_id=new JTextField(15);
      t_pass=new JPasswordField(15);
      bt_login=new JButton(ImageManager.getScaledIcon(this,"login.png", 50, 50));
      bt_login.setBorderPainted(false);      // 버튼 테투리 지우기
      bt_login.setFocusPainted(false);
      bt_login.setContentAreaFilled(false);   // 버튼 영역 배경 표시삭제
      bt_join=new JButton(ImageManager.getScaledIcon(this,"join.png", 50, 50));
      bt_join.setBorderPainted(false);      // 버튼 테투리 지우기
      bt_join.setFocusPainted(false);
      bt_join.setContentAreaFilled(false);   // 버튼 영역 배경 표시삭제
      
      p_south.setBackground(Color.white);
      setBackground(Color.white);
      p_south.setPreferredSize(new Dimension(300,130));
      
      la_id.setPreferredSize(new Dimension(70,30));
      la_pass.setPreferredSize(new Dimension(70,30));
      
      
      
      p_south.add(la_id);
      p_south.add(t_id);
      p_south.add(la_pass);
      p_south.add(t_pass);      
      p_south.add(bt_login);
      p_south.add(bt_join);
      add(p_south, BorderLayout.SOUTH);
      add(can);
      
      can.createImage(this.getClass().getClassLoader().getResource("bk.png").getFile());
      
      //--------------------------리스너-----------------------------
      bt_login.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            loginCheck();
         }
      });
      bt_join.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            //회원가입 폼을 호출
            JoinForm newOne=new JoinForm(boodyMain);
//                LoginForm.this.setVisible(false);
         }
      });
      
      this.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
             LoginForm.this.boodyMain.disConnect();
             System.exit(0);//kill process 
             
          }
       });
      
      //------------------------보여주기--------------------------
      setIconImage(img);
      setTitle("부디부디");
      setBounds(400, 100, 300, 350);
      setVisible(true);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
   }
   
   
   

   public void loginCheck() {
      
      String sql="select * from member where user_id=? and user_pass=?";
      
      //String id=loginVO.getUser_id();
      //String pass=loginVO.getUser_pass();
      
      PreparedStatement pstmt=null;
      ResultSet rs=null;
      
      try {
         pstmt=this.boodyMain.getCon().prepareStatement(sql);
         pstmt.setString(1, t_id.getText());
         pstmt.setString(2, ShaPassword.shaPassword(new String(t_pass.getPassword())));
         rs=pstmt.executeQuery();
         
         //회원인지 아닌지?
         if(rs.next()) {
        	member= new Member(); //Empty 상태임 

			member.setMember_id(rs.getInt("member_id"));
			member.setUser_emotion(rs.getInt("user_emotion"));
			member.setUser_id(rs.getString("user_id"));
			member.setUser_pass(rs.getString("user_pass"));
			member.setUser_name(rs.getString("user_name"));
			member.setUser_msg(rs.getString("user_msg"));
			member.setUser_email(rs.getString("user_email"));
			member.setUser_filename(rs.getString("user_filename"));
			member.setUser_regdate(rs.getString("user_regdate"));
			
//			System.out.println(member.getUser_filename());
        	 
        	 
        	 
            JOptionPane.showMessageDialog(this, member.getUser_name()+"님 환영합니다");
            t_id.setText("");
            t_pass.setText("");
            setVisible(false);
            dispose();
            
            new StatusForm(boodyMain, member, this);
//            new BoodyClient(member);
            //this.boodyMain.setSession(true);//인증성공의 데이터 대입
            
         }else {
            JOptionPane.showMessageDialog(this, "다시 로그인 해주세요");
            t_id.setText("");
            t_pass.setText("");
            t_id.requestFocus();
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }finally {
         this.boodyMain.release(pstmt, rs);
      }
   }
   
   

}