package ce.tm4scholion.tm.persistency.xtm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import ce.tm4scholion.tm.Association;
import ce.tm4scholion.tm.AssociationRole;
import ce.tm4scholion.tm.Occurrence;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.TopicName;

/**
 * Topic Map Engine - XTM Persistency - XtmTopic
 * 
 * XtmTopic is used as a wrapper class for ce.tm4scholion.tm.Topic. It simplifies the castor xml mapping to valid xtm 2.0 files and the retrieving of Topic objects from xtm 2.0 files
 */
public class XtmTopic extends Topic {

	private String id;
	
	private Set<XtmItemIdentity> xtmItemIdentifiers;
	
	private Set<XtmSubjectLocator> xtmSubjectLocators;
	
	private Set<XtmSubjectIdentifier> xtmSubjectIdentifiers;
	
	private Set<XtmTopicName> xtmTopicNames;
	
	private Set<XtmOccurrence> xtmOccurrences;
	
	private XtmInstanceOf instanceOf;
	
	/**
	 * default constructor
	 *
	 */
	public XtmTopic(){}
	
	/**
	 * Constructor
	 * 
	 * @param t Topic for which a XtmTopic wrapper is created
	 */
	public XtmTopic(Topic t){
		Iterator<String> iiIt = t.getItemIdentifiers().iterator();
		xtmItemIdentifiers = new HashSet<XtmItemIdentity>();
		while(iiIt.hasNext())
			xtmItemIdentifiers.add(new XtmItemIdentity(iiIt.next()));
		if(t.getSubjectLocators()!=null){
			Iterator<String> sIt = t.getSubjectLocators().iterator();
			xtmSubjectLocators = new HashSet<XtmSubjectLocator>();
			while(sIt.hasNext())
				xtmSubjectLocators.add(new XtmSubjectLocator(sIt.next()));
		}		
		if(t.getSubjectIdentifiers()!=null){
			Iterator<String> sIt = t.getSubjectIdentifiers().iterator();
			xtmSubjectIdentifiers = new HashSet<XtmSubjectIdentifier>();
			while(sIt.hasNext())
				xtmSubjectIdentifiers.add(new XtmSubjectIdentifier(sIt.next()));
		}
		if(t.getTopicNames() != null){
			xtmTopicNames = new HashSet<XtmTopicName>();
			Iterator<TopicName> tNIt = t.getTopicNames().iterator();
			while(tNIt.hasNext())
				xtmTopicNames.add(new XtmTopicName(tNIt.next()));
		}
		if(t.getOccurrences()!=null){
			xtmOccurrences = new HashSet<XtmOccurrence>();
			Iterator<Occurrence> oIt = t.getOccurrences().iterator();
			while(oIt.hasNext())
				xtmOccurrences.add(new XtmOccurrence(oIt.next()));
		}
		//instanceOf 
		initInstanceOf(t);
		//id is set by XtmTopicMap -> initXtmTopicMaps
	}
	
