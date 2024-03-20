package RPADeployment;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Arc;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;





public class GetBestPathPlugin {

	public static Object[] SimRankSamplingTechnique(Petrinet net,HashMap<String, Integer> DuraionTime)
	{
		

		/**
		 * 1.取出Petri网中的结点，计算其前驱和后继结点
		 */
		
		
		/*
		 * Iterate over all transitions.
		 */
//		HashMap<String, Integer> TE = new HashMap<String, Integer>();//存放每个活动的最早开始时间
//		HashMap<String, Integer> LE = new HashMap<String, Integer>();//最晚开始时间
//		HashMap<String, Integer> Mintime = new HashMap<String, Integer>();//把前边代码的Mintime实现
		
		HashMap<String, HashSet<String>> pre_transition = new HashMap<String, HashSet<String>>(50);//前驱变迁集合
		HashMap<String, HashSet<String>> pre_transition1 = new HashMap<String, HashSet<String>>(50);//前驱变迁集合
		HashMap<String, HashSet<String>> post_transition = new HashMap<String, HashSet<String>>(50);//后继变迁集合
		HashMap<String, Integer> KeyTime = new HashMap<String, Integer>(50);//关键活动及其持续时间
		for (Transition transitionA : net.getTransitions()) {
			HashSet<String> x_transition= new HashSet<String>();
			HashSet<String> y_transition= new HashSet<String>();
			Place placeX;//库所
			{
				for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> arc1:net.getInEdges(transitionA)) {//得到入边
					placeX = (Place) arc1.getSource();//入边的前继库所
					for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : net.getInEdges(placeX)) {
						Transition source = (Transition) edge.getSource();
						x_transition.add(source.toString());
					}
					
				}
				pre_transition.put(transitionA.toString(),x_transition);
				pre_transition1.put(transitionA.toString(),x_transition);
				
				
				
				//第二步
				Place placeY;
				{
//					System.out.println("变迁:"+transitionA);
					//net.getInEdges(transitionA) 判断大小  大于1是并发 小于1是选择
					//一个边只对应一个库所
					int size=net.getOutEdges(transitionA).size();	
					for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> arc1:net.getOutEdges(transitionA)) {
						placeX = (Place) arc1.getTarget();
						//net.getInEdges(placeX) 库所的入边大于1就是选择，忽略，=1且net.getInEdges(transitionA) >1满足就是并发
						for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : net.getOutEdges(placeX)) {
							Transition target = (Transition) edge.getTarget();
							y_transition.add(target.toString());
						}
						
					}
				}
				post_transition.put(transitionA.toString(),y_transition);
//				System.out.println("################################");
			  }
		}
