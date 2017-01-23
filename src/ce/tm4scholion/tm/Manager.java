package ce.tm4scholion.tm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * TopicMap Engine - Manager The Manager-class provides access-routines to
 * manipulate Topic Maps. In general, basic manipulation (that is, altering the
 * TopicMap) should be carried out using the methods defined here, as they
 * ensure consisteny across the whole Topic Map. Actually, the classes for
 * TopicMap-Constructs only provide public methods for operations, which cannot
 * corrupt the map's consistency. However, every operation publicly available
 * there can also be carried out using a (wrapper-)method of the Manager-class.
 * Every Manager-object can handle exactly one TopicMap. In addition to the
 * TopicMap itself, the Manager holds serveral indices of Topics used for typing
 * and of scopes for more efficient and consistent access. Whenever a
 * Manager-object is created, an according TopicMap-object is also set up, which
 * already contains the core topics (for miscellaneous types) defined in the
 * standard.
 * </p>
 * The indices are implemented as maps, where the subjectIdentifiers of the
 * Topics are used as the key values. If a Topic contains several
 * subjectIdentifiers, it is registered in the index several times accordingly.
 * As subjectIdentifiers have to be locators and thus URIs, the following
 * notation convention for subjectIdentifiers has been defined:
 * "urn:subject:CNxxx", where CN is
 * <ul>
 * <li> 'a' for Topics representing AssociationTypes </li>
 * <li> 'ar' for Topics representing AssociationRoleTypes </li>
 * <li> 'o' for Topics representing OccurrenceTypes </li>
 * <li> 'tn' for Topics representing TopicNameTypes </li>
 * <li> 't' for plain Topics </li>
 * </ul>
 * <ul>
 * <li>'reifier' for Topics used to reify a reifiable construct</li>
 * </ul>
 * <p/> 'xxx' stands for the name of the Topic, which is also used to create the
 * first TopicName of the resprective Topic. Note, that a topic may have several
 * subjectIdentifiers and thus can be used e.g. as a plain Topic and an
 * AssociationType at the same time. <p/> Additionally this notation is also
 * used for the naming of scopes (which actually do not have
 * subjectIdentifiers): 's' for the names of Scopes
 * 
 * @author oppl
 */
public class Manager {

	private TopicMap tm;

	/**
	 * @uml.property name="scopes"
	 * @uml.associationEnd qualifier="key:java.lang.Object
	 *                     ce.tm4scholion.tm.Scope"
	 */
	private Map<String, Scope> scopes;

	/**
	 * @uml.property name="topicTypes"
	 * @uml.associationEnd qualifier="key:java.lang.Object
	 *                     ce.tm4scholion.tm.Topic"
	 */
	private Map<String, Topic> topicTypes;

	/**
	 * @uml.property name="associationTypes"
	 * @uml.associationEnd qualifier="key:java.lang.Object
	 *                     ce.tm4scholion.tm.Topic"
	 */
	private Map<String, Topic> associationTypes;

	/**
	 * @uml.property name="associationRoleTypes"
	 * @uml.associationEnd qualifier="key:java.lang.Object
	 *                     ce.tm4scholion.tm.Topic"
	 */
	private Map<String, Topic> associationRoleTypes;

	/**
	 * @uml.property name="occurrenceTypes"
	 * @uml.associationEnd qualifier="key:java.lang.Object
	 *                     ce.tm4scholion.tm.Topic"
	 */
	private Map<String, Topic> occurrenceTypes;

	/**
	 * @uml.property name="topicNameTypes"
	 * @uml.associationEnd qualifier="key:java.lang.Object
	 *                     ce.tm4scholion.tm.Topic"
	 */
	private Map<String, Topic> topicNameTypes;

	private Topic typeInstanceAssociationTypeTopic;

	private Topic typeAssociationRoleTypeTopic;

	private Topic instanceAssociationRoleTypeTopic;

	private Topic supertypeSubtypeAssociationTypeTopic;

	private Topic supertypeAssociatonRoleTypeTopic;

	private Topic subtypeAssociatonRoleTypeTopic;

	private Scope sortNameScope;

	private Topic defaultTopicNameTypeTopic;

	private Topic associationTypeNameTopic;

	private Topic associationRoleTypeNameTopic;

	private Topic occurrenceTypeNameTopic;

	private Topic topicNameTypeNameTopic;

	private Topic scopeNameTopicNameTypeTopic;

	private Topic scopeNameAssociationTypeTopic;

	private Topic scopeDefiningTopicAssociationRoleTypeTopic;

	private Topic scopeNameAssociationRoleTypeTopic;

	/**
	 * the default constructor. Creates a Manager object and sets up the topic
	 * map and the indices to manage it.
	 * 
	 */
	public Manager() {
		tm = new TopicMap();

		topicTypes = new HashMap<String, Topic>();
		associationTypes = new HashMap<String, Topic>();
		associationRoleTypes = new HashMap<String, Topic>();
		occurrenceTypes = new HashMap<String, Topic>();
		topicNameTypes = new HashMap<String, Topic>();

		scopes = new HashMap<String, Scope>();

		generateCoreTopics();
	}
	
	/**
	 * constructor to create a manager for an already existing topic map
	 * 
	 * @param tm the Topic Map to build the manager for
	 */
	public Manager(TopicMap tm) {
		this.tm = tm;
		topicTypes = new HashMap<String, Topic>();
		associationTypes = new HashMap<String, Topic>();
		associationRoleTypes = new HashMap<String, Topic>();
		occurrenceTypes = new HashMap<String, Topic>();
		topicNameTypes = new HashMap<String, Topic>();
		scopes = new HashMap<String, Scope>();
		
		initCoreTopicsFromTM();
		registerScopes(getScopesFromTM());

		Iterator<Association> ai = tm.getAssociations().iterator();
		while (ai.hasNext()) {
			Association a = ai.next();
			registerType(associationTypes, a.getType());
			Iterator<AssociationRole> ari = a.getRoles().iterator();
			while (ari.hasNext()) {
				AssociationRole ar = ari.next();
				registerType(associationRoleTypes, ar.getType());
			}
		}

		Iterator<Topic> ti = tm.getTopics().iterator();
		while (ti.hasNext()) {
			Topic t = ti.next();
			
			Iterator<Topic> tti = this.getCounterpartTopics(t, instanceAssociationRoleTypeTopic, typeAssociationRoleTypeTopic).iterator();
			while (tti.hasNext()){
				registerType(topicTypes, tti.next());
			}
			
			if(t.getTopicNames()!=null){
				Iterator<TopicName> tni = t.getTopicNames().iterator();
				while (tni.hasNext()) {
					TopicName tn = tni.next();
					if(tn.getType()!=null)
						registerType(topicNameTypes, tn.getType());
				}
			}
			
			if(t.getOccurrences()!=null){
				Iterator<Occurrence> oi = t.getOccurrences().iterator();
				while (oi.hasNext()) {
					Occurrence o = oi.next();
					registerType(occurrenceTypes, o.getType());
				}
			}
		}

	}

	/**
	 * generates the core Topics defined in the standard (and some other helper
	 * topics) and registers them to the respective indices
	 * 
	 */
	private void generateCoreTopics() {
		//core Topics
		generateTypeInstanceAT();
		generateTypeAR();
		generateInstanceAR();
		
		generateSuperTypeSubtypeAT();
		generateSuperTypeAR();
		generateSubTypeAR();
		
		generateDefaultTN();
		generateSortT();

		// non-standard topics
		generateAssociationType();
		generateAssociationRoleType();
		generateOccurrenceType();
		generateTopicNameType();
		
		generateScopeName();
		generateAScopeName();
		generateScopeDefiningTopicAR();
		generateScopeNameAR();
	}
	
	/**
	 * generate a Topic which represents the core Topic 
	 * type-instance and register it
	 */
	private void generateTypeInstanceAT(){
		typeInstanceAssociationTypeTopic = new Topic();
		typeInstanceAssociationTypeTopic
				.addSubjectIdentifier("http://psi.topicmaps.org/iso13250/model/type-instance");
		typeInstanceAssociationTypeTopic
				.addSubjectIdentifier("urn:subject:aTypeInstance");
		TopicName tn = new TopicName();
		tn.setValue("TypeInstance");
		tn.setType(associationTypeNameTopic);
		typeInstanceAssociationTypeTopic.addTopicName(tn);		
		tm.addTopic(typeInstanceAssociationTypeTopic);
		registerType(associationTypes, typeInstanceAssociationTypeTopic);
	}
	
	/**
	 * generate a Topic which represents the core Topic 
	 * type and register it
	 */
	private void generateTypeAR(){
		typeAssociationRoleTypeTopic = new Topic();
		typeAssociationRoleTypeTopic
				.addSubjectIdentifier("http://psi.topicmaps.org/iso13250/model/type");
		typeAssociationRoleTypeTopic.addSubjectIdentifier("urn:subject:arType");
		TopicName tn = new TopicName();
		tn.setValue("Type");
		tn.setType(associationRoleTypeNameTopic);
		typeAssociationRoleTypeTopic.addTopicName(tn);
		tm.addTopic(typeAssociationRoleTypeTopic);
		registerType(associationRoleTypes, typeAssociationRoleTypeTopic);
	}
	
