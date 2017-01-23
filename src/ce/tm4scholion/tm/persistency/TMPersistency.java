package ce.tm4scholion.tm.persistency;

import ce.tm4scholion.tm.TopicMap;

/**
 * Topic Map Engine - Persistency
 * 
 * This interface is used to provide generic access to different implementations
 * for making a Topic Map persistent.
 * 
 * @author oppl
 * 
 */
public interface TMPersistency {

	/**
	 * connect to the data source/sink, from which the TopicMap should be
	 * retrieved or should be stored in
	 * 
	 * @param connection
	 *            a string adressing the data source/sink to connect to.
	 *            Concrete format is specified by implementations implementing
	 *            this interface.
	 * @return true, if successfully connected, false otherwise
	 */
	public boolean connect(String connection);

	/**
	 * store the given TopicMap into the connected data sink
	 * 
	 * @param tm
	 *            the TopicMap to be stored
	 * @return true, if storing was successful, false otherwise
	 */
	public boolean store(TopicMap tm);

	/**
	 * retrieve a TopicMap from the connected data source
	 * 
	 * @return the retrieved TopicMap
	 */
	public TopicMap retrieve();

}
