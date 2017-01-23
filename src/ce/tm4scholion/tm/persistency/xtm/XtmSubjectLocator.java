package ce.tm4scholion.tm.persistency.xtm;

/**
 * Topic Map Engine - XTM Persistency - XtmSubjectLocator
 * 
 * XtmSubjectLocator is used as a wrapper class for subjectLocator Strings in subjectLocator Sets 
 * This wrapper class simplifies the castor xml mapping (expecially the order in which castor maps variables to elements is guaranteed by this stub class)  
 */
public class XtmSubjectLocator {
	
	private String subjectLocator;
	
	/**
	 * default constructor
	 *
	 */
	public XtmSubjectLocator(){}
	
	/**
	 * Constructor
	 * 
	 * @param subjectLocator
	 */
	public XtmSubjectLocator(String subjectLocator){
		this.subjectLocator = subjectLocator;
	}

	/**
	 * getSubjectLocator gets the value of subjectLocator
	 * 
	 * @return a String which represents a subjectLocator
	 */
	public String getSubjectLocator() {
		return subjectLocator;
	}

	/**
	 * setSubjectLocator sets the value of subjectLocator
	 * 
	 * @param subjectLocator
	 */
	public void setSubjectLocator(String subjectLocator) {
		this.subjectLocator = subjectLocator;
	}
}
