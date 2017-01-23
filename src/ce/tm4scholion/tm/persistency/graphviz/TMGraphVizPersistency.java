package ce.tm4scholion.tm.persistency.graphviz;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ce.tm4scholion.tm.Association;
import ce.tm4scholion.tm.AssociationRole;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.TopicMap;
import ce.tm4scholion.tm.persistency.TMPersistency;

/**
 * TMGraphVizPersistency - a simple output formatter for topic maps using the
 * persistency interface. Outputs the topicmap (topics and associations) as a
 * graph in graphviz-format (.dot) Does not feature input capabilities as
 * graphviz format is not able to represent every information contained in the
 * topic map.
 * 
 * @author oppl
 * 
 */
public class TMGraphVizPersistency implements TMPersistency {

	private String path;

	private String nl = System.getProperty("line.separator");

	public TMGraphVizPersistency() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * connect to the file, in which the TopicMap be stored in
	 * 
	 * @param connection
	 *            a string which represents the path of an file
	 * @return always true, as actual connection is only established when
	 *         storing
	 */
	public boolean connect(String connection) {
		System.out.println("connected to " + connection);
		path = connection.replace(' ', '_');
		return true;
	}

	/**
	 * not implemented in this plugin - file does not contain all information to
	 * fully reconstruct the topic map
	 */
	public TopicMap retrieve() {
		// not possible with this persistency module
		return null;
	}

	/**
	 * renders a whole topic map to GraphViz's dot-format and stores the result
	 * in the connected file.
	 * 
	 * @param tm
	 *            the topic map to be stored
	 * @return true if topic map was sucessfully stored, false otherwise
	 */
	public boolean store(TopicMap tm) {
		if (path == null)
			return false;
		BufferedWriter w;
		try {
			w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					path)));
			w.write("graph tm {" + nl);
			w.write(labelTopics(tm.getTopics()));
			w.write(labelAssocs(tm.getAssociations()));
			w.write(generateLinks(tm.getAssociations()));
			w.write("}");
			w.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * renders the context of a given element to GraphViz's dot-format. The
	 * context are all directly associated elements. The result is stored in the
	 * connected file.
	 * 
	 * @param name
	 *            the name of the graph to be stored (e.g. the name of the
	 *            element)
	 * @param element
	 *            the element to be stored
	 * @return true if topic map was sucessfully stored, false otherwise
	 */
	public boolean storeElement(String name, Topic element) {
		if (path == null)
			return false;
		BufferedWriter w;
		Set<Association> assocs = (new HashSet<Association>(element
				.getAssociatedAssociations().values()));
		try {
			w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					path)));
			w.write("graph " + name + " {" + nl + "edge [len=\"2.00\"];");
			Iterator<Association> i = assocs.iterator();
			Set<Topic> topics = new HashSet<Topic>();
			while (i.hasNext())
				topics.addAll(i.next().getAssociatedTopics().values());
			topics.remove(element);
			w.write(labelTopics(topics));
			w.write("node [shape=box,style=filled,fillcolor=yellow]; node [label=\""
							+ element.getTopicNames().iterator().next()
									.getValue()
							+ "\", URL=\"");
			if (element.getOccurrences() != null) 
				w.write(element.getOccurrences().iterator().next().getValue());
			w.write("\", target=\"_blank\"] \"" + element.getFirstItemIdentifier() + "\";" + nl);
			w.write(labelAssocs(assocs));
			w.write(generateLinks(assocs));
			w.write("}");
			w.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * outputs the names of the elements in the dot-format. Takes the topics
	 * representing the elements and uses their topic-name-elements to retrieve
	 * the respective names.
	 * 
	 * @param topics
	 *            the topics to be analysed
	 * @return the result in dot-format
	 */
	private String labelTopics(Collection<Topic> topics) {
		if (topics == null)
			return "";
		StringWriter result = new StringWriter();
		result.write("  node [shape=none] \" \""+nl+"  node [shape=box];" + nl);
		Iterator<Topic> i = topics.iterator();
		while (i.hasNext()) {
			Topic t = i.next();
			if (t.getTopicNames() != null) {
				String name = t.getTopicNames().iterator().next().getValue();
				result.write("  node [label=\""
						+ name
						+ "\"");
				result.write(", URL=\"./"+name.replace(' ', '_')+".html\"");
				result.write("] \"" + t.getFirstItemIdentifier() + "\";" + nl);
			// else result.write(" node
			// [label=\""+t.getFirstItemIdentifier()+"\"]
			// \""+t.getFirstItemIdentifier()+"\"; "+nl);
			}
		}
		result.write(nl);
		return result.toString();
	}

	/**
	 * outputs the names of the associations in the dot-format. Takes the
	 * association elements and uses the topic-name-elements of their type to
	 * retrieve the respective names.
	 * 
	 * @param assocs
	 *            the associations to be analysed
	 * @return the result in dot-format
	 */
	private String labelAssocs(Collection<Association> assocs) {
		if (assocs == null)
			return "";
		StringWriter result = new StringWriter();
		result
				.write("node [shape=ellipse, fontsize=12, fillcolor=white, URL=\"\"];"
						+ nl);
		Iterator<Association> i = assocs.iterator();
		while (i.hasNext()) {
			Association a = i.next();
			if (a.getType().getTopicNames() == null)
				result.write("  node [label=\""
						+ a.getType().getFirstItemIdentifier() + "\"] \""
						+ a.getFirstItemIdentifier() + "\";" + nl);
			else
				result.write("  node [label=\""
						+ a.getType().getTopicNames().iterator().next()
								.getValue() + "\"] \""
						+ a.getFirstItemIdentifier() + "\";" + nl);
		}
		result.write(nl);
		return result.toString();
	}

	/**
	 * links elements with associations and adds the respective role labels.
	 * Uses the topic-name-elements of the association-role types to retrieve
	 * the respective names.
	 * 
	 * @param assocs
	 *            the associations to be analysed
	 * @return the result in dot-format
	 */
	private String generateLinks(Collection<Association> assocs) {
		if (assocs == null)
			return "";
		StringWriter result = new StringWriter();
		result.write(nl);
		Iterator<Association> i = assocs.iterator();
		while (i.hasNext()) {
			Association a = i.next();
			Map<AssociationRole, Topic> assocTopics = a.getAssociatedTopics();
			Iterator<AssociationRole> u = assocTopics.keySet().iterator();
			while (u.hasNext()) {
				AssociationRole ar = u.next();
				if (ar.getType().getTopicNames() == null)
					result.write("  \"" + a.getFirstItemIdentifier()
							+ "\" -- \""
							+ assocTopics.get(ar).getFirstItemIdentifier()
							+ "\" [fontsize=10, label=\""
							+ ar.getFirstItemIdentifier() + "\"];" + nl);
				else
					result.write("  \""
							+ a.getFirstItemIdentifier()
							+ "\" -- \""
							+ assocTopics.get(ar).getFirstItemIdentifier()
							+ "\" [fontsize=10, label=\""
							+ ar.getType().getTopicNames().iterator().next()
									.getValue() + "\"];" + nl);
			}
		}

		result.write(nl);
		return result.toString();
	}
}
