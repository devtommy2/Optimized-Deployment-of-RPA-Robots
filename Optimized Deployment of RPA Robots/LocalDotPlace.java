package RPADeployment;

import org.processmining.plugins.graphviz.dot.DotNode;

public class LocalDotPlace extends DotNode {
	

	public LocalDotPlace() {
		super("", null);
		setOption("shape", "circle");
	}
		public LocalDotPlace(int flag) {
			super("", null);
			if (flag==0) // the start place. with green.
			{
				setOption("shape", "doublecircle");
				setOption("style", "filled");
				setOption("fillcolor", "green");
//				setOption("image", "D:/triangle.svg");
				
			}
			else // the complete place with red
			{
				setOption("shape", "doublecircle");
				setOption("style", "filled");
				setOption("fillcolor", "red");
			}
		}
	


}
