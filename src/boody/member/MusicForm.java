package boody.member;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import boody.custom.CustomButton;
import boody.main.BoodyMain;
import boody.vo.MusicVO;
import javazoom.jl.player.MP3Player;
import util.ImageManager;

public class MusicForm extends JFrame implements ActionListener{
   BoodyMain boodyMain;
    ArrayList<MusicVO> playlist = new ArrayList<MusicVO>();
    MusicPlayer mp = new MusicPlayer(playlist);
    MP3Player mp3Player;
    String[] menu_title= {"1","2","3"};
   CustomButton[] bt_menu=new CustomButton[3];
   ImageManager imageManager=new ImageManager();
   Toolkit toolkit = Toolkit.getDefaultToolkit();
   Image img = toolkit.getImage(this.getClass().getClassLoader().getResource("music.png").getFile());
    
    //위쪽
    JPanel p_container;
    JLabel la_1;
    JLabel la_2;
    JLabel la_3;
    
    //아래쪽
    JPanel p_south;
    JButton bt_pause;
    
    Choice ch=new Choice();
   int index=ch.getSelectedIndex()-1;
    
   public MusicForm() {
      
      //위쪽
      p_container=new JPanel();
      p_container = new JPanel();
      for(int i=0;i< menu_title.length;i++) {
         bt_menu[i] = new CustomButton(menu_title[i]);
         bt_menu[i].setId(i); 
      }
      la_1=new JLabel();
      la_1.setText("<html>브레이브걸스<br>Rollin</html>");
      la_1.setHorizontalAlignment(JLabel.CENTER);
      la_2=new JLabel();
      la_2.setText("<html>BTS<br>Dynamite</html>");
      la_2.setHorizontalAlignment(JLabel.CENTER);
      la_3=new JLabel();
      la_3.setText("<html>잔잔하고<br>고요한</html>");
      la_3.setHorizontalAlignment(JLabel.CENTER);
      
      //아래쪽
      p_south=new JPanel();
      bt_pause=new JButton();
      
      bt_pause.setIcon(ImageManager.getScaledIcon(this,"pause.png", 40, 40));
      bt_menu[0].setIcon(ImageManager.getScaledIcon(this,"1.jpg", 100, 90));
      bt_menu[1].setIcon(ImageManager.getScaledIcon(this,"2.jpg", 100, 90));
      bt_menu[2].setIcon(ImageManager.getScaledIcon(this,"3.jpg", 100, 90));
      
       bt_pause.setBorderPainted(false);      
      bt_pause.setFocusPainted(false);
      bt_pause.setContentAreaFilled(false);
      
       bt_menu[0].setBorderPainted(false);      
      bt_menu[0].setFocusPainted(false);
      bt_menu[0].setContentAreaFilled(false);
      
      bt_menu[1].setBorderPainted(false);      
      bt_menu[1].setFocusPainted(false);
      bt_menu[1].setContentAreaFilled(false);
      
      bt_menu[2].setBorderPainted(false);      
      bt_menu[2].setFocusPainted(false);
      bt_menu[2].setContentAreaFilled(false);
      
      playlist.add(new MusicVO("1", "누구야1", "C:\\workspace\\korea202102_javaworkspace\\boody\\res\\music\\Brave Girls-Rollin.mp3"));
      playlist.add(new MusicVO("2", "누구야2", "C:\\workspace\\korea202102_javaworkspace\\boody\\res\\music\\BTS-Dynamite.mp3"));
      playlist.add(new MusicVO("3", "누구야3", "C:\\workspace\\korea202102_javaworkspace\\boody\\res\\music\\SimpleSound.mp3"));
      
      p_container.setLayout(new GridLayout(2,3));
      p_container.setPreferredSize(new Dimension(300,180));
      p_container.setBackground(Color.WHITE);
      Color col=new Color(100,168,168);
      p_south.setBackground(col);
      
      
      for(JButton bt : bt_menu) { 
         p_container.add(bt);
      }
      p_container.add(la_1);
      p_container.add(la_2);
      p_container.add(la_3);
      p_south.add(bt_pause);
      add(p_south, BorderLayout.SOUTH);
      add(p_container);
      
      setIconImage(img);
      setTitle("music");
      setBounds(500,300,300,200);
      setVisible(true);
      
      for(int i=0;i<bt_menu.length;i++) {
         bt_menu[i].addActionListener(this);            
      }
      
      bt_pause.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            stopMusic();
         }
      });
      
      this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
               mp.stop();
            }
         });
   }
   public void getMusic(int n) {
        if(n==0) {
           MusicVO Music = mp.play1();
      }else if(n==1) {
         MusicVO Music = mp.play2();
      }else if(n==2) {
         MusicVO Music = mp.play3();
      }
   }
   
   public void stopMusic() {
      mp.stop();
   }

   public void actionPerformed(ActionEvent e) {
      Object obj = e.getSource();
      CustomButton bt=(CustomButton)obj;
      System.out.println(bt.getId());
      
      getMusic(bt.getId());

   }
}