	/**
	 * generate a Topic which represents the core Topic 
	 * instance and register it
	 */
	private void generateInstanceAR(){
		instanceAssociationRoleTypeTopic = new Topic();
		instanceAssociationRoleTypeTopic
				.addSubjectIdentifier("http://psi.topicmaps.org/iso13250/model/instance");
		instanceAssociationRoleTypeTopic
				.addSubjectIdentifier("urn:subject:arInstance");
		TopicName tn = new TopicName();
		tn.setValue("Instance");
		tn.setType(associationRoleTypeNameTopic);
		instanceAssociationRoleTypeTopic.addTopicName(tn);
		tm.addTopic(instanceAssociationRoleTypeTopic);
		registerType(associationRoleTypes, instanceAssociationRoleTypeTopic);
	}
	
	/**
	 * generate a Topic which represents the core Topic 
	 * supertype-subtype and register it
	 */
	private void generateSuperTypeSubtypeAT(){
		supertypeSubtypeAssociationTypeTopic = new Topic();
		supertypeSubtypeAssociationTypeTopic
				.addSubjectIdentifier("http://psi.topicmaps.org/iso13250/model/supertype-subtype");
		supertypeSubtypeAssociationTypeTopic
				.addSubjectIdentifier("urn:subject:aSupertypeSubtype");
		TopicName tn = new TopicName();
		tn.setValue("SupertypeSubtype");
		tn.setType(associationTypeNameTopic);
		supertypeSubtypeAssociationTypeTopic.addTopicName(tn);		
		tm.addTopic(supertypeSubtypeAssociationTypeTopic);
		registerType(associationTypes, supertypeSubtypeAssociationTypeTopic);
	}
	
	/**
	 * generate a Topic which represents the core Topic 
	 * supertype and register it
	 */
	private void generateSuperTypeAR(){
		supertypeAssociatonRoleTypeTopic = new Topic();
		supertypeAssociatonRoleTypeTopic
				.addSubjectIdentifier("http://psi.topicmaps.org/iso13250/model/supertype");
		supertypeAssociatonRoleTypeTopic
				.addSubjectIdentifier("urn:subject:arSupertype");
		TopicName tn = new TopicName();
		tn.setValue("Supertype");
		tn.setType(associationRoleTypeNameTopic);
		supertypeAssociatonRoleTypeTopic.addTopicName(tn);
		tm.addTopic(supertypeAssociatonRoleTypeTopic);
		registerType(associationRoleTypes, supertypeAssociatonRoleTypeTopic);

	}
	
	/**
	 * generate a Topic which represents the core Topic 
	 * subtype and register it
	 */
	private void generateSubTypeAR(){
		subtypeAssociatonRoleTypeTopic = new Topic();
		subtypeAssociatonRoleTypeTopic
				.addSubjectIdentifier("http://psi.topicmaps.org/iso13250/model/subtype");
		subtypeAssociatonRoleTypeTopic
				.addSubjectIdentifier("urn:subject:arSubtype");
		TopicName tn = new TopicName();
		tn.setValue("Subtype");
		tn.setType(associationRoleTypeNameTopic);
		subtypeAssociatonRoleTypeTopic.addTopicName(tn);
		tm.addTopic(subtypeAssociatonRoleTypeTopic);
		registerType(associationRoleTypes, subtypeAssociatonRoleTypeTopic);

	}
	
	/**
	 * generate a Topic which represents the core Topic 
	 * name-type and register it
	 */
	private void generateDefaultTN(){
		defaultTopicNameTypeTopic = new Topic();
		defaultTopicNameTypeTopic
				.addSubjectIdentifier("http://psi.topicmaps.org/iso13250/model/name-type");
		defaultTopicNameTypeTopic.addSubjectIdentifier("urn:subject:tnDefault");
		tm.addTopic(defaultTopicNameTypeTopic);
		registerType(topicNameTypes, defaultTopicNameTypeTopic);
	}
	
	/**
	 * generate a Topic which represents the core Topic 
	 * sort and register it
	 */
	private void generateSortT(){
		sortNameScope = new Scope();
		Topic sortName = new Topic();
		sortName
				.addSubjectIdentifier("http://psi.topicmaps.org/iso13250/model/sort");
		sortName.addSubjectIdentifier("urn:subject:sSort");
		sortNameScope.addToContext(sortName);
		tm.addTopic(sortName);
		scopes.put(sortNameScope.getId(), sortNameScope);
	}
	
	/**
	 * generate a Topic which represents the non standard core Topic 
	 * nAssociationType and register it
	 */
	private void generateAssociationType(){
		associationTypeNameTopic = new Topic();
		associationTypeNameTopic
				.addSubjectIdentifier("urn:subject:tnAssociationType");
		tm.addTopic(associationTypeNameTopic);
		registerType(topicNameTypes, associationTypeNameTopic);
	}
	
	/**
	 * generate a Topic which represents the non standard core Topic 
	 * nAssociationRoleType and register it
	 */
	private void generateAssociationRoleType(){
		associationRoleTypeNameTopic = new Topic();
		associationRoleTypeNameTopic
				.addSubjectIdentifier("urn:subject:tnAssociationRoleType");
		tm.addTopic(associationRoleTypeNameTopic);
		registerType(topicNameTypes, associationRoleTypeNameTopic);
	}
	
	/**
	 * generate a Topic which represents the non standard core Topic 
	 * nOccurrenceType and register it
	 */
	private void generateOccurrenceType(){
		occurrenceTypeNameTopic = new Topic();
		occurrenceTypeNameTopic
				.addSubjectIdentifier("urn:subject:tnOccurrenceType");
		tm.addTopic(occurrenceTypeNameTopic);
		registerType(topicNameTypes, occurrenceTypeNameTopic);
	}
	
	/**
	 * generate a Topic which represents the non standard core Topic 
	 * nTopicNameType and register it
	 */
	private void generateTopicNameType(){
		topicNameTypeNameTopic = new Topic();
		topicNameTypeNameTopic
				.addSubjectIdentifier("urn:subject:tnTopicNameType");
		tm.addTopic(topicNameTypeNameTopic);
		registerType(topicNameTypes, topicNameTypeNameTopic);
	}
	
	/**
	 * generate a Topic which represents the non standard core Topic 
	 * nScopeName and register it
	 */
	private void generateScopeName(){
		scopeNameTopicNameTypeTopic = new Topic();
		scopeNameTopicNameTypeTopic
				.addSubjectIdentifier("urn:subject:tnScopeName");
		tm.addTopic(scopeNameTopicNameTypeTopic);
		registerType(topicNameTypes, scopeNameTopicNameTypeTopic);
	}
	
	/**
	 * generate a Topic which represents the non standard core Topic 
	 * ScopeName and register it
	 */
	private void generateAScopeName(){
		scopeNameAssociationTypeTopic = new Topic();
		scopeNameAssociationTypeTopic
				.addSubjectIdentifier("urn:subject:aScopeName");
		TopicName tn = new TopicName();
		tn.setValue("ScopeName");
		tn.setType(associationTypeNameTopic);
		scopeNameAssociationTypeTopic.addTopicName(tn);		
		tm.addTopic(scopeNameAssociationTypeTopic);
		registerType(associationTypes, scopeNameAssociationTypeTopic);
	}
	
	/**
	 * generate a Topic which represents the non standard core Topic 
	 * ScopeDefiningTopic and register it
	 */
	private void generateScopeDefiningTopicAR(){
		scopeDefiningTopicAssociationRoleTypeTopic = new Topic();
		scopeDefiningTopicAssociationRoleTypeTopic
				.addSubjectIdentifier("urn:subject:arScopeDefiningTopic");
		TopicName tn = new TopicName();
		tn.setValue("ScopeDefiningTopic");
		tn.setType(associationRoleTypeNameTopic);
		scopeDefiningTopicAssociationRoleTypeTopic.addTopicName(tn);
		tm.addTopic(scopeDefiningTopicAssociationRoleTypeTopic);
		registerType(associationRoleTypes,
				scopeDefiningTopicAssociationRoleTypeTopic);
	}
	
	/**
	 * generate a Topic which represents the non standard core Topic 
	 * ScopeName (AssociationRole) and register it
	 */
	private void generateScopeNameAR(){
		scopeNameAssociationRoleTypeTopic = new Topic();
		scopeNameAssociationRoleTypeTopic
				.addSubjectIdentifier("urn:subject:arScopeName");
		TopicName tn = new TopicName();
		tn.setValue("ScopeName");
		tn.setType(associationRoleTypeNameTopic);
		scopeNameAssociationRoleTypeTopic.addTopicName(tn);
		tm.addTopic(scopeNameAssociationRoleTypeTopic);
		registerType(associationRoleTypes, scopeNameAssociationRoleTypeTopic);
	}
	
