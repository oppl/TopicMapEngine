package ce.tm4scholion.visualizer;

import java.io.IOException;
import java.util.Vector;

import ce.tm4scholion.metamodel.Element;
import ce.tm4scholion.metamodel.Manager;
import ce.tm4scholion.metamodel.common.Course;
import ce.tm4scholion.metamodel.communication.forum.Discussion;
import ce.tm4scholion.metamodel.communication.forum.DiscussionTopic;
import ce.tm4scholion.metamodel.communication.forum.Forum;
import ce.tm4scholion.metamodel.learning.Block;
import ce.tm4scholion.metamodel.learning.LearningUnit;
import ce.tm4scholion.tm.TopicMap;
import ce.tm4scholion.tm.persistency.graphviz.TMGraphVizPersistency;
import ce.tm4scholion.tm.persistency.xtm.TMxtmPersistency;
import ce.tm4scholion.tm.persistency.xtm.XtmTopicMap;

/**
 * Visualizer uses Graphviz as a means to output semantic associations between
 * elements. In its main method, it establishes an exemplary structure of
 * content elements in order to demonstrate functionality of the approach.
 * 
 * @author oppl
 * 
 */
public class Visualizer {

	private TMGraphVizPersistency renderingHelper;
	private TMxtmPersistency xtmIO;

	private String basePath;

	public Visualizer(String basePath) {
		renderingHelper = new TMGraphVizPersistency();
		xtmIO = new TMxtmPersistency();
		this.basePath = basePath;
	}

	public void storeInXTM(TopicMap tm) {
		xtmIO.connect("/Users/oppl/Desktop/test.xtm");
		XtmTopicMap xtmTm = new XtmTopicMap(tm);
		xtmIO.store(xtmTm);
	}
	
	/**
	 * visualize the context (i.e. the directly associated elements) of a given
	 * element
	 * 
	 * @param e
	 *            the element to be visualized
	 */
	public void visualize(Element e) {
		renderingHelper.connect(basePath + "/" + e.getName() + ".dot");
		renderingHelper.storeElement(e.getName().replace(' ', '_'), e.getRep());
	}

	/**
	 * visualize a whole topic map (including all meta model elements)
	 * 
	 * @param tm
	 *            the topic map to be visualized
	 */
	public void visualize(TopicMap tm) {
		renderingHelper.connect(basePath + "/tm.dot");
		renderingHelper.store(tm);
	}

	/**
	 * Demonstrates the functionality of visualizer by establishing an exemplary
	 * structure of learning content and performing some visualizations.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Visualizer v = new Visualizer("/Users/oppl/Desktop/demo/nav");
		Manager m = new Manager();
		Course c1 = m.getCommonManager().generateCourse("Textbased Datamanagement");
		LearningUnit lu1 = m.getLearningManager().generateLearningUnit(
				"XML Basics");
		LearningUnit lu2 = m.getLearningManager().generateLearningUnit(
				"XML Structuring");
		Block b1 = m.getLearningManager().generateBlock("Introduction");
		Block b2 = m.getLearningManager().generateBlock("Basic Structure");
		Block b3 = m.getLearningManager().generateBlock("DTD");
		Block b4 = m.getLearningManager().generateBlock("Schema");
		Block b5 = m.getLearningManager().generateBlock("Structure Definition");
		Block b6 = m.getLearningManager().generateBlock("DTD Example");

		b1.addContent(lu1, "LOD2", "../content/b1.html");
		b2.addContent(lu1, "LOD2", "../content/b2.html");
		b3.addContent(lu1, "LOD2", "../content/b3.html");
		b4.addContent(lu1, "LOD2", "../content/b4.html");
		b5.addContent(lu1, "LOD2", "../content/b5.html");
		b6.addContent(lu1, "LOD2", "../content/b6.html");
		
		Forum f = m.getCommunicationManager().generateForum("XML Discussions");
		DiscussionTopic dt = m.getCommunicationManager()
				.generateDiscussionTopic("XML Specifics");
		Discussion d = m.getCommunicationManager().generateDiscussion(
				"Specify attributes");

		Vector<Block> blocks = new Vector<Block>();
		blocks.add(b1);
		blocks.add(b2);
		blocks.add(b5);
		lu1.setContainedBlocks(blocks);
		Vector<Block> blocks2 = new Vector<Block>();
		blocks2.add(b3);
		blocks2.add(b4);
		b5.setContainedBlocks(blocks2, lu1);

		Vector<Block> blocks3 = new Vector<Block>();
		blocks3.add(b5);
		blocks3.add(b6);
		lu2.setContainedBlocks(blocks3);

		Vector<LearningUnit> lus = new Vector<LearningUnit>();
		lus.add(lu1);
		lus.add(lu2);

		f.addDiscussionTopic(dt);
		dt.addDiscussion(d);

		m.getCommonManager().discuss(b5, d);

		c1.setLUs(lus);
		c1.addForum(f);

		c1.toTopicMap();

/*		v.visualize(b1);
		v.visualize(b2);
		v.visualize(b3);
		v.visualize(b4);
		v.visualize(b5);
		v.visualize(b6);
		v.visualize(f);
		v.visualize(dt);
		v.visualize(d);
		v.visualize(lu1);
		v.visualize(lu2);
		v.visualize(c1);*/
		
/*		try {
			Runtime.getRuntime().exec("/Users/oppl/Desktop/demo/generate.sh");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		v.visualize(m.getTMManager().getTopicMap());
		
//		v.storeInXTM(m.getTMManager().getTopicMap());
		
	}

}
