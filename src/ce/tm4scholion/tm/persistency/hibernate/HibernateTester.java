package ce.tm4scholion.tm.persistency.hibernate;

import ce.tm4scholion.metamodel.Manager;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.TopicMap;



public class HibernateTester {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		try{
			TMHibernatePersistency tmh = new TMHibernatePersistency();
			Manager manager= new Manager();
			Topic t = manager.getTMManager().addTopic("Georg TestTopic");
			TopicMap tm2 = t.getParent();
			tmh.store(tm2);
			tmh.closeConnection();


			//String id = "urn:uuid:6a45efa8-9a72-48e4-b17e-5b179af4a596";
			tmh=new TMHibernatePersistency();
			tmh.connect(tm2.getFirstItemIdentifier());
			TopicMap tm = tmh.retrieve();
			System.out.println("TopicMapID: " + tm.getFirstItemIdentifier());
			System.out.println("Anzahl Topics: "+ tm.getTopics().size());
			tmh.closeConnection();
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
		}
	}

}
