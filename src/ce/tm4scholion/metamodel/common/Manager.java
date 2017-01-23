package ce.tm4scholion.metamodel.common;

import java.util.HashSet;
import java.util.Set;

import ce.tm4scholion.metamodel.Element;
import ce.tm4scholion.metamodel.Manager.RoleElementCombination;
import ce.tm4scholion.metamodel.communication.chat.Chatroom;
import ce.tm4scholion.metamodel.communication.forum.Discussion;
import ce.tm4scholion.metamodel.learning.Block;
import ce.tm4scholion.metamodel.learning.LearningUnit;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.Manager.RoleTopic;

public class Manager {

	public Topic subject;
	public Topic course;

	private ce.tm4scholion.metamodel.Manager mmManager;	
	
	public Manager(ce.tm4scholion.metamodel.Manager tmManager) {
		this.mmManager = tmManager;
		this.generateMetaElements();
	}

	
	private void generateMetaElements() {

		//elements
		subject = mmManager.addMetamodelElement("Subject");
		course = mmManager.addMetamodelElement("Course");

		//associations
		Set<RoleElementCombination> roles = new HashSet<RoleElementCombination>();
		Set<Topic> elements = new HashSet<Topic>();

		elements.add(subject);
		roles.add(new RoleElementCombination("Author",elements,"card_1"));
		elements = new HashSet<Topic>();
		roles.add(new RoleElementCombination("AuthoredElement",elements,"card_n"));
		mmManager.addMetamodelAssociation("authors", roles);
		
		roles = new HashSet<RoleElementCombination>();
		elements = new HashSet<Topic>();

		elements.add(subject);
		roles.add(new RoleElementCombination("Owner",elements,"card_1"));
		elements = new HashSet<Topic>();
		roles.add(new RoleElementCombination("OwnedElement",elements,"card_n"));
		mmManager.addMetamodelAssociation("owns", roles);
		
		
		elements = new HashSet<Topic>();
		elements.add(mmManager.getLearningManager().learningUnit);
		mmManager.createNewSiblingAssocType(elements);
		
		Set<Topic> superordinates = new HashSet<Topic>();
		Set<Topic> subordinates = new HashSet<Topic>();
		superordinates.add(course);
		subordinates.add(mmManager.getLearningManager().learningUnit);
		mmManager.createNewNestedAssocType(superordinates, subordinates, mmManager.getLearningManager().learningUnit);
		
		superordinates = new HashSet<Topic>();
		subordinates = new HashSet<Topic>();
		superordinates.add(course);
		subordinates.add(mmManager.getCommunicationManager().forum);
		subordinates.add(mmManager.getCommunicationManager().infoboard);
		subordinates.add(mmManager.getCommunicationManager().chat);
		mmManager.createNewNestedAssocType(superordinates, subordinates, null);
		
		roles = new HashSet<RoleElementCombination>();
		elements = new HashSet<Topic>();
		elements.add(mmManager.getLearningManager().learningUnit);
		elements.add(mmManager.getLearningManager().block);
		roles.add(new RoleElementCombination("DiscussionObject",elements,"card_1"));
		elements = new HashSet<Topic>();
		elements.add(mmManager.getCommunicationManager().discussion);
		elements.add(mmManager.getCommunicationManager().chatRoom);
		roles.add(new RoleElementCombination("DiscussedIn",elements,"card_n"));
		mmManager.addMetamodelAssociation("Discussion", roles);
				
		//resources
		
		//concrete Roles
			
	}
	
	public Subject generateSubject(String name) {
		Topic newSubject = mmManager.instantiateElement(name, subject);
		return new Subject(newSubject, mmManager);
	}

	public Course generateCourse(String name) {
		Topic newCourse = mmManager.instantiateElement(name, course);
		return new Course(newCourse, mmManager);
	}
	
	public boolean discuss(Element discussionObject, Element discussedIn) {
		if (!((discussionObject instanceof LearningUnit) || (discussionObject instanceof Block))) return false;
		if (!((discussedIn instanceof Discussion) || (discussedIn instanceof Chatroom))) return false;
		Set<RoleTopic> discussion = new HashSet<RoleTopic>();
		discussion.add(new RoleTopic("DiscussionObject", discussionObject.getRep()));
		discussion.add(new RoleTopic("DiscussedIn", discussedIn.getRep()));
		mmManager.addAssociation("Discussion", discussion, null);					
		return true;
	}
	
	
}
