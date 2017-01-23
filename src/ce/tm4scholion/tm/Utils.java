package ce.tm4scholion.tm;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import com.hp.hpl.jena.util.FileUtils;

/**
 * TopicMap Engine - Utils
 * 
 * A collection of service routines for internal engine use (not accessible by
 * applications using the engine). Mainly used for type checking and generation
 * of unique IDs. The only items which are accessible externaly are the fields
 * defining the qualifiers for the default dataTypes given in the TMDM (String,
 * IRI, XML)
 * 
 * @author oppl
 * 
 */
public class Utils {

	/**
	 * the qualifier of the default dataType 'String'
	 */
	public static final String dtString = "http://www.w3.org/2001/XMLSchema#string";

	/**
	 * the qualifier of the default dataType 'IRI'
	 */
	public static final String dtIRI = "http://www.w3.org/2001/XMLSchema#anyURI";

	/**
	 * the qualifier of the default dataType 'XML'
	 */
	public static final String dtXML = "http://www.w3.org/2001/XMLSchema#anyType";

	/**
	 * generates an URI representing an unique ID using Java's internal
	 * UUID-generation routines (available since Java 5.0)
	 * 
	 * @return an URI of type UUID representing an unique ID
	 */
	protected static String getUniqueItemIdentifier() {
		return new String("urn:uuid:" + UUID.randomUUID().toString());
	}

	/**
	 * checks if a String is a Locator as defined in the TMDM standard (i.e. if
	 * the String is an URI). Actually, a Locator format is not explicitly
	 * specified in TMDM but is considered to be a URI in this implementation
	 * based on the IRI definition in 'datatypes'. Makes use of a routine of the
	 * JENA Semantic Web Framework.
	 * 
	 * @param s
	 *            the String to be checked
	 * @return true, if the String is an URI, false otherwise
	 */
	protected static boolean isLocator(String s) {
		return FileUtils.isURI(s);
	}

	/**
	 * checks if a String holds the URI of a default datatype as defined in the
	 * TMDM standard (i.e. a String, a IRI or XML).
	 * 
	 * @param s
	 *            the String to be checked
	 * @return true if the String holds the URI of a default datatype, false
	 *         otherwise
	 */
	protected static boolean isDefaultDataType(String s) {
		if (s.compareTo(dtString) == 0 || // String
				s.compareTo(dtIRI) == 0 || // IRI
				s.compareTo(dtXML) == 0) // XML
			return true;
		return false; // other datatypes may also be used
	}

	/**
	 * checks if a String holds the URI of the 'String' datatype as defined in
	 * the TMDM standard.
	 * 
	 * @param s
	 *            the String to be checked
	 * @return true if the String holds the URI of the 'String'-datatype, false
	 *         otherwise
	 */
	protected static boolean isStringDataType(String s) {
		if (s.compareTo(dtString) == 0) // String
			return true;
		return false; // other datatypes may also be used
	}

	/**
	 * checks if a String holds the URI of the 'IRI' datatype as defined in the
	 * TMDM standard.
	 * 
	 * @param s
	 *            the String to be checked
	 * @return true if the String holds the URI of the 'IRI'-datatype, false
	 *         otherwise
	 */
	protected static boolean isIRIDataType(String s) {
		if (s.compareTo(dtIRI) == 0) // IRI
			return true;
		return false; // other datatypes may also be used
	}

	/**
	 * checks if a String holds the URI of the 'XML' datatype as defined in the
	 * TMDM standard.
	 * 
	 * @param s
	 *            the String to be checked
	 * @return true if the String holds the URI of the 'XML'-datatype, false
	 *         otherwise
	 */
	protected static boolean isXMLDataType(String s) {
		if (s.compareTo(dtXML) == 0) // XML
			return true;
		return false; // other datatypes may also be used
	}

	/**
	 * checks if two sets of objects contain at least one common equal element.
	 * 
	 * @param s1
	 *            the first set to be compared
	 * @param s2
	 *            the second set to be compared
	 * @return true, if the two sets contain at least one common equal element,
	 *         false otherwise
	 */
	protected static boolean setsContainAtLeastOneEqualElement(Set s1, Set s2) {
		if (s1 == null) {
			if (s2 == null) return true;
			else return false;
		}
		if (s2 == null) return false;
		Iterator i = s1.iterator();
		while (i.hasNext()) {
			if (s2.contains(i.next()))
				return true;
		}
		return false;
	}

	/**
	 * checks if a set of objects is a superset of another set of objects.
	 * 
	 * @param s1
	 *            the assumed superset
	 * @param s2
	 *            the assumed subset
	 * @return true, if s1 is a superset of s2
	 */
	protected static boolean isSuperset(Set s1, Set s2) { // s1 is Superset of
		// s2
		Iterator i = s2.iterator();
		while (i.hasNext()) {
			if (!s1.contains(i.next()))
				return false;
		}
		return true;
	}

}
