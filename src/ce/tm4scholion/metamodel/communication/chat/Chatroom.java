package ce.tm4scholion.metamodel.communication.chat;

import ce.tm4scholion.metamodel.Manager;
import ce.tm4scholion.metamodel.communication.EntryContainer;
import ce.tm4scholion.tm.Topic;

public class Chatroom extends EntryContainer {
	
	public Chatroom(Topic myRep, Manager mgr) {
		super(myRep, mgr);
		// TODO Auto-generated constructor stub
	}

	public void toTopicMap() {
		super.toTopicMap();
		mgr.getTMManager().setTypeInstance(myRep, mgr.getTMManager().getTopic("Chatroom"));
	}
	
}
