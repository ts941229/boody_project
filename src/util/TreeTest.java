package util;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class TreeTest extends JFrame implements TreeSelectionListener {

	//FIELDS
	private JPanel treePanel = new JPanel();
	private JScrollPane treeScroll;
	private JTree tree;
	private DefaultTreeModel model; //트리 모델 인스턴스
	
	//CONSTRUCTOR
	TreeTest() {
		setTitle("트리 예제");
		setSize(400, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("플레이 리스트");
		DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("힙합");
		DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("재즈");
		DefaultMutableTreeNode child3 = new DefaultMutableTreeNode("락");
		DefaultMutableTreeNode child4 = new DefaultMutableTreeNode("가요");
		DefaultMutableTreeNode child5 = new DefaultMutableTreeNode("팝송");
		DefaultMutableTreeNode child6 = new DefaultMutableTreeNode("동요");

		root.add(child1);
		root.add(child2);
		root.add(child3);
		root.add(child4);
		root.add(child5);
		root.add(child6);

		DefaultMutableTreeNode child6child1 = new DefaultMutableTreeNode("아기공룡둘리");
		DefaultMutableTreeNode child6child2 = new DefaultMutableTreeNode("네모의 꿈");
		DefaultMutableTreeNode child6child3 = new DefaultMutableTreeNode("민들레꽃");
		
		child6.add(child6child1);
		child6.add(child6child2);
		child6.add(child6child3);

		tree = new JTree(root);

		/* 또는 아래와 같이 하여도 된다.
		tree = new JTree();
		model = new DefaultTreeModel(root);
		tree.setModel(model);
		*/

		tree.setVisibleRowCount(10);
		tree.addTreeSelectionListener((TreeSelectionListener) this);
		
		treeScroll = new JScrollPane(tree);
		treePanel.add(treeScroll);
		add(treePanel);

		setVisible(true);
	}

	//METHODS
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node; 
		node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		
		if(node == null) return;

		String nodeName = (String) node.getUserObject(); //캐스팅은 꼭해주어야 한다.
		System.out.println(nodeName);
	}

	public static void main(String[] args) {
		new TreeTest();
	}
}
