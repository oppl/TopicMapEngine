package ce.tm4scholion.tm.persistency.xtm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ce.tm4scholion.tm.Occurrence;

/**
 * 
 * Topic Map Engine - XTM Persistency - XtmOccurrence
 *
 * XtmOccurrence is used as a wrapper class for ce.tm4scholion.tm.Occurrence. It simplifies the castor xml mapping to valid xtm 2.0 files and the retrieving of Occurrence objects from xtm 2.0 files
 */
public class XtmOccurrence extends Occurrence {
	
	private String reifierUri;//itemIdentity of reifier (Topic) from Superclass
	
	private XtmType typeUri;//itemIdentity of type (Topic)from Superclass
	
	private XtmScope xtmScope;
	
	private Set<XtmItemIdentity> xtmItemIdentifiers;
	
	/**
	 * default constructor
	 *
	 */
	public XtmOccurrence(){}
	
	/**
	 * Consturctor
	 * 
	 * @param o Occurrence for which a XtmOccurrence wrapper is created
	 */
	public XtmOccurrence(Occurrence o){
		Iterator<String> iiIt = o.getItemIdentifiers().iterator();
		xtmItemIdentifiers = new HashSet<XtmItemIdentity>();
		while(iiIt.hasNext()){
			xtmItemIdentifiers.add(new XtmItemIdentity(iiIt.next()));
		}	
		if(o.getType()!=null) 
			typeUri=new XtmType(o.getType().getFirstItemIdentifier());
		if(o.getScope()!=null && !o.getScope().getContext().isEmpty())
			xtmScope = new XtmScope(o.getScope());
		if (o.getDataType()!=null && o.getDataType().equals("http://www.w3.org/2001/XMLSchema#anyURI"))
			this.value = o.getValue();//resourceRef
		else 
			this.dataType = o.getValue();//resourceData
		if(o.getReifier()!=null)
			reifierUri = o.getReifier().getFirstItemIdentifier();
	}
	
	/**
	 * setFirstItemIdentifier - sets value of the inherited String firstItemidentifier
	 *
	 */
	private void setFirstItemIdentifierValue(){
		this.firstItemIdentifier = this.xtmItemIdentifiers.iterator().next().getItemIdentity();
	}
	
	/**
	 * setItemIdentifierValues - sets values of the inherited Set<String> itemItemidentifiers
	 *
	 */
	private void setItemIdentifierValues(){
		Iterator<XtmItemIdentity> it = this.xtmItemIdentifiers.iterator();
		itemIdentifiers = new HashSet<String>();
		while(it.hasNext())
			itemIdentifiers.add(it.next().getItemIdentity());	
	}
	
	/**
	 * setTypeReference - searches typeUri in XtmItemIdentifiers of XtmTopics in XtmTopicMap. If a XtmTopic 
	 * which has an XtmItemIdentifier with the specified Uri is found, the inherited method setType of 
	 * ce.tm4scholion.tm.Occurrence is called and the reference is set. 
	 * 
	 * @param tm XtmTopicMap 
	 */
	private void setTypeReference(XtmTopicMap tm){
		if(typeUri!=null) {
			Iterator<XtmTopic>it = tm.getXtmTopics().iterator();
			XtmTopic refT;
			Boolean refFound = false;
			while(it.hasNext()){
				refT = it.next();
				Iterator<XtmItemIdentity> iIIt = refT.getXtmItemIdentifiers().iterator();
				while(iIIt.hasNext()){
					if(iIIt.next().getItemIdentity().equalsIgnoreCase(typeUri.getTypeUri())){
							this.setType(refT);
							refFound = true;
							break;
					}
				}
				if(refFound)
					break;
			}
		}
	}
	
	/**
	 * setReifierReference - searches reifierUri in XtmItemIdentifiers of XtmTopics in this XtmTopicMap. If a XtmTopic 
	 * which has an XtmItemIdentifier with the specified Uri is found, the Reifier reference of TopicMap is set. 
	 * 
	 * @param reifierUri
	 */
	private void setReifierReference(XtmTopicMap tm, String reifierUri){
		Set<XtmTopic> xtmTopics =tm.getXtmTopics(); 
		if(xtmTopics!=null){
			Boolean refFound = false;
			Iterator<XtmTopic> it = xtmTopics.iterator();
			while(it.hasNext()){
				XtmTopic t = it.next();
				Iterator<XtmItemIdentity> xtmII = t.getXtmItemIdentifiers().iterator();
				while(xtmII.hasNext()){
					if(xtmII.next().getItemIdentity().equalsIgnoreCase(reifierUri)){
						this.setReifier(t);
						t.setReified(this);
						refFound = true;
						break;
					}
				}
				if(refFound)
					break;
			}
		}
	}
	
	/**
	 * setOccurrenceValues should be called for every XtmOccurrence of a XtmTopic after creating a XtmTopicMap from a 
	 * xtm 2.0 file. values/references of XtmOccurrence are set.  
	 *    
	 * @param tm XtmTopicMap which was created from a xtm 2.0 file
	 * @param t XtmTopic which is the parent of Occurrence
	 */
	public void setOccurrenceValues(XtmTopicMap tm, XtmTopic t){
		this.setParent(t);
		this.setScope(this.getXtmScope());
		setFirstItemIdentifierValue();
		setItemIdentifierValues();
		setTypeReference(tm);
		if(reifierUri!=null)
			setReifierReference(tm, reifierUri);
	}
	
//getter and setter//
	
	/**
	 * getReifierUri of XtmOccurrence
	 * 
	 * @return a String which represents the URI of the Reifier
	 */
	public String getReifierUri() {
		return reifierUri;
	}

	/**
	 * setReifierUri of XtmOccurrence
	 * 
	 * @param reifierUri
	 */
	public void setReifierUri(String reifierUri) {
		this.reifierUri = reifierUri;
	}

	/**
	 * getTypeUri of XtmOccurrence
	 * 
	 * @return a XtmType
	 */
	public XtmType getTypeUri() {
		return typeUri;
	}

	/**
	 * setTypeUri of XtmOccurrence
	 * 
	 * @param typeUri
	 */
	public void setTypeUri(XtmType typeUri) {
		this.typeUri = typeUri;
	}

	/**
	 * getXtmScope of XtmOccurrence
	 * 
	 * @return xtmScope, null if XtmOccurrence has no scope
	 */
	public XtmScope getXtmScope() {
		return xtmScope;
	}

	/**
	 * setXtmScope of XtmOccurrence
	 * 
	 * @param xtmScope
	 */
	public void setXtmScope(XtmScope xtmScope) {
		this.xtmScope = xtmScope;
	}

	/**
	 * getXtmItemIdentifiers of XtmOccurrence
	 * 
	 * @return a Set of XtmItemIdentity Objects	 
	 */
	public Set<XtmItemIdentity> getXtmItemIdentifiers() {
		return xtmItemIdentifiers;
	}

	/**
	 * setXtmItemIdentifiers of XtmOccurrence
	 * 
	 * @param xtmItemIdentifiers - a set of XtmItemIdentity objects
	 */
	public void setXtmItemIdentifiers(Set<XtmItemIdentity> xtmItemIdentifiers) {
		this.xtmItemIdentifiers = xtmItemIdentifiers;
	}
}
