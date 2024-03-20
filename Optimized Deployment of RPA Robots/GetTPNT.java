package RPADeployment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.ERModel.visualize.TransformCrossOrganizationBusinessProcessModel2PetriNet;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.InductiveMiner.plugins.IMPetriNet;
import org.processmining.plugins.InductiveMiner.plugins.dialogs.IMMiningDialog;

@Plugin(name = "RPA", // plugin name

		returnLabels = { "Result" }, //return labels
		returnTypes = { ResultShowPanel.class }, //return class
		parameterLabels = {
				"Name of your first Log" }, userAccessible = true, help = "This plugin aims to to provide a method for deploying RPA robots in the process.")
public class GetTPNT {
	@UITopiaVariant(affiliation = "TU/e", author = "Qingxin Gao", email = "c.liu.3@tue.nl")
	@PluginVariant(variantLabel = "Merge two Event Log, default", requiredParameterLabels = { 0 })
	public static ResultShowPanel GetTimePetriNet(UIPluginContext context, XLog log) throws ParseException {
		//返回一个结果表格
		
		Petrinet net1 = GetPetrinet(context, log);
		Object[] time = Time_max_min(log, net1);
		HashMap<String, Integer> Mintime = (HashMap<String, Integer>) time[0];
		HashMap<String, Integer> Maxtime = (HashMap<String, Integer>) time[1];
		System.out.println("持续时间下限：" + Mintime);
		System.out.println("持续时间上限：" + Maxtime);
		/*context.getProvidedObjectManager().createProvidedObject("PetriNet ", net1, Petrinet.class, context);
		context.getProvidedObjectManager().createProvidedObject("Mintime ", Mintime, Object.class, context);*/

//		System.out.println("Mintime: " + Mintime);
		Object[] msg = GetBestPathPlugin.SimRankSamplingTechnique(net1, Mintime);//获取到时间下限的流程信息
		Object[] msg1 = GetBestPathPlugin.SimRankSamplingTechnique(net1, Maxtime);//获取到时间上限的流程信息
		HashMap<String, Integer> FT = (HashMap<String, Integer>) msg[0];
		HashMap<String, Integer> LT = (HashMap<String, Integer>) msg[1];
		HashMap<String, Integer> BestPath = (HashMap<String, Integer>) msg[2];
		ArrayList<String> bestPathArray = (ArrayList<String>) msg[3];
		HashMap<String, Integer> KeyTime = (HashMap<String, Integer>) msg[5];
		int Completedtime = (int) msg[4];
		System.out.println("时间下限得到的FT：" + FT);
		System.out.println("时间下限得到的LT：" + LT);
		System.out.println("时间下限得到的关键路径：" + BestPath);
		HashMap<String, Integer> FT1 = (HashMap<String, Integer>) msg1[0];
		HashMap<String, Integer> LT1 = (HashMap<String, Integer>) msg1[1];
		HashMap<String, Integer> BestPath1 = (HashMap<String, Integer>) msg1[2];
		ArrayList<String> bestPathArray1 = (ArrayList<String>) msg1[3];
		HashMap<String, Integer> KeyTime1 = (HashMap<String, Integer>) msg1[5];
		int Completedtime1 = (int) msg1[4];
		System.out.println("时间上限得到的FT：" + FT1);
		System.out.println("时间上限得到的LT：" + LT1);
		System.out.println("时间上限得到的关键路径：" + BestPath1);
		ProcessShowPanel parameters2 = new ProcessShowPanel();
		InteractionResult interActionResult1 = context.showConfiguration("Process", parameters2);
		if (interActionResult1.equals(InteractionResult.CANCEL)) {
//			context.getFutureResult(0).cancel(true);
//			return null;
			ResultShowPanel parameters1 = new ResultShowPanel();
								
			return parameters1;
		}
		else {
			ProcessShowPanel1 parameters3 = new ProcessShowPanel1();
			InteractionResult interActionResult3 = context.showConfiguration("Process", parameters3);
			if(interActionResult3.equals(InteractionResult.CANCEL)) {
				ResultShowPanel parameters1 = new ResultShowPanel();
				return parameters1;
			}/*else {
				ProcessShowPanel2 parameters4 = new ProcessShowPanel2();
				InteractionResult interActionResult4 = context.showConfiguration("Process", parameters4);
			}*/
		}
//		ProcessShowPanel parameters3 = new ProcessShowPanel();	
//		InteractionResult interActionResult2 = context.showConfiguration("Process", parameters3);
		ResultShowPanel parameters1 = new ResultShowPanel();
		return parameters1;

	}

