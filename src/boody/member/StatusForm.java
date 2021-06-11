package boody.member;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import boody.chat.ClientMsgThread;
import boody.custom.RoundButton;
import boody.hompy.HomePage;
import boody.main.BoodyMain;
import boody.page.Page;
import boody.page.Page_Boody;
import boody.page.Page_List;
import boody.page.Page_Memo;
import boody.vo.AllMember;
import boody.vo.Member;
import util.ImageManager;

public class StatusForm extends JFrame {
	BoodyMain boodyMain;
	AllMember allMember; // 회원리스트에 넣을 vo
	Member member; // 로그인한 내 정보
	MemberModel model;// 테이블에 부착할 모델
	LoginForm loginForm;

	// 소켓연결
	Socket socket;
	public ClientMsgThread clientMsgThread;

	// 페이지처리용
	public Page[] pages = new Page[3];

	// 북쪽영역
	JPanel p_north;
	JPanel p_profile;
	JPanel p_status;
	JPanel p_status_center;
	JPanel p_status_south;
	JButton bt_profile;
	JButton bt_status;
	JLabel la_name;
	JLabel la_status;

	// 서쪽영역
	JPanel p_west;
	JButton bt_boody;
	JButton bt_hompy;
	JButton bt_music;
	JButton bt_email;
	JButton bt_memo;

	// 센터영역
	JPanel p_center;
	JPanel p_center_table;
	JTable table;
	JScrollPane scroll_table;
	JPanel p_center_fList;
	JScrollPane scroll_fList;
	JPanel p_center_container; // 센터영역 컨테이너 (페이지용)

	// 센터남쪽영역
	JPanel p_center_south;
	RoundButton bt_addFriend;
	RoundButton bt_memList;
	RoundButton bt_deleteFriend;

	// 남쪽영역
	JPanel p_south;
	JPanel p_south_west;
	JPanel p_south_center;
	Choice ch_search;
	JTextField t_search;
	JButton bt_search;
	JButton bt_logout;

	int btWidth = 55;
	AllMember selectMember;

	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Image img = toolkit.getImage(this.getClass().getClassLoader().getResource("logo.png").getFile());

