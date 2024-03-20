package RPADeployment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.Dot.GraphDirection;
import org.processmining.plugins.graphviz.dot.DotNode;
import org.processmining.plugins.graphviz.visualisation.DotPanel;

public class Visual {
////直接去掉了ts、te
//	@SuppressWarnings("null")
	@Plugin(name = "0000000000000000000Visualize Visualize", 
			returnLabels = { "Dot visualization" }, 
			returnTypes = { JComponent.class }, 
			parameterLabels = { "petri" }, 
			userAccessible = true)
			@Visualizer
			@UITopiaVariant(affiliation = "SDUT", author = "Cong Liu", email = "liucongchina@sdut.edu.cn")	
			@PluginVariant(variantLabel = "Visualize ", 
					requiredParameterLabels = {0})// it needs one input parameter

			public JComponent visualize(PluginContext context, Petrinet petri) {
			//add <dependency org="prom" name="GraphViz" rev="latest" changing="true" transitive="true" /> to ivy.xml to resolve the lated GraphViz package

			final Dot dot = new Dot();
			dot.setDirection(GraphDirection.leftRight); //图是横着的
/*..................传过来的时间进行处理..................*/
			String regEx="[\n`~!@#$%^&*()+=|{}':;'\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
			String aa = " ";
			String getT = petri.getGroups().toString();
			String TM = getT.replaceAll(regEx,aa); 
			
			List<String> listTM= Arrays.asList(TM.split(",")); ///将传过来的时间 字符串转化成List
		
			
			
			
			/*-----------资源的一维数组-------------*/	
			List<String> listRM = new ArrayList<String>();  //存储所有的资源数值
			List<String> R1 = new ArrayList<String>(); List<String> R2 = new ArrayList<String>();
			List<String> R3 = new ArrayList<String>(); List<String> R4 = new ArrayList<String>();
			List<String> R5 = new ArrayList<String>(); List<String> R6 = new ArrayList<String>();
			List<String> R7 = new ArrayList<String>(); List<String> R8 = new ArrayList<String>();
			List<String> R9 = new ArrayList<String>(); List<String> R10 = new ArrayList<String>();
			List<String> R11 = new ArrayList<String>(); List<String> R12 = new ArrayList<String>();
			List<String> R13 = new ArrayList<String>(); List<String> R14 = new ArrayList<String>();
			List<String> R15 = new ArrayList<String>(); List<String> R16 = new ArrayList<String>();
			int r=0;
			for(int tm=16; tm<listTM.size(); tm++) {
				listRM.add(r, listTM.get(tm));
				r = r+1;
			}
		/*-------------------------------------*/
			int j=0;
			int count = -1;
//			System.out.println("***********************************");
			for(int i=0; i<listRM.size(); i++) {
				if(i%5==0) {
					count = count+1;
					j=0;
				}
				if(count ==0) {
					R1.add(j, listRM.get(i));
//					System.out.print(listRM.get(i)+"  ");
//					if(j==4) {
//						System.out.println("count:"+count);
//					System.out.println("-----------------------------");
//					System.out.println("R1:"+R1);
//					System.out.println("R1:"+R1.toString());
//					System.out.println("R1:"+R1.toArray());}
				}
				if(count ==1) {
					R2.add(j, listRM.get(i));
//					System.out.print(listRM.get(i)+"  ");
//					if(j==4) {
//						System.out.println("count:"+count);
//					System.out.println("-----------------------------");}
				}
				if(count ==2) {
					R3.add(j, listRM.get(i));
//					System.out.print(listRM.get(i)+"  ");
//					if(j==4) {
//						System.out.println("count:"+count);
//					System.out.println("-----------------------------");}
				}
				if(count ==3) {
					R4.add(j, listRM.get(i));
//					System.out.print(listRM.get(i)+"  ");
//					if(j==4) {
//						System.out.println("count:"+count);
//					System.out.println("-----------------------------");}
				}
				if(count ==4) {
					R5.add(j, listRM.get(i));
//					System.out.print(listRM.get(i)+"  ");
//					if(j==4) {
//						System.out.println("count:"+count);
//					System.out.println("-----------------------------");}
				}
				if(count ==5) {
					R6.add(j, listRM.get(i));
//					System.out.print(listRM.get(i)+"  ");
//					if(j==4) {
//						System.out.println("count:"+count);
//					System.out.println("-----------------------------");}
				}
				if(count ==6) {
					R7.add(j, listRM.get(i));
//					System.out.print(listRM.get(i)+"  ");
//					if(j==4) {
//						System.out.println("count:"+count);
//					System.out.println("-----------------------------");}
				}
				if(count ==7) {
					R8.add(j, listRM.get(i));
//					System.out.print(listRM.get(i)+"  ");
//					if(j==4) {
//						System.out.println("count:"+count);
//					System.out.println("-----------------------------");}
				}
				j=j+1;
			}
//			System.out.println("***********************************");
			
			
/*			int k=0;
			for(int i=0; i<16; i++) {
				for(int j=0; j<5; j++) {
					if(i)
					R1.add(i, listTM.get(k));
//					RM[i][j] = Integer.parseInt(listRM.get(k));
					String kkk = listTM.get(k).toString();
					System.out.println("kkk:"+kkk);
					int rmm= Integer.valueOf(kkk);//////////////??????????????????????????????????????????
					System.out.println("rmm :"+rmm);
					k=k+1;
				}
			}*/
			
//			int[][] RM = new int[16][5];
			
			/*
			int[][] RM = new int[16][5];    
			for(int i=0;i<16;i++) {
				for(; k<listTM.size(); k++) {
					//if(k/5==0) {j=j+1; continue;}	
									
					System.out.println("-------------------");
					String kkk = listTM.get(k).toString();
					System.out.println("kkk:"+kkk);
					RM[i][j] = Integer.parseInt(kkk);
//					System.out.println("kiii:"+kiii);
					System.out.println("-------------------");
					
//					RM[i][j] = kkk.;
					j=j+1;
//					RM[i][j] = arr1[k];
//					RM[i][j] = Integer.valueOf(listTM.get(k)).intValue();//.toString());
				}				
			}
			*/
//			System.out.println("*****************");
//			for(int i=0;i<RM.length;i++) {
//				for(int l=0;l<5;l++) {
//				System.out.print(" "+RM[i][l]+" ");
//				}
//				System.out.println();
//				System.out.println("*****************");
//			}
			
			HashMap<String, DotNode> activityToNode = new HashMap<String, DotNode>();

//			for(Transition transition:petri.getTransitions()) {
//				if (transition.getLabel().equals("t1")){
//					dot.setOption("label", "["+listTM.get(0)+","+listTM.get(1)+"]");
//					System.out.println("["+listTM.get(0)+","+listTM.get(1)+"]");
//				}
//			}
			
			for(Transition transition:petri.getTransitions()) {
				
				DotNode node = dot.addNode(transition.getLabel());
//				DotNode node1 = dot.addNode("["+listTM.get(0)+","+listTM.get(1)+"]");
				activityToNode.put(transition.getLabel(), node);
				if(transition.getLabel().equals("t1")) {
					node.setOption("label", transition.getLabel()+","+"["+listTM.get(0)+","+listTM.get(1)+"]"+R1+","+R2);
				}  ////后面加了资源向量
				if(transition.getLabel().equals("t2")) {
					node.setOption("label", transition.getLabel()+","+"["+listTM.get(2)+","+listTM.get(3)+"]");
				}
				if(transition.getLabel().equals("t3")) {
					node.setOption("label", transition.getLabel()+","+"["+listTM.get(4)+","+listTM.get(5)+"]");
				}
				if(transition.getLabel().equals("t4")) {
					node.setOption("label", transition.getLabel()+","+"["+listTM.get(6)+","+listTM.get(7)+"]");
				}
				if(transition.getLabel().equals("t5")) {
					node.setOption("label", transition.getLabel()+","+"["+listTM.get(8)+","+listTM.get(9)+"]");
				}
				if(transition.getLabel().equals("t6")) {
					node.setOption("label", transition.getLabel()+","+"["+listTM.get(10)+","+listTM.get(11)+"]");
				}
				if(transition.getLabel().equals("t7")) {
					node.setOption("label", transition.getLabel()+","+"["+listTM.get(12)+","+listTM.get(13)+"]");
				}
				if(transition.getLabel().equals("t8")) {
					node.setOption("label", transition.getLabel()+","+"["+listTM.get(14)+","+listTM.get(15)+"]");
				}
				
				node.setOption("shape","box");
				node.setOption("fontsize", "9");
				node.setOption("color", "red");
			}
			
			
			for(Place place:petri.getPlaces()) {				
				DotNode node = dot.addNode(place.getLabel());
				activityToNode.put(place.getLabel().toString(), node);
				node.setOption("shape","circle");
				node.setOption("fontsize", "9");
				node.setOption("color", "blue");
			}
			
			for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge :petri.getEdges()) 
			{
				String from = edge.getSource().toString();
				String to =  edge.getTarget().toString();

				dot.addEdge(activityToNode.get(from), activityToNode.get(to), "");
			}
					
			return new DotPanel(dot);
		}
	}
