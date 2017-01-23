package ce.tm4scholion.tm.persistency.xtm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ce.tm4scholion.tm.AssociationRole;

/**
 * 
 * Topic Map Engine - XTM Persistency - XtmAssociationRole
 * 
 * XtmAssociationRole is used as a wrapper class for ce.tm4scholion.tm.AssociationRole. It simplifies the castor xml mapping to valid xtm 2.0 files and the retrieving of AssociationRole objects from xtm 2.0 files
 *
 */
public class XtmAssociationRole extends AssociationRole {
	
	private Set<XtmItemIdentity> xtmItemIdentifiers;
	
	private String reifierUri;//itemIdentity of reifier (Topic) from Superclass
	
	private XtmType typeUri;//itemIdentity of type (Topic)from Superclass
	
	private String topicRefUri;//itemIdentity of Topic player from Superclass
	
	/**
	 * default constructor
	 *
	 */
	public XtmAssociationRole(){}
	
	/**
	 * Constructor
	 * 
	 * @param aR AssociationRole for which a XtmAssociationRole wrapper is created
	 */
	public XtmAssociationRole(AssociationRole aR){
		Iterator<String> iiIt = aR.getItemIdentifiers().iterator();
		xtmItemIdentifiers = new HashSet<XtmItemIdentity>();
		while(iiIt.hasNext()){
			xtmItemIdentifiers.add(new XtmItemIdentity(iiIt.next()));
		}
		if(aR.getReifier()!=null)
			this.reifierUri = aR.getReifier().getFirstItemIdentifier();
		if(aR.getPlayer()!=null)
			this.topicRefUri = aR.getPlayer().getFirstItemIdentifier();
		if(aR.getType()!=null)
			this.typeUri = new XtmType(aR.getType().getFirstItemIdentifier());
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
	 * setTypeReference sets the object reference of the varibale type (Topic) from the superclass of XtmAssociationRole
	 * according to the value of typeUri
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
	 * setPlayerReference sets the object reference of the varibale player (Topic) from the superclass of XtmAssociationRole
	 * according to the value of topicRefUri. Furthermore the player (Topic) is added an rolePlayed AssociationRole to map the
	 * 1..* relation correctly.
	 *  
	 * 
	 * @param tm XtmTopicMap
	 */	
	private void setPlayerReference(XtmTopicMap tm){
		if(topicRefUri!=null) {
			Iterator<XtmTopic>it = tm.getXtmTopics().iterator();
			XtmTopic refT;
			Boolean refFound = false;
			while(it.hasNext()){
				refT = it.next();
				Iterator<XtmItemIdentity> iIIt = refT.getXtmItemIdentifiers().iterator();
				while(iIIt.hasNext()){
					if(iIIt.next().getItemIdentity().equalsIgnoreCase(topicRefUri)){
							this.setPlayer(refT);
							refT.addRolesPlayed(this);
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
	 * setAssociationRoleValues should be called for every XtmAssociationRole of a XtmAssociation after creating a XtmTopicMap from a 
	 * xtm 2.0 file. values/references of XtmAssociationRole are set.  
	 *    
	 * @param tm XtmTopicMap which was created from a xtm 2.0 file
	 */	
	public void setAssociationRoleValues(XtmTopicMap tm, XtmAssociation a){
		this.setParent(a);
		setFirstItemIdentifierValue();
		setItemIdentifierValues();
		setTypeReference(tm);
		setPlayerReference(tm);
		if(reifierUri!=null)
			setReifierReference(tm, reifierUri);
	}
	
	
//getter and setter//

	/**
	 * getReifierUri of XtmAssociationRole
	 * 
	 * @return a String which represents the URI of the Reifier
	 */
	public String getReifierUri() {
		return reifierUri;
	}

	/**
	 * setReifierUri of XtmAssociationRole
	 * 
	 * @param reifierUri
	 */
	public void setReifierUri(String reifierUri) {
		this.reifierUri = reifierUri;
	}

	/**
	 * getTopicRefUri of XtmAssociationRole
	 * 
	 * @return a String which represents the URI of a referenced Topic
	 */
	public String getTopicRefUri() {
		return topicRefUri;
	}

	/**
	 * setTopicRefUri of XtmAssociationRole
	 * 
	 * @param topicRefUri
	 */
	public void setTopicRefUri(String topicRefUri) {
		this.topicRefUri = topicRefUri;
	}
	
	/**
	 * getTypeUri of XtmAssociationRole
	 * 
	 * @return a String which represents the URI of the Type
	 */
	public XtmType getTypeUri() {
		return typeUri;
	}

	/**
	 * setTypeUri of XtmAssociationRole
	 * 
	 * @param typeUri
	 */
	public void setTypeUri(XtmType typeUri) {
		this.typeUri = typeUri;
	}

	/**
	 * getXtmItemIdentifiers of XtmAssociationRole
	 * 
	 * @return a Set of XtmItemIdentity objects	 
	 */
	public Set<XtmItemIdentity> getXtmItemIdentifiers() {
		return xtmItemIdentifiers;
	}

	/**
	 * setXtmItemIdentifiers of XtmAssociationRole
	 * 
	 * @param xtmItemIdentifiers - a Set of XtmitemIdentity objects
	 */
	public void setXtmItemIdentifiers(Set<XtmItemIdentity> xtmItemIdentifiers) {
		this.xtmItemIdentifiers = xtmItemIdentifiers;
	}
	
}