	public StatusForm(BoodyMain boodyMain, Member member, LoginForm loginForm) {
		this.boodyMain = boodyMain;
		this.member = member;
		this.loginForm = loginForm;

		// 서버 접속

		// ----------------------생성-------------------
		// 북쪽영역
		p_north = new JPanel();
		p_profile = new JPanel();

		p_status = new JPanel();
		p_status_center = new JPanel();
		bt_profile = new JButton(ImageManager.getScaledIcon(this, "userPic\\" + member.getUser_filename(), 50, 50));
		bt_status = new JButton(
				ImageManager.getScaledIcon(this, "emotion\\" + member.getUser_emotion() + ".png", 25, 25));
		la_name = new JLabel(member.getUser_name());

		p_status_south = new JPanel();
		la_status = new JLabel("(" + member.getUser_msg() + ")");
		// 서쪽영역
		p_west = new JPanel();
		bt_boody = new JButton(ImageManager.getScaledIcon(this, "boody.png", btWidth, btWidth));
		bt_hompy = new JButton(ImageManager.getScaledIcon(this, "hompy.png", btWidth, btWidth));
		bt_music = new JButton(ImageManager.getScaledIcon(this, "music.png", btWidth, btWidth));
		bt_email = new JButton(ImageManager.getScaledIcon(this, "email.png", btWidth, btWidth));
		bt_memo = new JButton(ImageManager.getScaledIcon(this, "memo.png", btWidth, btWidth));
		// 센터영역
		p_center = new JPanel();
		p_center_table = new JPanel();
		table = new JTable();
		scroll_table = new JScrollPane(table);
		p_center_container = new JPanel();
		// 페이지
		pages[0] = new Page_Boody(boodyMain, member, this);
		pages[1] = new Page_Memo();
		pages[2] = new Page_List();

		// 센터남쪽영역
		p_center_south = new JPanel();
		bt_memList = new RoundButton("회원목록");
		bt_addFriend = new RoundButton("친구추가");
		bt_deleteFriend = new RoundButton("친구삭제");
		// 남쪽영역
		p_south = new JPanel();
		p_south_center = new JPanel();
		p_south_west = new JPanel();
		ch_search = new Choice();
		t_search = new JTextField(8);
		bt_logout = new JButton(ImageManager.getScaledIcon(this, "logout.png", 40, 40));

		bt_search = new JButton(ImageManager.getScaledIcon(this, "search.png", 30, 30));

		// ------------------스타일 & 레이아웃----------------------

		// 사이즈
		Dimension btSize = new Dimension(btWidth, btWidth);
		bt_profile.setPreferredSize(new Dimension(60, 50));
		bt_boody.setPreferredSize(btSize);
		bt_hompy.setPreferredSize(btSize);
		bt_music.setPreferredSize(btSize);
		bt_email.setPreferredSize(btSize);
		bt_memo.setPreferredSize(btSize);
		bt_search.setPreferredSize(new Dimension(30, 30));
		bt_logout.setPreferredSize(new Dimension(40, 40));

		bt_status.setPreferredSize(new Dimension(30, 25));
		p_west.setPreferredSize(new Dimension(65, 300));
		p_north.setPreferredSize(new Dimension(400, 65));

		// 색상
		Color col = new Color(100, 168, 168);
		p_center.setBackground(Color.WHITE);
		p_center_south.setBackground(Color.WHITE);
		p_north.setBackground(Color.WHITE);
		p_south.setBackground(col);
		p_south_center.setBackground(col);
		p_south_west.setBackground(col);
		p_status.setBackground(Color.WHITE);
		p_status_south.setBackground(Color.white);
		p_status_center.setBackground(Color.white);
		p_profile.setBackground(Color.WHITE);
		p_west.setBackground(col);

		// 레이아웃
		p_west.setLayout(new FlowLayout());
		p_center.setLayout(new BorderLayout());
		BorderLayout layout = (BorderLayout) p_center.getLayout();
		layout.setVgap(0);
		layout.setHgap(0);
		p_center_container.setLayout(new FlowLayout());
		FlowLayout layout2 = (FlowLayout) p_center_container.getLayout();
		layout2.setVgap(0);
		layout2.setHgap(0);
		p_north.setLayout(new FlowLayout(FlowLayout.LEFT));
		p_status_center.setLayout(new FlowLayout(FlowLayout.LEFT));
		p_status.setLayout(new BorderLayout());
		p_south.setLayout(new BorderLayout());

		// 버튼 테두리 지우기

		bt_profile.setBorderPainted(false); // 버튼 테투리 지우기
		bt_profile.setFocusPainted(false);
		bt_profile.setContentAreaFilled(false); // 버튼 영역 배경 표시삭제

		bt_boody.setBorderPainted(false);
		bt_boody.setFocusPainted(false);
		bt_boody.setContentAreaFilled(false);

		bt_hompy.setBorderPainted(false);
		bt_hompy.setFocusPainted(false);
		bt_hompy.setContentAreaFilled(false);

		bt_music.setBorderPainted(false);
		bt_music.setFocusPainted(false);
		bt_music.setContentAreaFilled(false);

		bt_email.setBorderPainted(false);
		bt_email.setFocusPainted(false);
		bt_email.setContentAreaFilled(false);

		bt_memo.setBorderPainted(false);
		bt_memo.setFocusPainted(false);
		bt_memo.setContentAreaFilled(false);

		bt_status.setBorderPainted(false);
		bt_status.setFocusPainted(false);
		bt_status.setContentAreaFilled(false);

		bt_search.setBorderPainted(false);
		bt_search.setFocusPainted(false);
		bt_search.setContentAreaFilled(false);

		bt_logout.setBorderPainted(false);
		bt_logout.setFocusPainted(false);
		bt_logout.setContentAreaFilled(false);

		table.setShowGrid(false);

		// ------------------이벤트---------------------

		// 회원목록 버튼 클릭시
		bt_memList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showMemberList();
				showHide(2);
			}
		});

		// 이모지 클릭시
		bt_status.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Emotion(StatusForm.this, boodyMain);
			}
		});

		bt_addFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectMember = model.data.get(table.getSelectedRow());

				if (selectMember == null) {
					JOptionPane.showMessageDialog(StatusForm.this, "친구를 선택해 주세요");
				} else if (selectMember.getMember_id() == member.getMember_id()) {
					JOptionPane.showMessageDialog(StatusForm.this, "내정보입니다.\n 친구를 선택해주세요");
				} else if (addFriendCheck()) {
					JOptionPane.showMessageDialog(StatusForm.this, "이미 추가된 친구입니다.");
				} else {
					addFriend();
				}

			}
		});

		bt_boody.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				showFriendsList();
				showHide(0); // boody 페이지
				((Page_Boody) pages[0]).getFriends();