//		System.out.println("pre_transition11111:"+pre_transition);
//		System.out.println("pre_transition11111:"+pre_transition1);
//		System.out.println("post_transition11111:"+post_transition);
		
		
//		HashMap<String, HashSet<String>> pre_transition = new HashMap<String, HashSet<String>>();//前驱变迁集合
        // Gson序列化，需要使用泛型参数TypeToken
        Gson gson = new Gson();
        String json = gson.toJson(pre_transition);
        HashMap<String, HashSet<String>> pre_transition2 = new HashMap<>();
        // 注意，gson的jsonStr转集合，需要使用泛型参数TypeToken，才能准确获取到需要的对象
        pre_transition2 = gson.fromJson(json, new TypeToken<HashMap<String, HashSet<String>>>(){}.getType());
		
        /**
         * 将变迁排序
         */
	
		  HashMap<String, Integer> FT = new HashMap<String, Integer>(50);//存放每个活动的最早开始时间
		  HashSet<String> sort_transition= new HashSet<String>();
		  ArrayList<String> sort_transition1= new ArrayList<String>();//存储正确排序顺序变迁
		  //先排序，找个起始变迁并赋值最早开始时间为0
		  Iterator < Entry < String, HashSet<String>>> iterator = pre_transition.entrySet().iterator();  //遍历
	        while (iterator.hasNext()) {  
	            Entry < String, HashSet<String> > entry = iterator.next();  

	            if(entry.getValue().isEmpty()) {
	            	FT.put(entry.getKey(), 0);
	            	sort_transition.add(entry.getKey());
	            	sort_transition1.add(entry.getKey());
	            	break;
	            }
	        } 
			
			for(int i=0;i<pre_transition.size();i++){
//					HashMap<String, HashSet<String>> pre_transition = new HashMap<String, HashSet<String>>();//前驱变迁集合
			        // Gson序列化，需要使用泛型参数TypeToken
			        Gson gson11 = new Gson();
			        String json11 = gson11.toJson(pre_transition2);
			        HashMap<String, HashSet<String>> pre_transition00 = new HashMap<>(50);
			        // 注意，gson的jsonStr转集合，需要使用泛型参数TypeToken，才能准确获取到需要的对象
			        pre_transition00 = gson11.fromJson(json11, new TypeToken<HashMap<String, HashSet<String>>>(){}.getType());
				 Iterator < Entry < String, HashSet<String>>> iterator1 = pre_transition00.entrySet().iterator();
				 while (iterator1.hasNext()) {  
			         Entry < String, HashSet<String> > entry1 = iterator1.next();  
			         
			         int size=entry1.getValue().size();
			         boolean flag=entry1.getValue().retainAll(sort_transition1);
			         //交集不为空且差集为0
//			         if( sort_transition.contains(entry1.getValue())){
			         if( !flag && entry1.getValue().size() == size){
			        	   if(!sort_transition1.contains(entry1.getKey())) {//去重
//			        		   sort_transition.add(entry1.getKey());
				            	sort_transition1.add(entry1.getKey());
			        	   }	
			            }
			   }
			 
//			System.out.println("sort_transition1:"+sort_transition1);  
//			System.out.println("########################################"); 
			}   
		   System.out.println(sort_transition); 
		   System.out.println(sort_transition1); //排序后的变迁集合
	        //如果pre_transition的值中有开始结点，则
			/**
			 * 2.计算活动的最早开始时间Fitst_Time  FT
			 * if 变迁的前驱数量==1  则活动k的最早开始时间FT(k)=FT(k-1)+d(k-1),FT(k-1)为活动k的前一个活动的最早开始时间
			 *    d(k-1)为前一个活动的持续时间
			 * else  //出现并发活动
			 *     活动k的最早开始时间 FT(k)= MAX(FT(j)+d(j),FT(i)+d(i),...),其中i,j,...都是活动k的前驱活动
			 * 			
			 */
//		    System.out.println("pre_transition1:"+pre_transition2);
		    int Wholetime = 0;
	        for (String TransitionName : sort_transition1) {
//	        	HashMap<String, Integer> FT = new HashMap<String, Integer>();//存放每个活动的最早开始时间
//	        	HashMap<String, HashSet<String>> pre_transition = new HashMap<String, HashSet<String>>();//前驱变迁集合
//	        	HashMap<String, Integer> DuraionTime = new HashMap<String, Integer>();//持续时间
//	        	System.out.println("pre_transition.get(TransitionName).size():"+pre_transition2.get(TransitionName).size());
	        	if(pre_transition2.get(TransitionName).size()==0) {
	        		FT.put(TransitionName,0);
	        	}else if(pre_transition2.get(TransitionName).size()==1) { //只有一个前驱
	        		Iterator<String> iterator10 = pre_transition2.get(TransitionName).iterator();
	        		while(iterator10.hasNext()){
//	        			System.out.print(iterator10.next()+",");
	        			String t1=iterator10.next();
//	        			System.out.println("DuraionTime.get(iterator10.next()):"+DuraionTime.get(iterator10.next()));
//	        			System.out.println("FT.get(iterator10.next()):"+FT.get(iterator10.next()));
	        			FT.put(TransitionName,DuraionTime.get(t1)+FT.get(t1));
	        		}	
	        	}else {
	        		HashSet<String> tmp_transition=pre_transition2.get(TransitionName);
	        		Iterator<String> iterator_tmp = tmp_transition.iterator();
	        		int MaxTime=0;
	        		while(iterator_tmp.hasNext()){
	        			String transition11=iterator_tmp.next();
	        			int tempTime=FT.get(transition11)+DuraionTime.get(transition11);
//	        			System.out.println("tempTime:"+tempTime);
	        			if(tempTime > MaxTime) {
	        				MaxTime=tempTime;
	        			}
	        		}
	        		FT.put(TransitionName,MaxTime);
	        		

	        	}
	        	
//	            System.out.println(TransitionName);   
	        }
	       
	        System.out.println(FT.values());
	        ArrayList<Integer> valueList = new ArrayList<>();
	        Collection<Integer> values = FT.values();
	        int maxtime = 0;
	        for(Integer Value:values) {
	        	if (Value>=maxtime)
	        		maxtime=Value;
	        	valueList.add(Value);
	        }
	        Wholetime=maxtime;