	/**
	 * initialize core Topics and Indices of a this Manager from an underlying Topic Map 
	 */
	private void initCoreTopicsFromTM(){
		Set<String> subIds = new HashSet<String>();
		subIds.add("http://psi.topicmaps.org/iso13250/model/type-instance");
		subIds.add("urn:subject:aTypeInstance");
		typeInstanceAssociationTypeTopic = this.getTopic(subIds);
		if(typeInstanceAssociationTypeTopic == null)
			generateTypeInstanceAT();
		
		subIds = new HashSet<String>();
		subIds.add("http://psi.topicmaps.org/iso13250/model/type");
		subIds.add("urn:subject:arType");
		typeAssociationRoleTypeTopic = this.getTopic(subIds);
		if(typeAssociationRoleTypeTopic == null)
			generateTypeAR();


		subIds = new HashSet<String>();
		subIds.add("http://psi.topicmaps.org/iso13250/model/instance");
		subIds.add("urn:subject:arInstance");
		instanceAssociationRoleTypeTopic = this.getTopic(subIds);
		if(instanceAssociationRoleTypeTopic == null)
			generateInstanceAR();
		
		subIds = new HashSet<String>();
		subIds.add("http://psi.topicmaps.org/iso13250/model/supertype-subtype");
		subIds.add("urn:subject:aSupertypeSubtype");
		supertypeSubtypeAssociationTypeTopic = this.getTopic(subIds);
		if(supertypeSubtypeAssociationTypeTopic == null)
			generateSuperTypeSubtypeAT();
		
		subIds = new HashSet<String>();
		subIds.add("http://psi.topicmaps.org/iso13250/model/supertype");
		subIds.add("urn:subject:arSupertype");
		supertypeAssociatonRoleTypeTopic = this.getTopic(subIds);
		if(supertypeAssociatonRoleTypeTopic == null)
			generateSuperTypeAR();

		subIds = new HashSet<String>();
		subIds.add("http://psi.topicmaps.org/iso13250/model/subtype");
		subIds.add("urn:subject:arSubtype");
		subtypeAssociatonRoleTypeTopic = this.getTopic(subIds);
		if(subtypeAssociatonRoleTypeTopic == null)
			generateSubTypeAR();

		subIds = new HashSet<String>();
		subIds.add("http://psi.topicmaps.org/iso13250/model/name-type");
		subIds.add("urn:subject:tnDefault");
		defaultTopicNameTypeTopic = this.getTopic(subIds);
		if(defaultTopicNameTypeTopic == null)
			generateDefaultTN();
		
		subIds = new HashSet<String>();
		subIds.add("http://psi.topicmaps.org/iso13250/model/sort");
		subIds.add("urn:subject:sSort");
		Topic sortName = this.getTopic(subIds);
		if(sortName == null){
			generateSortT();
		}else{
			sortNameScope = new Scope();
			sortNameScope.addToContext(sortName);
			scopes.put(sortNameScope.getId(), sortNameScope);
		}

		// non-standard topics
		associationTypeNameTopic = this.getTopic("nAssociationType");
		if(associationTypeNameTopic == null)
			generateAssociationType();
		
		associationRoleTypeNameTopic = this.getTopic("nAssociationRoleType");
		if(associationRoleTypeNameTopic == null)
			generateAssociationRoleType();

		occurrenceTypeNameTopic = this.getTopic("nOccurrenceType");
		if(occurrenceTypeNameTopic == null)
			generateOccurrenceType();

		topicNameTypeNameTopic = this.getTopic("nTopicNameType");
		if(topicNameTypeNameTopic == null)
			generateTopicNameType();

		scopeNameTopicNameTypeTopic = this.getTopic("nScopeName");
		if(scopeNameTopicNameTypeTopic == null)
			generateScopeName();
		
		scopeNameAssociationTypeTopic = this.getTopic("ScopeName");
		if(scopeNameAssociationTypeTopic == null)
			generateAScopeName();

		scopeDefiningTopicAssociationRoleTypeTopic = this.getTopic("ScopeDefiningTopic");
		if(scopeDefiningTopicAssociationRoleTypeTopic == null)
			generateScopeDefiningTopicAR();

		subIds = new HashSet<String>();
		subIds.add("urn:subject:arScopeName");
		scopeNameAssociationRoleTypeTopic = this.getTopic(subIds);
		if(scopeNameAssociationRoleTypeTopic == null)
			generateScopeNameAR();
	}
	
	/**
	 * get Scopes from a Topic Map - searches in all Statements of a Topic Map for different Scopes
	 * and returns a Set of Scopes found
	 *  
	 * @return Set of Scopes found, if none were found an empty set
	 */
	private Set<Scope> getScopesFromTM(){
		Set<Scope> returnSet = new HashSet<Scope>();
		if(tm.getTopics()!=null){
			Iterator<Topic> tIt = tm.getTopics().iterator();
			while(tIt.hasNext()){//topics
				Topic topic = tIt.next();
				if(topic.getTopicNames()!=null){
					Iterator<TopicName> tNIt = topic.getTopicNames().iterator();
					//get Scopes of TopicNames in current Topic
					while(tNIt.hasNext()){
						TopicName topicName = tNIt.next();
						if(topicName.getScope()!=null){
							Scope tNScope = getScope(returnSet, topicName.getScope().getContext());
							if(tNScope==null){
								returnSet.add(topicName.getScope());
							}else{
								topicName.setScope(tNScope);
								tNScope.addStatement(topicName);
							}
						}
						if(topicName.getVariants()!=null){
							Iterator<Variant> vIt = topicName.getVariants().iterator();
							//get Scopes of Variants in TopicName
							while(vIt.hasNext()){
								Variant variant = vIt.next();
								if(variant.getScope()!=null){
									Scope varScope = getScope(returnSet, variant.getScope().getContext());
									if(varScope==null){
										returnSet.add(variant.getScope());
									}else{
										variant.setScope(varScope);
										varScope.addStatement(variant);
									}
								}
							}	
						}	
					}
				}
				if(topic.getOccurrences()!=null){
					Iterator<Occurrence> oIt = topic.getOccurrences().iterator();
					//get Scopes of Occurrences of Topic
					while(oIt.hasNext()){
						Occurrence o = oIt.next();
						if(o.getScope()!=null){
							Scope occScope = getScope(returnSet, o.getScope().getContext());
							if(occScope==null){
								returnSet.add(o.getScope());
							}else{
								o.setScope(occScope);
								occScope.addStatement(o);
							}
						}
					}
				}
			}
		}
		if(tm.getAssociations()!=null){
			Iterator<Association> aIt = tm.getAssociations().iterator();
			//get Scopes of Associations 
			while(aIt.hasNext()){
				Association a = aIt.next();
				if(a.getScope()!=null){
					Scope assScope = getScope(returnSet, a.getScope().getContext());
					if(assScope==null){
						returnSet.add(a.getScope());
					}else{
						a.setScope(assScope);
						assScope.addStatement(a);
					}
				}
			}
		}
//		System.out.println("get Scopes size: "+returnSet.size());
		return returnSet;
	}
	
	/**
	 * register a Set of Scopes to the respective Indices
	 * 
	 * @param scopes Set of Scopes which will be registered
	 */
	private void registerScopes(Set<Scope> scopes){
		Iterator<Scope> i = scopes.iterator();
		while(i.hasNext()){
			Scope curS = i.next();
			this.scopes.put(curS.getId(),curS);
		}
	}
	
	/**
	 * get a Scope from a Set of Scopes by its context
	 * 
	 * @param scopes Set of Scopes in which a Scope with the given Context will be searched
	 * @param context Set of Topics which represents the context of the scope
	 * @return the Scope found, otherwise null
	 */
	private Scope getScope(Set<Scope> scopes, Set<Topic> context){
		Scope curS;
		Iterator<Scope> i = scopes.iterator();
		while(i.hasNext()){
			curS = i.next();
			if(context.containsAll(curS.getContext())&& curS.getContext().containsAll(context)){
				return curS;
			}
		}
		return null;
	}
	

	/**
	 * register a new type in one of the following indices: associationTypes,
	 * associationRoleTypes, occurrenceTypes, topicNameTypes, topicTypes
	 * 
	 * @param map
	 *            the type-index in which the type has to be registered
	 * @param type
	 *            the Topic representing the type to be registered
	 */
	private void registerType(Map<String, Topic> map, Topic type) {
//		System.out.println("registerType type: "+type);
		Iterator<String> i = type.getSubjectIdentifiers().iterator();
		while (i.hasNext())
			map.put(i.next(), type);
	}

	/**
	 * deregister a type in one of the following indices: associationTypes,
	 * associationRoleTypes, occurrenceTypes, topicNameTypes, topicTypes
	 * 
	 * @param map
	 *            the type-index in which the type has to be registered
	 * @param type
	 *            the Topic representing the type to be registered
	 */
	private void unregisterType(Map<String, Topic> map, Topic type) {
		Iterator<String> i = type.getSubjectIdentifiers().iterator();
		while (i.hasNext())
			map.remove(i.next());
	}

	/**
	 * add a Topic to the TopicMap using the given name. A respective TopicName
	 * item is also created
	 * 
	 * @param name
	 *            the name (subject) of the Topic
	 * @return the new Topic object
	 */
	public Topic addTopic(String name) {
		Topic t = generateTopic(name);
		tm.addTopic(t);
		return t;
	}

	/**
	 * generates a new Topic using the given name but does not add it to the
	 * TopicMap. A respective TopicName item is also created
	 * 
	 * @param name
	 *            the name (subject) of the Topic
	 * @return the new Topic object
	 */
	public Topic generateTopic(String name) {
		Topic t = new Topic();
		t.addSubjectIdentifier("urn:subject:t" + name);
		TopicName tn = new TopicName();
		tn.setValue(name);
		tn.setType(defaultTopicNameTypeTopic);
		t.addTopicName(tn);
		return t;
	}

