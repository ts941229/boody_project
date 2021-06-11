package boody.hompy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Visitor implements ActionListener{
   private JTextField t_input;//null
   private JTextArea area;
   private JButton bt_comments;

   public void actionPerformed(ActionEvent e) {
      JButton btn=(JButton)e.getSource();
      System.out.println(btn);
      
      if(btn==bt_comments) {
         System.out.println("전송성공");
         showText();
      }
      
   }

   public void setT_input(JTextField t_input) {
      this.t_input = t_input;
   }

   public void setArea(JTextArea area) {
      this.area = area;
   }

   public void setBt_comments(JButton bt_comments) {
      this.bt_comments = bt_comments;
   }

   public void showText() {
      String msg=t_input.getText();
      
      area.append(msg+"\n"+"---------------------------------------------------------------------------------------------------------------"+"\n");
      t_input.setText("");
   }


}