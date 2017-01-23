package ce.tm4scholion.metamodel.common;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import ce.tm4scholion.metamodel.Element;
import ce.tm4scholion.metamodel.Manager;
import ce.tm4scholion.metamodel.communication.chat.Chat;
import ce.tm4scholion.metamodel.communication.forum.Forum;
import ce.tm4scholion.metamodel.communication.infoboard.Infoboard;
import ce.tm4scholion.metamodel.learning.LearningUnit;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.Manager.RoleTopic;

public class Course extends Element {

	private Vector<LearningUnit> containedLearningUnits;
	private Set<Forum> containedForums;
	private Set<Chat> containedChats;
	private Infoboard containedInfoboard;
	
	public Course(Topic myRep, Manager mgr) {
		super(myRep, mgr);
		// TODO Auto-generated constructor stub
	}

	public void setLUs(Vector<LearningUnit> lus) {
		containedLearningUnits = lus;
	}
	
	public Vector<LearningUnit> getLUs() {
		return containedLearningUnits;
	}
	
	public Set<Chat> getContainedChats() {
		return containedChats;
	}

	public void setContainedChats(Set<Chat> containedChats) {
		this.containedChats = containedChats;
	}

	public void addChat(Chat chat) {
		if (containedChats == null) containedChats = new HashSet<Chat>();
		containedChats.add(chat);
	}	
	
	public Set<Forum> getContainedForums() {
		return containedForums;
	}

	public void setContainedForums(Set<Forum> containedForums) {
		this.containedForums = containedForums;
	}
	
	public void addForum(Forum forum) {
		if (containedForums == null) containedForums = new HashSet<Forum>();
		containedForums.add(forum);
	}

	public Infoboard getContainedInfoboard() {
		return containedInfoboard;
	}

	public void setContainedInfoboard(Infoboard containedInfoboard) {
		this.containedInfoboard = containedInfoboard;
	}
	
	public void toTopicMap() {
		super.toTopicMap();
		if (containedInfoboard != null) {
			containedInfoboard.toTopicMap();
			Set<RoleTopic> infoboard = new HashSet<RoleTopic>();
			infoboard.add(new RoleTopic("Subordinate", containedInfoboard.getRep()));
			infoboard.add(new RoleTopic("Superordinate", myRep));
			mgr.addAssociation("Nested", infoboard, null);			
		}
		if (containedForums != null && containedForums.size()>0) {
			Iterator<Forum> i = containedForums.iterator();
			Set<RoleTopic> forums = new HashSet<RoleTopic>();
			Forum current;
			while (i.hasNext()) {
				current = i.next();
				current.toTopicMap();
				forums.add(new RoleTopic("Subordinate", current.getRep()));
			}
			forums.add(new RoleTopic("Superordinate", myRep));
			mgr.addAssociation("Nested", forums, null);			
		}
		if (containedChats != null && containedChats.size()>0) {
			Iterator<Chat> i = containedChats.iterator();
			Set<RoleTopic> chats = new HashSet<RoleTopic>();
			Chat current;
			while (i.hasNext()) {
				current = i.next();
				current.toTopicMap();
				chats.add(new RoleTopic("Subordinate", current.getRep()));
			}
			chats.add(new RoleTopic("Superordinate", myRep));
			mgr.addAssociation("Nested", chats, null);						
		}
		if (containedLearningUnits != null && containedLearningUnits.size()>0) {
			Iterator<LearningUnit> i = containedLearningUnits.iterator();
			Set<RoleTopic> lus = new HashSet<RoleTopic>();
			LearningUnit current = i.next();
			current.toTopicMap();
			lus.add(new RoleTopic("Subordinate", current.getRep()));			
			while (i.hasNext()) {
				LearningUnit predecessor = current;
				current = i.next();
				current.toTopicMap();
				lus.add(new RoleTopic("Subordinate", current.getRep()));
				Set<RoleTopic> sibling = new HashSet<RoleTopic>();
				sibling.add(new RoleTopic("Predecessor", predecessor.getRep()));
				sibling.add(new RoleTopic("Successor", current.getRep()));
				mgr.addAssociation("Sibling", sibling, null);
			}
			lus.add(new RoleTopic("Superordinate", myRep));
			lus.add(new RoleTopic("FirstBlock", containedLearningUnits.firstElement().getRep()));
			mgr.addAssociation("Nested", lus, null);
			
		}
		
	}
	
}