	static HashMap<String, Integer> MTime1 = new HashMap<String, Integer>();
	static HashMap<String, Integer> MTime2 = new HashMap<String, Integer>();
	static HashMap<String, Integer> MMintime = new HashMap<String, Integer>();
	static HashMap<String, Integer> MMaxtime = new HashMap<String, Integer>();
	
	
	public static Object[] Time_max_min(XLog log, Petrinet petri) throws ParseException {//获取活动的持续时间上下限数组
		
		int tmin = 100000000, tmax = 0, ma = 0;
		int[] Ttime = new int[2];//[最小时间，最大时间]
		Date sdate = null, cdate = null;
		String sdate1 = null;
		Ttime[0] = tmin;
		Ttime[1] = tmax;
		HashMap<String, int[]> Transition_Time = new HashMap<String, int[]>();
		HashMap<String, Integer> Mintime = new HashMap<String, Integer>();
		HashMap<String, Integer> Maxtime = new HashMap<String, Integer>();//变迁名，最小时间，最大时间
		List<String> transitionVision = new ArrayList<>(); //所有的非不可见变迁
		///Petri网的不可见变迁的最小时间和最大时间设为0
		for (Transition transition : petri.getTransitions()) {
			if (!transition.isInvisible()) {
				transitionVision.add(transition.toString());
				Transition_Time.put(transition.toString(), Ttime);
				Mintime.put(transition.toString(), 0);
				Maxtime.put(transition.toString(), 0);
			}
		}
		for (XTrace trace : log) {
			List<String> Name = new ArrayList<>();
			for (XEvent event : trace) {
				String name = XConceptExtension.instance().extractName(event);
				if (name.equals("ts") || name.equals("te")) {
					int[] TSEtime = new int[2]; //Ts、Te的[最小时间，最大时间]
					TSEtime[0] = TSEtime[1] = 0;
					Transition_Time.put(name, TSEtime);
					Mintime.put(name, 0);
					Maxtime.put(name, 0);
				} else {
					String life = event.getAttributes().get("lifecycle:transition").toString();
					if (life.equals("start")) {

						String time = event.getAttributes().get("time:timestamp").toString();
						LocalDateTime date = LocalDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
						sdate1 = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); //老式日期解析						

						Name.add(name);
					} else if (life.equals("complete")) {
						if (Name.contains(name)) {//同一个任务的结束时间 为了计算同一个任务的时间差
							String time = event.getAttributes().get("time:timestamp").toString();
							LocalDateTime date = LocalDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
							String cdate1 = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); //老式日期解析						

							DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//String——>Date
							try {
								sdate = df.parse(sdate1);///一个任务的开始时间
								cdate = df.parse(cdate1);///一个任务的结束时间
								long dates = cdate.getTime() - sdate.getTime();
								ma = (int) (dates / (1000 * 60)); ///单位是秒	

								int[] Normaltime = new int[2]; //Ts、Te的[最小时间，最大时间]
								int[] Normaltime1 = new int[2];
								for (String transition : Transition_Time.keySet()) {
									if (name.equals(transition)) {
										tmax = Transition_Time.get(name)[1];
										tmin = Transition_Time.get(name)[0];
										if (ma > tmax) {
											tmax = ma;
										}
										if (ma < tmin) {
											tmin = ma;
										}
										Normaltime[0] = tmin;
										Normaltime[1] = tmax;
										
										Transition_Time.put(name, Normaltime);
										Mintime.put(name, Normaltime[0]);//将活动及其对应的完成时间放到一个hashmap中对应起来
										Maxtime.put(name, Normaltime[1]);
									
									}
								}

							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			Name.clear();

		}
	
		Iterator<Entry<String, Integer>> iterator = Mintime.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			MTime1.put(entry.getKey(), entry.getValue());
			MMintime.put(entry.getKey(), entry.getValue());
		}
		Iterator<Entry<String, Integer>> iterator1 = Maxtime.entrySet().iterator();
		while (iterator1.hasNext()) {
			Entry<String, Integer> entry = iterator1.next();
			MTime2.put(entry.getKey(), entry.getValue());
			MMaxtime.put(entry.getKey(), entry.getValue());
		}
		
		return new Object[] { Mintime, Maxtime };
	}

	static Petrinet net1;
//通过事件日志获取Petri网
	public static Petrinet GetPetrinet(UIPluginContext context, XLog log) {
		IMMiningDialog dialog = new IMMiningDialog(log);
		context.log("Mining...");

		Object[] IMP = IMPetriNet.minePetriNet(context, log, dialog.getMiningParameters());
		Petrinet net = TransformCrossOrganizationBusinessProcessModel2PetriNet
				.addingArtifitialSouceandTargetPlaces((Petrinet) IMP[0], (Marking) IMP[1], (Marking) IMP[2]);
		net1 = net;

		return net;
	}
}


/*System.out.println("************************");
System.out.println("FT:" + FT);
System.out.println("LT:" + LT);
System.out.println("BestPath:" + KeyTime);
System.out.println("bestPathArray:" + bestPathArray);*/