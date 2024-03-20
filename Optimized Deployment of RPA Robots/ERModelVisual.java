package RPADeployment;

import javax.swing.JComponent;

import org.processmining.ERModel.plugins.EResourseModel;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.Dot.GraphDirection;
import org.processmining.plugins.graphviz.dot.DotCluster;
import org.processmining.plugins.graphviz.visualisation.DotPanel;

public class ERModelVisual {
////直接去掉了ts、te
	@SuppressWarnings("null")
	@Plugin(name = "ERModelVisual Visualize", 
			returnLabels = { "Dot visualization" }, 
			returnTypes = { JComponent.class }, 
			parameterLabels = { "EResourseModel" }, 
			userAccessible = true)
			@Visualizer  ////调用哪个可视化插件，就在哪个可视化插件中写入此行代码
			@UITopiaVariant(affiliation = "SDUT", author = "Cong Liu", email = "liucongchina@sdut.edu.cn")	
			@PluginVariant(variantLabel = "Visualize ERModelVisual", 
					requiredParameterLabels = {0})// it needs one input parameter
	
	
	public JComponent visualizeERModel(PluginContext context, EResourseModel ermodel)  {

		Dot dot = new Dot();
		dot.setDirection(GraphDirection.leftRight); //图的方向
//		dot.setOption("label", "TimePetri Net");
		dot.setOption("fontsize", "24");
		
		DotCluster orgCluster =dot.addCluster();
		orgCluster.setOption("penwidth", "2.0"); // width of the organization border组织边界的宽度
		orgCluster.setOption("fontsize", "18");
		orgCluster.setOption("color","black");  
		orgCluster.setOption("style","dashed");
		
		System.out.println("ermodel: "+ermodel);
		System.out.println("ermodel.getPn: "+ermodel.getPn());
		System.out.println("ermodel.gettime_max_min: "+ermodel.gettime_max_min());
		
		ERModelVisual2.visualizeDot(ermodel,orgCluster);
	
		return new DotPanel(dot);
	}	
}