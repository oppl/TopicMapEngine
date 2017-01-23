package ce.tm4scholion.tm;

import java.util.HashSet;
import java.util.Set;

/**
 * TopicMap Engine - Variant Variants are alternative forms of a certain TopicName. They can be used to define synonyms for a given topic name but also enable to switch modality of representation, as they do not necessarily have to be strings (but can e.g. link to encoded audio). A special use case of variants is that of a sort name, which enables sorting of topics alphabetically (according to unicode order). A Variant is always attached to exactly one TopicName and is the only construct which has to have a mandatory scope definition for standard compliance.
 * @author  oppl
 */
public class Variant extends Statement {

	protected String value;

	protected String dataType;

	private TopicName parent;

	/**
	 * default construtor, generates Variant object with UUID as
	 * itemIdentifier
	 * 
	 */
	public Variant() {
		this(Utils.getUniqueItemIdentifier());
	}

	/**
	 * basic constructor, generates Variant object with the given String as
	 * itemIdentifier
	 * 
	 * @param itemIdentifier
	 *            has to be unique for the whole TopicMap
	 */
	public Variant(String itemIdentifier) {
		super(itemIdentifier);
		// TODO Auto-generated constructor stub
	}

	/**
	 * get the parent element of the Variant
	 * @return  the TopicName the Variant is bound to
	 * @uml.property  name="parent"
	 */
	public TopicName getParent() {
		return parent;
	}

	/**
	 * set the parent element of the Variant
	 * @param parent  the TopicName the Variant is bound to
	 * @uml.property  name="parent"
	 */
	protected void setParent(TopicName parent) {
		this.parent = parent;
	}

	/**
	 * get the dataType of the Variant
	 * @return  the dataType of the Variant
	 * @uml.property  name="dataType"
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * set the dataType of the Variant
	 * @param dataType  the dataType of the Variant
	 * @uml.property  name="dataType"
	 */
	protected void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * get the value of the Variant (contains the information ressource representing the Variant or the link to it, respectively)
	 * @return  the value of the Variant
	 * @uml.property  name="value"
	 */
	public String getValue() {
		return value;
	}

	/**
	 * set the value of the Variant (contains the information ressource representing the Variant or the link to it, respectively)
	 * @param value  the value of the Variant
	 * @uml.property  name="value"
	 */
	protected void setValue(String value) {
		this.value = value;
	}

	@Override
	public Set<TopicMapConstruct> checkTMDMCompliance() {
		Set<TopicMapConstruct> errors = new HashSet<TopicMapConstruct>();
		if (parent == null
				|| value == null
				|| scope == null
				|| !(Utils.isSuperset(this.scope.getContext(), this.parent
						.getScope().getContext())) || dataType == null)
			errors.add(this);
		if (errors.size() == 0)
			return null;
		return errors;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Variant))
			return false;
		Variant v = (Variant) arg0;
		if (this.value.equals(v.value) && this.dataType.equals(v.dataType)
				&& this.scope.equals(v.scope) && this.parent.equals(v.parent))
			return true;
		return false;
	}

}
