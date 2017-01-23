package ce.tm4scholion.tm.persistency.xtm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ce.tm4scholion.tm.Variant;

/**
 * Topic Map Engine - XTM Persistency - XtmVariant
 * 
 * XtmVariant is used as a wrapper class for ce.tm4scholion.tm.Variant. It simplifies the castor xml mapping to valid xtm 2.0 files and the retrieving of Variant objects from xtm 2.0 files
 */
public class XtmVariant extends Variant {
	
	private String reifierUri;//itemIdentity of reifier (Topic) from Superclass
	
	private XtmScope xtmScope;
	
	private Set<XtmItemIdentity> xtmItemIdentifiers;
	
	/**
	 * default constructor
	 *
	 */
	public XtmVariant(){}
	
	/**
	 * Constructor
	 * 
	 * @param v a Variant for which a XtmVariant wrapper is created
	 */
	public XtmVariant(Variant v){
		Iterator<String> iiIt = v.getItemIdentifiers().iterator();
		xtmItemIdentifiers = new HashSet<XtmItemIdentity>();
		while(iiIt.hasNext()){
			xtmItemIdentifiers.add(new XtmItemIdentity(iiIt.next()));
		}
		if(v.getScope()!=null && v.getScope().getContext()!=null && !v.getScope().getContext().isEmpty())
			xtmScope = new XtmScope(v.getScope());
		//xtmScope = new XtmScope(v.getScope());
		this.value = v.getValue();//resourceRef
		this.dataType = v.getDataType();//resourceData
		if(v.getReifier()!=null)
			reifierUri = v.getReifier().getFirstItemIdentifier();
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
	 * setReifierReference - searches reifierUri in XtmItemIdentifiers of XtmTopics in this XtmTopicMap. If a XtmTopic 
	 * which has an XtmItemIdentifier with the specified Uri is found, the Reifier reference of TopicMap is set and furthermore reified of Topic is set 
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
	 * setVariantValues should be called for every XtmVariant of a XtmTopicMap after creating a XtmTopicMap from a 
	 * xtm 2.0 file. values/references of XtmVariant are set.  
	 *    
	 * @param tm XtmTopicMap which was created from a xtm 2.0 file
	 */
	public void setVariantValues(XtmTopicMap tm, XtmTopicName tn){
		setFirstItemIdentifierValue();
		setItemIdentifierValues();
		this.setParent(tn);
		this.setScope(this.getXtmScope());
		if(reifierUri!=null)
			setReifierReference(tm,reifierUri);
	}
	
//getter and setter//
	
	/**
	 * getReifierUri of XtmVariant
	 * 
	 * @return a String which represents the URI of the Reifier
	 */
	public String getReifierUri() {
		return reifierUri;
	}

	/**
	 * setReifierUri of XtmVariant
	 * 
	 * @param reifierUri
	 */
	public void setReifierUri(String reifierUri) {
		this.reifierUri = reifierUri;
	}

	/**
	 * getXtmScope of XtmVariant
	 * 
	 * @return xtmScope, null if XtmVariant has no scope
	 */
	public XtmScope getXtmScope() {
		return xtmScope;
	}

	/**
	 * setXtmScope of XtmVariant
	 * 
	 * @param xtmScope
	 */
	public void setXtmScope(XtmScope xtmScope) {
		this.xtmScope = xtmScope;
	}

	/**
	 * getXtmItemIdentifiers of XtmVariant
	 * 
	 * @return a Set of XtmItemIdentity Objects	 
	 */
	public Set<XtmItemIdentity> getXtmItemIdentifiers() {
		return xtmItemIdentifiers;
	}

	/**
	 * setXtmItemIdentifiers of XtmVariant
	 * 
	 * @param xtmItemIdentifiers - a set of XtmItemIdentity objects
	 */
	public void setXtmItemIdentifiers(Set<XtmItemIdentity> xtmItemIdentifiers) {
		this.xtmItemIdentifiers = xtmItemIdentifiers;
	}
	
}
