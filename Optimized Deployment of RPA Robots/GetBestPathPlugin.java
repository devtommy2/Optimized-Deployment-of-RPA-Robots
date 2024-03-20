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
		 * 1.ȡ��Petri���еĽ�㣬������ǰ���ͺ�̽��
		 */
		
		
		/*
		 * Iterate over all transitions.
		 */
//		HashMap<String, Integer> TE = new HashMap<String, Integer>();//���ÿ��������翪ʼʱ��
//		HashMap<String, Integer> LE = new HashMap<String, Integer>();//����ʼʱ��
//		HashMap<String, Integer> Mintime = new HashMap<String, Integer>();//��ǰ�ߴ����Mintimeʵ��
		
		HashMap<String, HashSet<String>> pre_transition = new HashMap<String, HashSet<String>>(50);//ǰ����Ǩ����
		HashMap<String, HashSet<String>> pre_transition1 = new HashMap<String, HashSet<String>>(50);//ǰ����Ǩ����
		HashMap<String, HashSet<String>> post_transition = new HashMap<String, HashSet<String>>(50);//��̱�Ǩ����
		HashMap<String, Integer> KeyTime = new HashMap<String, Integer>(50);//�ؼ���������ʱ��
		for (Transition transitionA : net.getTransitions()) {
			HashSet<String> x_transition= new HashSet<String>();
			HashSet<String> y_transition= new HashSet<String>();
			Place placeX;//����
			{
				for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> arc1:net.getInEdges(transitionA)) {//�õ����
					placeX = (Place) arc1.getSource();//��ߵ�ǰ�̿���
					for (PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> edge : net.getInEdges(placeX)) {
						Transition source = (Transition) edge.getSource();
						x_transition.add(source.toString());
					}
					
				}
				pre_transition.put(transitionA.toString(),x_transition);
				pre_transition1.put(transitionA.toString(),x_transition);
				
				
				
				//�ڶ���
				Place placeY;
				{
//					System.out.println("��Ǩ:"+transitionA);
					//net.getInEdges(transitionA) �жϴ�С  ����1�ǲ��� С��1��ѡ��
					//һ����ֻ��Ӧһ������
					int size=net.getOutEdges(transitionA).size();	
					for(PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> arc1:net.getOutEdges(transitionA)) {
						placeX = (Place) arc1.getTarget();
						//net.getInEdges(placeX) ��������ߴ���1����ѡ�񣬺��ԣ�=1��net.getInEdges(transitionA) >1������ǲ���
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
		
		
//		HashMap<String, HashSet<String>> pre_transition = new HashMap<String, HashSet<String>>();//ǰ����Ǩ����
        // Gson���л�����Ҫʹ�÷��Ͳ���TypeToken
        Gson gson = new Gson();
        String json = gson.toJson(pre_transition);
        HashMap<String, HashSet<String>> pre_transition2 = new HashMap<>();
        // ע�⣬gson��jsonStrת���ϣ���Ҫʹ�÷��Ͳ���TypeToken������׼ȷ��ȡ����Ҫ�Ķ���
        pre_transition2 = gson.fromJson(json, new TypeToken<HashMap<String, HashSet<String>>>(){}.getType());
		
        /**
         * ����Ǩ����
         */
	
		  HashMap<String, Integer> FT = new HashMap<String, Integer>(50);//���ÿ��������翪ʼʱ��
		  HashSet<String> sort_transition= new HashSet<String>();
		  ArrayList<String> sort_transition1= new ArrayList<String>();//�洢��ȷ����˳���Ǩ
		  //�������Ҹ���ʼ��Ǩ����ֵ���翪ʼʱ��Ϊ0
		  Iterator < Entry < String, HashSet<String>>> iterator = pre_transition.entrySet().iterator();  //����
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
//					HashMap<String, HashSet<String>> pre_transition = new HashMap<String, HashSet<String>>();//ǰ����Ǩ����
			        // Gson���л�����Ҫʹ�÷��Ͳ���TypeToken
			        Gson gson11 = new Gson();
			        String json11 = gson11.toJson(pre_transition2);
			        HashMap<String, HashSet<String>> pre_transition00 = new HashMap<>(50);
			        // ע�⣬gson��jsonStrת���ϣ���Ҫʹ�÷��Ͳ���TypeToken������׼ȷ��ȡ����Ҫ�Ķ���
			        pre_transition00 = gson11.fromJson(json11, new TypeToken<HashMap<String, HashSet<String>>>(){}.getType());
				 Iterator < Entry < String, HashSet<String>>> iterator1 = pre_transition00.entrySet().iterator();
				 while (iterator1.hasNext()) {  
			         Entry < String, HashSet<String> > entry1 = iterator1.next();  
			         
			         int size=entry1.getValue().size();
			         boolean flag=entry1.getValue().retainAll(sort_transition1);
			         //������Ϊ���ҲΪ0
//			         if( sort_transition.contains(entry1.getValue())){
			         if( !flag && entry1.getValue().size() == size){
			        	   if(!sort_transition1.contains(entry1.getKey())) {//ȥ��
//			        		   sort_transition.add(entry1.getKey());
				            	sort_transition1.add(entry1.getKey());
			        	   }	
			            }
			   }
			 
//			System.out.println("sort_transition1:"+sort_transition1);  
//			System.out.println("########################################"); 
			}   
		   System.out.println(sort_transition); 
		   System.out.println(sort_transition1); //�����ı�Ǩ����
	        //���pre_transition��ֵ���п�ʼ��㣬��
			/**
			 * 2.���������翪ʼʱ��Fitst_Time  FT
			 * if ��Ǩ��ǰ������==1  ��k�����翪ʼʱ��FT(k)=FT(k-1)+d(k-1),FT(k-1)Ϊ�k��ǰһ��������翪ʼʱ��
			 *    d(k-1)Ϊǰһ����ĳ���ʱ��
			 * else  //���ֲ����
			 *     �k�����翪ʼʱ�� FT(k)= MAX(FT(j)+d(j),FT(i)+d(i),...),����i,j,...���ǻk��ǰ���
			 * 			
			 */
//		    System.out.println("pre_transition1:"+pre_transition2);
		    int Wholetime = 0;
	        for (String TransitionName : sort_transition1) {
//	        	HashMap<String, Integer> FT = new HashMap<String, Integer>();//���ÿ��������翪ʼʱ��
//	        	HashMap<String, HashSet<String>> pre_transition = new HashMap<String, HashSet<String>>();//ǰ����Ǩ����
//	        	HashMap<String, Integer> DuraionTime = new HashMap<String, Integer>();//����ʱ��
//	        	System.out.println("pre_transition.get(TransitionName).size():"+pre_transition2.get(TransitionName).size());
	        	if(pre_transition2.get(TransitionName).size()==0) {
	        		FT.put(TransitionName,0);
	        	}else if(pre_transition2.get(TransitionName).size()==1) { //ֻ��һ��ǰ��
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
//	        System.out.println("�����������Petri�����ʱ��");

		
		
		/**
		 *  3.���������翪ʼʱ��Last_Time  LT
		 *  3.1 Petri��������ʱ��=FT(n)+d(n)
		 *  3.2
		 *     if ��Ǩ��ǰ������==1  ��k������ʼʱ��LT(k)=LT(k+1)- d(k),LT(k+1)Ϊ�k��ǰһ���������ʼʱ��
		 *        d(k)Ϊ�û�ĳ���ʱ��
		 *     else  //���ֲ����
		 *         �k������ʼʱ�� LT(k)= MIN(LT(j),LT(i),...)-d(k),����i,j,...���ǻk�ĺ�̻
		 * 
		 */
	      
//		    System.out.println("post_transition:"+post_transition);

		    ArrayList<String> sort_transition3 = new ArrayList<String>( );//����������
		    for(int i = sort_transition1.size()-1; i>=0;i--)
		    {
		    	sort_transition3.add(sort_transition1.get(i));
		    }
		    HashMap<String, Integer> LT = new HashMap<String, Integer>(50);//���ÿ���������ʼʱ��
		    
	        for (String TransitionName : sort_transition3) {
//	        	HashMap<String, Integer> FT = new HashMap<String, Integer>();//���ÿ��������翪ʼʱ��
//	        	HashMap<String, HashSet<String>> pre_transition = new HashMap<String, HashSet<String>>();//ǰ����Ǩ����
//	        	HashMap<String, Integer> DuraionTime = new HashMap<String, Integer>();//����ʱ��
//	        	System.out.println("pre_transition.get(TransitionName).size():"+pre_transition2.get(TransitionName).size());
	        	if(post_transition.get(TransitionName).size()==0) {
//	        		System.out.println("1111111111111");
	        		LT.put(TransitionName,FT.get(TransitionName));
	        		
	        	}else if(post_transition.get(TransitionName).size()==1) { //ֻ��һ��ǰ��
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
			 * 4.��ȡ�ؼ������ʱ��
			 *   if ������翪ʼʱ��== ����ʼʱ��    ��ûΪ�ؼ��
			 */
//	        HashMap<String, Integer> LT = new HashMap<String, Integer>();//���ÿ���������ʼʱ��
	        HashMap<String, Integer> BestPath = new HashMap<String, Integer>();//�ؼ�����Լ�������翪ʼʱ��
			Iterator < Entry < String, Integer>> iterator33 = FT.entrySet().iterator();  
			int flag=0;
		     while (iterator33.hasNext()) {
		    	   flag++;
//		    	   System.out.println("1111��111"+flag+"�α���");
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
				            System.out.println("��������������������"+(entry44.getValue()== entry33.getValue())); 
				            System.out.println("%%%%%%%%%%"+(entry44.getKey().equals(entry33.getKey())));
			            }*/
			            if(entry44.getValue().equals(entry33.getValue()) && entry44.getKey().equals(entry33.getKey())) {
//			               System.out.println("�����"+entry33.getKey()+";"+entry33.getValue());
			               BestPath.put(entry33.getKey(), entry33.getValue());
			           }
//			            System.out.println("##############################");
			           
		          }
		           
		      }
		     System.out.println("FT:"+FT);
		     System.out.println("LT:"+LT);
		     System.out.println("1111111111111111111111111");
		     System.out.println("BestPath:"+BestPath); 
		     //���ؼ���������ʱ��ŵ�KeyTime������
		     Iterator < Entry < String, Integer>> iterator66 = BestPath.entrySet().iterator(); 
		     while(iterator66.hasNext()) {
		    	 Entry < String,Integer > entry66 = iterator66.next();
		    	 String name11 = entry66.getKey();
		    	 int durationtime1 = DuraionTime.get(name11);
		    	 KeyTime.put(name11,durationtime1);
		     }
//		     System.out.println("Key activity and duration�� "+KeyTime);
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
		if (postset.size() != 1) {//���㾲Ĭ��Ǩ�ĳ���ֻ��һ��
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
