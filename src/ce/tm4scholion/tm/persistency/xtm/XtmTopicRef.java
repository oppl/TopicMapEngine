package ce.tm4scholion.tm.persistency.xtm;

/**
 * Topic Map Engine - XTM Persistency - XtmTopicRef
 * 
 * XtmTopicRef is used as a wrapper class for topicRefs. topicRefs are ItemIdentities of Topics which are referenced.  
 * This wrapper class simplifies the castor xml mapping (expecially the order in which castor maps variables to elements is guaranteed by this stub class)
 */
public class XtmTopicRef {
	
	private String topicRef;
	
	/**
	 * default constructor
	 *
	 */
	public XtmTopicRef(){}
	
	/**
	 * Constructor
	 * 
	 * @param topicRef
	 */
	public XtmTopicRef(String topicRef){
		this.topicRef = topicRef;
	}

	/**
	 * getTopicRef of XtmTopicRef
	 * 
	 * @return a String which represents the Uri of an ItemIdentity of a Topic
	 */
	public String getTopicRef() {
		return topicRef;
	}

	/**
	 * setTopicRef of XtmTopicRef
	 * 
	 * @param topicRef
	 */
	public void setTopicRef(String topicRef) {
		this.topicRef = topicRef;
	}
	
}
