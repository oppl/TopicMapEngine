package ce.tm4scholion.tm.persistency.xtm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.TopicName;
import ce.tm4scholion.tm.Variant;

/**
 * Topic Map Engine - XTM Persistency - XtmTopicName
 * 
 * XtmTopicName is used as a wrapper class for ce.tm4scholion.tm.TopicName. It simplifies the castor xml mapping to valid xtm 2.0 files and the retrieving of TopicName objects from xtm 2.0 files
 * 
 */
public class XtmTopicName extends TopicName {
	
	private String reifierUri;//itemIdentity of reifier (Topic) from Superclass
	
	private XtmType typeUri;//itemIdentity of type (Topic) from Superclass
	
	private XtmScope xtmScope;
	
	private Set<XtmItemIdentity> xtmItemIdentifiers;
	
	private Set<XtmVariant> xtmVariants;
	
	/**
	 * default constructor
	 *
	 */
	public XtmTopicName(){}
	
	/**
	 * Constructor
	 * 
	 * @param tN TopicName for which a XtmTopicName wrapper is created
	 */
	public XtmTopicName(TopicName tN){
		Iterator<String> iiIt = tN.getItemIdentifiers().iterator();
		xtmItemIdentifiers = new HashSet<XtmItemIdentity>();
		while(iiIt.hasNext()){
			xtmItemIdentifiers.add(new XtmItemIdentity(iiIt.next()));
		}
		if(tN.getType()!=null)
			typeUri = new XtmType(tN.getType().getFirstItemIdentifier());
		if(tN.getScope()!=null && tN.getScope().getContext()!=null && !tN.getScope().getContext().isEmpty())
			xtmScope = new XtmScope(tN.getScope());
		this.value = tN.getValue();
		if(tN.getVariants()!=null){
			xtmVariants = new HashSet<XtmVariant>();
			Iterator<Variant> vIt = tN.getVariants().iterator();
			while(vIt.hasNext())
				xtmVariants.add(new XtmVariant(vIt.next()));
		}
		if(tN.getReifier()!=null)
			reifierUri = tN.getReifier().getFirstItemIdentifier();
	}
	
	/**
	 * setTypeReference - searches typeUri in XtmItemIdentifiers of XtmTopics in XtmTopicMap. If a XtmTopic 
	 * which has an XtmItemIdentifier with the specified Uri is found, the inherited method setType of 
	 * ce.tm4scholion.tm.TopicName is called and the reference is set. 
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
	 * setVariantValues - sets values of Set<Variant> variants of superclass TopicName
	 * 
	 * @param tm XtmTopicMap
	 */
	private void setVariantValues(XtmTopicMap tm){
		if(xtmVariants!=null){
			Iterator<XtmVariant> it = xtmVariants.iterator();
			while(it.hasNext()){
				it.next().setVariantValues(tm, this);
			}
		}
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
	 * setTopicNameValues should be called for every XtmTopicName of a XtmTopic after creating a XtmTopicMap from a 
	 * xtm 2.0 file. values/references of XtmTopicName are set.  
	 *    
	 * @param tm XtmTopicMap which was created from a xtm 2.0 file
	 * @param t parent of topicName
	 */
	public void setTopicNameValues(XtmTopicMap tm, Topic t){
		setFirstItemIdentifierValue();
		setItemIdentifierValues();
		setTypeReference(tm);
		this.setParent(t);
		this.setScope(getXtmScope());
		setVariantValues(tm);
//		this.setValue(t.getTopicNames().iterator().next().getValue());
		if(reifierUri!=null)
			setReifierReference(tm, reifierUri);
	}

//getter and setter//
	
	/**
	 * getReifierUri of XtmTopicName
	 * 
	 * @return a String which represents the URI of the Reifier
	 */
	public String getReifierUri() {
		return reifierUri;
	}

	/**
	 * setReifierUri of XtmTopicName
	 * 
	 * @param reifierUri
	 */
	public void setReifierUri(String reifierUri) {
		this.reifierUri = reifierUri;
	}

	/**
	 * getTypeUri of XtmTopicName
	 * 
	 * @return a XtmType 
	 */
	public XtmType getTypeUri() {
		return typeUri;
	}

	/**
	 * setTypeUri of XtmTopicName
	 * 
	 * @param typeUri
	 */
	public void setTypeUri(XtmType typeUri) {
		this.typeUri = typeUri;
	}

	/**
	 * getXtmScope of XtmTopicName
	 * 
	 * @return xtmScope, null if XtmAssociation has no scope
	 */
	public XtmScope getXtmScope() {
		return xtmScope;
	}

	/**
	 * setXtmScope of XtmTopicName
	 * 
	 * @param xtmScope
	 */
	public void setXtmScope(XtmScope xtmScope) {
		this.xtmScope = xtmScope;
	}

	/**
	 * getXtmVaiants of XtmTopicName
	 * 
	 * @return a set of XtmVariant objects of XtmTopicName
	 */
	public Set<XtmVariant> getXtmVariants() {
		return xtmVariants;
	}

	/**
	 * setXtmVariants of XtmTopicName
	 * 
	 * @param xtmVariants
	 */
	public void setXtmVariants(Set<XtmVariant> xtmVariants) {
		this.xtmVariants = xtmVariants;
	}

	/**
	 * getXtmItemIdentifiers of XtmTopicName
	 * 
	 * @return a Set of XtmItemIdentity Objects	 
	 */
	public Set<XtmItemIdentity> getXtmItemIdentifiers() {
		return xtmItemIdentifiers;
	}

	/**
	 * setXtmItemIdentifiers of XtmTopicName
	 * 
	 * @param xtmItemIdentifiers - a set of XtmItemIdentity objects
	 */
	public void setXtmItemIdentifiers(Set<XtmItemIdentity> xtmItemIdentifiers) {
		this.xtmItemIdentifiers = xtmItemIdentifiers;
	}
	
}
