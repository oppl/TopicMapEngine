package ce.tm4scholion.metamodel.communication;

import ce.tm4scholion.metamodel.Element;
import ce.tm4scholion.metamodel.Manager;
import ce.tm4scholion.tm.Topic;

public abstract class Entry extends Element {

	protected String data;
	
	public Entry(String name, Manager mgr) {
		super(name, mgr);
		// TODO Auto-generated constructor stub
	}

	public Entry(Topic myRep, Manager mgr) {
		super(myRep, mgr);
		// TODO Auto-generated constructor stub
	}

	public void setText(String data) {
		this.data = data;
	}

	public void toTopicMap() {
		super.toTopicMap();
		if (data != null) mgr.addResource(myRep, data, "Text", null);
	}
	
}