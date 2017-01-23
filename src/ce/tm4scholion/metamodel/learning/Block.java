package ce.tm4scholion.metamodel.learning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ce.tm4scholion.metamodel.Element;
import ce.tm4scholion.metamodel.common.Subject;
import ce.tm4scholion.tm.Topic;
import ce.tm4scholion.tm.Manager.RoleTopic;

public class Block extends Element {

	private Map<LearningUnit, Vector<Block>> containedBlocks;
	private Map<LearningUnit, String> contentTypeInScope;
	private Map<ContentScope, String> contentInScope;
	private Map<AnnotationScope, String> annotationInScope;
	
	public Block(Topic myRep, ce.tm4scholion.metamodel.Manager mgr) {
		super(myRep, mgr);
		contentTypeInScope = new HashMap<LearningUnit, String>();
		contentInScope = new HashMap<ContentScope, String>();
		annotationInScope = new HashMap<AnnotationScope, String>();
		containedBlocks = new HashMap<LearningUnit, Vector<Block>>();
	}

	public Block(String name, ce.tm4scholion.metamodel.Manager mgr) {
		super(name, mgr);
	}

	public void setContainedBlocks(Vector<Block> blocks, LearningUnit inScope) {
		containedBlocks.put(inScope, blocks);
		Iterator<Block> i = blocks.iterator();
		while (i.hasNext()) i.next().setContentTypeInLearningUnit("Content", inScope);
		
	}	
	
	public void addContent(LearningUnit lu, String lod, String data) {
		contentInScope.put(new ContentScope(lu,lod), data);
	}
	
	public String getContent(LearningUnit lu, String lod) {
		ContentScope rs = new ContentScope(lu,lod);
		return contentInScope.get(rs);
	}
	
	public void addAnnotation(LearningUnit lu, String lod, Subject author, String type, String data) {
		annotationInScope.put(new AnnotationScope(lu,lod, author, type), data);
/*		Set<Topic> scope = new HashSet<Topic>();
		scope.add(inScope.getRep());
		scope.add(author.getRep());
		mgr.addResource(myRep, data, type, scope);*/
	}

	public String getAnnotation(LearningUnit lu, String lod, Subject author, String type) {
		AnnotationScope rs = new AnnotationScope(lu,lod,author, type);
		return annotationInScope.get(rs);
	}

	
	public void setContentTypeInLearningUnit(String contentType, LearningUnit lu) {
		contentTypeInScope.put(lu, contentType);
	}
	
	public String getContentTypeInLearningUnit(LearningUnit lu) {
		return contentTypeInScope.get(lu);
	}
	
	public Vector<Block> getBlocks(LearningUnit lu) {
		return containedBlocks.get(lu);
	}
	
	public void toTopicMap() {
		super.toTopicMap();
//		mgr.getTMManager().setTypeInstance(myRep, mgr.getTMManager().getTopic("Block"));
		if (containedBlocks.size() > 0) {
			Iterator<LearningUnit> a = containedBlocks.keySet().iterator();
			while (a.hasNext()) {
				LearningUnit lu = a.next();
				Set<Topic> scope = new HashSet<Topic>();
				scope.add(lu.getRep());
				Iterator<Block> i = containedBlocks.get(lu).iterator();
				Set<RoleTopic> blocks = new HashSet<RoleTopic>();
				Block current = i.next();
				current.toTopicMap();
				blocks.add(new RoleTopic(current.getContentTypeInLearningUnit(lu), current.getRep()));			
				while (i.hasNext()) {
					Block predecessor = current;
					current = i.next();
					current.toTopicMap();
					blocks.add(new RoleTopic(current.getContentTypeInLearningUnit(lu), current.getRep()));
					Set<RoleTopic> sibling = new HashSet<RoleTopic>();
					sibling.add(new RoleTopic("Predecessor", predecessor.getRep()));
					sibling.add(new RoleTopic("Successor", current.getRep()));
					mgr.addAssociation("Sibling", sibling, scope);							
				}
				blocks.add(new RoleTopic("Superordinate", myRep));
				blocks.add(new RoleTopic("FirstBlock", containedBlocks.get(lu).firstElement().getRep()));
				mgr.addAssociation("Nested", blocks, scope);
			}
		}
		if (contentInScope!=null) {
			Iterator<ContentScope> i = contentInScope.keySet().iterator();
			while (i.hasNext()) {
				ContentScope cs = i.next();
				Set<Topic> scope = new HashSet<Topic>();
				scope.add(cs.lu.getRep());
				mgr.addResource(myRep, contentInScope.get(cs), cs.lod, scope);
			}
		}

	}

	public void toTopicMap(LearningUnit lu) {
		super.toTopicMap();
//		mgr.getTMManager().setTypeInstance(myRep, mgr.getTMManager().getTopic("Block"));
		if (containedBlocks.size() > 0) {
			if(containedBlocks.keySet().contains(lu)) {
				Set<Topic> scope = new HashSet<Topic>();
				scope.add(lu.getRep());
				Iterator<Block> i = containedBlocks.get(lu).iterator();
				Set<RoleTopic> blocks = new HashSet<RoleTopic>();
				Block current = i.next();
				current.toTopicMap(lu);
				blocks.add(new RoleTopic(current.getContentTypeInLearningUnit(lu), current.getRep()));			
				while (i.hasNext()) {
					Block predecessor = current;
					current = i.next();
					current.toTopicMap(lu);
					blocks.add(new RoleTopic(current.getContentTypeInLearningUnit(lu), current.getRep()));
					Set<RoleTopic> sibling = new HashSet<RoleTopic>();
					sibling.add(new RoleTopic("Predecessor", predecessor.getRep()));
					sibling.add(new RoleTopic("Successor", current.getRep()));
					mgr.addAssociation("Sibling", sibling, scope);							
				}
				blocks.add(new RoleTopic("Superordinate", myRep));
				blocks.add(new RoleTopic("FirstBlock", containedBlocks.get(lu).firstElement().getRep()));
				mgr.addAssociation("Nested", blocks, scope);
			}
		}
		if (contentInScope!=null) {
			System.out.println("Content found for element "+name);
			Iterator<ContentScope> i = contentInScope.keySet().iterator();
			while (i.hasNext()) {
				ContentScope cs = i.next();
				if (cs.lu == lu) {
					Set<Topic> scope = new HashSet<Topic>();
					scope.add(lu.getRep());
					mgr.addResource(myRep, contentInScope.get(cs), cs.lod, scope);
				}
			}
		}
	}
	
	private class ContentScope {
		public LearningUnit lu;
		public String lod;
		
		public ContentScope(LearningUnit lu, String lod) {
			this.lod = lod;
			this.lu = lu;
		}
	}
	private class AnnotationScope {
		public LearningUnit lu;
		public String lod;
		public Subject author;
		public String type;
		
		public AnnotationScope(LearningUnit lu, String lod,Subject author, String type) {
			this.lod = lod;
			this.lu = lu;
			this.author = author;
			this.type = type;
		}
	}
	
}
