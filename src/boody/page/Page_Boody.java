package boody.page;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import boody.chat.BoodyClient;
import boody.main.BoodyMain;
import boody.member.StatusForm;
import boody.vo.Member;
import util.ExtractId;
import util.ImageManager;

public class Page_Boody extends Page implements TreeSelectionListener {
	StatusForm statusForm;
	BoodyMain boodyMain;
	Member member;

	JTree tree;
	JScrollPane scroll;

	DefaultTreeModel model;
	DefaultMutableTreeNode root;

	ArrayList<String> list = new ArrayList<String>();

	String[] emogiList = new String[list.size()];

	Vector<Member> friendList = new Vector<Member>();

	Member selectFriend;
	int nodeNum;

	public JLabel onlineCount;

	BoodyClient boodyClient;

	String friendId;

	public Page_Boody(BoodyMain boodyMain, Member member, StatusForm statusForm) {
		this.boodyMain = boodyMain;
		this.member = member;
		this.statusForm = statusForm;

		setLayout(new BorderLayout());
		// setBackground(Color.BLUE);

		onlineCount = new JLabel("현재 접속자 수 : " + "0" + " 명");
		add(onlineCount, BorderLayout.NORTH);

		tree = new JTree();

		root = new DefaultMutableTreeNode("내 친구");

		getFriends();

		scroll = new JScrollPane(tree);
		add(scroll);

		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					if (node == null)
						return;
					Object nodeInfo = node.getUserObject();
					System.out.println(ExtractId.extractId((String) nodeInfo));
					System.out.println(
							"me : " + member.getUser_id() + ", " + "you : " + ExtractId.extractId((String) nodeInfo));
					boodyClient = new BoodyClient(member.getUser_id(), ExtractId.extractId((String) nodeInfo), statusForm);
					statusForm.clientMsgThread.setBoodyClient(boodyClient);
				} else if (e.getClickCount() == 1) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					if (node == null)
						return;
					Object nodeInfo = node.getUserObject();
					friendId = ExtractId.extractId((String) nodeInfo);
				}
			}
		});
	}

//=======================메서드 영역=====================

	public String getFriendId() {
		return friendId;
	}

	public BoodyClient getBoodyClient() {
		return boodyClient;
	}

	public void getFriends() {

		friendList.removeAllElements();

		root = new DefaultMutableTreeNode("내 친구");

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select member_id, user_emotion, user_id, user_name, user_msg, user_email, user_filename, user_regdate";
		sql += " from member";
		sql += " where member_id in (";
		sql += " select friendpk_id from friends where member_id=?)";

		try {
			pstmt = this.boodyMain.getCon().prepareStatement(sql);
			pstmt.setString(1, Integer.toString(member.getMember_id()));
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Member friend = new Member();

				friend.setMember_id(rs.getInt("member_id"));
				friend.setUser_emotion(rs.getInt("user_emotion"));
				friend.setUser_id(rs.getString("user_id"));
				friend.setUser_name(rs.getString("user_name"));
				friend.setUser_msg(rs.getString("user_msg"));
				friend.setUser_email(rs.getString("user_email"));
				friend.setUser_filename(rs.getString("user_filename"));
				friend.setUser_regdate(rs.getString("user_regdate"));

				friendList.add(friend); // Friend vo값 세팅후 벡터에 담기

				// 친구 한줄 나오게 하기
				String friendInfo = friend.getUser_name() + " (" + friend.getUser_id() + ")   ★" + friend.getUser_msg()
						+ "★";
				DefaultMutableTreeNode friendLine = new DefaultMutableTreeNode(friendInfo);
				root.add(friendLine);

				tree.putClientProperty("JTree.lineStyle", "None");

//				tree.putClientProperty("JTree.lineStyle", "Horizontal");

				// 친구 아이콘 설정
				ImageIcon leafIcon = ImageManager.getScaledIcon(this, "emotion\\0.png", 20, 20);
				if (leafIcon != null) {
					DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
					renderer.setLeafIcon(leafIcon);
					tree.setCellRenderer(renderer);
				}
			}

			model = new DefaultTreeModel(root);
			tree.setModel(model);
			tree.repaint();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node;
		node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

		if (node == null)
			return;

		nodeNum = Integer.parseInt((String) node.getUserObject()); // 캐스팅은 꼭해주어야 한다.
		System.out.println(nodeNum);

		System.out.println(friendList.get(nodeNum - 1).getUser_id());

	}

}
