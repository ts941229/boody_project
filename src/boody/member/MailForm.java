package boody.member;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import boody.vo.Member;
import util.FileManager;
import util.ImageManager;

public class MailForm extends JFrame{
	Member member;
	
   JPanel p_north;
   JTextField t_receiver;
   JTextField t_sender;
   JTextField t_title;
   JTextArea area;
   JButton bt;
   JLabel la_receiver, la_sender;
   JScrollPane scroll;
   JProgressBar bar;
   Properties props;//key-value 쌍으로 데이터를 처리하는 컬렉션 api 중 하나
   
   Thread mailThread;
   Thread loadThread;
   ImageIcon icon; //Icon 인터페이스를 구현한 클래스
   InputStream fis;
   FileOutputStream fos;
   HttpURLConnection httpCon;
   
   ImageManager imageManager=new ImageManager();
   Toolkit toolkit = Toolkit.getDefaultToolkit();
   Image img = toolkit.getImage(this.getClass().getClassLoader().getResource("email.png").getFile());
   
   public MailForm(Member member) {
	   this.member=member;
	   
      p_north=new JPanel();
      la_receiver=new JLabel("받는 사람");
      la_sender=new JLabel("보내는 사람");
      t_receiver=new JTextField(15);
      t_sender=new JTextField(member.getUser_email(), 15);
      t_title=new JTextField("제목을 입력하세요", 22);
      t_title.setForeground(Color.LIGHT_GRAY);
      area=new JTextArea("내용을 입력하세요");
      area.setForeground(Color.LIGHT_GRAY);
      scroll=new JScrollPane(area);
      bt=new JButton(ImageManager.getScaledIcon(this, "send.png", 40, 40));
      bt.setBorderPainted(false);      // 버튼 테투리 지우기
       bt.setFocusPainted(false);
       bt.setContentAreaFilled(false);   // 버튼 영역 배경 표시삭제
       bar = new JProgressBar();
      
      setLayout(new FlowLayout());
      area.setPreferredSize(new Dimension(250,150));
      p_north.setPreferredSize(new Dimension(250,70));      
       la_receiver.setPreferredSize(new Dimension(65,30));
       la_sender.setPreferredSize(new Dimension(65,30));
      
      p_north.add(la_receiver);
      p_north.add(t_receiver);
      p_north.add(la_sender);
      p_north.add(t_sender);
      add(p_north, BorderLayout.NORTH);
      add(t_title);
      add(scroll);
      add(bt);
      add(bar);
      
      t_title.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent e) {
             if (t_title.getText().equals("제목을 입력하세요")) {
                    t_title.setText("");
                    t_title.setForeground(Color.BLACK);
                 }
         }

         public void focusLost(FocusEvent e) {
            if (t_title.getText().isEmpty()) {
                 t_title.setForeground(Color.LIGHT_GRAY);
                 t_title.setText("제목을 입력하세요");
              }
         }
      });
      
      area.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent e) {
             if (area.getText().equals("내용을 입력하세요")) {
                area.setText("");
                area.setForeground(Color.BLACK);
                 }
         }

         public void focusLost(FocusEvent e) {
            if (area.getText().isEmpty()) {
               area.setForeground(Color.LIGHT_GRAY);
               area.setText("내용을 입력하세요");
              }
         }
      });
      
      bt.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            mailThread=new Thread() {
               public void run() {
                  sendMail();
               }
            };
            mailThread.start();
            
            loadThread=new Thread() {
               public void run() {
                  loadMail();
               }
            };
            loadThread.start();
         }
      });
      
      setIconImage(img);
      setTitle("email");
      setBounds(1000,100,300,350);
      setVisible(true);
   }
   
   //메일보내기
   public void sendMail() {
      props=new Properties();//Map 유형 중 하나
      props.put("mail.smtp.host", "smtp.gmail.com");//stmp 서버 주소 작성
      props.put("mail.smtp.port", 465);//stmp 서버 포트 번호
      props.put("mail.smtp.auth", "true");//권한 true
      props.put("mail.smtp.ssl.enable", "true");
      props.put("mail.smtp.ssl.trust", "smtp.gmail.com");//SSL 사용시
      
      Session session=Session.getDefaultInstance(props, new Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("sju01334@gmail.com", "nuujtqwrvflqzdwf");
         }
      });
      
      MimeMessage message=new MimeMessage(session);
      
      //발신자 정보
      try {
         message.setFrom(new InternetAddress(t_sender.getText()));
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(t_receiver.getText()));
         message.setSubject(t_title.getText());
         message.setText(area.getText(), "UTF-8");
         Transport.send(message);
         
         JOptionPane.showMessageDialog(this, "메일을 성공적으로 전송하였습니다.");
         setVisible(false);
         dispose();
      } catch (AddressException e) {
         e.printStackTrace();
      } catch (MessagingException e) {
         e.printStackTrace();
      }
   }
   
   public void loadMail() {
      try {
         URL url = new URL("http://"+t_sender.getText()); //사용자가 입력한  url 주소를 이용한 URL 객체생성
         httpCon =(HttpURLConnection)url.openConnection();
         httpCon.setRequestMethod("GET");
         fis=httpCon.getInputStream(); //서버의 자원과 입력 스트림 연결!!
         
         //파일의 총 크기를 구한다
         int total = httpCon.getContentLength();
         System.out.println("total 은 "+total);
         
         //파일명 결정 
         long time = System.currentTimeMillis();
         String exit = FileManager.getExtend(t_sender.getText(), "/");
         String filename=time+"."+exit;
         fos=new FileOutputStream("C:\\workspace\\korea202102_javaworkspace\\boody\\res\\userPic\\"+filename); //파일명 결정
         
         int data=-1;
         int count=0;
         
         while(true) {
            data=fis.read(); //1byte 읽기!!
            
            //count와  total의 비율을 이용하여 백분율을 구한 후, 프로그래스바에 반영해보자!!
            float percent = (count/(float)total)*100;
            System.out.println("percent = "+(int)percent);
            
            bar.setValue((int)percent);
            
            if(data==-1)break;
            count++; //데이터가 존재할때만 증가시키자!!
            fos.write(data); //1byte 출력 
         }
         System.out.println("count는 "+count);
         
      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }finally {
         if(fos!=null) {
            try {
               fos.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         if(fis!=null) {
            try {
               fis.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         
      }
   }
   

}