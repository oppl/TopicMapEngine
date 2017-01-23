package ce.tm4scholion.tm.persistency.xtm;

/**
 * Topic Map Engine - XTM Persistency - XtmItemIdentity
 * 
 * XtmItemIdentity is used as a wrapper class for itemIdentity Strings in ItemIdentifier Sets (each class which inherits from ce.tm4scholion.tm.TopicMapConstruct has itemidentifiers)
 * This wrapper class simplifies the castor xml mapping (expecially the order in which castor maps variables to elements is guaranteed by this stub class)  
 */
public class XtmItemIdentity {

	private String itemIdentity;
	
	/**
	 * default constuctor
	 *
	 */
	public XtmItemIdentity(){}
	
	/**
	 * Constructor
	 * 
	 * @param itemIdentity
	 */
	public XtmItemIdentity(String itemIdentity){
		this.itemIdentity = itemIdentity;
	}
	
	/**
	 * getItemIdentity gets the value of itemIdentity
	 * 
	 * @return a String which represents the itemIdentity
	 */
	public String getItemIdentity() {
		return itemIdentity;
	}

	/**
	 * setItemIdentity sets the value of itemIdentity
	 * 
	 * @param itemIdentity
	 */
	public void setItemIdentity(String itemIdentity) {
		this.itemIdentity = itemIdentity;
	}
	
}
