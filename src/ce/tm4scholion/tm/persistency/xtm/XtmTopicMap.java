package ce.tm4scholion.tm.persistency.xtm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ce.tm4scholion.tm.Association;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.TopicMap;

/**
 * Topic Map Engine - XTM Persistency - XtmTopicMap
 * 
 * XtmTopicMap is used as a wrapper class for ce.tm4scholion.tm.TopicMap. It simplifies the castor xml mapping to valid xtm 2.0 files and the retrieving of TopicMap objects from xtm 2.0 files
 */
public class XtmTopicMap extends TopicMap {

	private String reifierUri;//itemIdentity of reifier (Topic) from Superclass
	
	private String version;
	
	private Set<XtmItemIdentity> xtmItemIdentifiers;
	
	private Set<XtmTopic> xtmTopics;
	
	private Set<XtmAssociation> xtmAssociations;
	
	private Set<XtmScope> xtmScopes;
	
	/**
	 * default constructor
	 *
	 */
	public XtmTopicMap() {}	
	
	/**
	 * Constructor
	 * 
	 * @param tm TopicMap for which a XtmTopicMap wrapper is created
	 */
	public XtmTopicMap(TopicMap tm){
		initXtmItemIdentifiers(tm);
		initXtmTopics(tm);
		initXtmAssociations(tm);
		if(tm.getReifier()!=null)
			reifierUri = tm.getReifier().getFirstItemIdentifier();
		version = "2.0";
	}
	
	/**
	 * initXtmItemIdentifiers - inits Set<XtmItemIdentity> xtmItemIdentifiers of XtmTopicMap; Should only be used by Constructor
	 * 
	 * @param tm TopicMap
	 */
	private void initXtmItemIdentifiers(TopicMap tm){
		Iterator<String> iiIt = tm.getItemIdentifiers().iterator();
		xtmItemIdentifiers = new HashSet<XtmItemIdentity>();
		while(iiIt.hasNext())
			xtmItemIdentifiers.add(new XtmItemIdentity(iiIt.next()));		
	}
	
	/**
	 * initXtmTopic - inits Set<XtmTopic> xtmTopics of XtmTopicMap; Should only be used by Constructor
	 * 
	 * @param tm TopicMap
	 */
	private void initXtmTopics(TopicMap tm){
		if(tm.getTopics()!=null){
			xtmTopics = new HashSet<XtmTopic>();
			Topic t;
			XtmTopic xtmT;
			int tId = 0;
			Iterator<Topic> tIt = tm.getTopics().iterator();
			while(tIt.hasNext()){
				t = tIt.next();
				xtmT = new XtmTopic(t);
				xtmT.setId("t"+tId++);//set id of XtmTopic
				xtmTopics.add(xtmT);
			}
		}		
	}
	
