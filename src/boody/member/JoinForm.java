package boody.member;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import boody.main.BoodyMain;
import util.FileManager;
import util.ImageManager;
import util.ShaPassword;

public class JoinForm extends JFrame {
	BoodyMain boodyMain;

	JPanel p_north;
	JPanel p_east;
	JPanel p_west;
	JPanel p_container; // BorderLayout
	JPanel p_center; // form container
	JPanel p_cen1;// form
	JPanel p_cen2;// picture
	JPanel p_south; // 버튼 영역
	JLabel la_title, la_id, la_pass, la_passCheck, la_name, la_myMsg, la_email;
	JTextField t_id, t_name, t_myMsg, t_email;
	JPasswordField t_pass, t_passCheck;
	Canvas can;

	JButton bt_join;
	JButton bt_file;

	ImageIcon icon; // Icon 인터페이스를 구현한 클래스
	JFileChooser chooser;

	String filename = "00000.jpg";// default 사진 이미지
	Toolkit kit = Toolkit.getDefaultToolkit();
	Image image = kit.getImage(this.getClass().getClassLoader().getResource("userPic\\"+filename));// 아무것도 설정 안했을때의 사진
	Image logo = kit.getImage(this.getClass().getClassLoader().getResource("logo.png"));

	ArrayList<Integer> fileBuff = new ArrayList<Integer>();

	public JoinForm(BoodyMain boodyMain) {
		this.boodyMain = boodyMain;
		// -------------------------------[생성]
		p_container = new JPanel();
		// 북쪽
		p_north = new JPanel();
		la_title = new JLabel("JOIN FORM");

		// 센터 위
		p_center = new JPanel();
		p_cen1 = new JPanel();
		can = new Canvas() {
			public void paint(Graphics g) {

				g.drawImage(image, 0, 0, 120, 120, can);
			}
		};
		bt_file = new JButton(ImageManager.getScaledIcon(this, "findpicture.png", 35, 40));
		chooser = new JFileChooser("C:\\workspace\\korea202102_jsworkspace\\images");
		// 센터 아래
		p_cen2 = new JPanel();
		la_id = new JLabel("ID");
		la_pass = new JLabel("PW");
		la_passCheck = new JLabel("PW confirm");
		la_name = new JLabel("NickName");
		la_myMsg = new JLabel("MyMsg");
		la_email = new JLabel("Email");

		t_id = new JTextField();
		t_pass = new JPasswordField();
		t_passCheck = new JPasswordField();
		t_name = new JTextField();
		t_myMsg = new JTextField();
		t_email = new JTextField();

		// 남쪽
		p_south = new JPanel();
		bt_join = new JButton(ImageManager.getScaledIcon(this, "regist.png", 50, 50));

		p_east = new JPanel();
		p_west = new JPanel();

		// -------------------------------[스타일 레이아웃]
		p_north.setPreferredSize(new Dimension(100, 60));
		p_east.setPreferredSize(new Dimension(60, 100));
		p_west.setPreferredSize(new Dimension(60, 100));
		p_south.setPreferredSize(new Dimension(100, 80));
		p_cen1.setPreferredSize(new Dimension(300, 150));
		p_cen2.setPreferredSize(new Dimension(260, 250));
		can.setPreferredSize(new Dimension(120, 120));

		p_north.setBackground(Color.white);
		p_east.setBackground(Color.white);
		p_west.setBackground(Color.white);
		p_south.setBackground(Color.white);
		p_center.setBackground(Color.white);
		p_cen1.setBackground(Color.white);
		p_cen2.setBackground(Color.white);

		bt_file.setBorder(null);
		bt_file.setBackground(Color.white);

		p_container.setLayout(new BorderLayout());
		GridLayout grid1 = new GridLayout(6, 2);
		grid1.setVgap(15);
		p_cen2.setLayout(grid1);

		Font f1 = new Font("고딕", Font.BOLD, 30);
		Font f2 = new Font("고딕", Font.BOLD, 15);

		la_title.setFont(f1);
		la_id.setFont(f2);
		la_pass.setFont(f2);
		la_passCheck.setFont(f2);
		la_name.setFont(f2);
		la_myMsg.setFont(f2);
		la_email.setFont(f2);

		bt_join.setBorderPainted(false); // 버튼 테투리 지우기
		bt_join.setFocusPainted(false);
		bt_join.setContentAreaFilled(false); // 버튼 영역 배경 표시삭제

		// -------------------------------[조립]
		p_north.add(la_title);
		p_south.add(bt_join);

		p_cen1.add(can);
		p_cen1.add(bt_file);

		p_cen2.add(la_id);
		p_cen2.add(t_id);
		p_cen2.add(la_pass);
		p_cen2.add(t_pass);
		p_cen2.add(la_passCheck);
		p_cen2.add(t_passCheck);
		p_cen2.add(la_name);
		p_cen2.add(t_name);
		p_cen2.add(la_myMsg);
		p_cen2.add(t_myMsg);
		p_cen2.add(la_email);
		p_cen2.add(t_email);
		p_center.add(p_cen1);
		p_center.add(p_cen2);

		p_container.add(p_north, BorderLayout.NORTH);
		p_container.add(p_east, BorderLayout.EAST);
		p_container.add(p_west, BorderLayout.WEST);
		p_container.add(p_center);
		p_container.add(p_south, BorderLayout.SOUTH);

		add(p_container);

		// ------------------------------[리스너 연결]

		bt_file.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				findPicture();
			}
		});

		bt_join.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (registCheck()) {
					regist();
				}
			}
		});

		t_myMsg.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				JTextField src = (JTextField) e.getSource();
				if (src.getText().length() >= 15) {
					e.consume();
					JOptionPane.showMessageDialog(src, "15글자 이하로 작성해주세요!!");
				}
			}
		});

		// ------------------------------[보여주기]
		setIconImage(logo);
		setTitle("부디부디 회원가입");
		setBounds(800, 100, 400, 600);
		setVisible(true);

	}

