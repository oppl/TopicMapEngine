package ce.tm4scholion.tm;

import java.util.HashSet;
import java.util.Set;

/**
 * TopicMap Engine - Occurrence Occurrences are a Topic's links to the 'outer world' (outside the topic map). An occurrence is a representation of a relationship between a subject and an information resource. Occurrences always have an OccurrenceType (which is represented using a Topic) - every Occurrence of the same nature references the same OccurrenceType. Every Occurrence is attached to exactly one Topic.
 * @author  oppl
 */
public class Occurrence extends Statement {

	protected String value;

	protected String dataType;

	private Topic parent;

	private Topic type;

	/**
	 * default construtor, generates Occurrence object with UUID as
	 * itemIdentifier
	 * 
	 */
	public Occurrence() {
		this(Utils.getUniqueItemIdentifier());
	}

	/**
	 * basic constructor, generates Occurrence object with the given String as
	 * itemIdentifier
	 * 
	 * @param itemIdentifier
	 *            has to be unique for the whole TopicMap
	 */
	public Occurrence(String itemIdentifier) {
		super(itemIdentifier);
		// TODO Auto-generated constructor stub
	}

	/**
	 * set the parent element of the Occurrence
	 * @param parent  the Topic the Occurrence is bound to
	 * @uml.property  name="parent"
	 */
	public Topic getParent() {
		return parent;
	}

	/**
	 * get the parent element of the Occurrence
	 * @return  the Topic the Occurrence is bound to
	 * @uml.property  name="parent"
	 */
	protected void setParent(Topic parent) {
		this.parent = parent;
	}

	/**
	 * get the dataType of the Occurrence
	 * @return  the dataType of the Occurrence
	 * @uml.property  name="dataType"
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * set the dataType of the Occurrence
	 * @param dataType  the dataType of the Occurrence
	 * @uml.property  name="dataType"
	 */
	protected void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * get the value of the Occurrence (contains the information ressource or the link to it, respectively)
	 * @return  the value of the Occurrence
	 * @uml.property  name="value"
	 */
	public String getValue() {
		return value;
	}

	/**
	 * set the value of the Occurrence (contains the information ressource or the link to it, respectively)
	 * @param value  the value of the Occurrence
	 * @uml.property  name="value"
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * get the OccurrenceType of the Occurrence
	 * @return  the Topic representing the OccurrenceType of the Occurrence
	 * @uml.property  name="type"
	 */
	public Topic getType() {
		return type;
	}

	/**
	 * set the OccurrenceType of the Occurrence
	 * @param type  the Topic representing the OccurrenceType of the Occurrence
	 * @uml.property  name="type"
	 */
	protected void setType(Topic type) {
		this.type = type;
	}

	@Override
	public Set<TopicMapConstruct> checkTMDMCompliance() {
		Set<TopicMapConstruct> errors = new HashSet<TopicMapConstruct>();
		if (parent == null || type == null || value == null
				|| !(Utils.isIRIDataType(dataType) && Utils.isLocator(value))
				|| dataType == null)
			errors.add(this);
		if (errors.size() == 0)
			return null;
		return errors;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Occurrence))
			return false;
		Occurrence o = (Occurrence) arg0;
		if (this.value.equals(o.value) && this.dataType.equals(o.dataType)
				&& this.scope.equals(o.scope) && this.type.equals(o.type)
				&& this.parent.equals(o.parent))
			return true;
		return false;
	}

}
