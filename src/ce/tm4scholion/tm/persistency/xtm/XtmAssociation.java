package ce.tm4scholion.tm.persistency.xtm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ce.tm4scholion.tm.Association;
import ce.tm4scholion.tm.AssociationRole;

/**
 * Topic Map Engine - XTM Persistency - XtmAssociation
 * 
 * XtmAssociation is used as a wrapper class for ce.tm4scholion.tm.Association. It simplifies the castor xml mapping to valid xtm 2.0 files and the retrieving of Association objects from xtm 2.0 files  
 */
public class XtmAssociation extends Association {
	
	private Set<XtmItemIdentity> xtmItemIdentifiers;
	
	private String reifierUri;//itemIdentity of reifier (Topic) from Superclass
	
	private XtmType typeUri;//itemIdentity of type (Topic) from Superclass
	
	private Set<XtmAssociationRole> xtmRoles;
	
	private XtmScope xtmScope;
	
	
	/**
	 * default constructor
	 *
	 */
	public XtmAssociation(){
		
	}
	
	/**
	 * Constructor
	 * 
	 * @param a Association for which a XtmAssociation wrapper is created
	 */
	public XtmAssociation(Association a){
		Iterator<String> iiIt = a.getItemIdentifiers().iterator();
		xtmItemIdentifiers = new HashSet<XtmItemIdentity>();
		while(iiIt.hasNext()){
			xtmItemIdentifiers.add(new XtmItemIdentity(iiIt.next()));
		}
		if(a.getType()!=null)
			typeUri = new XtmType(a.getType().getFirstItemIdentifier());
		if(a.getScope()!=null && a.getScope().getContext()!=null && !a.getScope().getContext().isEmpty()){
			xtmScope = new XtmScope(a.getScope());
			//System.out.println("Association "+a.getType().getTopicNames().iterator().next().getValue()+" has Scope "+ a.getScope()+" ! xtmScope = "+xtmScope);
		}
		if(a.getRoles()!=null){
			xtmRoles = new HashSet<XtmAssociationRole>();
			Iterator<AssociationRole> aRIt = a.getRoles().iterator();
			while(aRIt.hasNext()){
				xtmRoles.add(new XtmAssociationRole(aRIt.next()));
			}
		}
		if(a.getReifier()!=null)
			reifierUri = a.getReifier().getFirstItemIdentifier();
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
	 * ce.tm4scholion.tm.Association is called and the reference is set. 
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
	 * setAssociationRoleValues - calls for each XtmAssociationRole in XtmAssociation the Method setAssocationRoleValues. 
	 * See ce.tm4scholion.tm.xtmpersisteny.xtmAssociationRole
	 * 
	 * @param tm XtmTopicMap
	 */
	private void setAssociationRoleValues(XtmTopicMap tm){
		if(xtmRoles!=null){
			Iterator<XtmAssociationRole> it = xtmRoles.iterator();
			while(it.hasNext()){
				it.next().setAssociationRoleValues(tm, this);
			}
			this.setRoles(castXtmAssociationRoles());
		}
	}
	
	/**
	 * get XtmAssociationRoles as ce.tm4scholion.tm.AssociationRoles
	 * 
	 * @return a Set of ce.tm4scholion.tm.AssociationRoles, null if the association has no XtmAssociationRoles
	 */
	private Set<AssociationRole> castXtmAssociationRoles(){
		if(xtmRoles!=null){
			Set<AssociationRole> aRS = new HashSet<AssociationRole>();
			Iterator<XtmAssociationRole> it = xtmRoles.iterator();
			while(it.hasNext()){
				aRS.add(it.next());
			}
			return aRS;
		}
		return null;
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
	 * setAssociationValues should be called for every XtmAssociation of a XtmTopicMap after creating a XtmTopicMap from a 
	 * xtm 2.0 file. values/references of XtmAssociation are set.  
	 *    
	 * @param tm XtmTopicMap which was created from a xtm 2.0 file
	 */
	public void setAssociationValues(XtmTopicMap tm){
		this.setParent(tm);
		this.setScope(getXtmScope());
		setFirstItemIdentifierValue();
		setItemIdentifierValues();
		setTypeReference(tm);
		setAssociationRoleValues(tm);
		if(reifierUri!=null)
			setReifierReference(tm, reifierUri);
	}
	
	
//getter and setter//
	
	/**
	 * getXtmScope of XtmAssociation
	 * 
	 * @return xtmScope, null if XtmAssociation has no scope
	 */
	public XtmScope getXtmScope() {
		return xtmScope;
	}
	
	/**
	 * setXtmScope of XtmAssociation
	 * 
	 * @param xtmScope
	 */
	public void setXtmScope(XtmScope xtmScope) {
		this.xtmScope = xtmScope;
	}
	
	/**
	 * getReifierUri of XtmAssociation
	 * 
	 * @return a String which represents the URI of the Reifier
	 */
	public String getReifierUri() {
		return reifierUri;
	}

	/**
	 * setReifierUri of XtmAssociation
	 * 
	 * @param reifierUri
	 */
	public void setReifierUri(String reifierUri) {
		this.reifierUri = reifierUri;
	}

	/**
	 * getXtmRoles of XtmAssociation
	 * 
	 * @return xtmRoles, null if there are no xtmRoles
	 */
	public Set<XtmAssociationRole> getXtmRoles() {
		return xtmRoles;
	}

	/**
	 * setXtmRoles of XtmAssociation
	 * 
	 * @param roles
	 */
	public void setXtmRoles(Set<XtmAssociationRole> roles) {
		this.xtmRoles = roles;
	}

	/**
	 * getTypeUri of XtmAssociation
	 * 
	 * @return a XtmType 
	 */
	public XtmType getTypeUri() {
		return typeUri;
	}

	/**
	 * setTypeUri of XtmAssociation
	 * 
	 * @param typeUri
	 */
	public void setTypeUri(XtmType typeUri) {
		this.typeUri = typeUri;
	}

	/**
	 * getXtmItemIdentifiers of XtmAssociation
	 * 
	 * @return a Set of XtmItemIdentity Objects	 
	 */
	public Set<XtmItemIdentity> getXtmItemIdentifiers() {
		return xtmItemIdentifiers;
	}
	
	/**
	 * setXtmItemIdentifiers of XtmAssociation
	 * 
	 * @param xtmItemIdentifiers - a set of XtmItemIdentity objects
	 */
	public void setXtmItemIdentifiers(Set<XtmItemIdentity> xtmItemIdentifiers) {
		this.xtmItemIdentifiers = xtmItemIdentifiers;
	}
}
