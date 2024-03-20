package RPADeployment;

import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.plugins.graphviz.dot.DotNode;

public class LocalDotNormalTransition extends DotNode {
	
	public LocalDotNormalTransition(Transition transition, int[] timeRegion) {
		super(" ", null);
		setOption("shape", "box");
		setOption("fillcolor", "green");
		setOption("width", "0.45");
		setOption("height", "0.15");		
		setOption("label", transition.getLabel()+"\n"+"["+timeRegion[0]+","+timeRegion[1]+"]");
	}

}