//    		System.out.println(Wholetime);
//	        System.out.println(valueList);
//	        System.out.println("上述获得整个Petri网完成时间");

		
		
		/**
		 *  3.计算活动的最早开始时间Last_Time  LT
		 *  3.1 Petri网的整体时间=FT(n)+d(n)
		 *  3.2
		 *     if 变迁的前驱数量==1  则活动k的最晚开始时间LT(k)=LT(k+1)- d(k),LT(k+1)为活动k的前一个活动的最晚开始时间
		 *        d(k)为该活动的持续时间
		 *     else  //出现并发活动
		 *         活动k的最晚开始时间 LT(k)= MIN(LT(j),LT(i),...)-d(k),其中i,j,...都是活动k的后继活动
		 * 
		 */
	      
//		    System.out.println("post_transition:"+post_transition);

		    ArrayList<String> sort_transition3 = new ArrayList<String>( );//逆序后的数组
		    for(int i = sort_transition1.size()-1; i>=0;i--)
		    {
		    	sort_transition3.add(sort_transition1.get(i));
		    }
		    HashMap<String, Integer> LT = new HashMap<String, Integer>(50);//存放每个活动的最晚开始时间
		    
	        for (String TransitionName : sort_transition3) {
//	        	HashMap<String, Integer> FT = new HashMap<String, Integer>();//存放每个活动的最早开始时间
//	        	HashMap<String, HashSet<String>> pre_transition = new HashMap<String, HashSet<String>>();//前驱变迁集合
//	        	HashMap<String, Integer> DuraionTime = new HashMap<String, Integer>();//持续时间
//	        	System.out.println("pre_transition.get(TransitionName).size():"+pre_transition2.get(TransitionName).size());
	        	if(post_transition.get(TransitionName).size()==0) {
//	        		System.out.println("1111111111111");
	        		LT.put(TransitionName,FT.get(TransitionName));
	        		
	        	}else if(post_transition.get(TransitionName).size()==1) { //只有一个前驱
//	        		System.out.println("22222222222222");
	        		Iterator<String> iterator20 = post_transition.get(TransitionName).iterator();
	        		while(iterator20.hasNext()){
	        			String t1=iterator20.next();
	        			LT.put(TransitionName,LT.get(t1)-DuraionTime.get(TransitionName));
	        		}	
	        	}else {
//	        		System.out.println("3333333333333333");
	        		HashSet<String> tmp_transition=post_transition.get(TransitionName);
	        		Iterator<String> iterator_tmp = tmp_transition.iterator();
	        		int MinTime=99999999;
	        		while(iterator_tmp.hasNext()){
//	        			System.out.print(iterator_tmp.next()+",");
	        			String transition11=iterator_tmp.next();
	        			int tempTime=LT.get(transition11);
//	        			System.out.println("tempTime:"+tempTime);
	        			if(tempTime < MinTime) {
	        				MinTime=tempTime;
	        			}
	        		}
	        		LT.put(TransitionName,MinTime-DuraionTime.get(TransitionName));

	        	}
	        	
//	            System.out.println(TransitionName);   
	        }

			/**
			 * 4.获取关键活动及其时间
			 *   if 活动的最早开始时间== 最晚开始时间    则该活动为关键活动
			 */
