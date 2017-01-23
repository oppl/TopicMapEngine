package ce.tm4scholion.tm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * TopicMap Engine - TopicName TopicNames are constructs which determine the natural language designator of a Topic (and are actually equal to the name(s) of thr represented subject in most of the cases). TopicNames always have an TopicNameType (which is represented using a Topic) - every TopicName of the same nature references the same TopicNameType. As it is not always necessary to define a type for a TopicName, the standard defines a defaultTopicNameType, which has to be used in these cases.
 * @author  oppl
 */
public class TopicName extends Statement {

	protected String value;

	private Topic parent;

	private Topic type;

	private Set<Variant> variants;

	/**
	 * default construtor, generates TopicName object with UUID as
	 * itemIdentifier
	 *
	 */
	public TopicName() {
		this(Utils.getUniqueItemIdentifier());
	}

	/**
	 * basic constructor, generates TopicName object with the given String as
	 * itemIdentifier
	 *
	 * @param itemIdentifier
	 *            has to be unique for the whole TopicMap
	 */
	public TopicName(String itemIdentifier) {
		super(itemIdentifier);
		// TODO Auto-generated constructor stub
	}

	/**
	 * get the value of the TopicName (contains the name of the Topic the TopicName is attached to)
	 * @return  the value of the TopicName
	 * @uml.property  name="value"
	 */
	public String getValue() {
		return value;
	}

	/**
	 * set the value of the TopicName (contains the name of the Topic the TopicName is attached to)
	 * @return  the value of the TopicName
	 * @uml.property  name="value"
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * get the parent element of the TopicName
	 * @return  the Topic the TopicName is bound to
	 * @uml.property  name="parent"
	 */
	protected void setParent(Topic parent) {
		this.parent = parent;
	}

	/**
	 * set the parent element of the TopicName
	 * @param parent  the Topic the TopicName is bound to
	 * @uml.property  name="parent"
	 */
	public Topic getParent() {
		return parent;
	}

	/**
	 * get the TopicNameType of the TopicName
	 * @return  the Topic representing the TopicNameType of the TopicName
	 * @uml.property  name="type"
	 */
	public Topic getType() {
		return type;
	}

	/**
	 * set the TopicNameType of the TopicName
	 * @param type  the Topic representing the TopicNameType of the TopicName
	 * @uml.property  name="type"
	 */
	public void setType(Topic type) {
		this.type = type;
	}

	/**
	 * add a Variant to the TopicName
	 *
	 * @param variant
	 *            the Variant object to be added
	 */
	public void addVariant(Variant variant) {
		if (variants == null)
			variants = new HashSet<Variant>();
		variants.add(variant);
		variant.setParent(this);
	}

	/**
	 * generate a new Variant for the TopicName with the given value and datatype in the given Scope.
	 *
	 * @param value the value of the variant
	 * @param dataType the datatype of the variant's value
	 * @param s the Scope the Variant is valid in
	 * @return the new Variant object
	 */
	public Variant newVariant(String value, String dataType, Scope s) {
		Variant v = new Variant(Utils.getUniqueItemIdentifier());
		v.setValue(value);
		v.setDataType(dataType);
		v.setScope(s);
		this.addVariant(v);
		return v;
	}

	/**
	 * remove a Variant from the TopicName
	 *
	 * @param v
	 *            the Variant to be removed
	 */
	public void removeVariant(Variant v) {
		variants.remove(v);
	}

	/**
	 * get the set of Variants of this TopicName
	 * @return  the set of Variants of this TopicName
	 * @uml.property  name="variants"
	 */
	public Set<Variant> getVariants() {
		return variants;
	}
	/**
	 * set the Variants of this TopicName
	 * @param variants
	 * @uml.property name="variants"
	 */
	public void setVariants(Set<Variant> variants){
		this.variants=variants;
	}

	@Override
	public Set<TopicMapConstruct> checkTMDMCompliance() {
		Set<TopicMapConstruct> errors = new HashSet<TopicMapConstruct>();
		if (parent == null || type == null || value == null)
			errors.add(this);
		Iterator<Variant> i = variants.iterator();
		while (i.hasNext())
			errors.addAll(i.next().checkTMDMCompliance());
		if (errors.size() == 0)
			return null;
		return errors;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof TopicName))
			return false;
		TopicName tn = (TopicName) arg0;
		if (this.value.equals(tn.value) && this.scope.equals(tn.scope)
				&& this.type.equals(tn.type) && this.parent.equals(tn.parent))
			return true;
		return false;
	}

}
