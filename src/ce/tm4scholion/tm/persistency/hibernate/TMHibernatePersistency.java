package ce.tm4scholion.tm.persistency.hibernate;


import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import ce.tm4scholion.tm.TopicMap;
import ce.tm4scholion.tm.persistency.TMPersistency;

/**
 *
 * @author gema
 *
 */

public class TMHibernatePersistency implements TMPersistency {


	private static SessionFactory sessionFactory = null;
	private static Configuration conf = null;
	private Session session = null;

	private String topicMapID = null;
	
	/**
	 * constructor, sets the configuration, sessionFactory and opens a Session to connect with the Database
	 *
	 */
	public TMHibernatePersistency(){
		conf = new Configuration().configure();
		sessionFactory = conf.buildSessionFactory();
		session = sessionFactory.openSession();
	}
	
	/**
	 * sets the value for the topicMapID of the TopicMap should be retrieved from the Database
	 * @param topicMapID
	 * 					is the firstItemIdentifier of a TopicMap
	 */
	public boolean connect(String topicMapID){
		boolean succ = false;
		if(topicMapID!=null){
			this.topicMapID = topicMapID;
			succ=true;
		}
		return succ;
	}
	
	/**
	 * close all Sessions and the sessionfactory of the Hibernate Connection
	 * @return true if the connection is successfully closed
	 */
	public boolean closeConnection(){
		try{
			sessionFactory.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return sessionFactory.isClosed();
	}
	
	/**
	 * saves a topicMap into a Database with Hibernate
	 * @param tm
	 * 			represents the TopicMap to be saved
	 */
	public boolean store(TopicMap tm){
		boolean succ = false;
		try{
				Transaction t = null;
				t = session.beginTransaction();
				Query q = session.createSQLQuery("SELECT id FROM topicmap where id=(SELECT id FROM itemidentifiers i where i.id='" + tm.getFirstItemIdentifier() + "')");
				List l = q.list();
				q.list();

				Iterator it = l.iterator();
				if(it.hasNext()){
					String id = (String)it.next();
					tm.setFirstItemIdentifier(id);
					session.saveOrUpdate(tm);
				}
				else{
					session.saveOrUpdate(tm);
				}
				t.commit();

			succ=true;
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return succ;
	}
	
	/**
	 * retrieves the TopicMap with the given itemIdentifier topicMapID which is set in the connect(String string) method
	 * @return tm
	 */
	public TopicMap retrieve(){
		TopicMap tm = null;

		try{
			if(topicMapID!=null){
				String itemIdentifier;
				Query q = session.createSQLQuery("SELECT id FROM topicmap where id=(SELECT id FROM itemidentifiers i where i.id='" + topicMapID + "')");				
				List l = q.list();
				Iterator it = l.iterator();
				if (it.hasNext()){
					itemIdentifier = (String)it.next();
					tm = (TopicMap) session.get(TopicMap.class, itemIdentifier);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return tm;
	}

}
