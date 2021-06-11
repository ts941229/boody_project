package boody.page;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Note implements ActionListener{
   private JTextArea area;
   private JTextField t_input;
   private JButton bt_regist;
   
   
   public void actionPerformed(ActionEvent e) {
      JButton btn=(JButton)e.getSource();
         System.out.println(btn);
         
         if(btn==bt_regist) {
            System.out.println("전송성공");
            showText();
         }
   }

   public void setArea(JTextArea area) {
      this.area = area;
   }

   public void setT_input(JTextField t_input) {
      this.t_input = t_input;
   }

   public void setBt_regist(JButton bt_regist) {
      this.bt_regist = bt_regist;
   }


   public void showText() {
         String msg=t_input.getText();
         
         area.append("               "+msg+"\n"+"\n");
         t_input.setText("");
      }
}