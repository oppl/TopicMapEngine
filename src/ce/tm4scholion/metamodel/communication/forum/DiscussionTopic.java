package ce.tm4scholion.metamodel.communication.forum;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ce.tm4scholion.metamodel.Element;
import ce.tm4scholion.metamodel.Manager;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.Manager.RoleTopic;

public class DiscussionTopic extends Element {

	private Set<Discussion> discussions;

	public DiscussionTopic(Topic myRep, Manager mgr) {
		super(myRep, mgr);
		// TODO Auto-generated constructor stub
	}
		
	public void addDiscussion(Discussion d) {
		if (discussions == null) discussions = new HashSet<Discussion>();
		discussions.add(d);
	}
	
	public void toTopicMap() {
		super.toTopicMap();
		if (discussions != null && discussions.size()>0) {
			Iterator<Discussion> i = discussions.iterator();
			Set<RoleTopic> ds = new HashSet<RoleTopic>();
			Discussion current;
			while (i.hasNext()) {
				current = i.next();
				current.toTopicMap();
				ds.add(new RoleTopic("Subordinate", current.getRep()));
			}
			ds.add(new RoleTopic("Superordinate", myRep));
			mgr.addAssociation("Nested", ds, null);						
		}

	}
	

}
