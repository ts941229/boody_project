package boody.hompy;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import boody.main.BoodyMain;
import boody.vo.AllMember;
import boody.vo.Member;
import util.ImageManager;

public class HomePage extends JFrame{
   BoodyMain boodyMain;
   Member member;
   
   //서쪽영역(버튼영역)
   JPanel p_west;
   JButton bt_1;
   JButton bt_2;
   JButton bt_3;
   JButton bt_4;
   
   //센터영역(프로필)
   JPanel p_center;
   JPanel p_profile;
   JButton bt_profile;
   JPanel p_user;
   JLabel la_id;
   JLabel la_name;
   JLabel la_status;
   JLabel la_email;
   JLabel la_id2;
   JLabel la_name2;
   JLabel la_status2;
   JLabel la_email2;
   Choice ch_search;
   
   //동쪽영역(싸이월드 미니룸+댓글기능)
   JPanel p_east;
   JPanel p_room;
   JButton bt_room;
   JLabel la_today;
   JTextArea area;
   JScrollPane scroll;
   JTextField t_input;
   JButton bt_comments;
   
   Toolkit toolkit = Toolkit.getDefaultToolkit();
   Image img = toolkit.getImage(this.getClass().getClassLoader().getResource("hompy.png").getFile());
   
   public HomePage(BoodyMain boodyMain, Member member) {
      this.boodyMain=boodyMain;
      this.member=member;
      
      //서쪽영역
      p_west=new JPanel();
      bt_1=new JButton(ImageManager.getScaledIcon(this,"home.png", 50, 50));   
      bt_2=new JButton(ImageManager.getScaledIcon(this,"diary.png", 50, 50));   
      bt_3=new JButton(ImageManager.getScaledIcon(this,"album.png", 50, 50));   
      bt_4=new JButton(ImageManager.getScaledIcon(this,"favorite.png", 50, 50));   
      
      //센터영역
      p_center = new JPanel();
      p_profile=new JPanel();
      bt_profile=new JButton(ImageManager.getScaledIcon(this, "userPic\\"+member.getUser_filename(), 170, 200));
      p_user=new JPanel();
      la_id=new JLabel("ID   :   ");
      la_name=new JLabel("닉네임   :   ");
      la_status=new JLabel("나의 한마디   :   ");
      la_email=new JLabel("이메일   :   ");
      la_id2=new JLabel(member.getUser_id());
      la_name2=new JLabel(member.getUser_name());
      la_status2=new JLabel(member.getUser_msg());
      la_email2=new JLabel(member.getUser_email());
      ch_search=new Choice();
      
      //동쪽영역
      p_east=new JPanel();
      p_room=new JPanel();
      bt_room=new JButton(ImageManager.getScaledIcon(this, "room2.jpg", 450, 300));
      la_today=new JLabel("내 미니룸 어때? 한줄평 남겨봐~!");
      area=new JTextArea();
      scroll=new JScrollPane(area);
      t_input=new JTextField(25);
      Font bigFont =t_input.getFont().deriveFont(Font.PLAIN, 15f);
      t_input.setFont(bigFont);
      bt_comments=new JButton(ImageManager.getScaledIcon(this,"comments.png", 68, 63));
      
      p_west.setPreferredSize(new Dimension(65,300));
      GridLayout grid1 = new GridLayout(4, 2);
       grid1.setVgap(40);
      p_user.setLayout(grid1);
      p_east.setLayout(new FlowLayout());
      p_east.setPreferredSize(new Dimension(500,300));
      scroll.setPreferredSize(new Dimension(450,100));
      ch_search.setPreferredSize(new Dimension(200,40));
      
      Font f1 = new Font("고딕", Font.BOLD, 15);
      Font f2 = new Font("고딕", Font.BOLD, 17);
      Font f3 = new Font("고딕", Font.BOLD, 13);
      Font f4 = new Font("고딕", Font.BOLD, 20);
      la_id.setFont(f1);
      la_id.setForeground(Color.LIGHT_GRAY);
      la_id.setHorizontalAlignment(JLabel.RIGHT);
      la_name.setFont(f1);
      la_name.setForeground(Color.LIGHT_GRAY);
      la_name.setHorizontalAlignment(JLabel.RIGHT);
      la_status.setFont(f1);
      la_status.setForeground(Color.LIGHT_GRAY);
      la_status.setHorizontalAlignment(JLabel.RIGHT);
      la_email.setFont(f1);
      la_email.setForeground(Color.LIGHT_GRAY);
      la_email.setHorizontalAlignment(JLabel.RIGHT);
      la_id2.setFont(f2);
      la_name2.setFont(f2);
      la_status2.setFont(f3);
      la_email2.setFont(f3);
      la_today.setFont(f4);
      
      Color col=new Color(100,168,168);
      Color col2=new Color(176, 224, 230);
      p_west.setBackground(col);
      p_center.setBackground(Color.WHITE);
      p_profile.setBackground(Color.WHITE);
      p_user.setBackground(Color.WHITE);
      p_east.setBackground(col);
      p_room.setBackground(col);
      bt_1.setBackground(Color.WHITE);
      bt_2.setBackground(col);
      bt_3.setBackground(col);
      bt_4.setBackground(col);
      ch_search.setBackground(col2);
      
      bt_profile.setBorderPainted(false);
      bt_profile.setFocusPainted(false);
      bt_profile.setContentAreaFilled(false); 
      
      bt_room.setBorderPainted(false);   
      bt_room.setFocusPainted(false);
      bt_room.setContentAreaFilled(false); 
      
      bt_comments.setBorderPainted(false);   
      bt_comments.setFocusPainted(false);
      bt_comments.setContentAreaFilled(false);
      
      Visitor vt=null;
      bt_comments.addActionListener(vt=new Visitor());
      vt.setT_input(t_input);
      vt.setArea(area);
      vt.setBt_comments(bt_comments);
      
      //붙이기
      p_west.add(bt_1);
      p_west.add(bt_2);
      p_west.add(bt_3);
      p_west.add(bt_4);
      
      p_profile.add(bt_profile);
      p_center.add(p_profile);
      p_user.add(la_id);
      p_user.add(la_id2);
      p_user.add(la_name);
      p_user.add(la_name2);
      p_user.add(la_status);
      p_user.add(la_status2);
      p_user.add(la_email);
      p_user.add(la_email2);
      p_center.add(p_user);
      p_center.add(ch_search);
      
      p_room.add(bt_room);
      p_east.add(p_room);
      p_east.add(la_today);
      p_east.add(scroll);
      p_east.add(t_input);
      p_east.add(bt_comments);
      
      add(p_west, BorderLayout.WEST);
      add(p_east, BorderLayout.EAST);
      add(p_center);

      setIconImage(img);
      setTitle("hompy");
      setBounds(50, 50, 900, 600);
      setVisible(true);
      
      getFriendList();
   }
   
   public void getFriendList() {
      PreparedStatement pstmt=null;
      ResultSet rs=null;
      
      String sql = "select user_id";
      sql += " from member";
      sql += " where member_id in (";
      sql += " select friendpk_id from friends where member_id=?)";
      
      ch_search.add("My Friend List");
      try {
         pstmt=this.boodyMain.getCon().prepareStatement(sql);
         
         pstmt.setInt(1, member.getMember_id());
         
         rs=pstmt.executeQuery();
         
         while(rs.next()) {
            ch_search.add(rs.getString("user_id"));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }finally {
         this.boodyMain.release(pstmt, rs);
      }
   }
}