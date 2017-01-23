package ce.tm4scholion.tm.persistency.xtm;

/**
 * Topic Map Engine - XTM Persistency - XtmType
 * 
 * XtmType is used as a wrapper class for typeUris. TypeUris are ItemIdentities of Topics which are types.  
 * This wrapper class simplifies the castor xml mapping (expecially the order in which castor maps variables to elements is guaranteed by this stub class)
 */
public class XtmType {

	private String typeUri;
	
	/**
	 * default constructor
	 *
	 */
	public XtmType(){}
	
	/**
	 * Constructor
	 * 
	 * @param typeUri
	 */
	public XtmType(String typeUri){
		this.typeUri = typeUri;
	}

	/**
	 * getTypeUri of XtmType
	 * 
	 * @return a String which represents the Uri of an ItemIdentity of a Topic
	 */
	public String getTypeUri() {
		return typeUri;
	}

	/**
	 * setTypeUri of XtmType
	 * 
	 * @param typeUri
	 */
	public void setTypeUri(String typeUri) {
		this.typeUri = typeUri;
	}
}
