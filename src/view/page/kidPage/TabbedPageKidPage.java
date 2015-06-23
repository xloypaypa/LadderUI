package view.page.kidPage;

import view.page.NamedPage;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class TabbedPageKidPage extends NamedPage implements ShowAblePage {
	
	JPanel show;

	public TabbedPageKidPage(String name) {
		super(name);
		show=new JPanel();
		show.setName(name);
		show.setLayout(null);
	}
	
	@Override
	public JComponent getPage(){
		return show;
	}

}
