package ce.tm4scholion.tm.persistency.xtm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ce.tm4scholion.tm.Scope;
import ce.tm4scholion.tm.Statement;
import ce.tm4scholion.tm.Topic;

/**
 * Topic Map Engine - XTM Persistency - XtmScope
 * 
 * XtmScope is used as a wrapper class for ce.tm4scholion.tm.Scope. It simplifies the castor xml mapping to valid xtm 2.0 files and the retrieving of Scope objects from xtm 2.0 files
 */
public class XtmScope extends Scope {
	
	private Set<XtmTopicRef> topicRefUris;//FirstitemIdentifiers of Topics in variable context from superclass 
	
	/**
	 * default constructor
	 *
	 */
	public XtmScope(){
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param s Scope for which a XtmScope wrapper is created
	 */
	public XtmScope(Scope s){
		if(s.getContext()!=null){
			Iterator<Topic> it = s.getContext().iterator();
			topicRefUris = new HashSet<XtmTopicRef>();
			while(it.hasNext()){
				Topic curT = it.next();
//				System.out.println("curT: "+ curT);
//				topicRefUris.add(new XtmTopicRef(it.next().getFirstItemIdentifier()));
				if(curT != null)
					topicRefUris.add(new XtmTopicRef(curT.getFirstItemIdentifier()));
			}
		}
	}
	
	/**
	 * setScopeValues should be called for every XtmScope of a XtmTopicMap after creating a XtmTopicMap from a 
	 * xtm 2.0 file (XtmTopicName, XtmVariant, XtmOccurrence, XtmAssociation objects own XtmScope variables). values/references of XtmScope are set.  
	 *    
	 * @param tm XtmTopicMap which was created from a xtm 2.0 file
	 */
	public void setScopeValues(XtmTopicMap tm){
		//set Topic References
		if(topicRefUris!=null){
			Iterator<XtmTopicRef> it = topicRefUris.iterator();
			XtmTopicRef topicRef;
			while(it.hasNext()){
				topicRef = it.next();
				Iterator<XtmTopic> tIt = tm.getXtmTopics().iterator();
				//Boolean refFound = false;
				while(tIt.hasNext()){
					XtmTopic ref = tIt.next();
					Iterator<XtmItemIdentity> iIIt = ref.getXtmItemIdentifiers().iterator();
					while(iIIt.hasNext()){
						if(iIIt.next().getItemIdentity().equalsIgnoreCase(topicRef.getTopicRef())){
//							System.out.println("setScopeValues ref getxtmtopicname: "+ref.getXtmTopicNames().iterator().next().getValue());
							this.addToContext(ref);
							//refFound = true;
							break;
						}
					}
					/*if(refFound)
						break;*/
				}
			}
		}
	}
	
	/**
	 *  compareTopicRefUris
	 *  
	 * @param topicRefUris
	 * @return true if the topicRefUris of the context of this XtmScope own the same elements like the input paramter
	 */
	protected boolean compareTopicRefUris(Set<XtmTopicRef> topicRefUris){
		Set<String> tRUris = getTopicRefUrisAsString(topicRefUris);
		Set<String> thisTRUris = getTopicRefUrisAsString(this.topicRefUris);
		if(tRUris.containsAll(thisTRUris) && thisTRUris.containsAll(tRUris))
			return true;
		return false;
//		
//		Boolean contains = true;
//		//this.topicRefUris contains topicRefUris ?
//		Iterator<XtmTopicRef> it = topicRefUris.iterator();
//		while(it.hasNext()){
//			if(!this.getTopicRefUrisAsString().contains(it.next().getTopicRef()))
//				contains = false;
//		}
//		//topicRefUris contains this.topicRefUris
//		Iterator<XtmTopicRef> it1 = this.topicRefUris.iterator();
//		while(it1.hasNext()){
//			if(!getTopicRefUrisAsString().contains(it1.next().getTopicRef()))
//				contains = false;
//		}
//		return contains;
	}
	
//	private Set<String> getTopicRefUrisAsString(){
//		Set<String> topicRefs = new HashSet<String>();
//		if(getTopicRefUris()!=null){
//			Iterator<XtmTopicRef> i = getTopicRefUris().iterator();
//			while(i.hasNext()){
//				topicRefs.add(i.next().getTopicRef());
//			}
//		}
//		return topicRefs;
//		
//	}
	
	private Set<String> getTopicRefUrisAsString(Set<XtmTopicRef> topicRefUris){
		Set<String> topicRefs = new HashSet<String>();
		if(getTopicRefUris()!=null){
			Iterator<XtmTopicRef> i = topicRefUris.iterator();
			while(i.hasNext()){
				topicRefs.add(i.next().getTopicRef());
			}
		}
		return topicRefs;
		
	}

	/**
	 * add a Statement to the Scope, thus defining it to be valid within the
	 * Scope
	 *
	 * @param s
	 *            the Statement to be added
	 */
	protected void addStatement(Statement s){
		super.addStatement(s);
	}
	
//getter and setter//	
	/**
	 * getTopicRefUris gets the Uris of referenced Topics
	 * 
	 * @return a Set of Strings which represents Uris of Topics
	 */
	public Set<XtmTopicRef> getTopicRefUris() {
		return topicRefUris;
	}
	
	/**
	 * setTopicRefUris sets the TopicRefUri set
	 * 
	 * @param topicRefUris
	 */
	public void setTopicRefUris(Set<XtmTopicRef> topicRefUris) {
		this.topicRefUris = topicRefUris;
	}
}
