package org.processmining.ERModel.plugins;

// ���ص���һ��HashMap<String, int[]> Transition_Time; :��Ǩ��, [ʱ����Сֵ][ʱ�����ֵ]
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;

public class Time_MAX_MIN {

	@Plugin(
			name = "TongYong_Time_MAX_MIN", 
			parameterLabels = { "log","Petri" }, 
			returnLabels = { "log" },
			returnTypes = { List.class },
			userAccessible = true)     ///
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "  ", email = "    ")
	
	public HashMap<String, int[]> Time_max_min(XLog log, Petrinet petri)  {		

		int tmin=100000000, tmax=0, ma =0;
		int[] Ttime = new int[2];//[��Сʱ�䣬���ʱ��]
		Date sdate = null,cdate = null;
		String sdate1 =null;
		Ttime[0] = tmin;	Ttime[1] = tmax;

		HashMap<String, int[]> Transition_Time = new HashMap<String, int[]>();//��Ǩ������Сʱ�䣬���ʱ��
		HashMap<String, Integer> T_min = new HashMap<String, Integer>();//��Ǩ����Сʱ��
		List<String> transitionVision = new ArrayList<>();   //���еķǲ��ɼ���Ǩ
		///Petri���Ĳ��ɼ���Ǩ
		for(Transition transition:petri.getTransitions()) {
			if(!transition.isInvisible()){
				transitionVision.add(transition.toString());
				Transition_Time.put(transition.toString(), Ttime);
			}
		}
		
        for (XTrace trace: log)	{   
		    	List<String> Name=new ArrayList<>();

				for(XEvent event: trace ){  
					String name = XConceptExtension.instance().extractName(event);	
			
					if(name.equals("ts")||name.equals("te")) { 
						int[] TSEtime = new int[2];    //Ts��Te��[��Сʱ�䣬���ʱ��]
						TSEtime[0] =TSEtime[1] = 0;
						Transition_Time.put(name, TSEtime);
					}
					else {			//else if(name.equals("t1")){								
						String life = event.getAttributes().get("lifecycle:transition").toString();
						if (life.equals("start")) {

							String time = event.getAttributes().get("time:timestamp").toString();
							LocalDateTime date = LocalDateTime.parse(time,DateTimeFormatter.ISO_OFFSET_DATE_TIME);
							sdate1 = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));  //��ʽ���ڽ���						
							
							Name.add(name);						
						}
						else if(life.equals("complete")){
							if (Name.contains(name)) {//ͬһ������Ľ���ʱ�� Ϊ�˼���ͬһ�������ʱ���
								String time = event.getAttributes().get("time:timestamp").toString();
								LocalDateTime date = LocalDateTime.parse(time,DateTimeFormatter.ISO_OFFSET_DATE_TIME);
								String cdate1 = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));  //��ʽ���ڽ���						
												
								DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//String����>Date
								try {
									sdate = df.parse(sdate1);///һ������Ŀ�ʼʱ��
									cdate = df.parse(cdate1);///һ������Ľ���ʱ��
									long dates=cdate.getTime() - sdate.getTime();
									ma = (int) (dates / (1000 * 60));  ///��λ����	

									int[] Normaltime = new int[2];
									int[] Normaltime1 = new int[2]; //Ts��Te��[��Сʱ�䣬���ʱ��]
									int a=6,b=12;
//									int Tmin = Normaltime[0];//���ʱ������
									for(String transition:Transition_Time.keySet()) {
										if(name.equals(transition)){
											tmax = Transition_Time.get(name)[1];
											tmin = Transition_Time.get(name)[0];
											if(ma>tmax) {tmax=ma;}
											if(ma<tmin) {tmin=ma;}
											Normaltime[0] = tmin;
											Normaltime[1] = tmax;
//											Normaltime1[0] = a;
//											Normaltime1[1] = b;
											Transition_Time.put(name, Normaltime);
//											Transition_Time.put("t7", Normaltime1);
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

		System.out.println("Transition_Time.keySet():  "+Transition_Time.keySet());	
		for(String key: Transition_Time.keySet()) {
			System.out.println("key:  "+key);
			System.out.println("value():  "+Transition_Time.values());	
				
			System.out.println("************************");
			
		}
		
			return Transition_Time; 
	}

}
