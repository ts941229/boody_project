package boody.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import boody.member.StatusForm;
import boody.vo.Member;
import util.ImageManager;

public class BoodyClient extends JFrame {
	Member member;

	JPanel p_north;
	JTextField t_ip;
	JTextField t_port;
	JButton bt_connect;

	JTextArea area;
	JScrollPane scroll;

	JPanel p_south;
	JButton bt_em;
	JTextField t_input;
	JButton bt_send;

	ImageManager imageManager = new ImageManager();
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image img = toolkit.getImage(this.getClass().getClassLoader().getResource("chat.png").getFile());

	String m_id, f_id;

	ClientMsgThread clientMsgThread;
	StatusForm statusForm;

	public BoodyClient(String me, String you, StatusForm statusForm) {
		m_id = me;
		f_id = you;
		this.statusForm = statusForm;
		this.clientMsgThread = statusForm.clientMsgThread;

		// 생성
		p_north = new JPanel();
		t_ip = new JTextField("118.221.75.220", 15);
		t_port = new JTextField(6);
		bt_connect = new JButton("연결");

		area = new JTextArea() {
			public void paintComponent(Graphics g) {
				g.drawImage(
						new ImageIcon("C:\\workspace\\korea202102_javaworkspace\\boody\\res\\chatbk4.jpg").getImage(),
						0, 0, 400, 450, null);
				setOpaque(false);
				super.paintComponent(g);
			}

		};
		scroll = new JScrollPane(area);

		area.setEnabled(false);
		area.setDisabledTextColor(Color.BLACK);
		

		p_south = new JPanel();
		bt_em = new JButton(ImageManager.getScaledIcon(this, "emotion\\" + "0.png", 20, 20));
		// bt_em.setHorizontalTextPosition(SwingConstants.LEFT);

		t_input = new JTextField(25);
		Font bigFont = t_input.getFont().deriveFont(Font.PLAIN, 15f);
		t_input.setFont(bigFont);
		t_input.setBorder(null);
		Font f1 = new Font("고딕", Font.BOLD, 15);
		area.setFont(f1);
		area.setLineWrap(false);
		scroll.setBorder(null);

		bt_send = new JButton(ImageManager.getScaledIcon(this, "chatsend.png", 50, 50));

		// 스타일
		setTitle(statusForm.getMember().getUser_name() + "의 채팅★");
		Color col = new Color(255, 255, 210);
		p_south.setBackground(col);
		t_input.setBackground(Color.LIGHT_GRAY);
		// t_input.setForeground(Color.WHITE);

		bt_em.setBorderPainted(false);
		bt_em.setFocusPainted(false);
		bt_em.setContentAreaFilled(false);
		bt_send.setBorderPainted(false);
		bt_send.setFocusPainted(false);
		bt_send.setContentAreaFilled(false);

		// 조립
		p_north.add(t_ip);
		p_north.add(t_port);
		p_north.add(bt_connect);

		// add(p_north, BorderLayout.NORTH);
		add(scroll);

		p_south.add(bt_em);
		p_south.add(t_input);
		p_south.add(bt_send);
		add(p_south, BorderLayout.SOUTH);

		// 이벤트
		bt_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		bt_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMsgInfo();
				t_input.setText("");
				t_input.requestFocus();
			}
		});

		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMsgInfo();
					t_input.setText("");
					t_input.requestFocus();
				}
			}
		});

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
//				clientMsgThread.flag = false;
				// System.exit(0);
			}
		});

		// 보이기
		setIconImage(img);

		setBounds(400, 300, 400, 500);
		setVisible(true);
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

//	public void sendMsg() {
//		String msg = t_input.getText();
//		
//		clientMsgThread.send(msg);
//		
//		t_input.setText("");
//		t_input.requestFocus();
//	}

	public void sendMsgInfo() {
		member = statusForm.getMember();

		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"type\" : \"chat\",");
		sb.append("\"me\" : \"" + m_id + "\",");
		sb.append("\"you\" : \"" + f_id + "\",");
		sb.append("\"my_name\" : \"" + member.getUser_name() + "\",");
		sb.append("\"msg\" : \"" + t_input.getText() + "\"");
		sb.append("}");

		clientMsgThread.send(sb.toString());
	}

}