	/**
	 * add a given Topic to the TopicMap
	 * 
	 * @param t
	 *            the Topic to be added
	 * @return the added Topic
	 */
	public Topic addTopic(Topic t) {
		tm.addTopic(t);
		return t;
	}

	/**
	 * Retrieve a Topic from the TopicMap based on its subject name
	 * 
	 * @param name
	 *            the subject name to search for
	 * @return the fond topic, null, if no topic was found
	 */
	public Topic getTopic(String name) {
		Set<Topic> topics = tm.getTopics();
		Iterator<Topic> i = topics.iterator();
		while (i.hasNext()) {
			Topic t = i.next();
			if (t.subjectIdentifiers.contains("urn:subject:t" + name))
				return t;
			if (t.subjectIdentifiers.contains("urn:subject:o" + name))
				return t;
			if (t.subjectIdentifiers.contains("urn:subject:a" + name))
				return t;
			if (t.subjectIdentifiers.contains("urn:subject:ar" + name))
				return t;
		}
		return null;
	}
	
	/**
	 * Retrieve a Topic from the TopicMap based on its subjectIdentifiers.
	 * Retrieves the Topic if a Topic with exactly the same subjectIdentifiers 
	 * was found in the Topic Map
	 * 
	 * @param subjectIdentifiers
	 * @return the topic found, otherwise null
	 */
	public Topic getTopic(Set<String> subjectIdentifiers){
		Set<Topic> topics = tm.getTopics();
		Iterator<Topic> i = topics.iterator();
		while (i.hasNext()) {
			Topic t = i.next();
			if (t.subjectIdentifiers.containsAll(subjectIdentifiers) && subjectIdentifiers.containsAll(t.subjectIdentifiers))
				return t;
		}
		return null;
	}
	
	/**
	 * get a Topic for a given name and type from the Topic Map
	 * @param name name of the searched Topic
	 * @param type type of the searched Topic
	 * @return found Topic, otherwise null
	 */
	public Topic getTopicOfType(String name, Topic type){
		Set<Topic> tFound = new HashSet<Topic>();
		Set<Topic> topics = tm.getTopics();
		Iterator<Topic> i = topics.iterator();
		while (i.hasNext()) {
			Topic t = i.next();
			if (t.subjectIdentifiers.contains("urn:subject:t" + name))
				tFound.add(t);
			if (t.subjectIdentifiers.contains("urn:subject:o" + name))
				tFound.add(t);
			if (t.subjectIdentifiers.contains("urn:subject:a" + name))
				tFound.add(t);
			if (t.subjectIdentifiers.contains("urn:subject:ar" + name))
				tFound.add(t);
		}
		if(tFound.isEmpty())
			return null;
		Topic t;
		Iterator<Topic> fI = tFound.iterator();
		while(fI.hasNext()){
			t = fI.next();
			if(this.getTypeOf(t).equals(type))
				return t;
		}
		return null;
	}
	
	public Topic getTopicOfType(String name, Topic type, Scope scope){
		Set<Topic> tFound = new HashSet<Topic>();
		Set<Topic> topics = tm.getTopics();
		Iterator<Topic> i = topics.iterator();
		while (i.hasNext()) {
			Topic t = i.next();
			if (t.subjectIdentifiers.contains("urn:subject:t" + name))
				tFound.add(t);
			if (t.subjectIdentifiers.contains("urn:subject:o" + name))
				tFound.add(t);
			if (t.subjectIdentifiers.contains("urn:subject:a" + name))
				tFound.add(t);
			if (t.subjectIdentifiers.contains("urn:subject:ar" + name))
				tFound.add(t);
		}
		if(tFound.isEmpty())
			return null;
		Topic t;
		Iterator<Topic> fI = tFound.iterator();
		while(fI.hasNext()){
			t = fI.next();
			if(getTypeOf(t, scope) != null && getTypeOf(t, scope).equals(type))
				return t;
		}
		return null;
	}
	

	/**
	 * add a Topic to the TopicMap using the given name and make it an instance
	 * of the given type. A respective TopicName item is also created
	 * 
	 * @param name
	 *            the name (subject) of the Topic
	 * @param type
	 *            the Topic Type the new Topic should by an instance of (has be
	 *            existent)
	 * @return the new Topic object, null if the given Topic Type does not exist
	 */
	public Topic addTopicOfType(String name, Topic type) {
		if (!topicTypes.containsValue(type)) {
			if (!tm.getTopics().contains(type)) addTopic(type);
			registerType(topicTypes,type);
		}
		Topic t = addTopic(name);
		setTypeInstance(t, type);
		return t;
	}
	
	public Topic addTopicOfType(String name, Topic type, Scope scope) {
		if (!topicTypes.containsValue(type)) {
			if (!tm.getTopics().contains(type)) addTopic(type);
			registerType(topicTypes,type);
		}
		Topic t = addTopic(name);
		setTypeInstance(t, type, scope);
		return t;
	}
	
	/**
	 * get a Topic which represents a type within type-instance relationships
	 * in the Topic Map
	 * 
	 * @param type specifies the searched type
	 * @return Topic where TopicName = type and subjectIdentifier = "urn:subject:t"+type, null if none exists
	 */
	public Topic getTopicType(String type){
		String subjectIdentifier = "urn:subject:t"+type;
		return topicTypes.get(subjectIdentifier);
	}
	
	/**
	 * register a new Topic type in index topicTypes
	 * 
	 * @param type Topic which represents the type
	 */
	public void registerTopicType(Topic type){
		registerType(topicTypes,type);
	}
	
	/**
	 * Retrieves the topics that take a certain role in a specified association
	 * 
	 * @param a
	 *            the association to be analysed
	 * @param roleType
	 *            the association role type to be looked for
	 * @return the topics that take the given role in the given association
	 */
	public Set<Topic> getTopicsInAssocRole(Association a, Topic roleType) {
		Set<Topic> topics = new HashSet<Topic>();
		Map<AssociationRole, Topic> t = a.getAssociatedTopics();
		Iterator<AssociationRole> i = t.keySet().iterator();
		while (i.hasNext()) {
			AssociationRole ar = i.next();
			if (ar.getType().equals(roleType))
				topics.add(t.get(/*roleType*/ar));//Mane Änderung
		}
		return topics;
	}
	
	/**
	 * get Associations of a Topic Map in a Map where key = Topic which 
	 * represents the AssociationType and Value = current Associations 
	 * for the specified type
	 * 
	 * @return
	 */
	public Map<Topic,Set<Association>> getAssociationsOfType(){
		Map<Topic,Set<Association>> returnSet = new HashMap<Topic,Set<Association>>();
		Association a;
		Iterator<Association> i = tm.getAssociations().iterator();
		while(i.hasNext()){
			a = i.next();
			if(returnSet.containsKey(a.getType())){
				returnSet.get(a.getType()).add(a);
			}else{
				HashSet<Association> assocSet = new HashSet<Association>();
				assocSet.add(a);
				returnSet.put(a.getType(), assocSet);
			}
		}
		return returnSet;
	}
	
	/**
	 * Retrieves the associations a specified topic participates in in a certain
	 * role
	 * 
	 * @param t
	 *            the topic to be analysed
	 * @param roleType
	 *            the association role type to be looked for
	 * @return the associations the given topic participates in in the given
	 *         role
	 */
	public Set<Association> getAssociationsParticipatedInRole(Topic t,
			Topic roleType) {
		Set<Association> associations = new HashSet<Association>();
		Set<Topic> validRoleTypes = getSubtypes(roleType);
		validRoleTypes.add(roleType);
		Map<AssociationRole, Association> a = t.getAssociatedAssociations();
		Iterator<AssociationRole> i = a.keySet().iterator();
		while (i.hasNext()) {
			AssociationRole ar = i.next();
			if (validRoleTypes.contains(ar.getType())){
				associations.add(a.get(ar));//Mane Änderung
//				associations.add(a.get(roleType));
			}
		}
//		System.out.println("getAssociationsParticipatedInRole - associations size:"+ associations.size());
		return associations;
	}

	/**
	 * Retrieves the association roles that build the bridge between a specified
	 * topic and association
	 * 
	 * @param t
	 *            the topic to be analysed
	 * @param a
	 *            the assoication to be analysed
	 * @return the association roles that build the bridge between the given
	 *         topic and association
	 */
	public Set<AssociationRole> getAssociationsRolesBetweenAssociationAndTopic(
			Topic t, Association a) {
		Set<AssociationRole> associationRoles = new HashSet<AssociationRole>();
		Map<AssociationRole, Association> assocs = t
				.getAssociatedAssociations();
		Iterator<AssociationRole> i = assocs.keySet().iterator();
		while (i.hasNext()) {
			AssociationRole ar = i.next();
			if (assocs.get(ar).equals(a))
				associationRoles.add(ar);
		}
		return associationRoles;
	}
	