//				((Page_Boody) pages[0]).reloadTree();
			}
		});

		bt_email.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MailForm(member);
			}
		});

		bt_profile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DetailForm(boodyMain, StatusForm.this, member);
			}
		});

		bt_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getListBySearch();
			}
		});

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
//				StatusForm.this.boodyMain.disConnect();
//				System.exit(0);// kill process

			}
		});

		bt_memo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showHide(1);
			}
		});

		bt_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(StatusForm.this, "로그아웃 하시겠습니까?") == JOptionPane.OK_OPTION) {
					logout();
				}
			}
		});

		bt_hompy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new HomePage(boodyMain, member);
			}
		});

		bt_music.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MusicForm newOne = new MusicForm();
			}
		});

		bt_deleteFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteFriend();
			}
		});

		// -------------------조립-----------------------
		// 북쪽
		p_profile.add(bt_profile);
		p_status_center.add(bt_status);
		p_status_center.add(la_name);
		p_status.add(p_status_center);
		p_status_south.add(la_status);
		p_status.add(p_status_south, BorderLayout.SOUTH);

		p_north.add(p_profile);
		p_north.add(p_status);

		// 서쪽
		p_west.add(bt_boody);
		p_west.add(bt_hompy);
		p_west.add(bt_music);
		p_west.add(bt_email);
		p_west.add(bt_memo);

		// 센터
		p_center.add(p_center_south, BorderLayout.SOUTH);
		for (int i = 0; i < pages.length; i++) {
			p_center_container.add(pages[i]);
		}
		pages[2].add(scroll_table);
		p_center.add(p_center_container);
		// 센터남쪽
		p_center_south.add(bt_memList);
		p_center_south.add(bt_addFriend);
		p_center_south.add(bt_deleteFriend);
		ch_search.add("user_id");
		ch_search.add("user_name");

		// 남쪽
		p_south_center.add(ch_search);
		p_south_center.add(t_search);
		p_south_center.add(bt_search);
		p_south_west.add(bt_logout);
		p_south.add(p_south_center, BorderLayout.CENTER);
		p_south.add(p_south_west, BorderLayout.WEST);

		add(p_north, BorderLayout.NORTH);
		add(p_west, BorderLayout.WEST);
		add(p_south, BorderLayout.SOUTH);
		add(p_center);

		// 보이기
		setTitle("부디부디");
		setIconImage(img);
		setBounds(600, 200, 400, 600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		showHide(0);

		// 서버접속
		connectServer();

		// 서버에 정보 보내기
		sendInfo();
	}

