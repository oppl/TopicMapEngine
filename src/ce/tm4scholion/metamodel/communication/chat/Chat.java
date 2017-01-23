package ce.tm4scholion.metamodel.communication.chat;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ce.tm4scholion.metamodel.Element;
import ce.tm4scholion.metamodel.Manager;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.Manager.RoleTopic;

public class Chat extends Element {

	private Set<Chatroom> chatRooms;
	
	public Chat(Topic myRep, Manager mgr) {
		super(myRep, mgr);
		// TODO Auto-generated constructor stub
	}
	
	public Chat(String name, Manager mgr) {
		super(name, mgr);
		// TODO Auto-generated constructor stub
	}

	public Set<Chatroom> getChatRooms() {
		return chatRooms;
	}

	public void setChatRooms(Set<Chatroom> chatRooms) {
		this.chatRooms = chatRooms;
	}
	
	public void addChatRoom(Chatroom chatRoom) {
		this.chatRooms.add(chatRoom);
	}

	public void toTopicMap() {
		super.toTopicMap();
		mgr.getTMManager().setTypeInstance(myRep, mgr.getTMManager().getTopic("Chat"));
		Iterator<Chatroom> i = chatRooms.iterator();
		Set<RoleTopic> rooms = new HashSet<RoleTopic>();
		while (i.hasNext()) {
			Chatroom cr = i.next();
			cr.toTopicMap();
			rooms.add(new RoleTopic("Subordinate", cr.getRep()));
		}
		rooms.add(new RoleTopic("Superordinate", myRep));
		mgr.addAssociation("Nested", rooms, null);
	}

	
}
