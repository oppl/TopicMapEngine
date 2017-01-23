package ce.tm4scholion.tm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * TopicMap Engine - Topic Topics are the central element of each TopicMap and represent subjects of the real world. Topics have subjectIdentifiers, which are locators (i.e. URIs) that link to resources that describe the nature of the topic. SubjectLocators accordingly link to a resource which is the subject of the topic itself. A topic might have occurrences, which are representations of a relationships between a subject and an information resource. Topics might also have a TopicType, which describes the type of the subject represented by teh topic. The TopicType, however, is not stored directly within the Topic but is represented using a predefined form of Assocaiton (TypeInstance-Association). Topics are designated using TopicNames, i.e. a single Topic can have several names. To be linked to other Topics, a Topic may take roles in Associations. Topics can also be used to reify other constructs (only reifiable ones), that is to further specify and detail constructs by the means of attaching a Topic.
 * @author  oppl
 */
public class Topic extends TopicMapConstruct {

	protected Set<String> subjectLocators;

	protected Set<String> subjectIdentifiers;

	private Set<TopicName> topicNames;

	private Set<Occurrence> occurrences;

	private Set<AssociationRole> rolesPlayed;

	private Reifiable reified;

	private TopicMap parent;

	/**
	 * default construtor, generates Topic object with UUID as itemIdentifier
	 *
	 */
	public Topic() {
		this(Utils.getUniqueItemIdentifier());
	}

	/**
	 * basic constructor, generates Topic object with the given String as
	 * itemIdentifier
	 *
	 * @param itemIdentifier
	 *            has to be unique for the whole TopicMap
	 */
	public Topic(String itemIdentifier) {
		super(itemIdentifier);
	}

	/**
	 * add a TopicName to the Topic
	 *
	 * @param topicName
	 *            the TopicName object to be added
	 */
	public void addTopicName(TopicName topicName) {
		if (topicNames == null)
			topicNames = new HashSet<TopicName>();
		topicNames.add(topicName);
		topicName.setParent(this);
	}

	/**
	 * generate a new TopicName for the Topic with the given designator.
	 *
	 * @param value
	 *            the designator to be used for the new TopicName
	 * @return the new TopicName object
	 */
	public TopicName newTopicName(String value) {
		TopicName tn = new TopicName(Utils.getUniqueItemIdentifier());
		tn.setValue(value);
		this.addTopicName(tn);
		return tn;
	}

	/**
	 * get the set of TopicNames of this Topic
	 * @return  the set of TopicNames of this Topic
	 * @uml.property  name="topicNames"
	 */
	public Set<TopicName> getTopicNames() {
		return topicNames;
	}
	
	/**
	 * set the set of TopicNames of this Topic
	 * @param topicNames
	 * @uml.property name="topicNames"
	 */
	public void setTopicNames(Set<TopicName> topicNames){
		this.topicNames=topicNames;
	}

	/**
	 * remove a TopicName from the Topic
	 *
	 * @param tn
	 *            the TopicName to be removed
	 */
	public void removeTopicName(TopicName tn) {
		topicNames.remove(tn);
	}

	/**
	 * adds an Occurrence to the Topic
	 *
	 * @param occurrence
	 *            the Occurrence object to be added
	 */
	public void addOccurrence(Occurrence occurrence) {
		if (occurrences == null)
			occurrences = new HashSet<Occurrence>();
		occurrences.add(occurrence);
		occurrence.setParent(this);
	}

	/**
	 * generate a new Occurrence for the Topic with the given value and
	 * datatype.
	 *
	 * @param value
	 *            the value of the new Occurrence
	 * @param dataType
	 *            the datatype of the new Occurrence
	 * @return the new Occurrence object
	 */
	public Occurrence newOccurrence(String value, String dataType) {
		Occurrence o = new Occurrence(Utils.getUniqueItemIdentifier());
		o.setValue(value);
		o.setDataType(dataType);
		this.addOccurrence(o);
		return o;
	}

	/**
	 * get the set of Occurrences of the Topic
	 * @return  the set of Occurrences of the Topic
	 * @uml.property  name="occurrences"
	 */
	public Set<Occurrence> getOccurrences() {
		return occurrences;
	}
	
	/**
	 * set the set of Occurrences of the Topic
	 * @param occurrences
	 * @uml.property name="occurrences"
	 */
	public void setOccurrences(Set<Occurrence> occurrences){
		this.occurrences=occurrences;
	}

