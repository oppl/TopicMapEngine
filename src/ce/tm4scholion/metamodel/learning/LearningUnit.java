package ce.tm4scholion.metamodel.learning;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import ce.tm4scholion.metamodel.Element;
import ce.tm4scholion.metamodel.Manager;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.Manager.RoleTopic;

public class LearningUnit extends Element {
	
	private Vector<Block> containedBlocks;
	
	public LearningUnit(String name, Manager mgr) {
		super(name, mgr);
		containedBlocks = null; 
	}

	public LearningUnit(Topic myRep, ce.tm4scholion.metamodel.Manager mgr) {
		super(myRep, mgr);
		containedBlocks = null;
	}

	public void setContainedBlocks(Vector<Block> blocks) {
		containedBlocks = blocks;
		Iterator<Block> i = blocks.iterator();
		while (i.hasNext()) i.next().setContentTypeInLearningUnit("Content", this);
	}
	
	public Vector<Block> getBlocks() {
		return containedBlocks;
	}

	public void toTopicMap() {
		super.toTopicMap();
//		mgr.getTMManager().setTypeInstance(myRep, mgr.getTMManager().getTopic("LearningUnit"));
		if (containedBlocks != null && containedBlocks.size() > 0) {
			Set<Topic> scope = new HashSet<Topic>();
			scope.add(myRep);
			Iterator<Block> i = containedBlocks.iterator();
			Set<RoleTopic> blocks = new HashSet<RoleTopic>();
			Block current = i.next();
			current.toTopicMap(this);
			blocks.add(new RoleTopic(current.getContentTypeInLearningUnit(this), current.getRep()));			
			while (i.hasNext()) {
				Block predecessor = current;
				current = i.next();
				current.toTopicMap(this);
				blocks.add(new RoleTopic(current.getContentTypeInLearningUnit(this), current.getRep()));
				Set<RoleTopic> sibling = new HashSet<RoleTopic>();
				sibling.add(new RoleTopic("Predecessor", predecessor.getRep()));
				sibling.add(new RoleTopic("Successor", current.getRep()));
				mgr.addAssociation("Sibling", sibling, scope);
			}
			blocks.add(new RoleTopic("Superordinate", myRep));
			blocks.add(new RoleTopic("FirstBlock", containedBlocks.firstElement().getRep()));
			mgr.addAssociation("Nested", blocks, scope);
		}
	}

}
