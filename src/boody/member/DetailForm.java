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
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
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
import javax.swing.JTextField;

import boody.custom.RoundButton;
import boody.main.BoodyMain;
import boody.vo.Member;
import util.FileManager;
import util.ImageManager;

public class DetailForm extends JFrame {
	BoodyMain boodyMain;
	StatusForm statusForm;
	Member member;
	LoginForm loginForm;

	JPanel p_north;
	JPanel p_east;
	JPanel p_west;
	JPanel p_container; // BorderLayout
	JPanel p_center; // form container
	JPanel p_cen1;// form
	JPanel p_cen2;// picture
	JPanel p_south; // 버튼 영역
	JLabel la_title, la_id, la_name, la_myMsg, la_email;
	JTextField t_id, t_name, t_myMsg, t_email;
	Canvas can;

	RoundButton bt_edit;
	RoundButton bt_editPass;
	JButton bt_goodbye;
	JButton bt_file;

	ImageIcon icon; // Icon 인터페이스를 구현한 클래스
	JFileChooser chooser;

	String filename;// 로그인한 사람의 사진 이미지
	Toolkit kit = Toolkit.getDefaultToolkit();
	Image logo = kit.getImage(this.getClass().getClassLoader().getResource("logo.png").getFile());
	Image image;

	ArrayList<Integer> fileBuff = new ArrayList<Integer>();

	public DetailForm(BoodyMain boodyMain, StatusForm statusForm, Member member) {
		this.boodyMain = boodyMain;
		this.statusForm = statusForm;
		this.member = member;

		filename = member.getUser_filename();
//      System.out.println(filename);
		image = kit.getImage(this.getClass().getClassLoader().getResource("userPic\\" + filename));// 아무것도 설정 안했을때의 사진
		System.out.println(this.getClass().getClassLoader().getResource("userPic\\" + filename));

		// -------------------------------[생성]
		p_container = new JPanel();
		// 북쪽
		p_north = new JPanel();
		la_title = new JLabel("My Info");

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
		la_name = new JLabel("NickName");
		la_myMsg = new JLabel("MyMsg");
		la_email = new JLabel("Email");

		t_id = new JTextField(member.getUser_id());
		t_name = new JTextField(member.getUser_name());
		t_myMsg = new JTextField(member.getUser_msg());
		t_email = new JTextField(member.getUser_email());

		// 남쪽
		p_south = new JPanel();
		bt_edit = new RoundButton("수정하기");
		bt_editPass = new RoundButton("비밀번호 수정");
		bt_goodbye = new JButton(ImageManager.getScaledIcon(this, "resign.jpg", 40, 40));

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
		t_id.setBackground(Color.LIGHT_GRAY);

		p_container.setLayout(new BorderLayout());
		GridLayout grid1 = new GridLayout(6, 2);
		grid1.setVgap(15);
		p_cen2.setLayout(grid1);

		Font f1 = new Font("고딕", Font.BOLD, 30);
		Font f2 = new Font("고딕", Font.BOLD, 15);

		la_title.setFont(f1);
		la_id.setFont(f2);
		la_name.setFont(f2);
		la_myMsg.setFont(f2);
		la_email.setFont(f2);

		bt_goodbye.setBorderPainted(false); // 버튼 테투리 지우기
		bt_goodbye.setFocusPainted(false);
		bt_goodbye.setContentAreaFilled(false); // 버튼 영역 배경 표시삭제

		t_id.setRequestFocusEnabled(false);

		// -------------------------------[조립]
		p_north.add(la_title);
		p_south.add(bt_edit);
		p_south.add(bt_editPass);
		p_south.add(bt_goodbye);

		p_cen1.add(can);
		p_cen1.add(bt_file);

		p_cen2.add(la_id);
		p_cen2.add(t_id);
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

		bt_edit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (registCheck()) {
					editForm();
					DetailForm.this.setVisible(false);

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

		bt_goodbye.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(DetailForm.this, "정말로 탈퇴하시겠습니까?") == JOptionPane.OK_OPTION) {
					deleteMemberFriends();
				}
			}
		});

		bt_editPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new EditPassForm(boodyMain, member);
			}
		});

		// ------------------------------[보여주기]
		setIconImage(logo);
		setTitle("부디부디 회원가입");
		setBounds(800, 100, 400, 600);
		setVisible(true);

		System.out.println(statusForm.member.getUser_msg());
	}

//============================메서드 영역 ============================

	// 사진 화면에 보여주기
	public void findPicture() {
		fileBuff.removeAll(fileBuff);

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

		// 공란 검사
		if (t_id.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "id를 입력하세요");
			t_id.setText("");
			t_id.requestFocus();
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
			if (isEmail(t_email.getText()) == false) {
				JOptionPane.showMessageDialog(this, "***@****.*** 형식으로 입력하세요");
				t_email.setText("");
				t_email.requestFocus();
				flag = false;
			}

		}
		return flag;
	}

	// 수정하기
	public void editForm() {
		PreparedStatement pstmt = null;

		String sql = "update member set user_name=?, user_msg=?, user_email=?, user_filename=?";
		sql += " where member_id=?";

		try {
			pstmt = this.boodyMain.getCon().prepareStatement(sql);

			pstmt.setString(1, t_name.getText());
			pstmt.setString(2, t_myMsg.getText());
			pstmt.setString(3, t_email.getText());
			pstmt.setString(4, filename);
			pstmt.setInt(5, member.getMember_id());

			int result = pstmt.executeUpdate();
			if (result > 0) {
				JOptionPane.showMessageDialog(this, "수정 성공");

				// 파일이 바뀌었는지 안바뀌었는지확인 필요
				if (filename != "00000.jpg") {
					File file = new File(this.getClass().getClassLoader()
							.getResource("userPic\\" + member.getUser_filename()).toURI());
					System.out.println(file);
					file.delete();
					savePicture();
				}


				member.setUser_name(t_name.getText());
				member.setUser_msg(t_myMsg.getText());
				member.setUser_email(t_email.getText());
				member.setUser_filename(filename);

//			   System.out.println(member.getUser_filename());

				statusForm.la_name.setText(member.getUser_name());
				statusForm.la_status.setText(member.getUser_msg());
			} else {
				JOptionPane.showMessageDialog(this, "수정 실패");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} finally {
			this.boodyMain.release(pstmt);
		}

	}

	// 친구 추가 리스트 삭제
	public void deleteMemberFriends() {
		PreparedStatement pstmt = null;

		String sql = "delete from friends where member_id=? or friendpk_id=?";
		try {
			pstmt = this.boodyMain.getCon().prepareStatement(sql);

			pstmt.setInt(1, member.getMember_id());
			pstmt.setInt(2, member.getMember_id());

			int result = pstmt.executeUpdate();

			deleteMember();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.boodyMain.release(pstmt);
		}

	}

	// 멤버 삭제
	public void deleteMember() {
		PreparedStatement pstmt = null;

		String sql = "delete from member where member_id=" + member.getMember_id();

		try {
			pstmt = this.boodyMain.getCon().prepareStatement(sql);

			int result = pstmt.executeUpdate();

			if (result > 0) {
				JOptionPane.showMessageDialog(this, "탈퇴 되었습니다~\n다음에 또만나요~!");
//				File file = new File(this.getClass().getClassLoader()
//						.getResource("userPic\\" + statusForm.member.getUser_filename()).toURI());
//				file.delete();
				setVisible(false);
				dispose();
				statusForm.loginForm.setVisible(true);
				statusForm.setVisible(false);
				statusForm.dispose();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.boodyMain.release(pstmt);
		}

	}
}