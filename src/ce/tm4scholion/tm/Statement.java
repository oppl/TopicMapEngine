package ce.tm4scholion.tm;

/**
 * TopicMap Engine - Statement Statement are no defined construct of the TMDM standard but are described as all constructs of a TopicMap which can have a Scope in which they are valid. According to the TMDM, Statements are Assocations, AssociationRoles, Occurrences, TopicNames, TopicNames and Variants.
 * @author  oppl
 */
public abstract class Statement extends Reifiable {

	protected Scope scope;

	/**
	 * basic constructor
	 *
	 * @param itemIdentifier
	 *            has to be unique for the whole TopicMap
	 */
	public Statement(String itemIdentifier) {
		super(itemIdentifier);
		// TODO Auto-generated constructor stub
	}

	/**
	 * set the Scope for the Statement
	 * @param s  the Scope to be set for this Statement
	 * @uml.property  name="scope"
	 */
	public void setScope(Scope s) {
/*		if (s == null)
			s.removeStatement(this);*/
		scope = s;
		if(s!=null)
			s.addStatement(this);		
	}

	/**
	 * get the Scope of the Statement
	 * @return  the Scope of the Statement
	 * @uml.property  name="scope"
	 */
	public Scope getScope() {
		return scope;
	}

}