	/**
	 * initInstanceOf - initializes the instanceOf value of XtmTopic. If the Topic type of AssociationRole of Topic t is a type-instance, 
	 * instanceOf is added the firstItemIdentifier Uri of Topic type of the belonging Association. This method should just be used by the constructor 
	 * 
	 * @param t Topic
	 */
	private void initInstanceOf(Topic t){
		if(t.getAssociatedAssociations()!=null){
			Iterator<Entry<AssociationRole,Association>> it = t.getAssociatedAssociations().entrySet().iterator();
			Entry e;
			Set<String> topicRefUris = new HashSet<String>();
			while(it.hasNext()){
				e = it.next();
				AssociationRole aR = (AssociationRole)e.getKey();
				if(aR.getType().getSubjectIdentifiers()!=null){
					Iterator<String> sIIt = aR.getType().getSubjectIdentifiers().iterator();
					String sId;
					while(sIIt.hasNext()){
						sId = sIIt.next();
						if(sId.equalsIgnoreCase("http://psi.topicmaps.org/iso13250/model/type-instance")||sId.equalsIgnoreCase("urn:subject:aTypeInstance")){
							Association a = (Association) e.getValue();
							topicRefUris.add(a.getType().getFirstItemIdentifier());
							break;
						}
						
					}
				}
			}
			if(!topicRefUris.isEmpty())
				instanceOf = new XtmInstanceOf(topicRefUris);
				
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
	 * setSubjectLocatorValues - sets values of the Set<String> subjectLocators of the superclass Topic
	 *
	 */
	private void setSubjectLocatorValues(){
		if(xtmSubjectLocators!=null){
			Iterator<XtmSubjectLocator> it = this.xtmSubjectLocators.iterator();
			while(it.hasNext())
				this.addSubjectLocator(it.next().getSubjectLocator());
		}
	}
	
	/**
	 * setSubjectIdentifierValues - sets values of the Set<String> subjectIdentifiers of the superclass Topic
	 *
	 */
	private void setSubjectIdentifierValues(){
		if(xtmSubjectIdentifiers!=null){
			Iterator<XtmSubjectIdentifier> it = this.xtmSubjectIdentifiers.iterator();
			while(it.hasNext())
				this.addSubjectIdentifier(it.next().getSubjectIdentifier());
		}
	}
	
	/**
	 * setTopicNameValues - sets values of Set<TopicName> topicNames of the superclass Topic
	 * 
	 * @param tm XtmTopicMap
	 */
	private void setTopicNameValues(XtmTopicMap tm){
		if(xtmTopicNames!=null){
			//this.setTopicNames((Set<TopicName>)xtmTopicNames);
			Iterator<XtmTopicName> it = xtmTopicNames.iterator();
			while(it.hasNext()){
				it.next().setTopicNameValues(tm, this);
			}
			this.setTopicNames(castXtmTopicNames());
		}
	}
	
	/**
	 * get XtmTopicNames as a Set of ce.tm4scholion.tm.TopicNames
	 * 
	 * @return a set of ce.tm4scholion.tm.TopicNames, null if XtmTopic has no XtmTopicNames
	 */
	private Set<TopicName> castXtmTopicNames(){
		if(xtmTopicNames!=null){
			Set<TopicName> tNS = new HashSet<TopicName>();
			Iterator<XtmTopicName> it = xtmTopicNames.iterator();
			while(it.hasNext()){
				tNS.add(it.next());
			}
			return tNS;
		}
		return null;
	}
	
	/**
	 * setOccurrenceValues - sets values of Set<Occurrence> occurrences of the superclass Topic
	 * 
	 * @param tm XtmTopicMap
	 */
	private void setOccurrenceValues(XtmTopicMap tm){
		if(xtmOccurrences!=null){
			Iterator<XtmOccurrence> it = xtmOccurrences.iterator();
			while(it.hasNext()){
				it.next().setOccurrenceValues(tm, this);
			}
			this.setOccurrences(castXtmOccurrences());
			
		}
	}
	
	/**
	 * get XtmOccurrences as a Set of ce.tm4scholion.tm.Occurrences
	 * 
	 * @return a set of ce.tm4scholion.tm.Occurrences, null if XtmTopic has no XtmOccurrences
	 */
	private Set<Occurrence> castXtmOccurrences(){
		if(xtmOccurrences!=null){
			Set<Occurrence> tOs = new HashSet<Occurrence>();
			Iterator<XtmOccurrence> it = xtmOccurrences.iterator();
			while(it.hasNext()){
				tOs.add(it.next());
			}
			return tOs;
		}
		return null;
	}
	
	//useless -> is set by AssociationRole because of performance reasons
/*	*
 * //**
	 * setRolesPlayed - sets values of Set<AssociationRole> rolesPlayed of the superclass Topic
	 * 
	 * @param tm XtmTopicMap
	 *//*
	private void setRolesPlayed(XtmTopicMap tm){
		Iterator<XtmAssociation> aIt = tm.getXtmAssociations().iterator();
		while(aIt.hasNext()){
			XtmAssociation a = aIt.next();
			Iterator<XtmAssociationRole> aRIt = a.getXtmRoles().iterator();
			while(aRIt.hasNext()){
				XtmAssociationRole aR = aRIt.next();
				Iterator<XtmItemIdentity> iIIt = this.getXtmItemIdentifiers().iterator();
				while(iIIt.hasNext()){
					if(aR.getTopicRefUri().equalsIgnoreCase(iIIt.next().getItemIdentity())){//player uri of XtmAssociation equals Uri in ItemIdentifiers of this Topic
						this.addRolePlayed(aR);
						aR.setPlayer(this);
						break;
					}
					
				}
			}
		}
			
	}
*/
	/**
	 * setTopicValues should be called for every XtmTopic of a XtmTopicMap after creating a XtmTopicMap from a 
	 * xtm 2.0 file. values/references of XtmTopic are set.  
	 *    
	 * @param tm XtmTopicMap which was created from a xtm 2.0 file
	 */
	public void setTopicValues(XtmTopicMap tm){
		setFirstItemIdentifierValue();
		setItemIdentifierValues();
		setSubjectLocatorValues();
		setSubjectIdentifierValues();
		setTopicNameValues(tm);
		setOccurrenceValues(tm);
		//setRolesPlayed(tm);
		this.setParent(tm);
	}

//getter and setter//
	
	/**
	 * add an AssocationRole this Topic plays (invoked by AssociationRole to
	 * establish backlink)
	 *
	 * @param associationRole
	 *            the role to be played by the topic
	 */
	protected void addRolesPlayed(AssociationRole associationRole){
		this.addRolePlayed(associationRole);
	}
	
	/**
	 * getId gets the id of XtmTopic
	 */
	public String getId() {
		return id;
	}

	/**
	 * setId sets the id of XtmTopic
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * getXtmOccurrences gets the set of XtmOccurrence objects which belongs to XtmTopic
	 * 
	 * @return a Set of XtmOccurrence objects
	 */
	public Set<XtmOccurrence> getXtmOccurrences() {
		return xtmOccurrences;
	}

	/**
	 * setXtmOccurrence sets the set of XtmOccurrence objects which belong to XtmTopic 
	 * 
	 * @param xtmOccurrences
	 */
	public void setXtmOccurrences(Set<XtmOccurrence> xtmOccurrences) {
		this.xtmOccurrences = xtmOccurrences;
	}

	/**
	 * getXtmTopicNames gets the XtmTopicName objects of XtmTopic
	 * 
	 * @return a set of XtmTopicName objects
	 */
	public Set<XtmTopicName> getXtmTopicNames() {
		return xtmTopicNames;
	}

	/**
	 * setXtmTopicNames sets the set of XtmTopicName objects which belong to XtmTopic
	 * 
	 * @param xtmTopicNames
	 */
	public void setXtmTopicNames(Set<XtmTopicName> xtmTopicNames) {
		this.xtmTopicNames = xtmTopicNames;
	}

	/**
	 * getXtmSubjectIdentifiers gets the set of XtmSubjectIdentifier of XtmTopic
	 * 
	 * @return a set of XtmSubjectIdentifier
	 */
	public Set<XtmSubjectIdentifier> getXtmSubjectIdentifiers() {
		return xtmSubjectIdentifiers;
	}
	
	/**
	 * setXtmSubjectIdentifiers sets the set of XtmSubjectIdentifier of XtmTopic
	 * 
	 * @param xtmSubjectIdentifiers
	 */
	public void setXtmSubjectIdentifiers(
			Set<XtmSubjectIdentifier> xtmSubjectIdentifiers) {
		this.xtmSubjectIdentifiers = xtmSubjectIdentifiers;
	}

	/**
	 * getXtmSubjectLocators gets the set of XtmSubjectLocator objects of XtmTopic
	 * 
	 * @return a set of XtmSubjectLocator objects
	 */
	public Set<XtmSubjectLocator> getXtmSubjectLocators() {
		return xtmSubjectLocators;
	}

	/**
	 * setXtmSubjectLocators sets the set of XtmSubjectLocator objects of XtmTopic
	 * 
	 * @param xtmSubjectLocators
	 */
	public void setXtmSubjectLocators(Set<XtmSubjectLocator> xtmSubjectLocators) {
		this.xtmSubjectLocators = xtmSubjectLocators;
	}

	/**
	 * getXtmItemIdentifiers gets the set of XtmItemIdentity objects of XtmTopic
	 * 
	 * @return a set of XtmItemIdentity objects
	 */
	public Set<XtmItemIdentity> getXtmItemIdentifiers() {
		return xtmItemIdentifiers;
	}

	/**
	 * setXtmItemIdentifiers sets the set of XtmItemIdentity objects of XtmTopic
	 * 
	 * @param xtmItemIdentifiers
	 */
	public void setXtmItemIdentifiers(Set<XtmItemIdentity> xtmItemIdentifiers) {
		this.xtmItemIdentifiers = xtmItemIdentifiers;
	}

	/**
	 * getInstanceOf gets the XtmInstanceOf variable of XtmTopic
	 * 
	 * @return a XtmInstanceOf object, otherwise null
	 */
	public XtmInstanceOf getInstanceOf() {
		return instanceOf;
	}

	/**
	 * setInstanceOf sets the XtmInstanceOf variable of XtmTopic
	 * 
	 * @param instanceOf
	 */
	public void setInstanceOf(XtmInstanceOf instanceOf) {
		this.instanceOf = instanceOf;
	}
}
