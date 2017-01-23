package ce.tm4scholion.tm;

import java.util.HashSet;
import java.util.Set;

/**
 * TopicMap Engine - TopicMapConstruct The basic element of the TMDM, from which all standardized elements (constructs) are derived. Basically only defines the Set of itemIdentifiers every construct has to have. ItemIdentifiers have to be locators (i.e. URIs) and have to be unique for the TopicMap the Construct is part of.
 * @author  oppl
 */
public abstract class TopicMapConstruct {
	protected String firstItemIdentifier;
	protected Set<String> itemIdentifiers;

	/**
	 * basic constructor, is invoked by subtypes to generate a respective
	 * object with the given String as itemIdentifier. If the String is not an
	 * locator, an URI incorporating an UUID is used as itemIdentifier
	 *
	 * @param itemIdentifier
	 *            has to be unique for the whole TopicMap
	 */
	public TopicMapConstruct(String itemIdentifier) {
		firstItemIdentifier = itemIdentifier;
		itemIdentifiers = new HashSet<String>();
		if (!addItemIdentifier(itemIdentifier))
			addItemIdentifier(Utils.getUniqueItemIdentifier());
	}

	/**
	 * get the itemIdentifiers of the construct
	 * @return  the itemIdentifiers of the construct
	 * @uml.property  name="itemIdentifiers"
	 */
	public Set<String> getItemIdentifiers() {
		return itemIdentifiers;
	}
	
	/**
	 * set the itemIdentifiers of the construct
	 * @param itemIdentifiers
	 * @uml.property name="itemIdentifiers"
	 */
	public void setItemIdentifiers(Set<String> itemIdentifiers){
		this.itemIdentifiers=itemIdentifiers;
	}

	/**
	 * add an itemIdentifier to a construct. itemIdentifiers have to locators
	 * (that is, URIs) and be unique for the TopicMap the construct is part of
	 *
	 * @param itemIdentifier
	 *            the string to be added as an itemIdentifier
	 * @return true, if itemIdentifier was successfully added, false if adding
	 *         failed (because the parameter-string was no locator, i.e. an URI)
	 */
	public boolean addItemIdentifier(String itemIdentifier) {
		if (Utils.isLocator(itemIdentifier)) {
			itemIdentifiers.add(itemIdentifier);
			return true;
		}
		return false;
	}

	/**
	 * check the compliance of this construct (including all directly attached
	 * constructs) to the TMDM
	 *
	 * @return a Set of all checked constructs which do not adhere the TMDM,
	 *         null is everything is compliant
	 */
	public abstract Set<TopicMapConstruct> checkTMDMCompliance();

	
	/**
	 * get the first itemIdentifier of a TopicMapConstruct
	 * @return firstItemIdentifier
	 */
	public String getFirstItemIdentifier(){
		if (firstItemIdentifier == null) 
			firstItemIdentifier = itemIdentifiers.iterator().next();
		return firstItemIdentifier;
	}
	
	/**
	 * set the first itemIdentifier of a TopicMapConstruct
	 * @param firstItemIdentifier
	 */
	public void setFirstItemIdentifier(String firstItemIdentifier){
		this.firstItemIdentifier=firstItemIdentifier;
	}
}
