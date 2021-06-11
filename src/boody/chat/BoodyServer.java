package boody.chat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import boody.vo.Member;

public class BoodyServer extends JFrame{
	JPanel p_north; //포트 텍스트필드 , 버튼 올 패널
	JTextField t_port;
	JButton bt_run;
	
	JTextArea area;
	JScrollPane scroll;
	
	ServerSocket server;
	Thread serverThread;
	
	Vector<ServerMsgThread> clientList = new Vector<ServerMsgThread>();
	
	public BoodyServer() {
		//생성----
		//북쪽
		p_north = new JPanel();
		t_port = new JTextField("7777",10);
		bt_run = new JButton("접속");
		//센터
		area = new JTextArea();
		scroll = new JScrollPane(area);
		
		//스타일
		
		//조립
		p_north.add(t_port);
		p_north.add(bt_run);
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		
		//이벤트
		bt_run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serverThread = new Thread() {
					public void run() {
						runServer();
					}
				};
				serverThread.start();
			}
		});
		
		//보여주기
		setSize(400,500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void runServer() {
		int port = Integer.parseInt(t_port.getText());
		try {
			server = new ServerSocket(port); //서버 구동
			area.append("서버 가동\n");
			area.append("클라이언트 접속을 기다리는중...\n");
			
			while(true) {
				Socket socket = server.accept(); //전화기 설치 (응답 대기)
				String ip = socket.getInetAddress().getHostAddress();
				area.append(ip + " 클라이언트 접속 감지!\n");
				
				//접속 감지후 각 클라이언트마다 서버메시지 쓰레드 부여!
				ServerMsgThread serverMsgThread = new ServerMsgThread(socket, this);
				serverMsgThread.start(); //서버메시지쓰레드 가동!
				clientList.add(serverMsgThread);
				
				area.append("현재 접속자 수는 : "+clientList.size()+"명\n");
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new BoodyServer();
	}
}
