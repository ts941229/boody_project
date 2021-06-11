package boody.page;

import java.awt.BorderLayout;
import java.awt.Color;

public class Page_List extends Page{
	public Page_List() {
		setBackground(Color.RED);
		setLayout(new BorderLayout());
		BorderLayout layout = (BorderLayout) getLayout();
		layout.setHgap(0);
		layout.setVgap(0);
	}
}