//============================메서드 영역 ============================

	// 사진 화면에 보여주기
	public void findPicture() {
		FileInputStream fis = null;

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();

			image = kit.getImage(file.getAbsolutePath());
			can.repaint();

			try {
				fis = new FileInputStream(file);
				long time = System.currentTimeMillis();
				filename = time + "." + FileManager.getExtend(file.getAbsolutePath(), "\\");

				int data = -1;
//            byte[] buff=new byte[1024]; 
				while (true) {
					data = fis.read();
					if (data == -1)
						break;
					fileBuff.add(data);
//               System.out.println(fileBuff);
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		;

	}

	public void savePicture() {
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream("C:\\workspace\\korea202102_javaworkspace\\boody\\res\\userPic\\" + filename); // 복사될 경로

			for (int i = 0; i < fileBuff.size(); i++) {
				int buff = fileBuff.get(i);
				fos.write(buff);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// 이메일 검사기
	public boolean isEmail(String str) {
		return Pattern.matches("^[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$", str);
	}

	// 회원등록 유효성 검사
	public boolean registCheck() {
		boolean flag = true;

		String pass = new String(t_pass.getPassword());
		String passCheck = new String(t_passCheck.getPassword());

		// 공란 검사
		if (t_id.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "id를 입력하세요");
			t_id.setText("");
			t_id.requestFocus();
			flag = false;
		} else if (pass.equals("")) {
			JOptionPane.showMessageDialog(this, "Password를 입력하세요");
			t_pass.setText("");
			t_pass.requestFocus();
			flag = false;
		} else if (passCheck.equals("")) {
			JOptionPane.showMessageDialog(this, "ConfirmPW 를 입력하세요");
			t_passCheck.setText("");
			t_passCheck.requestFocus();
			flag = false;
		} else if (t_name.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "name을 입력하세요");
			t_name.setText("");
			t_name.requestFocus();
			flag = false;
		} else if (t_email.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Email을 입력하세요");
			t_email.setText("");
			t_email.requestFocus();
			flag = false;
		} else {
			// 중복검사
			if (overlapCheck()) {
				JOptionPane.showMessageDialog(this, "같은 아이디가 있습니다");
				t_id.setText("");
				t_id.requestFocus();
				flag = false;
			} else {
				// pw 검사
				if (pass.equals(passCheck) == false) {
					JOptionPane.showMessageDialog(this, "비밀번호가 다릅니다");
					t_passCheck.setText("");
					t_passCheck.requestFocus();
					flag = false;
				} else {
					if (isEmail(t_email.getText()) == false) {
						JOptionPane.showMessageDialog(this, "***@****.*** 형식으로 입력하세요");
						t_email.setText("");
						t_email.requestFocus();
						flag = false;
					}

				}
			}

		}
		return flag;
	}

	// id 중복검사
	public boolean overlapCheck() {
		boolean flag = false;

		String sql = "select user_id from member where user_id=?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = this.boodyMain.getCon().prepareStatement(sql);
			pstmt.setString(1, t_id.getText());
			rs = pstmt.executeQuery();

			flag = rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return flag;
	}

	// 회원등록
	public void regist() {
		String sql = "insert into member( user_id, user_pass, user_name, user_msg, user_filename, user_email) values(?,?,?,?,?,?)";
		PreparedStatement pstmt = null;

		try {

			pstmt = this.boodyMain.getCon().prepareStatement(sql);

			pstmt.setString(1, t_id.getText());
			pstmt.setString(2, ShaPassword.shaPassword(new String(t_pass.getPassword())));
//         pstmt.setString(2, new String(t_pass.getPassword()));
			pstmt.setString(3, t_name.getText());
			pstmt.setString(4, t_myMsg.getText());
			pstmt.setString(5, filename);
			pstmt.setString(6, t_email.getText());

			int result = pstmt.executeUpdate();

			if (result == 1) {
				if (filename != "00000.jpg") {
					savePicture();
				}
				JOptionPane.showMessageDialog(this, "가입성공");
				setVisible(false);
				dispose();

			} else {
				JOptionPane.showMessageDialog(this, "가입실패");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.boodyMain.release(pstmt);
		}

	}

}