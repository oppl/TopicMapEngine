package ce.tm4scholion.tm.persistency.xtm;

/**
 * Topic Map Engine - XTM Persistency - XtmSubjectIdentifier
 * 
 * XtmSubjectIdentifier is used as a wrapper class for subjectIdentifier Strings in subjectIdentifier Sets 
 * This wrapper class simplifies the castor xml mapping (expecially the order in which castor maps variables to elements is guaranteed by this stub class)  
 */
public class XtmSubjectIdentifier {

	private String subjectIdentifier;
	
	/**
	 * default constructor
	 *
	 */
	public XtmSubjectIdentifier(){}
	
	/**
	 * Constructor
	 * 
	 * @param subjectIdentifier
	 */
	public XtmSubjectIdentifier(String subjectIdentifier){
		this.subjectIdentifier = subjectIdentifier;
	}
	
	/**
	 * getSubjectIdentifier gets the value of subjectIdentifier
	 * 
	 * @return a String which represents the subjectIdentifier
	 */
	public String getSubjectIdentifier() {
		return subjectIdentifier;
	}

	/**
	 * setSubjectIdentifier sets the value ob subjectIdentifier
	 * 
	 * @param subjectIdentifier
	 */
	public void setSubjectIdentifier(String subjectIdentifier) {
		this.subjectIdentifier = subjectIdentifier;
	}
	
}