	/**
	 * get an association between 2 Topic within a given scope
	 * 
	 * @param assocType
	 * @param roleT1
	 * @param t1
	 * @param roleT2
	 * @param t2
	 * @param scope
	 * @return
	 */
	public Association getAssociationBetween2Topics(String assocType, String roleT1, Topic t1, String roleT2, Topic t2, Scope scope){
		Topic assocTT = this.getDefinedAssociationType(assocType);
		Topic rT1T = this.getDefinedAssociationRoleType(roleT1);
		Topic rT2T = this.getDefinedAssociationRoleType(roleT2);
		if(assocTT == null || rT1T == null || rT2T == null)
			return null;
		
//		boolean t1Included = false;
//		boolean t2Included = false;
		Association curA;
		Iterator<Association> i = this.getAssocsFromScope(scope).iterator();
		while(i.hasNext()){
			curA = i.next();
			if(curA.getType().equals(assocTT) && topicsPlayRoles(rT1T, t1, rT2T, t2, curA.getRoles()))
				return curA;
//			if(curA.getType().equals(assocTT) && curA.getRoles().size()==2){
//				AssociationRole curAr;
//				Iterator<AssociationRole> aI = curA.getRoles().iterator();
//				while(aI.hasNext()){
//					curAr = aI.next();
//					if(curAr.getType().equals(rT1T) && curAr.getPlayer().equals(t1))
//						t1Included = true;
//					if(curAr.getType().equals(rT2T) && curAr.getPlayer().equals(t2))
//						t2Included = true;
//				}
//				if(t1Included && t2Included){
//					return curA;
//				}else{
//					t1Included = false;
//					t2Included = false;
//				}	
//			}
		}
		return null;
	}

	/**
	 * check if two given Topics play given role types in a set of association roles. This method is used for binary associations, 
	 * therefore the size of assocRoles should be 2 to ensure right outcome
	 * 
	 * @param roleT1 associationRole type of t1 
	 * @param t1 Topic which is  player in an associationRole of type roleT1
	 * @param roleT2 associationRole type of t2
	 * @param t2 Topic which is  player in an associationRole of type roleT2
	 * @param assocRoles a Set of associationRoles which will be evaluated
	 * @return true if the set consists of two associationRoles of the given types and with the given players, any other case false
	 */
	public boolean topicsPlayRoles(Topic roleT1, Topic t1, Topic roleT2, Topic t2, Set<AssociationRole> assocRoles){
		if(assocRoles.size() != 2)
			return false;
		AssociationRole curAr;
		boolean t1Included = false;
		boolean t2Included = false;
		Iterator<AssociationRole> aI = assocRoles.iterator();
		while(aI.hasNext()){
			curAr = aI.next();
			if(curAr.getType().equals(roleT1) && curAr.getPlayer().equals(t1))
				t1Included = true;
			if(curAr.getType().equals(roleT2) && curAr.getPlayer().equals(t2))
				t2Included = true;
		}
		return t1Included && t2Included;
	}
	
	/**
	 * Combines getAssociationsParticipatedInRole and getTopicsInAssocRole and
	 * thus retrieves all topics that are assocaited with a given one in a
	 * specified role setting
	 * 
	 * @param givenTopic
	 *            the originating topic (to be analysed)
	 * @param givenRoleType
	 *            the role type of the given topic to be looked for
	 * @param searchedRoleType
	 *            the role type of the counterpart topics to be looked for
	 * @return all topics that are associated with the given topic in a certain -
	 *         given - role setting
	 */
	public Set<Topic> getCounterpartTopics(Topic givenTopic,
			Topic givenRoleType, Topic searchedRoleType) {
		Set<Topic> topics = new HashSet<Topic>();
		Set<Association> assocs = getAssociationsParticipatedInRole(givenTopic,
				givenRoleType);
//		System.out.println("getCounterpartTopics - assocs size:"+assocs.size());
		Iterator<Association> i = assocs.iterator();
		while (i.hasNext()) {
			Association a = i.next();
			topics.addAll(getTopicsInAssocRole(a, searchedRoleType));
		}

		return topics;
	}

	/**
	 * removes a Topic from the TopicMap. The Topic is not removed, if it is
	 * used to represent a type (except for topicTypes). If it represents a
	 * topicType, all according typeInstance-Associations are removed before the
	 * Topic is deleted from the TopicMap and the topicTypes-index.
	 * 
	 * @param t
	 *            the Topic to be removed
	 * @return true if the Topic has been successfully removed, false otherwise
	 */
	public boolean removeTopic(Topic t) {
		Set<Association> assocsToRemove = new HashSet<Association>();
		Set<String> identifiers = t.getSubjectIdentifiers();
		if (identifiers.contains("urn:subject:tnAssociation")
				|| identifiers.contains("urn:subject:tnAssociationRole")
				|| identifiers.contains("urn:subject:tnOccurrence")
				|| identifiers.contains("urn:subject:tnTopicName"))
			return false; // cannot be removed
		if (t.getRolesPlayed() != null) {
			Set<Association> associations = tm.getAssociations();
			Iterator<Association> i = associations.iterator();
			while (i.hasNext()) {
				Association a = i.next();
				if (a.getAssociatedTopics().containsValue(t)){
					//disassociate(a);
					assocsToRemove.add(a);
				}
			}
		}
		//remove associations
		Association curA;
		Iterator<Association> aIt = assocsToRemove.iterator();
		while(aIt.hasNext()){
			curA = aIt.next();
			if(curA.getScope()!= null && curA.getScope().getContents() != null)
				curA.getScope().getContents().remove(curA);
			disassociate(curA);
		}
		
		if (topicTypes.containsValue(t)) {
			unregisterType(topicTypes, t);
		}
		tm.removeTopic(t);
		return true;
	}

	/**
	 * Checks if a Topic is an instance of a given Type. Not only evaluates
	 * direct type-instance-relationships but also checks supertypes of the
	 * given topic's types (necessary because of transitive characteristics of
	 * this relationship).
	 * 
	 * @param t
	 *            the Topic to be evaluated
	 * @param type
	 *            the Type to be checked for
	 * @return true if the given Topic is an instance of the given TopicType,
	 *         false otherwise
	 */
	public boolean topicIsInstanceOf(Topic t, Topic type) {
		if (!topicTypes.containsValue(type))
			return false;
		Map<AssociationRole, Association> assocs = t
				.getAssociatedAssociations();
		Iterator<AssociationRole> i = assocs.keySet().iterator();
		while (i.hasNext()) {
			AssociationRole ar = i.next();
			if (ar.getType().equals(instanceAssociationRoleTypeTopic)) {
				Association a = assocs.get(ar);
				//mane
				Set<AssociationRole> arS = getAssocRoleOfType(typeAssociationRoleTypeTopic, a.getRoles());
				Topic topicType = a.getAssociatedTopics().get(arS.iterator().next());
//				Topic topicType = a.getAssociatedTopics().get(
//						typeAssociationRoleTypeTopic);
				if (topicType.equals(type))
					return true;
				Set<Topic> supertypes = getSupertypes(topicType);
				if (supertypes.contains(type))
					return true;
			}
		}
		return false;
	}
	
	//get AssociationRoles of type type from a set of association roles
	public Set<AssociationRole> getAssocRoleOfType(Topic type, Set<AssociationRole> arS){
		Set<AssociationRole> arReturnSet = new HashSet<AssociationRole>();
		AssociationRole ar = null;
		Iterator<AssociationRole> i = arS.iterator();
		while(i.hasNext()){
			ar = i.next();
			if(ar.getType().equals(type))
				arReturnSet.add(ar);
		}
		
		return arReturnSet;
	}
	
	//Mane
	public Set<Topic> getInstancesOf(Topic t){
		//Topic assocType = typeInstanceAssociationTypeTopic;
		Set<Topic> instances = new HashSet<Topic>();
		//Set<Topic> returnSet = new HashSet<Topic>();
		if(t != null){
			Map<AssociationRole, Association> assocs = t.getAssociatedAssociations();
			Iterator<AssociationRole> it = assocs.keySet().iterator();
			while (it.hasNext()) {
				AssociationRole ar = it.next();
				if (ar.getType().equals(typeAssociationRoleTypeTopic)) {
					Association a = assocs.get(ar);
					Topic instance = null;
					Map<AssociationRole, Topic> topics = a.getAssociatedTopics();
					Iterator<AssociationRole> tIt = topics.keySet().iterator();
					while (tIt.hasNext()) {
						AssociationRole current = tIt.next();
						if (current.getType().equals(instanceAssociationRoleTypeTopic)) instance = topics.get(current);
					}
					if (instance != null) instances.add(instance);
				}
			}
		}
		return instances;
	}
	
	/**
	 * get instances of a Topic within a given scope
	 * 
	 * @param t
	 * @param scope
	 * @return
	 */
	public Set<Topic> getInstancesOf(Topic t, Scope scope){
		Set<Topic> instances = new HashSet<Topic>();
		Association curA;
		Iterator<Association> i = this.getAssocsFromScope(scope).iterator();
		while(i.hasNext()){
			curA = i.next();
			if(curA.getType().equals(typeInstanceAssociationTypeTopic) && curA.getAssociatedTopics().values().contains(t)){
				Iterator<AssociationRole> it = curA.getRoles().iterator();
				while (it.hasNext()) {
					AssociationRole ar = it.next();
					if (ar.getType().equals(instanceAssociationRoleTypeTopic)) {
						Topic instance = ar.getPlayer();
						if (instance != null) instances.add(instance);
					}
				}
			}
		}
		return instances;
	}
	
