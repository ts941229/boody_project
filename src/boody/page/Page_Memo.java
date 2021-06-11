package boody.page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.ImageManager;

public class Page_Memo extends Page{
   //상단영역
   JPanel p_north;
   JTextArea area;
   
   //하단영역
   JPanel p_south;
   JTextField t_input;
   JButton bt_regist;
   
   ImageManager imageManager=new ImageManager();
   
   public Page_Memo() {
      //상단영역
      p_north=new JPanel();
      area=new JTextArea() {
         public void paintComponent(Graphics g) {
            g.drawImage(new ImageIcon("C:\\workspace\\korea202102_javaworkspace\\boody\\res\\note.png").getImage(), 0, 0, 320, 320,null);
            setOpaque(false);
            super.paintComponent(g);
         }
      };
      
      //하단영역
      p_south=new JPanel();
      t_input=new JTextField(17);
      bt_regist=new JButton(ImageManager.getScaledIcon(this, "memo1.png", 50, 50));
      
      area.setPreferredSize(new Dimension(320,320));
      
      bt_regist.setBorderPainted(false);
      bt_regist.setFocusPainted(false);
      bt_regist.setContentAreaFilled(false);
      
      Note nt=null;
      bt_regist.addActionListener(nt=new Note());
       nt.setT_input(t_input);
       nt.setArea(area);
       nt.setBt_regist(bt_regist);
      
      p_north.add(area);
      p_south.add(t_input);
      p_south.add(bt_regist);
      add(p_north);
      add(p_south, BorderLayout.SOUTH);
      
      p_south.setBackground(Color.WHITE);
      setBackground(Color.WHITE);
      
      t_input.addKeyListener(new KeyAdapter() {
         public void keyTyped(KeyEvent e) {
            JTextField src = (JTextField) e.getSource();
            if (src.getText().length() >= 30) {
               e.consume();
               JOptionPane.showMessageDialog(src, "간단하게만 작성해주세요!!");
            }
         }
      });
      
   }
      
}