package ce.tm4scholion.metamodel.communication;

import java.util.HashSet;
import java.util.Set;

import ce.tm4scholion.metamodel.communication.chat.Chat;
import ce.tm4scholion.metamodel.communication.chat.ChatEntry;
import ce.tm4scholion.metamodel.communication.chat.Chatroom;
import ce.tm4scholion.metamodel.communication.forum.Discussion;
import ce.tm4scholion.metamodel.communication.forum.DiscussionEntry;
import ce.tm4scholion.metamodel.communication.forum.DiscussionTopic;
import ce.tm4scholion.metamodel.communication.forum.Forum;
import ce.tm4scholion.metamodel.communication.infoboard.Infoboard;
import ce.tm4scholion.metamodel.communication.infoboard.InfoboardEntry;
import ce.tm4scholion.tm.Topic;

public class Manager {

	public Topic forum;
	public Topic discussionTopic;
	public Topic discussion;
	public Topic discussionEntry;
	
	public Topic chat;
	public Topic chatRoom;
	public Topic chatEntry;
	
	public Topic infoboard;
	public Topic infoboardEntry;
	
	private ce.tm4scholion.metamodel.Manager mmManager;

	public Manager(ce.tm4scholion.metamodel.Manager tmManager) {
		this.mmManager = tmManager;
		this.generateMetaElements();
	}

	private void generateMetaElements() {

		//elements
		forum = mmManager.addMetamodelElement("Forum");
		discussionTopic = mmManager.addMetamodelElement("DiscussionTopic");		
		discussion = mmManager.addMetamodelElement("Discussion");
		discussionEntry = mmManager.addMetamodelElement("DiscussionEntry");

		chat = mmManager.addMetamodelElement("Chat");
		chatRoom = mmManager.addMetamodelElement("Chatroom");
		chatEntry = mmManager.addMetamodelElement("ChatEntry");

		infoboard = mmManager.addMetamodelElement("Infoboard");
		infoboardEntry = mmManager.addMetamodelElement("InfoboardEntry");
		
		//associations
		
		Set<Topic> superordinates = new HashSet<Topic>();
		Set<Topic> subordinates = new HashSet<Topic>();
		superordinates.add(forum);
		subordinates.add(discussionTopic);
		mmManager.createNewNestedAssocType(superordinates, subordinates);

		superordinates = new HashSet<Topic>();
		subordinates = new HashSet<Topic>();
		superordinates.add(discussionTopic);
		subordinates.add(discussion);
		mmManager.createNewNestedAssocType(superordinates, subordinates);

		superordinates = new HashSet<Topic>();
		subordinates = new HashSet<Topic>();
		superordinates.add(discussion);
		superordinates.add(discussionEntry);
		subordinates.add(discussionEntry);
		mmManager.createNewNestedAssocType(superordinates, subordinates, discussionEntry);
		
		Set<Topic> elements = new HashSet<Topic>();
		elements.add(discussionEntry);
		mmManager.createNewSiblingAssocType(elements);

		
		superordinates = new HashSet<Topic>();
		subordinates = new HashSet<Topic>();
		superordinates.add(chat);
		subordinates.add(chatRoom);
		mmManager.createNewNestedAssocType(superordinates, subordinates);
		
		superordinates = new HashSet<Topic>();
		subordinates = new HashSet<Topic>();
		superordinates.add(chatRoom);
		subordinates.add(chatEntry);
		mmManager.createNewNestedAssocType(superordinates, subordinates,chatEntry);

		elements = new HashSet<Topic>();
		elements.add(chatEntry);
		mmManager.createNewSiblingAssocType(elements);
		

		superordinates = new HashSet<Topic>();
		subordinates = new HashSet<Topic>();
		superordinates.add(infoboard);
		subordinates.add(infoboardEntry);
		mmManager.createNewNestedAssocType(superordinates, subordinates);

		elements = new HashSet<Topic>();
		elements.add(infoboardEntry);
		mmManager.createNewSiblingAssocType(elements);

		
		//resources
		Set<String> resTypes = new HashSet<String>();
		resTypes.add("Text");
		mmManager.addResourceTypesForElement(discussionEntry, resTypes);
		mmManager.addResourceTypesForElement(chatEntry, resTypes);
		mmManager.addResourceTypesForElement(infoboardEntry, resTypes);
		
		resTypes = new HashSet<String>();
		resTypes.add("Description");
		mmManager.addResourceTypesForElement(forum, resTypes);
		mmManager.addResourceTypesForElement(discussionTopic, resTypes);
		mmManager.addResourceTypesForElement(discussion, resTypes);
		mmManager.addResourceTypesForElement(chat, resTypes);
		mmManager.addResourceTypesForElement(chatRoom, resTypes);

		//concrete Roles
	}

	public Forum generateForum(String name) {
		Topic newForum = mmManager.instantiateElement(name, forum);
		return new Forum(newForum, mmManager);
	}

	public DiscussionTopic generateDiscussionTopic(String name) {
		Topic newDiscussionTopic = mmManager.instantiateElement(name, discussionTopic);
		return new DiscussionTopic(newDiscussionTopic, mmManager);
	}

	public Discussion generateDiscussion(String name) {
		Topic newDiscussion = mmManager.instantiateElement(name, discussion);
		return new Discussion(newDiscussion, mmManager);
	}

	public DiscussionEntry generateDiscussionEntry(String name) {
		Topic newDiscussionEntry = mmManager.instantiateElement(name, discussionEntry);
		return new DiscussionEntry(newDiscussionEntry, mmManager);
	}
	
	public Chat generateChat(String name) {
		Topic newChat = mmManager.instantiateElement(name, chat);
		return new Chat(newChat, mmManager);
	}
	
	public Chatroom generateChatroom(String name) {
		Topic newChatroom = mmManager.instantiateElement(name, chatRoom);
		return new Chatroom(newChatroom, mmManager);
	}
	
	public Entry generateChatEntry(String name) {
		Topic newChatEntry = mmManager.instantiateElement(name, chatEntry);
		return new ChatEntry(newChatEntry, mmManager);
	}
	
	public Infoboard generateInfoboard(String name) {
		Topic newInfoboard = mmManager.instantiateElement(name, infoboard);
		return new Infoboard(newInfoboard, mmManager);
	}
		
	public InfoboardEntry generateInfoboardEntry(String name) {
		Topic newInfoboardEntry = mmManager.instantiateElement(name, infoboardEntry);
		return new InfoboardEntry(newInfoboardEntry, mmManager);
	}
	
}