	/**
	 * get an instance of a given topic type by name
	 * 
	 * @param name name of the searched instance
	 * @param type type of the searched instance
	 * @return null if no instance with given name of type exists
	 */
	public Topic getInstanceOfBy(String name, Topic type){
		Topic curI;
		Iterator<Topic> i = this.getInstancesOf(type).iterator();
		while(i.hasNext()){
			curI = i.next();
			if(curI.getTopicNames().iterator().next().getValue().equals(name))
				return curI;
		}
		return null;
	}
	
	/**
	 * get the type of a Topic by its Type-Instance relationship
	 *  
	 * @param t Topic which will be evaluated
	 * @return Topic which represents the type of the given Topic, null if no Type-instance association exists
	 */
	public Topic getTypeOf(Topic t){
		Topic type = null;
		if(t.getRolesPlayed() != null){
			Set<AssociationRole> assocs = t.getRolesPlayed();
			Iterator<AssociationRole> it = assocs.iterator();
			while (it.hasNext()) {
				AssociationRole ar = it.next();
				if (ar.getType().equals(instanceAssociationRoleTypeTopic)) {
					Association a = ar.getParent();
					Iterator<AssociationRole> arIt = a.getRoles().iterator();
					AssociationRole ar1;
					while(arIt.hasNext()){
						ar1 = arIt.next();
						if(!ar1.getPlayer().equals(t) && ar1.getType().equals(typeAssociationRoleTypeTopic))
							return ar1.getPlayer();
					}
				}
			}
		}
		return type;
	}
	
	/**
	 * get the type of a Topic by its Type-Instance relationship within a given scope
	 * 
	 * @param t Topic which will be evaluated
	 * @param scope Scoep within the Type-Instance relationship is searched for the given Topic
	 * @return Topic which represents the type of the given Topic, null if no Type-instance association exists
	 */
	public Topic getTypeOf(Topic t, Scope scope){
		Association curA;
		Iterator<Association> i = this.getAssocsFromScope(scope).iterator();
		while(i.hasNext()){
			curA = i.next();
			if(curA.getType().equals(typeInstanceAssociationTypeTopic) && curA.getAssociatedTopics().values().contains(t)){
				AssociationRole ar;
				Iterator<AssociationRole> it = curA.getRoles().iterator();
				while(it.hasNext()){
					ar = it.next();
					if(!ar.getPlayer().equals(t) && ar.getType().equals(typeAssociationRoleTypeTopic))
						return ar.getPlayer();
				}
			}
		}
		return null;
	}

	/**
	 * Return a Set of Topics which are associated with the given Topic in a
	 * superType-subType-Relationship either directly or indirecly (transitivly
	 * via other superTypes)
	 * 
	 * @param t
	 *            the Topic to be evaluated
	 * @return a Set of Topics which are superTypes of the given Topic
	 */
	public Set<Topic> getSupertypes(Topic t) {
		Set<Topic> superTypes = new HashSet<Topic>();
		Set<Topic> returnSet = new HashSet<Topic>();
		Map<AssociationRole, Association> assocs = t
				.getAssociatedAssociations();
		Iterator<AssociationRole> i = assocs.keySet().iterator();
		while (i.hasNext()) {
			AssociationRole ar = i.next();
			if (ar.getType().equals(subtypeAssociatonRoleTypeTopic)) {
				Association a = assocs.get(ar);
				Topic supertype = null;
				Map<AssociationRole, Topic> topics = a.getAssociatedTopics();
				Iterator<AssociationRole> u = topics.keySet().iterator();
				while (u.hasNext()) {
					AssociationRole current = u.next();
					if (current.getType().equals(supertypeAssociatonRoleTypeTopic)) supertype = topics.get(current);
				}
				if (supertype != null) {
					superTypes.add(supertype);
				}
			}
		}
		Iterator<Topic> i2 = superTypes.iterator();
		while (i2.hasNext()) {
			returnSet.addAll(getSupertypes(i2.next()));			
		}
		returnSet.addAll(superTypes);
		return returnSet;
	}

	/**
	 * Return a Set of Topics which are associated with the given Topic in a
	 * superType-subType-Relationship either directly or indirecly (transitivly
	 * via other subTypes)
	 * 
	 * @param t
	 *            the Topic to be evaluated
	 * @return a Set of Topics which are subTypes of the given Topic
	 */
	public Set<Topic> getSubtypes(Topic t) {
		Set<Topic> subTypes = new HashSet<Topic>();
		Set<Topic> returnSet = new HashSet<Topic>();
		Map<AssociationRole, Association> assocs = t
				.getAssociatedAssociations();
		Iterator<AssociationRole> i = assocs.keySet().iterator();
		while (i.hasNext()) {
			AssociationRole ar = i.next();
			if (ar.getType().equals(supertypeAssociatonRoleTypeTopic)) {
				Association a = assocs.get(ar);
				Topic subtype = null;
				Map<AssociationRole, Topic> topics = a.getAssociatedTopics();
				Iterator<AssociationRole> u = topics.keySet().iterator();
				while (u.hasNext()) {
					AssociationRole current = u.next();
					if (current.getType().equals(subtypeAssociatonRoleTypeTopic)) subtype = topics.get(current);
				}
				if (subtype != null) subTypes.add(subtype);
			}
		}
		Iterator<Topic> i2 = subTypes.iterator();
		while (i2.hasNext())
			returnSet.addAll(getSubtypes(i2.next()));
		returnSet.addAll(subTypes);
		return returnSet;
	}

	/**
	 * add a TopicName of the default TopicNameType to a Topic
	 * 
	 * @param t
	 *            the Topic to which the TopicName has to be added
	 * @param name
	 *            the name to be added
	 * @return the new TopicName object
	 */
	public TopicName addTopicName(Topic t, String name) {
		return addTopicName(t, name, "Default");
	}

	/**
	 * add a TopicName of the given TopicNameType to a Topic. If the given
	 * TopicNameType does not exist, it is created.
	 * 
	 * @param t
	 *            the Topic to which the TopicName has to be added
	 * @param name
	 *            the name to be added
	 * @param type
	 *            the TopicNameType of the TopicName
	 * @return the new TopicName object
	 */
	public TopicName addTopicName(Topic t, String name, String type) {
		TopicName tn = new TopicName();
		tn.setValue(name);
		if (!topicNameTypes.containsKey("urn:subject:tn" + type))
			defineTopicNameType(type);
		tn.setType(topicNameTypes.get("urn:subject:tn" + type));
		t.addTopicName(tn);
		return tn;
	}

	/**
	 * add a TopicName of the given TopicNameType to a Topic. If the given
	 * TopicNameType is not yet registered, it is added to the index.
	 * 
	 * @param t
	 *            the Topic to which the TopicName has to be added
	 * @param name
	 *            the name to be added
	 * @param type
	 *            the TopicNameType of the TopicName
	 * @return the new TopicName object
	 */
	public TopicName addTopicName(Topic t, String name, Topic type) {
		TopicName tn = new TopicName();
		tn.setValue(name);
		if (!topicNameTypes.containsValue(type)) {
			Iterator<TopicName> i = type.getTopicNames().iterator();
			String value = "urn:subject:tn" + i.next().getValue();
			type.addSubjectIdentifier(value);
			this.topicNameTypes.put(value, type);
		}
		tn.setType(type);
		t.addTopicName(tn);
		return tn;
	}

	/**
	 * removes a TopicName from a Topic
	 * 
	 * @param t
	 *            the Topic from which the TopicName has to be removed
	 * @param tn
	 *            the TopicName to be removed
	 */
	public void removeTopicName(Topic t, TopicName tn) {
		t.removeTopicName(tn);
	}

	/**
	 * add a Variant of the 'String'-datatype to a TopicName
	 * 
	 * @param tn
	 *            the TopicName to which the Variant has to be added
	 * @param variant
	 *            the Variant to be added
	 * @return the new Variant object
	 */
	public Variant addVariant(TopicName tn, String variant) {
		return addVariant(tn, variant, Utils.dtString);
	}

	/**
	 * add a sortable Variant of the 'String'-datatype to a TopicName
	 * 
	 * @param tn
	 *            the TopicName to which the Variant has to be added
	 * @param variant
	 *            the sortable Variant to be added
	 * @return the new Variant object
	 */
	public Variant addSortVariant(TopicName tn, String variant) {
		Variant v = addVariant(tn, variant, Utils.dtString);
		v.setScope(sortNameScope);
		return v;
	}

	/**
	 * add a Variant of the given datatype to a TopicName
	 * 
	 * @param tn
	 *            the TopicName to which the Variant has to be added
	 * @param variant
	 *            the Variant to be added
	 * @param dataType
	 *            the dataType of the Variant (for default dataTypes, see
	 *            Utils-class)
	 * @return the new Variant object
	 */
	public Variant addVariant(TopicName tn, String variant, String dataType) {
		Variant v = new Variant();
		v.setValue(variant);
		v.setDataType(dataType);
		tn.addVariant(v);
		return v;
	}

	/**
	 * mark a Variant to be 'sortable' (as defined in the TMDM)
	 * 
	 * @param v
	 *            the Variant to be marked as 'sortable'
	 */
	public void markVariantSortable(Variant v) {
		v.setScope(sortNameScope);
	}

	/**
	 * removes a Variant from a TopicName
	 * 
	 * @param tn
	 *            the TopicName from which the Variant has to be removed
	 * @param v
	 *            the Variant to be removed
	 */
	public void removeVariant(TopicName tn, Variant v) {
		tn.removeVariant(v);
	}

