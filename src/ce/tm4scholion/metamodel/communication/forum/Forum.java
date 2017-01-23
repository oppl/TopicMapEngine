package ce.tm4scholion.metamodel.communication.forum;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ce.tm4scholion.metamodel.Element;
import ce.tm4scholion.metamodel.Manager;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.Manager.RoleTopic;

public class Forum extends Element {

	private Set<DiscussionTopic> topics;
	
	public Forum(Topic myRep, Manager mgr) {
		super(myRep, mgr);
		// TODO Auto-generated constructor stub
	}
	
	public void addDiscussionTopic(DiscussionTopic dt) {
		if (topics == null) topics = new HashSet<DiscussionTopic>();
		topics.add(dt);
	}
	
	public void toTopicMap() {
		super.toTopicMap();
		if (topics != null && topics.size()>0) {
			Iterator<DiscussionTopic> i = topics.iterator();
			Set<RoleTopic> dts = new HashSet<RoleTopic>();
			DiscussionTopic current;
			while (i.hasNext()) {
				current = i.next();
				current.toTopicMap();
				dts.add(new RoleTopic("Subordinate", current.getRep()));
			}
			dts.add(new RoleTopic("Superordinate", myRep));
			mgr.addAssociation("Nested", dts, null);						
		}

	}

}