	/**
	 * remove an Occurrence from the Topic
	 *
	 * @param o
	 *            the Occurrence to be removed
	 */
	public void removeOccurrence(Occurrence o) {
		occurrences.remove(o);
	}

	/**
	 * set the parent element of the Topic
	 * @param parent  the TopicMap the Topic is contained in
	 * @uml.property  name="parent"
	 */
	protected void setParent(TopicMap parent) {
		this.parent = parent;
	}

	/**
	 * get the parent element of the Topic
	 * @return  the TopicMap the Topic is contained in
	 * @uml.property  name="parent"
	 */
	public TopicMap getParent() {
		return parent;
	}

	/**
	 * set the reified construct (invoked by Reifiable to establish backlink)
	 * @param reified  the construct to be reified
	 * @uml.property  name="reified"
	 */
	public void setReified(Reifiable reified) {
		this.reified = reified;
	}

	/**
	 * get the construct this Topic reifies
	 * @return  the construct which is reified by this Topic, null, if the Topic  is no reifier
	 * @uml.property  name="reified"
	 */
	public Reifiable getReified() {
		return reified;
	}

	/**
	 * add an AssocationRole this Topic plays (invoked by AssociationRole to
	 * establish backlink)
	 *
	 * @param associationRole
	 *            the role to be played by the topic
	 */
	protected void addRolePlayed(AssociationRole associationRole) {
		if (rolesPlayed == null)
			rolesPlayed = new HashSet<AssociationRole>();
		rolesPlayed.add(associationRole);
	}

	/**
	 * remove a played role from this Topic (invoked by AssociationRole to
	 * remove backlink)
	 *
	 * @param ar
	 *            the role to be removed
	 */
	protected void removeRolePlayed(AssociationRole ar) {
		rolesPlayed.remove(ar);
	}

	/**
	 * get the set of roles played by this topic
	 * @return  the set of roles played by this Topic
	 * @uml.property  name="rolesPlayed"
	 */
	public Set<AssociationRole> getRolesPlayed() {
		return rolesPlayed;
	}
	
	/**
	 * set the set of roles played by this topic
	 * @param rolesPlayed
	 * @uml.property name="rolesPlayed"
	 */
	public void setRolesPlayed(Set<AssociationRole> rolesPlayed){
		this.rolesPlayed=rolesPlayed;
	}

	/**
	 * get the Associations associated by this Topic including the information
	 * which via which AssocationRole the Topic is involved in an Association
	 *
	 * @return a Map of <AssociationRole, Association>-Tupels, representing
	 *         which Associations are associated to the Topic via which
	 *         AssocationRole
	 */
	public Map<AssociationRole, Association> getAssociatedAssociations() {
		Map<AssociationRole, Association> associations = new HashMap<AssociationRole, Association>();
		if(rolesPlayed!=null){
			Iterator<AssociationRole> i = rolesPlayed.iterator();
			while (i.hasNext()) {
				AssociationRole ar = i.next();
				associations.put(ar, ar.getParent());
			}
		}
		return associations;
	}
	
	/**
	 * get the Associations in which this topic is a player for a specific type 
	 * of association
	 * 
	 * @param type type of association
	 * @return a Set of Associations of type type where this topic plays an association role, an empty set if there are not associations found
	 */
	public Set<Association> getAssociatedAssociationsOfType(Topic type){
		Set<Association> associations = new HashSet<Association>();
		if(rolesPlayed!=null){
			Iterator<AssociationRole> i = rolesPlayed.iterator();
			while (i.hasNext()) {
				AssociationRole ar = i.next();
				if(ar.getParent().getType().equals(type))
					associations.add(ar.getParent());
			}
		}
		return associations;
	}

	/**
	 * add a new subjectLocator to the Topic, only performed if the
	 * subjectLocator is a locator in terms of the TMDM
	 *
	 * @param subjectLocator
	 *            the subjectLocator to be added
	 * @return true, if the subjectLocator was successfully added, false
	 *         otherwise
	 */
	public boolean addSubjectLocator(String subjectLocator) {
		if (!Utils.isLocator(subjectLocator))
			return false;
		if (subjectLocators == null)
			subjectLocators = new HashSet<String>();
		subjectLocators.add(subjectLocator);
		return true;
	}

