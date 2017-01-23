package ce.tm4scholion.tm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * TopicMap Engine - Association Associations are the second main element of TopicMaps (beside Topics) and represent the relationships between Topics. Associations are named using an AssociationType (which is represented using a Topic) - every Association of the same nature references the same AssociationType. Topics are connected to the Association using AssociationRoles, which describe the role the Topic takes in this Association.
 * @author  oppl
 */
public class Association extends Statement {

	private TopicMap parent;

	private Topic type;

	private Set<AssociationRole> roles;

	/**
	 * default construtor, generates Association object with UUID as
	 * itemIdentifier
	 *
	 */
	public Association() {
		this(Utils.getUniqueItemIdentifier());
	}

	/**
	 * basic constructor, generates Associaton object with the given String as
	 * itemIdentifier
	 *
	 * @param itemIdentifier
	 *            has to be unique for the whole TopicMap
	 */
	public Association(String itemIdentifier) {
		super(itemIdentifier);
		// TODO Auto-generated constructor stub
	}

	/**
	 * set the parent element of the Association
	 * @param parent  the TopicMap the Association is contained in
	 * @uml.property  name="parent"
	 */
	protected void setParent(TopicMap parent) {
		this.parent = parent;
	}

	/**
	 * get the parent element of the Association
	 * @return  the TopicMap the Association is contained in
	 * @uml.property  name="parent"
	 */
	public TopicMap getParent() {
		return parent;
	}

	/**
	 * get the AssociationType of the Association
	 * @return  the Topic representing the AssocaitonType of the Association
	 * @uml.property  name="type"
	 */
	public Topic getType() {
		return type;
	}

	/**
	 * set the AssociationType of the Association
	 * @param type  the Topic representing the AssocaitonType of the Association
	 * @uml.property  name="type"
	 */
	protected void setType(Topic type) {
		this.type = type;
	}

	/**
	 * add an AssociationRole to this Assciation. AssociationRoles define the
	 * connection points between Topics and Associations
	 *
	 * @param associationRole
	 *            the AssociationRole to be added to this Association
	 */
	public void addAssociationRole(AssociationRole associationRole) {
		if (roles == null)
			roles = new HashSet<AssociationRole>();
		roles.add(associationRole);
		associationRole.setParent(this);
	}

	/**
	 * request a new AssociationRole for this Association. The AssoicationRole
	 * is automatically added to the Association
	 *
	 * @return the new AssociationRole
	 */
	public AssociationRole newAssociationRole() {
		AssociationRole ar = new AssociationRole(Utils
				.getUniqueItemIdentifier());
		this.addAssociationRole(ar);
		return ar;
	}

	/**
	 * remove an AssociationRole from an Association. If the role is already
	 * taken by a Topic, this Topic is informed of being removed
	 *
	 * @param ar
	 *            the AssociationRole to be removed from the Association
	 */
	public void removeAssociationRole(AssociationRole ar) {
		roles.remove(ar);
		if (ar.getPlayer() != null)
			ar.getPlayer().removeRolePlayed(ar);
	}

	/**
	 * get the Topics associated by this Association including the information
	 * which AssocationRoles they take
	 *
	 * @return a Map of <AssociationRole, Topic>-Tupels, representing which
	 *         Topics have taken which AssociationRole for this Association
	 */
	public Map<AssociationRole, Topic> getAssociatedTopics() {
		Map<AssociationRole, Topic> topics = new HashMap<AssociationRole, Topic>();
		Iterator<AssociationRole> i = roles.iterator();
		while (i.hasNext()) {
			AssociationRole ar = i.next();
			topics.put(ar, ar.getPlayer());
		}
		return topics;
	}

	/**
	 * get the AssociationRoles of this Association (regardless if they are taken or not)
	 * @return  the AssociationRoles of this Association
	 * @uml.property  name="roles"
	 */
	public Set<AssociationRole> getRoles() {
		return roles;
	}

	/**
	 * set the AssociationRoles of this Association
	 * @param roles
	 * @uml.property name="roles"
	 */
	public void setRoles(Set<AssociationRole> roles){
		this.roles = roles;
	}

	@Override
	public Set<TopicMapConstruct> checkTMDMCompliance() {
		Set<TopicMapConstruct> errors = new HashSet<TopicMapConstruct>();
		if (parent == null || type == null || roles == null
				|| roles.size() == 0)
			errors.add(this);
		Iterator<AssociationRole> i = roles.iterator();
		while (i.hasNext())
			errors.addAll(i.next().checkTMDMCompliance());
		if (errors.size() == 0)
			return null;
		return errors;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Association))
			return false;
		Association a = (Association) arg0;
		if (this.scope.equals(a.scope) && this.type.equals(a.type)
				&& this.roles.containsAll(a.getRoles()))
			return true;
		return false;
	}

}