	/**
	 * add an Occurrence of the 'String'-datatype and the given type to a Topic.
	 * If the given TopicNameType does not exist, it is created.
	 * 
	 * @param t
	 *            the Topic to which the Occurrence has to be added
	 * @param value
	 *            the value of the Occurrence
	 * @param type
	 *            the type of the Occurrence
	 * @return the new Occurrence object
	 */
	public Occurrence addOccurrence(Topic t, String value, String type) {
		return addOccurrence(t, value, Utils.dtIRI, type);
	}

	/**
	 * add an Occurrence of the given datatype and the given type to a Topic. If
	 * the given TopicNameType does not exist, it is created.
	 * 
	 * @param t
	 *            the Topic to which the Occurrence has to be added
	 * @param value
	 *            the value of the Occurrence
	 * @param dataType
	 *            the dataType of the Occurrence (for default dataTypes, see
	 *            Utils-class)
	 * @param type
	 *            the type of the Occurrence
	 * @return the new Occurrence object
	 */
	public Occurrence addOccurrence(Topic t, String value, String dataType,
			String type) {
		Occurrence o = new Occurrence();
		o.setValue(value);
		o.setDataType(dataType);
		if (!occurrenceTypes.containsKey("urn:subject:o" + type))
			defineOccurrenceType(type);
		o.setType(occurrenceTypes.get("urn:subject:o" + type));
		t.addOccurrence(o);
		return o;
	}

	/**
	 * removes an Occurrence from a Topic
	 * 
	 * @param t
	 *            the Occurrence from which the TopicName has to be removed
	 * @param tn
	 *            the Occurrence to be removed
	 */
	public void removeOccurrence(Topic t, Occurrence o) {
		t.removeOccurrence(o);
	}

	/**
	 * define a new TopicNameType with the given name. The Topic representing
	 * the type is registered in the respective index and added to the TopicMap
	 * (including a respective TopicName, which type is set to
	 * 'urn:subject:tnTopicNameType', marking that this is the name of a
	 * TopicNameType).
	 * 
	 * @param name
	 *            the name of the new TopicNameType
	 * @return the Topic representing the TopicNameType
	 */
	public Topic defineTopicNameType(String name) {
		Topic t = new Topic();
		t.addSubjectIdentifier("urn:subject:tn" + name);
		TopicName tn = new TopicName();
		tn.setValue(name);
		tn.setType(topicNameTypeNameTopic);
		t.addTopicName(tn);
		tm.addTopic(t);
		registerType(topicNameTypes, t);
		return t;
	}

	/**
	 * define a new OccurrenceType with the given name. The Topic representing
	 * the type is registered in the respective index and added to the TopicMap
	 * (including a respective TopicName, which type is set to
	 * 'urn:subject:tnOccurrenceType', marking that this is the name of an
	 * OccurrenceType).
	 * 
	 * @param name
	 *            the name of the new OccurrenceType
	 * @return the Topic representing the OccurrenceType
	 */
	public Topic defineOccurrenceType(String name) {
		if (occurrenceTypes.containsKey("urn:subject:o" + name))
			return occurrenceTypes.get("urn:subject:o" + name);
		Topic t = new Topic();
		t.addSubjectIdentifier("urn:subject:o" + name);
		TopicName tn = new TopicName();
		tn.setValue(name);
		tn.setType(occurrenceTypeNameTopic);
		t.addTopicName(tn);
		tm.addTopic(t);
		registerType(occurrenceTypes, t);
		return t;
	}
	
	/**
	 * get a defined OccurrenceType with the given name.
	 * 
	 * @param name the name of the new OccurrenceType
	 * @return the Topic representing the OccurrenceType, null if none exists
	 */
	public Topic getDefinedOccurrenceType(String name){
		if (occurrenceTypes.containsKey("urn:subject:o" + name))
			return occurrenceTypes.get("urn:subject:o" + name);
		return null;
	}

	/**
	 * define a new AssociationType with the given name and the given roles. The
	 * Topic representing the associationType and the associationRoleTypes are
	 * registered in the respective index and added to the TopicMap (including a
	 * respective TopicName, which type is set to
	 * 'urn:subject:tnAssociationType' or 'urn:subject:tnAssociationRoleType',
	 * marking that this is the name of an AssociationType or an
	 * AssociationRoleTypes, respectively). If an AssociationRoleType already
	 * exists in the index (because it is already use by another association, it
	 * is not created a second time.
	 * 
	 * @param name
	 *            the name of the new AssociationType
	 * @param roles
	 *            set of the names of the according AssociationRoleTypes
	 * @return the Topic representing the AssociationType
	 */
	public Topic defineAssociationType(String name, Set<String> roles) {
		if (roles != null) {
			Iterator<String> i = roles.iterator();
			while (i.hasNext())
				defineAssociationRoleType(i.next());
		}
		if (associationTypes.containsKey("urn:subject:a" + name))
			return associationTypes.get("urn:subject:a" + name);
		Topic a = new Topic();
		a.addSubjectIdentifier("urn:subject:a" + name);
		TopicName tn = new TopicName();
		tn.setValue(name);
		tn.setType(associationTypeNameTopic);
		a.addTopicName(tn);
		tm.addTopic(a);
		registerType(associationTypes, a);
		return a;
	}
	
	/**
	 * get a defined AssociationType with the given name
	 * 
	 * @param name  the name of the new AssociationType
	 * @return the Topic representing the AssociationType, null if none exists
	 */
	public Topic getDefinedAssociationType(String name){
		if (associationTypes.containsKey("urn:subject:a" + name))
			return associationTypes.get("urn:subject:a" + name);
		return null;
	}

	/**
	 * define a new AssociationRoleType with the given name. The Topic
	 * representing the type is registered in the respective index and added to
	 * the TopicMap (including a respective TopicName, which type is set to
	 * 'urn:subject:tnAssociationRoleType', marking that this is the name of an
	 * AssociationRoleType).
	 * 
	 * @param name
	 *            the name of the new AssociationRoleType
	 * @return the Topic representing the AssociationRoleType
	 */
	public Topic defineAssociationRoleType(String name) {
		if (associationRoleTypes.containsKey("urn:subject:ar" + name))
			return associationRoleTypes.get("urn:subject:ar" + name);
		Topic ar = new Topic();
		ar.addSubjectIdentifier("urn:subject:ar" + name);
		TopicName arTn = new TopicName();
		arTn.setValue(name);
		arTn.setType(associationRoleTypeNameTopic);
		ar.addTopicName(arTn);
		tm.addTopic(ar);
		registerType(associationRoleTypes, ar);
		return ar;
	}
	
	/**
	 * get a defined AssociationRoleType with the given name.
	 * 
	 * @param name the name of the new AssociationRoleType
	 * @return the Topic representing the AssociationRoleType, null if none exists
	 */
	public Topic getDefinedAssociationRoleType(String name){
		if (associationRoleTypes.containsKey("urn:subject:ar" + name))
			return associationRoleTypes.get("urn:subject:ar" + name);
		return null;
	}
	
	/**
	 * associate a set of Topics with an Association of the given Type, where
	 * the Topics each take the role of the given AssociationRoleType. If the
	 * given AssociationType or an AssociationRoleType does not exist, it is
	 * created.
	 * 
	 * @param associationType
	 * @param rolesPlayed
	 * @return
	 */
	public Association associate(String associationType,
			Set<RoleTopic> rolesPlayed) {
		Association a = new Association();
		if (!associationTypes.containsKey("urn:subject:a" + associationType))
			defineAssociationType(associationType, null);
		a.setType(associationTypes.get("urn:subject:a" + associationType));
		Iterator<RoleTopic> i = rolesPlayed.iterator();
		while (i.hasNext()) {
			RoleTopic r = i.next();
			String arType = r.role;
			if (!associationRoleTypes.containsKey("urn:subject:ar" + arType))
				defineAssociationRoleType(arType);
			AssociationRole ar = new AssociationRole();
			ar.setType(associationRoleTypes.get("urn:subject:ar" + arType));
			ar.setPlayer(r.topic);
			a.addAssociationRole(ar);
		}

		tm.addAssociation(a);
		return a;
	}

	/**
	 * remove an Association from the TopicMap and ensure consistency by
	 * removing the respective AssociationRoles from the affected Topics.
	 * 
	 * @param a
	 *            the Association to be removed
	 */
	public void disassociate(Association a) {
		Set<AssociationRole> ars = a.getRoles();
		Iterator<AssociationRole> i = ars.iterator();
		while (i.hasNext()) {
			AssociationRole ar = i.next();
			ar.getPlayer().removeRolePlayed(ar);
		}
		tm.removeAssociation(a);
	}

	/**
	 * define a new Scope and register it to the respective index
	 * 
	 * @return the new Scope object
	 */
	public Scope defineScope(Set<Topic> context) {
		Scope s = new Scope();
		s.setContext(context);
		if (!scopes.values().contains(s)){
			scopes.put(s.getId(), s);
		}
		return s;
	}

