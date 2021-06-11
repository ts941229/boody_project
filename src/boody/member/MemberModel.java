package boody.member;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import boody.vo.AllMember;

public class MemberModel extends AbstractTableModel{

	Vector<AllMember> data = new Vector<AllMember>();
	Vector<String> column = new Vector<String>();
	
	public MemberModel() {
		String[] title= {"아이디","닉네임","상태메세지","이메일"};
		for (int i = 0; i < title.length; i++) {
			column.add(title[i]);
		}
	}
	
	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return column.size();
	}
	
	@Override
	public String getColumnName(int col) {
		return column.get(col);
	}

	@Override
	public Object getValueAt(int row, int col) {
		AllMember mb=data.get(row); //벡터에서 vo 꺼내기
		String value=null;
		
		if(col==0) {
			value=mb.getUser_id();
		}else if(col==1) {
			value=mb.getUser_name();
		}else if(col==2) {
			value=mb.getUser_msg();
		}else if(col==3) {
			value=mb.getUser_email();
		}
		
		return value;
	}
	
}
