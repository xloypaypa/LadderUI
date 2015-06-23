package view.page;

public class NamedPage {
	
	String name;
	
	public NamedPage(String name) {
		this.name=name;
	}
	
	public String getName() {
		return this.name;
	}

	public boolean isThisPage(String name) {
		return this.name.equals(name);
	}

}