	/**
	 * get a Scope for a given Context; if a scope with such a context already exists,
	 * it will be returned, otherwise a new scope will be created and registered to
	 * the respective index
	 * 
	 * @param context context of the scope
	 * @return scope
	 */
	public Scope getScope(Set<Topic> context){
		Scope s = this.getScopeByContext(context);
		if(s != null)
			return s;
		s = new Scope();
		s.setContext(context);
		scopes.put(s.getId(), s);
		return s;
	}
	
	/**
	 * get a Scope for a given Context; if a scope with such a context already exists,
	 * it will be returned, otherwise a new scope will be created and registered to
	 * the respective index
	 * 
	 * @param context context of the scope
	 * @return scope
	 */
	public Scope getScope(Topic context){
		Scope s = this.getScopeByContext(context);
		if(s != null)
			return s;
		s = new Scope();
		s.addToContext(context);
		scopes.put(s.getId(), s);
		return s;
	}
	
	/**
	 * get all scopes which contain the given Topic in their context
	 * 
	 * @param t
	 * @return empty set if none exists
	 */
	public Set<Scope> getScopesWhereContextContains(Topic t){
		Set<Scope> returnSet = new HashSet<Scope>();
		Scope curS;
		Iterator<Scope> i = scopes.values().iterator();
		while(i.hasNext()){
			curS = i.next();
			if(curS.getContext().contains(t))
				returnSet.add(curS);
		}
		return returnSet;
	}
	
	/**
	 * assign a name to a Scope
	 * 
	 * @param s
	 *            the Scope to be named
	 * @param name
	 *            the name to be assigned to the Scope
	 */
	public void nameScope(Scope s, String name) {
		Topic sn = this.addTopic(name);
		this.addTopicName(sn, name, scopeNameTopicNameTypeTopic);
//		Set<Topic> context = s.getContext();
//		Map<AssociationRole, Topic> assocParticipants = new HashMap<AssociationRole, Topic>();
		// assocParticipants.put(scopeDefiningTopicAssociationRoleTypeTopic,
		// sn);
		// todo
		s.setName(sn);
	}

	/**
	 * set a Statement's Scope
	 * 
	 * @param statement
	 *            the Statement to be added to the scope
	 * @param scope
	 *            the Scope to which the Statement has to be added
	 */
	public void setScope(Statement statement, Scope scope) {
		statement.setScope(scope);
	}
	
	public Map<String, Scope> getScopes(){
		return this.scopes;
	}
	
	/**
	 * get an existing scope by its context
	 * 
	 * @param context context of scope which is represented by a Topic
	 * @return scope with specified context if one exists, otherwise null
	 */
	public Scope getScopeByContext(Topic context){
		Scope curS;
		Iterator<String> i = this.scopes.keySet().iterator();
		while(i.hasNext()){
			curS = scopes.get(i.next());
			if((curS.getContext().size()==1) && curS.getContext().contains(context)){
				return curS;
			}
		}
		return null;
	}
	
	/**
	 * get an existing scope by its context
	 * 
	 * @param context context which is represented by a set of Topics
	 * @return scope with specified context if one exists, otherwise null
	 */
	public Scope getScopeByContext(Set<Topic> context){
		Scope curS;
		Iterator<String> i = this.scopes.keySet().iterator();
		while(i.hasNext()){
			curS = scopes.get(i.next());
			if(context.containsAll(curS.getContext())&& curS.getContext().containsAll(context)){
				return curS;
			}
		}
		return null;
	}

	/**
	 * define a Type-Instance-relationship in the terms of the standard (and
	 * implicitly create a TopicType). If the Topic given as 'type' is not yet
	 * registered as a topicType, it is add to the respective index.
	 * 
	 * @param instance
	 *            the Topic which is the instance
	 * @param type
	 *            the Topic to be used as the topicType
	 */
	public void setTypeInstance(Topic instance, Topic type) {
		registerType(topicTypes, type);
		Set<RoleTopic> topics = new HashSet<RoleTopic>();
		topics.add(new RoleTopic("Type", type));
		topics.add(new RoleTopic("Instance", instance));
		associate("TypeInstance", topics);
	}
	
	/**
	 * define a Type-Instance-relationship in the terms of the standard (and
	 * implicitly create a TopicType). If the Topic given as 'type' is not yet
	 * registered as a topicType, it is add to the respective index.
	 * 
	 * @param instance
	 *            the Topic which is the instance
	 * @param type
	 *            the Topic to be used as the topicType
	 * @param scope
	 * 			  the Scope in which the relationship is valid
	 */
	public void setTypeInstance(Topic instance, Topic type, Scope scope) {
		registerType(topicTypes, type);
		Set<RoleTopic> topics = new HashSet<RoleTopic>();
		topics.add(new RoleTopic("Type", type));
		topics.add(new RoleTopic("Instance", instance));
		associate("TypeInstance", topics).setScope(scope);
	}

	/**
	 * define a Supertype-Subtype-relationship in the terms of the standard
	 * 
	 * @param superType
	 *            the Topic which is the supertype
	 * @param subType
	 *            the Topic which is the subtype
	 */
	public void setSuperSubType(Topic superType, Topic subType) {
		Set<RoleTopic> topics = new HashSet<RoleTopic>();
		topics.add(new RoleTopic("Supertype", superType));
		topics.add(new RoleTopic("Subtype", subType));
		associate("SupertypeSubtype", topics);
	}

	/**
	 * reify a reifiable construct with the given Topic
	 * 
	 * @param reified
	 *            the construct to be reified
	 * @param reifier
	 *            the Topic which is used as the reifier
	 */
	public void reify(Reifiable reified, Topic reifier) {
		reified.setReifier(reifier);
		reifier.setReified(reified);
		reifier.addSubjectIdentifier("urn:subject:reifier");
	}

	/**
	 * remove a reification
	 * 
	 * @param reified
	 *            the construct, from which the reification should be removed
	 * @param reifier
	 *            the Topic, from which the reification should be removed
	 */
	public void unReify(Reifiable reified, Topic reifier) {
		reified.setReifier(null);
		reifier.setReified(null);
		reifier.removeSubjectIdentifier("urn:subject:reifier");
	}

	/**
	 * check wether a Topic is contained in a Topic Map
	 * 
	 * @param t
	 *            the Topic to be search
	 * @return ture if the Topic was found, false otherwise
	 */
	public boolean containsTopic(Topic t) {
		return tm.getTopics().contains(t);
	}
	
	/**
	 * returns the topic map managed by the resprective instance of this class
	 * @return the underlying topic map
	 */
	public TopicMap getTopicMap() {
		return this.tm;
	}
	
	/**
	 * get an association of the given type within a scope; method is only convinient when the association just 
	 * occurs once within the scope
	 * 
	 * @param assoc specifies the type of the association
	 * @param scope specifies the scope in which the association with the given type is searched
	 * @return null if none exists
	 */
	public Association getAssociationBy(String assoc, Scope scope){
		Topic assocType = getDefinedAssociationType(assoc);
		Association a = null;
		Iterator<Association> it = getAssocsFromScope(scope).iterator();
		while(it.hasNext()){
			a = it.next();
			if(a.getType().equals(assocType)){
				return a;
			}
		}
		return null;
	}
	
	/**
	 * get all associations from a given scope
	 * 
	 * @param scope Scope which will be evaluated
	 * @return a Set of Associations which are valid within the given scope, an empty set if none were found
	 */
	public Set<Association> getAssocsFromScope(Scope scope){
		Set<Association> returnSet = new HashSet<Association>();
		if(scope == null)
			return returnSet;
		Statement stat;
		Iterator<Statement> i = scope.getContents().iterator();
		while(i.hasNext()){
			stat = i.next();
			if(stat instanceof Association){
				Association a = (Association)stat;
				returnSet.add(a);
			}
		}
		return returnSet;
	}
	
	/**
	 * get all Topics from a specified association type within a scope which are related to the specified topic
	 * assumed that the association of type only exists once within the scope
	 * 
	 * @param assocType
	 * @param scope
	 * @return
	 */
	public Set<Topic> getRelatedTopics(String assocType, Topic t, Scope scope){
		Set<Topic> returnSet = new HashSet<Topic>();
		Association a = getAssociationBy(assocType, scope);
		if(a != null){
			Topic curT;
			Iterator<Topic> i = a.getAssociatedTopics().values().iterator();
			while(i.hasNext()){
				curT = i.next();
				if(!curT.equals(t))
					returnSet.add(curT);
			}
		}
		return returnSet;
	}
	
	/**
	 * get Associations of specified type from a defined Scope
	 * 
	 * @param scope scope in which associations with the given type are searched
	 * @param assocType association type 
	 * @return s set of association, an empty set if none were found
	 */
	public Set<Association> getAssociationsOfTypeFromScope(Topic assocType, Scope scope){
		Set<Association> returnSet = new HashSet<Association>();
		Statement stat;
		Iterator<Statement> i = scope.getContents().iterator();
		while(i.hasNext()){
			stat = i.next();
			if(stat instanceof Association){
				Association a = (Association)stat;
				if(a.getType().equals(assocType))
					returnSet.add(a);
				
			}
		}
		return returnSet;
	}
	

	/**
	 * Wrapper-class for management of Association-Role-Topic-Combinations in
	 * Sets
	 * 
	 * @author oppl
	 * 
	 */
	public static class RoleTopic {
		public String role;

		public Topic topic;

		public RoleTopic(String role, Topic topic) {
			this.role = role;
			this.topic = topic;
		}
	}

}
