package ce.tm4scholion.metamodel;

import java.util.HashSet;
import java.util.Set;

import ce.tm4scholion.metamodel.common.Subject;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.Manager.RoleTopic;

/**
 * The Element class is the basic class for all content elements used in the OL
 * learning models. An element in minimum has a name and always may have an
 * author and/or an owner assigned. Every element is represented by exactly one
 * topic in the topic map (addressed using the myRep-field). In order to map the
 * element onto the topic map, it is however not sufficient to simply output
 * this topic, the contained associations and attached or embedded elements also
 * have to be considered. The method toTopicMap covers this functionality and
 * has to be extended for every actual element type implemented in the meta
 * models.
 * 
 * @author oppl
 * 
 */
public abstract class Element {

	protected ce.tm4scholion.metamodel.Manager mgr;

	protected String name;

	protected Subject author;

	protected Subject owner;

	protected Topic myRep;

	/**
	 * Constructor to reconstruct the element from an already existing topic map
	 * 
	 * @param myRep
	 *            the topic representing the element to be created
	 * @param mgr
	 *            the manager of the model this element is contained in
	 */
	public Element(Topic myRep, ce.tm4scholion.metamodel.Manager mgr) {
		this(myRep.getTopicNames().iterator().next().getValue(), mgr);
		// possibly further creation of elements
		this.myRep = myRep;
	}

	/**
	 * Constructor to create a new element from scratch
	 * 
	 * @param name
	 *            the name of the new element
	 * @param mgr
	 *            the manager of the model this element is contained in
	 */
	public Element(String name, ce.tm4scholion.metamodel.Manager mgr) {
		this.mgr = mgr;
		this.name = name;
		author = null;
		owner = null;
		myRep = null;
	}

	/**
	 * set the author of this element
	 * 
	 * @param author
	 *            the Subject element representing the author
	 */
	public void setAuthor(Subject author) {
		this.author = author;
	}

	/**
	 * set the owner of this element
	 * 
	 * @param owner
	 *            the Subject element representing the owner
	 */
	public void setOwner(Subject owner) {
		this.owner = owner;
	}

	/**
	 * returns the author of this element as a Subject object
	 * 
	 * @return the Subject element representing the author of this element
	 */
	public Subject getAuthor() {
		return author;
	}

	/**
	 * returns the name of the element
	 * 
	 * @return the name of the element
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns the owner of this element as a Subject object
	 * 
	 * @return the Subject element representing the owner of this element
	 */
	public Subject getOwner() {
		return owner;
	}

	/**
	 * return the topic used to represent this element. If the topic not yet
	 * exists, one is created.
	 * 
	 * @return the topic representing this element
	 */
	public Topic getRep() {
		if (myRep == null) {
			myRep = mgr.getTMManager().generateTopic(name);
		}
		return myRep;
	}

	/**
	 * map a default element to the underlying topic map. This includes mapping
	 * and attaching possibly available author and/or owner elements.
	 * 
	 */
	public void toTopicMap() {
		mgr.getTMManager().addTopic(getRep());
		if (author != null) {
			author.toTopicMap();
			Set<RoleTopic> authors = new HashSet<RoleTopic>();
			authors.add(new RoleTopic("Author", author.getRep()));
			authors.add(new RoleTopic("AuthoredElement", myRep));
			mgr.addAssociation("authors", authors, null);
		}
		if (owner != null) {
			owner.toTopicMap();
			Set<RoleTopic> authors = new HashSet<RoleTopic>();
			authors.add(new RoleTopic("Owner", owner.getRep()));
			authors.add(new RoleTopic("OwnedElement", myRep));
			mgr.addAssociation("owns", authors, null);
		}
	}

}
