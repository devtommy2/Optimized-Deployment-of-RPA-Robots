package RPADeployment;

import org.processmining.plugins.graphviz.dot.DotNode;

public class LocalDotTransition extends DotNode {
	public LocalDotTransition() {
		super("", null);
		setOption("style", "filled");
		setOption("fillcolor", "black");
		setOption("width", "0.45");
		setOption("height", "0.15");
		setOption("shape", "box");
	}
}
