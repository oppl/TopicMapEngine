package ce.tm4scholion.metamodel.communication;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import ce.tm4scholion.metamodel.Element;
import ce.tm4scholion.metamodel.Manager;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.Manager.RoleTopic;

public abstract class EntryContainer extends Element {

	protected Vector<Entry> entries;
	
	protected String data;
	
	public EntryContainer(String name, Manager mgr) {
		super(name, mgr);
		// TODO Auto-generated constructor stub
	}

	public EntryContainer(Topic myRep, Manager mgr) {
		super(myRep, mgr);
		// TODO Auto-generated constructor stub
	}

	public void addDescription(String data) {
		this.data = data;
	}
	
	public Vector<Entry> getEntries() {
/*		Set<Entry> entries = new HashSet<Entry>();
		Set<Topic> entryTopics = mgr.getTMManager().getCounterpartTopics(myRep, mgr.getMetamodelElement("Superordinate"), mgr.getMetamodelElement("Subordinate"));
		Iterator<Topic> i = entryTopics.iterator();
		while (i.hasNext()) {
			Topic entryTopic = i.next();
//			Entry entry = new Entry(entryTopic, mgr);
		}*/
		return entries;
	}
	
	
	public void setEntries(Vector<Entry> entries) {
		this.entries = entries;
	}

	public void addEntry(Entry entry) {
		this.entries.add(entry);
	}
	
	public void toTopicMap() {
		super.toTopicMap();
		if (data != null) mgr.addResource(myRep, data, "Description", null);
		if (entries != null) {
			Iterator<Entry> i = entries.iterator();
			Set<RoleTopic> rooms = new HashSet<RoleTopic>();
			while (i.hasNext()) {
				Entry e = i.next();
				e.toTopicMap();
				rooms.add(new RoleTopic("Subordinate", e.getRep()));
			}
			rooms.add(new RoleTopic("Superordinate", myRep));
			mgr.addAssociation("Nested", rooms, null);
		}
	}

}