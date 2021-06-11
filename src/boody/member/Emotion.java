package boody.member;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import boody.custom.CustomButton;
import boody.main.BoodyMain;
import util.ImageManager;

public class Emotion extends JFrame implements ActionListener{
   BoodyMain boodyMain;
   StatusForm statusForm;
   CustomButton custom;
   ImageManager imageManager=new ImageManager();

   JPanel p_container;
   CustomButton[] bt_emoji=new CustomButton[10];

   public Emotion(StatusForm statusForm, BoodyMain boodyMain) {
      this.statusForm=statusForm;
      this.boodyMain=boodyMain;
      
      p_container=new JPanel();
      for(int i=0;i<10;i++) {
         bt_emoji[i]=new CustomButton(this,i);
         bt_emoji[i].setId(i);
      }

      p_container.setLayout(new GridLayout(2,5));
      p_container.setBackground(Color.white);
      
      for(JButton bt:bt_emoji) {
         p_container.add(bt);
      }
      add(p_container);
      
      for(int i=0;i<bt_emoji.length;i++) {
         bt_emoji[i].addActionListener(this);         
      }
      
      setBounds(700,270,250,130);
      setVisible(true);
   }

   public void actionPerformed(ActionEvent e) {
      Object obj=e.getSource();
      System.out.println("이벤트의 정보 : "+e.getSource());
      
      CustomButton bt=(CustomButton)obj;
      System.out.println("bt의 정보 : "+bt.getId());
      
      selectEmoji(bt.getId());
      insertEmoji(bt.getId());
      this.setVisible(false);

   }
   
   public void selectEmoji(int n) {
      for(int i=0;i<bt_emoji.length;i++) {
        if(n==i) {
           statusForm.bt_status.setIcon(ImageManager.getScaledIcon(this, "emotion\\"+(i+1)+".png", 25, 25)); 
        }
      }
   }
   
   public void insertEmoji(int n) {
	   
	   PreparedStatement pstmt=null;
	   
	   String sql="update member set user_emotion=? where member_id=?";
	   
	   try {
		pstmt=this.boodyMain.getCon().prepareStatement(sql);
		
		pstmt.setInt(1, n+1);
		pstmt.setInt(2, statusForm.member.getMember_id());
		
		int result=pstmt.executeUpdate();
		
		if(result>0) {
			System.out.println("이모지 DB 업데이트 완료");
		}else {
			System.out.println("이모지 업데이트 실패!");
		}
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
	   
	   
   }
   
}