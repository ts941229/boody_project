package boody.member;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import boody.custom.RoundButton;
import boody.main.BoodyMain;
import boody.vo.Member;
import util.ShaPassword;

public class EditPassForm extends JFrame {
   BoodyMain boodyMain;
   Member member;
   
   JPanel p_container;
   JPanel p_north;
   JPanel p_center;
   JPanel p_south;
   
   JLabel la_title;
   JLabel la_nowpass;
   JLabel la_editpass;
   JLabel la_editpassCheck;
   
   JPasswordField t_nowpass;
   JPasswordField t_editpass;
   JPasswordField t_editpassCheck;
   
   RoundButton bt_edit;
   
   //String pass;//user의 현재 비밀번호(sha)
   String editpass;// user의 변경할 비밀변호(sha xx)
   
   
   
   Toolkit toolkit = Toolkit.getDefaultToolkit();
   Image img = toolkit.getImage(this.getClass().getClassLoader().getResource("logo.png").getFile());
   
   
   public EditPassForm(BoodyMain boodyMain, Member member) {
      this.boodyMain=boodyMain;
      this.member=member;
      
      //생성
      p_container=new JPanel();
      p_north=new JPanel();
      p_center=new JPanel();
      p_south=new JPanel();
      
      la_title=new JLabel("Change Password");
      la_nowpass=new JLabel("Your Password");
      la_editpass=new JLabel("New passowrd");
      la_editpassCheck=new JLabel("Confirm pass");
      t_nowpass=new JPasswordField(15);
      t_editpass=new JPasswordField(15);
      t_editpassCheck=new JPasswordField(15);
      
      bt_edit=new RoundButton("수정하기");
      
      
      
      //스타일 & 레이아웃
      p_container.setLayout(new BorderLayout());
      
      la_nowpass.setPreferredSize(new Dimension(130,40));
      la_editpass.setPreferredSize(new Dimension(130,30));
      la_editpassCheck.setPreferredSize(new Dimension(130,30));
      
      p_north.setPreferredSize(new Dimension(350,60));
      p_south.setPreferredSize(new Dimension(350,50));
      
      setBackground(Color.white);
      p_center.setBackground(Color.white);
      p_north.setBackground(Color.white);
      p_south.setBackground(Color.white);
      
      Font f = new Font("고딕", Font.BOLD, 15);
      la_title.setFont(new Font("고딕", Font.BOLD, 30));
      la_nowpass.setFont(f);
      la_editpass.setFont(f);
      la_editpassCheck.setFont(f);
      
      
      //리스너
      bt_edit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            String nowpass=new String(t_nowpass.getPassword());
            editpass=new String(t_editpass.getPassword());
            String editpassCheck=new String(t_editpassCheck.getPassword());
            
//            System.out.println("작성한 password : "+nowpass);
//            System.out.println("sha-256한 작성한 password : "+ShaPassword.shaPassword(nowpass));
//            System.out.println(pass);

            if(checkNowpass().equals(ShaPassword.shaPassword(nowpass))) {
               if(editpass.equals(editpassCheck)) {
                  if(nowpass.equals(editpass)) {
                     JOptionPane.showMessageDialog(EditPassForm.this, "현재 비밀번호과 \n변경할 비밀번호가 같습니다");
                  }else {
                     editPassword();
                  }
               }else {
                  JOptionPane.showMessageDialog(EditPassForm.this, "변경된 비밀변호가 일치하지 않습니다");
               }
            }else {
               JOptionPane.showMessageDialog(EditPassForm.this, "현재 비밀번호가 틀립니다");
            }
         }
      });
      
      
      //조립
      
      p_north.add(la_title);
      p_center.add(la_nowpass);
      p_center.add(t_nowpass);
      p_center.add(la_editpass);
      p_center.add(t_editpass);
      p_center.add(la_editpassCheck);
      p_center.add(t_editpassCheck);
      p_south.add(bt_edit);
      
      p_container.add(p_north, BorderLayout.NORTH);
      p_container.add(p_south, BorderLayout.SOUTH);
      p_container.add(p_center);
      
      add(p_container);
      
      
      //보여주기
      setIconImage(img);
      setTitle("비밀번호 수정");
      setBounds(1200, 50, 350, 280);
      setVisible(true);
      
//      System.out.println("DB에서 return 받은 password : "+checkNowpass());
      
      
   }
   
//==================메서드 영역=====================
   
   public String checkNowpass() {
      
      String sql="select user_pass from member where member_id="+member.getMember_id();
      
      PreparedStatement pstmt=null;
      ResultSet rs=null;
      
      String pass=null;
      try {
         pstmt=this.boodyMain.getCon().prepareStatement(sql);
         
         rs=pstmt.executeQuery();
         
         if(rs.next()) {
            pass=rs.getString("user_pass");
         }
         
      } catch (SQLException e) {
         e.printStackTrace();
      }finally {
         this.boodyMain.release(pstmt, rs);
      }
      
      return pass;
   }
   
   
   public void editPassword() {
      PreparedStatement pstmt=null;
      
      
         
      String sql="update member set user_pass=\""+ShaPassword.shaPassword(editpass)+"\"";
      sql+=" where member_id="+member.getMember_id();
      
      try {
         pstmt=this.boodyMain.getCon().prepareStatement(sql);
         
         int result=pstmt.executeUpdate();
         
         if(result>0) {
            JOptionPane.showMessageDialog(this, "수정 성공");
            setVisible(false);
            dispose();
         }else {
            JOptionPane.showMessageDialog(this, "수정 실패");   
         }
         
      } catch (SQLException e) {
         e.printStackTrace();
      }finally {
         this.boodyMain.release(pstmt);
      }
   }

   
}