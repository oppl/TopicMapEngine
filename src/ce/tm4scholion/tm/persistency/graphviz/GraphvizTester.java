package ce.tm4scholion.tm.persistency.graphviz;

import ce.tm4scholion.metamodel.Manager;
import ce.tm4scholion.tm.TopicMap;
import ce.tm4scholion.tm.persistency.xtm.TMxtmPersistency;

public class GraphvizTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			TMGraphVizPersistency tmg = new TMGraphVizPersistency();
			TMxtmPersistency tmx = new TMxtmPersistency();
			tmg.connect("/Users/oppl/Desktop/test.dot");
			tmx.connect("/Users/oppl/Desktop/TMResult.xml");
			TopicMap tm = tmx.retrieve();
			tmg.store(tm);
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
		}

	}

}