//========================메서드 영역==========================

	// 회원목록보기
	public void showMemberList() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select user_id, user_name, user_msg, user_email, member_id from member";

		try {
			pstmt = this.boodyMain.getCon().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();

			model = new MemberModel();

			while (rs.next()) {
				allMember = new AllMember();
				allMember.setUser_id(rs.getString("user_id"));
				allMember.setUser_name(rs.getString("user_name"));
				allMember.setUser_msg(rs.getString("user_msg"));
				allMember.setUser_email(rs.getString("user_email"));
				allMember.setMember_id(rs.getInt("member_id"));
//				System.out.println(rs.getString("user_name"));
//				System.out.println(rs.getString("user_msg"));
//				System.out.println(rs.getString("user_email"));

				model.data.add(allMember);
			}
			System.out.println(model);
			System.out.println(model.data);
			table.setModel(model);
			table.updateUI();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.boodyMain.release(pstmt, rs);
		}

	}

	// 친구 추가하기
	public void addFriend() {

		selectMember = model.data.get(table.getSelectedRow());

		PreparedStatement pstmt = null;

		String sql = "insert into friends(member_id, friendpk_id) values(?,?)";

		try {
			pstmt = this.boodyMain.getCon().prepareStatement(sql);

			pstmt.setInt(1, member.getMember_id());
			pstmt.setInt(2, selectMember.getMember_id());

			int result = pstmt.executeUpdate();

			if (result == 1) {
				JOptionPane.showMessageDialog(this, "친구등록 성공");
			} else {
				JOptionPane.showMessageDialog(this, "친구등록 실패");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.boodyMain.release(pstmt);
		}

	}

	// 친구삭제
	public void deleteFriend() {
		
	      PreparedStatement pstmt=null;
	      String sql="delete from friends where member_id=? and friendpk_id=?";
	      
	      try {
	         pstmt = this.boodyMain.getCon().prepareStatement(sql);

	         pstmt.setInt(1, member.getMember_id());
	         pstmt.setInt(2, getMember_id());

	         int result = pstmt.executeUpdate();

	         if(result>0) {
	            JOptionPane.showMessageDialog(this, "친구삭제 성공");
	            ((Page_Boody) pages[0]).getFriends();
	         }else {
	            JOptionPane.showMessageDialog(this, "친구삭제 실패");
	         }
	         
	      } catch (SQLException e) {
	         e.printStackTrace();
	      } finally {
	         this.boodyMain.release(pstmt);
	      }

	}

	public int getMember_id() {
		   int member_id=0;

			PreparedStatement pstmt = null;
			ResultSet rs = null;

			String sql = "select member_id from member where user_id=?";

			try {
				pstmt = this.boodyMain.getCon().prepareStatement(sql);

				pstmt.setString(1, ((Page_Boody) pages[0]).getFriendId());

				rs = pstmt.executeQuery();

				if(rs.next()) {
					member_id=rs.getInt("member_id");
				};

			} catch (SQLException e) {
				e.printStackTrace();
			}

			return member_id;
		}

	// 이미 추가한 친구라면 return true
	public boolean addFriendCheck() {
		boolean flag = true;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select * from friends where member_id=? and friendpk_id=?";

		try {
			pstmt = this.boodyMain.getCon().prepareStatement(sql);

			pstmt.setInt(1, member.getMember_id());
			pstmt.setInt(2, selectMember.getMember_id());

			rs = pstmt.executeQuery();

			flag = rs.next();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return flag;
	}

	// 검색하기
	public void getListBySearch() {
		String category = ch_search.getSelectedItem();
		String keyword = t_search.getText();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select user_id, user_name, user_msg, user_email, member_id from member where " + category
				+ " like '%" + keyword + "%' ";

		try {
			pstmt = this.boodyMain.getCon().prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();

			model = new MemberModel();

			while (rs.next()) {
				allMember = new AllMember();
				allMember.setUser_id(rs.getString("user_id"));
				allMember.setUser_name(rs.getString("user_name"));
				allMember.setUser_msg(rs.getString("user_msg"));
				allMember.setUser_email(rs.getString("user_email"));
				allMember.setMember_id(rs.getInt("member_id"));

				model.data.add(allMember);

			}
			table.setModel(model);
			table.updateUI();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.boodyMain.release(pstmt, rs);
		}
	}

	// 로그아웃
	public void logout() {

		setVisible(false);
		dispose();

		loginForm.setVisible(true);

		// 로그아웃시 접속자로 clientList에 들어있는 사람도 내보내야함

	}

	// 페이지 보이기, 숨기기
	public void showHide(int n) {

		for (int i = 0; i < pages.length; i++) {
			if (n == i) {
				pages[i].setVisible(true);
			} else {
				pages[i].setVisible(false);
			}
		}
	}

	// 멤버 보내기
	public Member getMember() {
		return member;
	}

//======================소켓 연결=====================
	public void connectServer() {
		String ip = "118.221.75.220";
		int port = 7777;

		try {
			socket = new Socket(ip, port);

			clientMsgThread = new ClientMsgThread(socket, this);
			clientMsgThread.start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"type\" : \"login\",");
		sb.append("\"member\":{");
		sb.append("\"member_id\" : \"" + member.getMember_id() + "\",");
		sb.append("\"user_emotion\" : \"" + member.getUser_emotion() + "\",");
		sb.append("\"user_id\" : \"" + member.getUser_id() + "\",");
		sb.append("\"user_name\" : \"" + member.getUser_name() + "\",");
		sb.append("\"user_msg\" : \"" + member.getUser_msg() + "\",");
		sb.append("\"user_email\" : \"" + member.getUser_email() + "\",");
		sb.append("\"user_filename\" : \"" + member.getUser_filename() + "\",");
		sb.append("\"user_regdate\" : \"" + member.getUser_regdate() + "\"");
		sb.append("}");
		sb.append("}");

		clientMsgThread.send(sb.toString());
	}

}
