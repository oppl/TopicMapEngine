package ce.tm4scholion.tm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * TopicMap Engine - TopicMap TopicMap is the root element of every Topic Map. It contains references to all topics and associations which are part of the Topic Map.
 * @author  oppl
 */
public class TopicMap extends Reifiable {

	private Set<Topic> topics;

	private Set<Association> associations;

	/**
	 * default construtor, generates TopicMap object with UUID as itemIdentifier
	 * 
	 */
	public TopicMap() {
		this(Utils.getUniqueItemIdentifier());
	}

	/**
	 * basic constructor, generates TopicMap object with the given String as
	 * itemIdentifier
	 * 
	 * @param itemIdentifier
	 *            has to be unique for the whole TopicMap
	 */
	public TopicMap(String itemIdentifier) {
		super(itemIdentifier);
		// TODO Auto-generated constructor stub
	}

	/**
	 * add a Topic to this TopicMap
	 * 
	 * @param topic
	 *            the Topic to be added
	 */
	protected void addTopic(Topic topic) {
		if (topics == null)
			topics = new HashSet<Topic>();
		topics.add(topic);
		topic.setParent(this);
	}

	/**
	 * generate a new Topic in the TopicMap with an UUID-itemIdentifier.
	 * 
	 * @return the new Topic object
	 */
	protected Topic newTopic() {
		Topic t = new Topic(Utils.getUniqueItemIdentifier());
		this.addTopic(t);
		return t;
	}

	/**
	 * remove a Topic from the TopicMap (without checking any dependencies, this
	 * is done in the resprective Manager-routine)
	 * 
	 * @param t
	 *            the Topic to be removed
	 */
	protected void removeTopic(Topic t) {
		topics.remove(t);
	}

	/**
	 * get the set of Topics of the TopicMap
	 * @return  the set of Topics of the TopicMap
	 * @uml.property  name="topics"
	 */
	public Set<Topic> getTopics() {
		return topics;
	}

	/**
	 * set the Topics of the TopicMap as a whole
	 * @param topics  the Topics to be set as the contents of the TopicMap
	 * @uml.property  name="topics"
	 */
	protected void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}

	/**
	 * add an Association to the TopicMap
	 * 
	 * @param association
	 *            the Association to be added
	 */
	protected void addAssociation(Association association) {
		if (associations == null)
			associations = new HashSet<Association>();
		associations.add(association);
		association.setParent(this);
	}

	/**
	 * generate a new Association in the TopicMap with an UUID-itemIdentifier.
	 * 
	 * @return the new Association object
	 */
	protected Association newAssociation() {
		Association a = new Association(Utils.getUniqueItemIdentifier());
		this.addAssociation(a);
		return a;
	}

	/**
	 * remove a Association from the TopicMap (without checking any
	 * dependencies, this is done in the resprective Manager-routine)
	 * 
	 * @param t
	 *            the Association to be removed
	 */
	protected void removeAssociation(Association a) {
		associations.remove(a);
	}

	/**
	 * get the set of Associations of the TopicMap
	 * @return  the set of Associations of the TopicMap
	 * @uml.property  name="associations"
	 */
	public Set<Association> getAssociations() {
		return associations;
	}

	/**
	 * set the Associations of the TopicMap as a whole
	 * @param associations  the Associations to be set as the contents of the TopicMap
	 * @uml.property  name="associations"
	 */
	protected void setAssociations(Set<Association> associations) {
		this.associations = associations;
	}

	@Override
	public Set<TopicMapConstruct> checkTMDMCompliance() {
		Set<TopicMapConstruct> errors = new HashSet<TopicMapConstruct>();
		Iterator<Topic> i = topics.iterator();
		while (i.hasNext())
			errors.addAll(i.next().checkTMDMCompliance());
		Iterator<Association> a = associations.iterator();
		while (a.hasNext())
			errors.addAll(a.next().checkTMDMCompliance());
		if (errors.size() == 0)
			return null;
		return errors;
	}

}
