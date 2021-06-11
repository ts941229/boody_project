package boody.custom;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JButton;

import boody.member.Emotion;
import util.ImageManager;

public class CustomButton extends JButton { 
     private int id;
     Emotion emotion;
     Toolkit toolKit;
     Image image;
         
    String dir="C:\\workspace\\korea202102_javaworkspace\\boody\\res\\emotion\\";
    String[] filename= {"1.png","2.png","3.png","4.png","5.png","6.png","7.png","8.png","9.png","10.png"};
    ImageManager imageManager=new ImageManager();
    
       
     public CustomButton(Emotion emotion, int i) {
        this.emotion = emotion;
        
        this.setIcon(this.imageManager.getScaledIcon(this, "emotion\\"+filename[i], 40, 40));
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
     }
     
     public CustomButton(String title) {
         super(title);
      }
     
   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }


}