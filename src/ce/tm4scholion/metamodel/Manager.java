package ce.tm4scholion.metamodel;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ce.tm4scholion.tm.Association;
import ce.tm4scholion.tm.Occurrence;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.Manager.RoleTopic;

/**
 * Metamodel Manager - this class provides access methods to generate and manage
 * meta-models of content management.
 * 
 * @author oppl
 * 
 */
public class Manager {

	private ce.tm4scholion.tm.Manager tmManager;

	private ce.tm4scholion.metamodel.common.Manager commonManager;

	private ce.tm4scholion.metamodel.learning.Manager learningManager;

	private ce.tm4scholion.metamodel.communication.Manager communicationManager;

	// private ce.tm4scholion.metamodel.workprocess.Manager workprocessManager;

	private Topic metamodelElement;

	private Topic arbitraryElement;

	/**
	 * Constructor of the meta-model manager class. Also creates and
	 * administrates the manager classes for the contained meta-models.
	 */
	public Manager() {
		tmManager = new ce.tm4scholion.tm.Manager();
		this.generateMetaMetaElements();

		learningManager = new ce.tm4scholion.metamodel.learning.Manager(this);
		communicationManager = new ce.tm4scholion.metamodel.communication.Manager(
				this);
		// workprocessManager = new
		// ce.tm4scholion.metamodel.workprocess.Manager(this);
		commonManager = new ce.tm4scholion.metamodel.common.Manager(this);

	}

	/**
	 * Generate the topics and associations necessary to define meta-models.
	 */
	private void generateMetaMetaElements() {

		metamodelElement = tmManager.addTopic("metamodelElement");
		arbitraryElement = tmManager.addTopic("arbitraryElement");

		Set<String> roles = new HashSet<String>();
		roles.add("card_n");
		roles.add("card_1");
		roles.add("assocType");
		tmManager.defineAssociationType("assocDef", roles);

		roles.clear();
		roles.add("assocRole");
		roles.add("elementType");
		tmManager.defineAssociationType("validRoleElementCombination", roles);

		roles.clear();
		roles.add("resType");
		roles.add("elementType");
		tmManager.defineAssociationType("validElementResourceCombination",
				roles);

	}

	/**
	 * retrieve the manager of the underlying topic map
	 * 
	 * @return the manager object of the underlying topic map
	 */
	public ce.tm4scholion.tm.Manager getTMManager() {
		return tmManager;
	}

	/**
	 * retrieve the manager for common content elements
	 * 
	 * @return the manager for common content elements
	 */
	public ce.tm4scholion.metamodel.common.Manager getCommonManager() {
		return commonManager;
	}

	/**
	 * retrieve the manager for learning content
	 * 
	 * @return the manager for learning content
	 */
	public ce.tm4scholion.metamodel.learning.Manager getLearningManager() {
		return learningManager;
	}

	/**
	 * retrieve the manager for communication content
	 * 
	 * @return the manager for communication content
	 */
	public ce.tm4scholion.metamodel.communication.Manager getCommunicationManager() {
		return communicationManager;
	}

	/**
	 * register a new element for a certain meta model. Every type of element
	 * that should be used to represent content of any kind has to be registered
	 * using this method
	 * 
	 * @param name
	 *            the name of the new meta model element
	 * @return the topic representing the new meta element
	 */
	public Topic addMetamodelElement(String name) {
		Topic t = tmManager.getTopic(name);
		if (t != null) {
			if (!tmManager.getSupertypes(t).contains(metamodelElement))
				tmManager.setSuperSubType(metamodelElement, t);
			return t;
		}
		// return tmManager.addTopicOfType(name, metamodelElement);
		Topic newElement = tmManager.addTopic(name);
		tmManager.setSuperSubType(metamodelElement, newElement);
		return newElement;
	}

	/**
	 * retrieve the topic representing a certain meta element by name
	 * 
	 * @param name
	 *            the name of the meta element to retrieve
	 * @return the topic representing the meta element, false if not found
	 */
	public Topic getMetamodelElement(String name) {
		Topic t = tmManager.getTopic(name);
		if (t != null && tmManager.getSupertypes(t).contains(metamodelElement))
			return t;
		return null;
	}

