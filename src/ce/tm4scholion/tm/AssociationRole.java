package ce.tm4scholion.tm;

import java.util.HashSet;
import java.util.Set;

/**
 * TopicMap Engine - AssociationRole AssociationRoles provide the link between Topics and Assoications. They define in which way (role) a Topic particiates in an Association. A particular AssociationRole is always attached to exactly one Association and can be taken by exactly one Topic at the same time. AssocaitionRoles are specified using AssoicationRoleTypes (which are represented using Topics) - every AssociationRole of the same nature references the same AssociationRoleType.
 * @author  oppl
 */
public class AssociationRole extends Reifiable {

	private Association parent;

	private Topic type;

	private Topic player;

	/**
	 * default construtor, generates AssociationRole object with UUID as
	 * itemIdentifier
	 * 
	 */
	public AssociationRole() {
		this(Utils.getUniqueItemIdentifier());
	}

	/**
	 * basic constructor, generates Associaton object with the given String as
	 * itemIdentifier
	 * 
	 * @param itemIdentifier
	 *            has to be unique for the whole TopicMap
	 */
	public AssociationRole(String itemIdentifier) {
		super(itemIdentifier);
		// TODO Auto-generated constructor stub
	}

	/**
	 * set the parent element of the AssociationRole
	 * @param parent  the Associaton the AssociationRole is attached to
	 * @uml.property  name="parent"
	 */
	protected void setParent(Association parent) {
		this.parent = parent;
	}

	/**
	 * get the parent element of the AssociationRole
	 * @return  the Association the AssociationRole is attached to
	 * @uml.property  name="parent"
	 */	
	public Association getParent() {
		return parent;
	}

	/**
	 * get the AssociationRoleType of the AssociationRole
	 * @return  the Topic representing the AssocaitonRoleType of the AssociationRole
	 * @uml.property  name="type"
	 */	
	public Topic getType() {
		return type;
	}

	/**
	 * set the AssociationRoleType of the AssociationRole
	 * @param type  the Topic representing the AssocaitonRoleType of the AssociationRole
	 * @uml.property  name="type"
	 */	
	public void setType(Topic type) {
		this.type = type;
	}

	/**
	 * get the Topic which plays the role defined in the AssociationRole
	 * @return  the Topic which plays the role defined in the AssociationRole
	 * @uml.property  name="player"
	 */
	public Topic getPlayer() {
		return player;
	}

	/**
	 * set the Topic which plays the role defined in the AssociationRole
	 * @param player  the Topic which plays the role defined in the AssociationRole
	 * @uml.property  name="player"
	 */
	public void setPlayer(Topic player) {
		this.player = player;
		if(player!=null)
			player.addRolePlayed(this);
	}

	@Override
	public Set<TopicMapConstruct> checkTMDMCompliance() {
		Set<TopicMapConstruct> errors = new HashSet<TopicMapConstruct>();
		if (parent == null || player == null || type == null)
			errors.add(this);
		if (errors.size() == 0)
			return null;
		return errors;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof AssociationRole))
			return false;
		AssociationRole ar = (AssociationRole) arg0;
		if (((this.player == null && ar.player == null) || this.player.equals(ar.player)) && 
			((this.type == null && ar.type == null) ||	this.type.equals(ar.type)) &&
			((this.parent == null && ar.parent == null) || this.parent.equals(ar.parent)))
			return true;
		return false;
	}

}