	/**
	 * remove a subjectLocator from the Topic
	 *
	 * @param subjectLocator
	 *            the subjectLocator to be removed
	 */
	public void removeSubjectLocator(String subjectLocator) {
		subjectLocators.remove(subjectLocator);
	}

	/**
	 * get the set of subjectLocators of the Topic
	 * @return  the set of subjectLocators of the Topic
	 * @uml.property  name="subjectLocators"
	 */
	public Set<String> getSubjectLocators() {
		return subjectLocators;
	}
	
	/**
	 * set the set of subjectLocators of the Topic
	 * @param subjectLocators
	 * @uml.property name="subjectLocators"
	 */
	public void setSubjectLocators(Set<String> subjectLocators){
		this.subjectLocators=subjectLocators;
	}

	/**
	 * add a new subjectIdentifier to the Topic, only performed if the
	 * subjectIdentifier is a locator in terms of the TMDM
	 *
	 * @param subjectIdentifier
	 *            the subjectLocator to be added
	 * @return true, if the subjectIdentifier was successfully added, false
	 *         otherwise
	 */
	public boolean addSubjectIdentifier(String subjectIdentifier) {
		if (!Utils.isLocator(subjectIdentifier))
			return false;
		if (subjectIdentifiers == null)
			subjectIdentifiers = new HashSet<String>();
		subjectIdentifiers.add(subjectIdentifier);
		return true;
	}

	/**
	 * remove a subjectIdentifier from the Topic
	 *
	 * @param subjectIdentifier
	 *            the subjectLocator to be removed
	 */
	protected void removeSubjectIdentifier(String subjectIdentifier) {
		subjectLocators.remove(subjectIdentifier);
	}

	/**
	 * get the set of subjectIdentifier of the Topic
	 * @return  the set of subjectIdentifier of the Topic
	 * @uml.property  name="subjectIdentifiers"
	 */
	public Set<String> getSubjectIdentifiers() {
		return subjectIdentifiers;
	}
	
	/**
	 * set the set of subjectIdentifiers of the Topic
	 * @param subjectIdentifiers
	 * @uml.property name="subjectIdentifiers"
	 */
	public void setSubjectIdentifiers(Set<String> subjectIdentifiers){
		this.subjectIdentifiers=subjectIdentifiers;
	}

	@Override
	public Set<TopicMapConstruct> checkTMDMCompliance() {
		Set<TopicMapConstruct> errors = new HashSet<TopicMapConstruct>();
		if (parent == null
				|| (subjectIdentifiers == null || subjectIdentifiers.size() == 0)
				&& (subjectLocators == null || subjectLocators.size() == 0)
				&& (itemIdentifiers == null || itemIdentifiers.size() == 0))
			errors.add(this);
		boolean locatorError = false;
		Iterator<String> u = subjectLocators.iterator();
		while (u.hasNext())
			if (!Utils.isLocator(u.next()))
				locatorError = true;
		u = subjectIdentifiers.iterator();
		while (u.hasNext())
			if (!Utils.isLocator(u.next()))
				locatorError = true;
		if (locatorError)
			errors.add(this);
		Iterator<Occurrence> i = occurrences.iterator();
		while (i.hasNext())
			errors.addAll(i.next().checkTMDMCompliance());
		Iterator<TopicName> a = topicNames.iterator();
		while (a.hasNext())
			errors.addAll(a.next().checkTMDMCompliance());
		if (errors.size() == 0)
			return null;
		return errors;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Topic))
			return false;
		Topic t = (Topic) arg0;
/*		if ((Utils.setsContainAtLeastOneEqualElement(this.subjectIdentifiers,
				t.subjectIdentifiers))
				|| (Utils.setsContainAtLeastOneEqualElement(
						this.subjectLocators, t.subjectLocators))
				|| (Utils.setsContainAtLeastOneEqualElement(
						this.itemIdentifiers, t.itemIdentifiers))
				|| (Utils.setsContainAtLeastOneEqualElement(
						this.subjectIdentifiers, t.itemIdentifiers))
				|| (Utils.setsContainAtLeastOneEqualElement(
						this.itemIdentifiers, t.subjectIdentifiers))
				|| (this.reified == t.reified))
			return true;*/
		if (this.firstItemIdentifier.equals(t.firstItemIdentifier)) return true;
		return false;
	}

}