//	        HashMap<String, Integer> LT = new HashMap<String, Integer>();//存放每个活动的最晚开始时间
	        HashMap<String, Integer> BestPath = new HashMap<String, Integer>();//关键活动名以及活动的最早开始时间
			Iterator < Entry < String, Integer>> iterator33 = FT.entrySet().iterator();  
			int flag=0;
		     while (iterator33.hasNext()) {
		    	   flag++;
//		    	   System.out.println("1111第111"+flag+"次遍历");
		           Entry < String,Integer > entry33 = iterator33.next(); 
//		           System.out.println("entry33.getKey();"+entry33.getKey());  
//		            System.out.println("entry33.getValue();"+entry33.getValue());
		           Iterator < Entry < String, Integer>> iterator44 = LT.entrySet().iterator(); 
		           while (iterator44.hasNext()) {
//		        	    System.out.println("222222222222222");
			            Entry < String,Integer > entry44 = iterator44.next();  
//			            System.out.println("entry44.getKey();"+entry44.getKey());  
//			            System.out.println("entry44.getKey();"+entry44.getValue());
			            /*if(entry33.getKey().equals("t7")) {
				            System.out.println("￥￥￥￥￥￥￥￥￥￥"+(entry44.getValue()== entry33.getValue())); 
				            System.out.println("%%%%%%%%%%"+(entry44.getKey().equals(entry33.getKey())));
			            }*/
			            if(entry44.getValue().equals(entry33.getValue()) && entry44.getKey().equals(entry33.getKey())) {
//			               System.out.println("相等了"+entry33.getKey()+";"+entry33.getValue());
			               BestPath.put(entry33.getKey(), entry33.getValue());
			           }
//			            System.out.println("##############################");
			           
		          }
		           
		      }
		     System.out.println("FT:"+FT);
		     System.out.println("LT:"+LT);
		     System.out.println("1111111111111111111111111");
		     System.out.println("BestPath:"+BestPath); 
		     //将关键活动及其持续时间放到KeyTime集合中
		     Iterator < Entry < String, Integer>> iterator66 = BestPath.entrySet().iterator(); 
		     while(iterator66.hasNext()) {
		    	 Entry < String,Integer > entry66 = iterator66.next();
		    	 String name11 = entry66.getKey();
		    	 int durationtime1 = DuraionTime.get(name11);
		    	 KeyTime.put(name11,durationtime1);
		     }
//		     System.out.println("Key activity and duration： "+KeyTime);
//		     System.out.println("22222222222222222222222222222222");
		     //
		     //sort_transition1   ArrayList<String> sort_transition1= new ArrayList<String>();
		     ArrayList<String> bestPathArray= new ArrayList<String>();
			    for(int i = 0; i<=sort_transition1.size()-1;i++)
			    {
			    	bestPathArray.add(sort_transition1.get(i));
			    }
			    
		     for(int i=0;i<sort_transition1.size();i++) {
		    	 if(!BestPath.keySet().contains(sort_transition1.get(i))) {
		    		 bestPathArray.remove(sort_transition1.get(i));
		    	 }
		     }
//		     System.out.println("bestPathArray:"+bestPathArray); 
		return new Object[]{FT,LT,BestPath,bestPathArray,Wholetime,KeyTime,sort_transition1}; 
   }
	public static Place getPlaceY(Petrinet net, Transition transitionA) {
		Collection<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> postset = net.getOutEdges(transitionA);
		if (postset.size() != 1) {//满足静默变迁的出边只有一个
			return null;
		}
		PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> arc2 = postset.iterator().next();
		if (!(arc2 instanceof Arc)) {
			return null;
		}

//		if (((Arc) arc2).getWeight() != 1) {
//			return null;
//		}

		return (Place) arc2.getTarget();
	}
}