	/**
	 * register a new association between defined modeling elements. Every type
	 * of association that should be used to interlink content of any kind has
	 * to be registered using this method. Additionally, the types of elements
	 * to be linked and their respective roles have to be specified.
	 * 
	 * @param name
	 *            the name of the new association type
	 * @param validRoleElementCombinations
	 *            a set of Role-Element-Combinations which specify the element
	 *            types that can be interlinked with this association in certain
	 *            roles
	 */
	public void addMetamodelAssociation(String name,
			Set<RoleElementCombination> validRoleElementCombinations) {
		Set<RoleTopic> roles = new HashSet<RoleTopic>();
		roles.add(new RoleTopic("assocType", addMetamodelElement(name)));
		Iterator<RoleElementCombination> i = validRoleElementCombinations
				.iterator();
		while (i.hasNext()) {
			RoleElementCombination r = i.next();
			Topic role = tmManager.addTopic(r.role);
			roles.add(new RoleTopic(r.cardinality, role));

			Set<RoleTopic> combinations = new HashSet<RoleTopic>();
			combinations.add(new RoleTopic("assocRole", role));
			if (r.elements.size() == 0)
				combinations
						.add(new RoleTopic("elementType", arbitraryElement));
			else {
				Iterator<Topic> a = r.elements.iterator();
				while (a.hasNext()) {
					combinations.add(new RoleTopic("elementType", a.next()));
				}
			}
			tmManager.associate("validRoleElementCombination", combinations);
		}
		tmManager.associate("assocDef", roles);
	}

	/**
	 * register new resource types (occurrences) for a defined modeling
	 * elements. Every type of resource that should be used to instantiate the
	 * respective element has to be registered using this method.
	 * 
	 * @param element
	 *            the topic representing the meta-element, to which the resource
	 *            types should be added
	 * @param resourceTypes
	 *            a set of names for the new resource types to be added
	 */
	public void addResourceTypesForElement(Topic element,
			Set<String> resourceTypes) {
		Iterator<String> i = resourceTypes.iterator();
		Set<RoleTopic> roles = new HashSet<RoleTopic>();
		roles.add(new RoleTopic("elementType", element));
		while (i.hasNext()) {
			String resourceType = i.next();
			Topic rt = tmManager.defineOccurrenceType(resourceType);
			roles.add(new RoleTopic("resType", rt));
		}
		tmManager.associate("validElementResourceCombination", roles);
	}

	/**
	 * allows to specify subroles (or concrete roles) which can be inserted
	 * instead of the given top-level role in any association type it is used
	 * in. This allows to specify roles that refine a given role while assuring
	 * that associations using this role are still verified correctly when using
	 * one of the concrete, refined role types.
	 * 
	 * @param role
	 *            the role to be refined with concrete roles
	 * @param concreteRoles
	 *            a set of names for the concrete roles
	 */
	public void concretizeRole(String role, Set<String> concreteRoles) {
		Topic supertype = tmManager.getTopic(role);
		if (supertype == null)
			return;
		Iterator<String> i = concreteRoles.iterator();
		while (i.hasNext()) {
			Topic concreteRole = tmManager.addTopic(i.next());
			tmManager.setSuperSubType(supertype, concreteRole);
		}
	}

	/**
	 * creates a topic representing an actual element of a certain type
	 * 
	 * @param name
	 *            the name of the instantiated element
	 * @param elementType
	 *            the topic representing the element type of the new element
	 * @return
	 */
	public Topic instantiateElement(String name, Topic elementType) {
		if (!tmManager.containsTopic(elementType))
			return null;
		Topic existing = tmManager.getTopic(name);
		if (existing != null
				&& tmManager.topicIsInstanceOf(existing, elementType))
			return existing;
		return tmManager.addTopicOfType(name, elementType);
	}

	/**
	 * add an association between defined topics in specified roles and - if
	 * necessary - a certain scope
	 * 
	 * @param assocType
	 *            the name of the association type to be used
	 * @param rt
	 *            the set of topics to be associated in the assigned roles
	 * @param scope
	 *            a set of topics defining the scope of this association
	 * @return
	 */
	public boolean addAssociation(String assocType, Set<RoleTopic> rt,
			Set<Topic> scope) {
		if (!validateAssociation(assocType, rt))
			return false;
		Association a = tmManager.associate(assocType, rt);
		if (scope != null && scope.size() > 0)
			tmManager.setScope(a, tmManager.defineScope(scope));
		return true;
	}

	/**
	 * add resource data of a certain type to a specified topic - if necessary,
	 * set a scope
	 * 
	 * @param element
	 *            the topic representing the element to which the resource has
	 *            to be added
	 * @param data
	 *            the actual resource data (either a link, or raw or xml content
	 * @param type
	 *            the type of the resource
	 * @param scope
	 *            a set of topics defining the scope of this association
	 * @return
	 */
	public boolean addResource(Topic element, String data, String type,
			Set<Topic> scope) {
		if (!validateResource(element, type))
			return false;
		Occurrence o = tmManager.addOccurrence(element, data, type);
		if (scope != null && scope.size() > 0)
			tmManager.setScope(o, tmManager.defineScope(scope));
		return true;
	}