	/**
	 * initXtmAssociations - inits Set<XtmAssociation> xtmAssociation of XtmTopicMap; Should only be used by Constructor
	 * 
	 * @param tm TopicMap
	 */
	private void initXtmAssociations(TopicMap tm){
		if(tm.getAssociations()!=null){
			xtmAssociations = new HashSet<XtmAssociation>();
			Iterator<Association> aIt = tm.getAssociations().iterator();
			while(aIt.hasNext())
				xtmAssociations.add(new XtmAssociation(aIt.next()));
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
	 * setReifierReference - searches reifierUri in XtmItemIdentifiers of XtmTopics in this XtmTopicMap. If a XtmTopic 
	 * which has an XtmItemIdentifier with the specified Uri is found, the Reifier reference of TopicMap is set and furthermore reified of Topic is set
	 * 
	 * @param reifierUri
	 */
	private void setReifierReference(String reifierUri){
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
	 * setTopicValues sets for every XtmTopic of XtmTopicMap the Topic values
	 *
	 */
	private void setTopicValues(){
		if(xtmTopics!=null){
			Iterator<XtmTopic> it = xtmTopics.iterator();
			while(it.hasNext()){
				it.next().setTopicValues(this);
			}
			this.setTopics(castXtmTopics());
		}
	}
	
	/**
	 * get XtmTopics of XtmTopic map as ce.tm4scholion.tm.Topics
	 * 
	 * @return a Set of ce.tm4scholion.tm.Topics, null if XtmTopicMap has no XtmTopics
	 */
	private Set<Topic> castXtmTopics(){
		if(xtmTopics!=null){
			Set<Topic> topics = new HashSet<Topic>();
			Iterator<XtmTopic> it = xtmTopics.iterator();
			while(it.hasNext()){
				topics.add(it.next());
			}
			return topics;
		}
		return null;
	}
	
	/**
	 * setAssociationValues sets for every XtmAssociation of XtmTopicMap the Association values
	 *
	 */
	private void setAssociationValues(){
		if(xtmAssociations!=null){
			Iterator<XtmAssociation> it = xtmAssociations.iterator();
			while(it.hasNext()){
				it.next().setAssociationValues(this);
			}
			this.setAssociations(castXtmAssociations());
		}
	}
	
	/**
	 * get XtmAssociations of XtmTopic map as ce.tm4scholion.tm.Associations
	 * 
	 * @return a Set of ce.tm4scholion.tm.Associations, null if XtmTopicMap has no XtmAssociations
	 */
	private Set<Association> castXtmAssociations(){
		if(xtmAssociations!=null){
			Set<Association> associations = new HashSet<Association>();
			Iterator<XtmAssociation> it = xtmAssociations.iterator();
			while(it.hasNext()){
				associations.add(it.next());
			}
			return associations;
		}
		return null;
	}
	
	/**
	 * setTopicMapValues should be called for a XtmTopicMap after creating a XtmTopicMap from a 
	 * xtm 2.0 file. values/references of XtmTopicMap elements are set.  
	 *    
	 */
	public void setTopicMapValues(){
		setFirstItemIdentifierValue();
		setItemIdentifierValues();
		if(reifierUri!=null)
			setReifierReference(reifierUri);
		setXtmScopes();
		setTopicReferencesOfXtmScopes();
		
		setTopicValues();
		setAssociationValues();
//		setXtmScopes();
//		setTopicReferencesOfXtmScopes();
	}
	
	/**
	 * setXtmScopes sets the correct XtmScope references for XtmTopicNames, XtmVariants, XtmOccurrences and XtmAssociations. Additionally the statemenets
	 * (XtmTopicNames, XtmVariants, XtmOccurrences, XtmAssociations) are added to the XtmScopes. This method should only be used by setTopicMapValues().
	 * 
	 */
	private void setXtmScopes(){
//		int scopeCount = 0;
		if(xtmTopics!=null){
			Iterator<XtmTopic> tIt = xtmTopics.iterator();
			while(tIt.hasNext()){//XtmTopics
				XtmTopic topic = tIt.next();
				if(topic.getXtmTopicNames()!=null){
					Iterator<XtmTopicName> tNIt = topic.getXtmTopicNames().iterator();
					//set XtmScopes of XtmTopicNames in XtmTopic
					while(tNIt.hasNext()){
						XtmTopicName topicName = tNIt.next();
						if(topicName.getXtmScope()!=null){
							XtmScope tNScope = xtmScopesContain(topicName.getXtmScope().getTopicRefUris());
							if(tNScope==null){
//								++scopeCount;
								addScope(topicName.getXtmScope());
//								topicName.getXtmScope().addStatment(topicName);
							}else{
								topicName.setXtmScope(tNScope);
								tNScope.addStatement(topicName);
							}
						}
						if(topicName.getXtmVariants()!=null){
							Iterator<XtmVariant> vIt = topicName.getXtmVariants().iterator();
							//set XtmScopes of XtmVariants in XtmTopicName
							while(vIt.hasNext()){
								XtmVariant variant = vIt.next();
								if(variant.getXtmScope()!=null){
									XtmScope varScope = xtmScopesContain(variant.getXtmScope().getTopicRefUris());
									if(varScope==null){
//										++scopeCount;
										addScope(variant.getXtmScope());
//										variant.getXtmScope().addStatment(variant);
									}else{
										variant.setXtmScope(varScope);
										varScope.addStatement(variant);
									}
								}
							}	
						}	
					}
				}
				if(topic.getXtmOccurrences()!=null){
					Iterator<XtmOccurrence> oIt = topic.getXtmOccurrences().iterator();
					//set XtmScopes of XtmOccurrences of XtmTopic
					while(oIt.hasNext()){
						XtmOccurrence o = oIt.next();
						if(o.getXtmScope()!=null){
							XtmScope occScope = xtmScopesContain(o.getXtmScope().getTopicRefUris());
							if(occScope==null){
//								++scopeCount;
								addScope(o.getXtmScope());
//								o.getXtmScope().addStatment(o);
							}else{
								o.setXtmScope(occScope);
								occScope.addStatement(o);
							}
						}
					}
				}
			}
		}
		if(xtmAssociations!=null){
			Iterator<XtmAssociation> aIt = xtmAssociations.iterator();
			//set XtmScopes of XtmAssociations of XtmTopics 
			while(aIt.hasNext()){
				XtmAssociation a = aIt.next();
				if(a.getXtmScope()!=null){
					XtmScope assScope = xtmScopesContain(a.getXtmScope().getTopicRefUris());
					if(assScope==null){
//						++scopeCount;
						addScope(a.getXtmScope());
//						a.getXtmScope().addStatment(a);
					}else{
						a.setXtmScope(assScope);
						assScope.addStatement(a);
					}
				}
			}
		}
//		System.out.println("Scope count = " +scopeCount);
	}
	
	/**
	 * setTopicReferences of XtmScopes sets the Topic References of the XtmScopes of this XtmTopicMap. This method should only be called by 
	 * setTopicMapValues after setXtmScopes
	 *
	 */
	private void setTopicReferencesOfXtmScopes(){
		if(xtmScopes!=null){
			Iterator<XtmScope> it = xtmScopes.iterator();
			while(it.hasNext()){
				it.next().setScopeValues(this);
			}
		}
	}
	
	/**
	 * xtmScopesContain
	 * 
	 * @param topicRefUris
	 * @return true if a XtmScope in XtmScopes has a Set<String> topicRefUris which has the same elements as the input parameter
	 */
	private XtmScope xtmScopesContain(Set<XtmTopicRef> topicRefUris){
		if(xtmScopes!=null){
			Iterator<XtmScope> it = xtmScopes.iterator();
			XtmScope s =null;
			while(it.hasNext()){
				s = it.next();
				if(s.compareTopicRefUris(topicRefUris))
					return s;
			}
		}
		return null;
	}
	
	/**
	 * addScope adds an XtmScope to Set<XtmScope> xtmScopes
	 * 
	 * @param s
	 */
	private void addScope(XtmScope s){
		if(xtmScopes==null)
			xtmScopes = new HashSet<XtmScope>();
		xtmScopes.add(s);
	}
	
//getter and setter//
	
	/**
	 * getReifierUri of XtmTopicMap
	 * 
	 * @return a String which represents the URI of the Reifier
	 */
	public String getReifierUri() {
		return reifierUri;
	}

	/**
	 * setReifierUri of XtmTopicMap
	 * 
	 * @param reifierUri
	 */
	public void setReifierUri(String reifierUri) {
		this.reifierUri = reifierUri;
	}

	/**
	 * getVersion of XtmTopicMap
	 * 
	 * @return a String which represents the version of the TopicMap
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * setVersion of XtmTopicMap
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * getXtmAssociations of XtmTopicMap
	 * 
	 * @return the set of XtmAssociations of XtmTopicMap 
	 */
	public Set<XtmAssociation> getXtmAssociations() {
		return xtmAssociations;
	}

	/**
	 * setXtmAssociations of XtmTopicMap
	 * 
	 * @param xtmAssociations
	 */
	public void setXtmAssociations(Set<XtmAssociation> xtmAssociations) {
		this.xtmAssociations = xtmAssociations;
	}

	/**
	 * getXtmTopics of XtmTopicMap
	 * 
	 * @return the set of XtmTopics of XtmTopicMap
	 */
	public Set<XtmTopic> getXtmTopics() {
		return xtmTopics;
	}

	/**
	 * setXtmTopics sets XtmTopics of XtmTopicMap
	 * 
	 * @param xtmTopics
	 */
	public void setXtmTopics(Set<XtmTopic> xtmTopics) {
		this.xtmTopics = xtmTopics;
	}
	
	/**
	 * getItemIdentifiers of XtmTopicMap
	 * 
	 * @return the set of XtmItemIdentities of XtmTopicMap
	 */
	public Set<XtmItemIdentity> getXtmItemIdentifiers() {
		return xtmItemIdentifiers;
	}
	
	/**
	 * setXtmItemIdentifiers of XtmTopicMap
	 * 
	 * @param xtmItemIdentifiers
	 */
	public void setXtmItemIdentifiers(Set<XtmItemIdentity> xtmItemIdentifiers) {
		this.xtmItemIdentifiers = xtmItemIdentifiers;
	}
	
	
	//Test
	public Set<XtmScope> getXTMScopes(){
		return xtmScopes;
	}
}
