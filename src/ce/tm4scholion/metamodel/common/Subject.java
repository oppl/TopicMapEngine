package ce.tm4scholion.metamodel.common;

import ce.tm4scholion.metamodel.Element;
import ce.tm4scholion.metamodel.Manager;
import ce.tm4scholion.tm.Topic;

public class Subject extends Element {

	public Subject(Topic myRep, Manager mgr) {
		super(myRep, mgr);
		// TODO Auto-generated constructor stub
	}
	
	public void toTopicMap() {
		super.toTopicMap();
		mgr.getTMManager().setTypeInstance(myRep, mgr.getTMManager().getTopic("Subject"));
	}

}