	/**
	 * validates, weather a given set of topic-role-combinations can be used in a
	 * certain association. The validation is based on the specified association
	 * types
	 * 
	 * @param assocType
	 *            the association type for which the validation should be
	 *            carried out
	 * @param rt
	 *            the topic-role combination of actual elements to be validated
	 * @return
	 */
	public boolean validateAssociation(String assocType, Set<RoleTopic> rt) {
		/*
		 * Topic t = tmManager.getTopic(assocType); if (t == null) return false;
		 * Map<AssociationRole, Association> roles =
		 * t.getAssociatedAssociations(); Iterator<Association> i =
		 * roles.values().iterator(); Association a = null; while (i.hasNext()) {
		 * Association a = i.next(); if
		 * (ar.getType().getSubjectIdentifiers().contains("urn:subject:ar" +
		 * assocType)) a = ar.getParent(); } if (a == null) return false; Map<AssociationRole,
		 * Topic> types = a.getAssociatedTopics(); i =
		 * types.keySet().iterator(); // check roles and cardinality
		 */
		// todo
		return true;
	}

	/**
	 * validates, weather a given element (represented by a topic) of a certain
	 * type can contain a resource of a certain type. The validation is based on
	 * the specified resource types
	 * 
	 * @param element
	 *            the element to be validated
	 * @param type
	 *            the resource type for which the validation should be carried
	 *            out
	 * @return
	 */
	public boolean validateResource(Topic element, String type) {
		// todo
		return true;
	}

	/**
	 * shortcut to define a new association type named 'nested', which is needed
	 * regularly to define hierarchical relationships
	 * 
	 * @param superordinates
	 *            the element types to be used on the upper layer of the
	 *            hierarchy
	 * @param subordinates
	 *            the element types to be used on the lower layer of the
	 *            hierarchy
	 */
	public void createNewNestedAssocType(Set<Topic> superordinates,
			Set<Topic> subordinates) {
		createNewNestedAssocType(superordinates, subordinates, null);
	}

	/**
	 * shortcut to define a new association type named 'nested', which is needed
	 * regularly to define hierarchical relationships. In this case the,
	 * subordinates are an ordered set and thus have a fist element
	 * 
	 * @param superordinates
	 *            the element types to be used on the upper layer of the
	 *            hierarchy
	 * @param subordinates
	 *            the element types to be used on the lower layer of the
	 *            hierarchy
	 * @param firstSubelement
	 *            the element type for the first element of the lower layer of
	 *            the hierarchy
	 */
	public void createNewNestedAssocType(Set<Topic> superordinates,
			Set<Topic> subordinates, Topic firstSubelement) {
		Set<RoleElementCombination> roles = new HashSet<RoleElementCombination>();
		roles.add(new RoleElementCombination("Superordinate", superordinates,
				"card_1"));
		roles.add(new RoleElementCombination("Subordinate", subordinates,
				"card_n"));
		if (firstSubelement != null) {
			Set<Topic> elements = new HashSet<Topic>();
			elements.add(firstSubelement);
			roles.add(new RoleElementCombination("FirstBlock", elements,
					"card_1"));
		}
		addMetamodelAssociation("Nested", roles);
	}

	/**
	 * shortcut to define a new association type named 'sibling', which is needed
	 * regularly to define ordered sets of elements
	 * 
	 * @param elements
	 *            the types of elements that may be used in the ordered set of
	 *            elements
	 */
	public void createNewSiblingAssocType(Set<Topic> elements) {
		Set<RoleElementCombination> roles = new HashSet<RoleElementCombination>();
		roles
				.add(new RoleElementCombination("Predecessor", elements,
						"card_1"));
		roles.add(new RoleElementCombination("Successor", elements, "card_1"));
		addMetamodelAssociation("Sibling", roles);
	}

	/**
	 * A wrapper class to represent combinations of elements and roles to be
	 * used in associations
	 * 
	 * @author oppl
	 * 
	 */
	public static class RoleElementCombination {
		public String role;

		public Set<Topic> elements;

		public String cardinality;

		public RoleElementCombination(String role, Set<Topic> elements,
				String cardinality) {
			this.role = role;
			this.elements = elements;
			this.cardinality = cardinality;
		}
	}
}
