package ce.tm4scholion.tm.persistency.xtm;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;


import ce.tm4scholion.tm.Manager;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.TopicMap;


public class XtmTestDriver {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//ce.tm4scholion.metamodel.Manager mMManager = new ce.tm4scholion.metamodel.Manager();
		
		/*Topic mn = mMManager.addMetamodelElement("ManeWorld");
		Set <String> resourceTypes = new HashSet<String>();
		resourceTypes.add("FUEL");
		
		
		mMManager.addResourceTypesForElement(mn, resourceTypes);
		
		Set <String> concreteRoles = new HashSet<String>();
		concreteRoles.add("SUPER_95");
		concreteRoles.add("SUPER_100");
		mMManager.concretizeRole("FUEL", concreteRoles);
		
		Topic elem = mMManager.instantiateElement("myWorldToday", mn);
		mMManager.addResource(elem, "60L", "SUPER_100",null);*/
		
		/*LearningUnit lu = mMManager.getLearningManager().generateLearningUnit("ManeLU");
		
		Block b = mMManager.getLearningManager().generateBlock("ManeBlock");
		Block subB = mMManager.getLearningManager().generateBlock("ManeSubBlock");
		Vector<Block> vB = new Vector<Block>();
		vB.add(subB);
		
		b.setContainedBlocks(vB, lu);
		b.addContent(lu, "LOD1", "This is my Test Content!!");
		//lu.toTopicMap();
		b.toTopicMap(lu);		
		
		Topic t = mMManager.getTMManager().addTopic("ManeTopic");*/
		/*TopicMap tm = mn.getParent();
		XtmTopicMap xtmTm = new XtmTopicMap(tm);
		*/
		/*Manager tmManager = new Manager();
		Topic t = tmManager.addTopic("ManeTopic");
		TopicMap tm = t.getParent();
		XtmTopicMap xtmTm = new XtmTopicMap(tm);*/
		
		TMxtmPersistency xtmP = new TMxtmPersistency();
		xtmP.connect("C:\\Dokumente und Einstellungen\\Matthias\\Desktop\\Test\\TMxtmResult.xml");
		//xtmP.store(xtmTm);
		//System.out.println("TopicMap gespeichert!");
		//xtmP.validateTMxmlAgainstXTMSchema(xtmTm);
		//System.out.println("Dokument valid: "+ xtmP.validateTMxmlAgainstXTMSchema());
		
		//XtmTopicMap xtmTM = (XtmTopicMap)xtmP.retrieve();
		//System.out.println("ItemIdentity von TM: "+xtmTM.getXtmItemIdentifiers().iterator().next().getItemIdentity());
		//System.out.println(xtmTM.getItemIdentifiers().iterator().next());
		//xtmP.connect("E:\\eclipse_workspace\\tm4scholion\\src\\ce\\tm4scholion\\tm\\xtmpersistency\\TMxtmResult.xml");
		//System.out.println("XtmTopicMap gespeichert: "+xtmP.store(xtmTm));
		
		
	}

}
