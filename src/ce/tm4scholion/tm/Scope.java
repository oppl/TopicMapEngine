package ce.tm4scholion.tm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * TopicMap Engine - Scope Scopes are sets of Topics which span across a set of
 * subjects (context), in which certain Statements are valid. Scopes are no
 * defined construct of the TMDM standard but are used to describe the validity
 * of Statements and have been introduced as a class for convenience. Scopes by
 * definition do not have a designator. For reasons of adressablity, this
 * implementation allows to define names for scopes, which are represented in a
 * TMDM-compliant way using a Topic of a specific type (ScopeNameTopicType).
 *
 * @author oppl
 */
public class Scope {

	private Set<Topic> context;

	private String id;

	private Topic name;

	private Set<Statement> contents;

	/**
	 * default constructor, used to construct a new Scope
	 *
	 */
	public Scope() {
		this.id = "urn:subject:s"+Utils.getUniqueItemIdentifier();
	}

	/**
	 * add a Statement to the Scope, thus defining it to be valid within the
	 * Scope
	 *
	 * @param s
	 *            the Statement to be added
	 */
	protected void addStatement(Statement s) {
		if (contents == null)
			contents = new HashSet<Statement>();
		contents.add(s);
	}

	/**
	 * get the set of Statements which are valid in this Scope
	 *
	 * @return the set of Statements which are valid in this Scope
	 * @uml.property name="contents"
	 */
	public Set<Statement> getContents() {
		if (contents == null){
			contents = new HashSet<Statement>();
		}
		return contents;
	}
	/**
	 * set the set of Statements in this Scope
	 * @param contents
	 * @uml.property name="contents"
	 */
	public void setContents(Set<Statement> contents){
		this.contents=contents;
	}

	/**
	 * remove a Statement from the Scope, thus defining it not be known to be
	 * valid anymore within the Scope
	 *
	 * @param s
	 *            the Statement to be removed
	 */
	protected void removeStatement(Statement s) {
		contents.remove(s);
	}

	/**
	 * add a Topic to the Scope, thus defining/extending the context in which
	 * Statements contained in this Scope are valid
	 *
	 * @param t
	 *            the Topic to be added
	 */
	public void addToContext(Topic t) {
		if (context == null)
			context = new HashSet<Topic>();
		context.add(t);
	}

	/**
	 * set the Topics defining the Scope's validity context as a whole
	 *
	 * @param context
	 *            the set of Topics which determine the Scope's validitiy
	 *            context
	 * @uml.property name="context"
	 */
	public void setContext(Set<Topic> context) {
		this.context = context;
	}

	/**
	 * get the set of Topics determining the Scope's validity context
	 *
	 * @return the set of Topics determining the Scope's validity context
	 * @uml.property name="context"
	 */
	public Set<Topic> getContext() {
		return context;
	}

	/**
	 * remove a Topic from the Scope, thus restricting the context in which
	 * Statements contained in this Scope are valid
	 *
	 * @param t
	 *            the Topic to be removed
	 */
	public void removeFromContext(Topic t) {
		context.remove(t);
	}

	/**
	 * get the name of the Scope
	 *
	 * @return the name of the Scope
	 * @uml.property name="name"
	 */
	public Topic getName(){
		return name;
	}
	
	/**
	 * get the ID of the Scope
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * set the ID of the Scope
	 * @param id
	 */
	public void setId(String id){
		this.id = id;
	}

	/**
	 * set the Topic containing the designator of this Scope
	 * @param t the Topic containing the designator of this Scope
	 */
	protected void setName(Topic t) {
		name = t;
	}

	/**
	 * gets the names assigned to this Scope in a Set of Strings
	 * @return the names assigned to this Scope
	 */
	public Set<String> getScopeNames() {
		if (name == null) return null;
		Set<TopicName> namingTopicNames = name.getTopicNames();
		Set<String> names = new HashSet<String>();
		Iterator<TopicName> i = namingTopicNames.iterator();
		while (i.hasNext()) names.add(i.next().getValue());
		return names;
	}

	@Override
	public boolean equals(Object arg0) {
		if (!(arg0 instanceof Scope))
			return false;
		Scope s = (Scope) arg0;
		if (((this.contents == null) && (s.contents != null)) ||
			((this.contents == null) && (s.contents != null)))
			return false;
		if (((this.context == null && s.context == null) || this.context
				.containsAll(s.getContext()))
				&& ((this.contents == null && s.contents == null) || this.contents
						.containsAll(s.getContents())))
			return true;
		return false;
	}

}
