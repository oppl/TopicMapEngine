package ce.tm4scholion.tm.persistency.xtm;

import java.util.HashSet;
import java.util.Set;

/**
 * Topic Map Engine - XTM Persistency - XtmInstanceOf
 * 
 * XtmInstanceOf is used by XtmTopic to map the instanceOf element of XTM 2.0 Schema correctly
 */
public class XtmInstanceOf {

	private Set<String> topicRefUris;
	
	/**
	 * default constructor
	 *
	 */
	public XtmInstanceOf(){}
	
	/**
	 * Constructor
	 * 
	 * @param topicRefUris
	 */
	public XtmInstanceOf(Set<String> topicRefUris){
		this.topicRefUris = topicRefUris;
	}
	
	/**
	 * addTopicRefUri - adds an Uri of a Topic to topicRefUris of XtmInstanceOf
	 * 
	 * @param topicRefUri
	 */
	public void addTopicRefUri(String topicRefUri){
		if(topicRefUris==null)
			topicRefUris = new HashSet<String>();
		topicRefUris.add(topicRefUri);
	}

	/**
	 * getTopicRefUris - gets topicRefUris of XtmInstanceOf
	 * 
	 * @return a HashSet<String> which consists of different Uris of different Topics
	 */
	public Set<String> getTopicRefUris() {
		return topicRefUris;
	}

	/**
	 * setTopicRefUris - sets topicRefUris of XtmInstanceOf
	 * 
	 * @param topicRefUris
	 */
	public void setTopicRefUris(Set<String> topicRefUris) {
		this.topicRefUris = topicRefUris;
	}
}
