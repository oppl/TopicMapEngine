package ce.tm4scholion.metamodel.learning;

import java.util.HashSet;
import java.util.Set;

import ce.tm4scholion.tm.Topic;

public class Manager {

	public Topic block;
	public Topic learningUnit;
	
	private ce.tm4scholion.metamodel.Manager mmManager;

	public Manager(ce.tm4scholion.metamodel.Manager tmManager) {
		this.mmManager = tmManager;
		this.generateMetaElements();
	}

	
	private void generateMetaElements() {

		//elements
		block = mmManager.addMetamodelElement("Block");
		learningUnit = mmManager.addMetamodelElement("LearningUnit");		

		//associations

		Set<Topic> elements = new HashSet<Topic>();
		elements.add(block);
		mmManager.createNewSiblingAssocType(elements);
		
		Set<Topic> superordinates = new HashSet<Topic>();
		Set<Topic> subordinates = new HashSet<Topic>();
		superordinates.add(block);
		superordinates.add(learningUnit);
		subordinates.add(block);
		mmManager.createNewNestedAssocType(superordinates, subordinates, block);
		
		//resources
		Set<String> resTypes = new HashSet<String>();
		resTypes.add("Content");
		resTypes.add("Annotation");
		mmManager.addResourceTypesForElement(block, resTypes);
		
		//concrete Roles
		Set<String> concrete = new HashSet<String>();
		concrete.add("LOD1");
		concrete.add("LOD2");
		concrete.add("LOD3");
		mmManager.concretizeRole("Content", concrete);
		
		concrete = new HashSet<String>();
		concrete.add("TextAnnotationLOD1");
		concrete.add("TextAnnotationLOD2");
		concrete.add("TextAnnotationLOD3");
		concrete.add("MarkerAnnotationLOD1");
		concrete.add("MarkerAnnotationLOD2");
		concrete.add("MarkerAnnotationLOD3");
		mmManager.concretizeRole("Annotation", concrete);

		concrete = new HashSet<String>();
		concrete.add("Overview");
		concrete.add("Explanation");
		concrete.add("Reference");
		concrete.add("Example");
		concrete.add("Definition");
		concrete.add("Supplement");
		concrete.add("Motivation");
		concrete.add("Information");
		concrete.add("Directive");
		concrete.add("Content");
		mmManager.concretizeRole("Subordinate", concrete);
	
	}
	
	public Block generateBlock(String name) {
		Topic newBlock = mmManager.instantiateElement(name, block);
		return new Block(newBlock, mmManager);
	}
	
	public LearningUnit generateLearningUnit(String name) {
		Topic newLearningUnit = mmManager.instantiateElement(name, learningUnit);
		return new LearningUnit(newLearningUnit, mmManager);
	}

	
}
