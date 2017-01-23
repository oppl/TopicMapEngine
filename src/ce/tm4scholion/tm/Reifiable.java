package ce.tm4scholion.tm;

/**
 * TopicMap Engine - Reifiable Reifiable TopicMap constructs are constructs which may be further specified or detailed by attaching a topic to them. With reification, it is for example possible to add an occurrence to an association or to assign a name to an occurrence. A more complex use case would be the assignment of associations to a reifier to put it into relationship with other topics (or reifiers). Reifiable constructs are all standardized constructs except Topics themselves.
 * @author  oppl
 */
public abstract class Reifiable extends TopicMapConstruct {

	private Topic reifier;

	/**
	 * basic constructor
	 *
	 * @param itemIdentifier
	 *            has to be unique for the whole TopicMap
	 */
	public Reifiable(String itemIdentifier) {
		super(itemIdentifier);
		reifier = null;
	}

	/**
	 * get the reifing Topic of the reifiable construct. null if there is no reifier
	 * @return  the reifing Topic
	 * @uml.property  name="reifier"
	 */
	public Topic getReifier() {
		return reifier;
	}
	
	/**
	 * set the reifing Topic of the reifiable construct
	 * @param reifier  the Topic reifing the reifiable construct
	 * @uml.property  name="reifier"
	 */
	public void setReifier(Topic reifier) {
		this.reifier = reifier;
		if(reifier!=null)
			reifier.addSubjectIdentifier("urn:subject:reifier");
	}
}
