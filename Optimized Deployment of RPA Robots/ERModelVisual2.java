package RPADeployment;

import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JComponent;

import org.processmining.ERModel.plugins.EResourseModel;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.plugins.graphviz.dot.DotCluster;
import org.processmining.plugins.graphviz.dot.DotNode;

public class ERModelVisual2 {
////直接去掉了ts、te
	@Plugin(name = "ERModelVisual Visualize", 
			returnLabels = { "Dot visualization" }, 
			returnTypes = { JComponent.class }, 
			parameterLabels = { "EResourseModel" }, 
			userAccessible = true)
//			@Visualizer  ////调用哪个可视化插件，就在哪个可视化插件中写入此行代码
			@UITopiaVariant(affiliation = "SDUT", author = "HuiLing LI", email = "17864309765@163.com")	
			@PluginVariant(variantLabel = "Visualize ERModelVisual", 
					requiredParameterLabels = {0})// it needs one input parameter
	
	
	public static DotCluster visualizeDot(EResourseModel ermodel, DotCluster cluster)  {
		
		HashSet<DotNode> tDotNodeSet = new HashSet<DotNode>();
		HashMap<PetrinetNode, DotNode> mapPetrinet2Dot = new HashMap<PetrinetNode, DotNode>();		
		Petrinet petri = ermodel.getPn();	
		HashMap<String, int[]> Time_max_min = ermodel.gettime_max_min();			
			
//	Time:		HashMap<String, int[]> Time_max_min
		for(Transition transition: petri.getTransitions()) {
			
			DotNode tDot = null;
			if (transition.isInvisible()) {
				tDot = new LocalDotTransition();
			} 
			else{	
				for(String ttransition:Time_max_min.keySet()) {
					if(transition.getLabel().equals(ttransition)) {  
						int[] TimeRegion = Time_max_min.get(ttransition);
						tDot = new LocalDotNormalTransition(transition, TimeRegion);	
						tDotNodeSet.add(tDot);	
					}
				}							
			}
			
			 cluster.addNode(tDot);
			mapPetrinet2Dot.put(transition, tDot);	
		}	
		
		//add places
		for(Place place:petri.getPlaces()) {
			int startFlag=0;
			int endFlag=0;
			//check if the p is start or end. 
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : petri.getEdges())	{
				//if there is no incoming arc, this place is a start place
				if(edge.getTarget().getId().equals(place.getId()))	{
					startFlag=1;
					break;
				}
			}
			
			//check if the p is start or end. 
			for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : petri.getEdges()) {
				//if there is no out-going arc, this place is a end place
				if(edge.getSource().getId().equals(place.getId()))	{
					endFlag=1;
					break;
				}
			}
			
			//if the place is a start place
			DotNode pDot;
			if(startFlag==0) {
				pDot = new LocalDotPlace(0);
			}
			else if (endFlag ==0)	{
				pDot = new LocalDotPlace(1);
			}
			else {
				pDot = new LocalDotPlace();
			}
			
			cluster.addNode(pDot);
			mapPetrinet2Dot.put(place, pDot);
		}
			
		for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : petri.getEdges()) {
			if (mapPetrinet2Dot.get(edge.getSource()) != null && mapPetrinet2Dot.get(edge.getTarget()) != null) {
				cluster.addEdge(mapPetrinet2Dot.get(edge.getSource()), mapPetrinet2Dot.get(edge.getTarget()));
			}
		}

//		System.out.println("cluster: "+cluster);
		
		return cluster;
		}
	}
