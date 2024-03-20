package org.processmining.ERModel.plugins;

import java.util.HashMap;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetImpl;

/*
 * this class defines the cross-organization business process model
 * It involves 
 * (1) a set of organizational models that each is represented as a flat Petri net.
 * (2) a set of interactions (message-based) that each is represented as a sender activity and a receiver activity.
 */
public class EResourseModel {

/*******以下*************************************************************/
	private HashMap<String, int[]> TimeRegion;
	private HashMap<XEventClass, HashMap<Petrinet, String>> XEventClass2crossOrg;

	private Petrinet pn;
	
	public EResourseModel()	{
		
		this.TimeRegion = new HashMap<String, int[]>();
		this.XEventClass2crossOrg = new HashMap<XEventClass, HashMap<Petrinet, String>>();
		this.pn = new PetrinetImpl("");
	}
	/****************************/
	public HashMap<String, int[]> gettime_max_min() {
		return TimeRegion;		
	}
	public void settime_max_min(HashMap<String, int[]> timeregion) {
		TimeRegion = timeregion;
	}
	
		
	
	public Petrinet getPn() {
		return pn;
	}

	public void setPn(Petrinet pn) {
		this.pn = pn;
	}
	///////xeventclass -- hash<set>Org

	
/**********以上******************************************************************/	
	
	
	// the organization models 
	private HashMap<String, Petrinet> org2PN = new HashMap<String, Petrinet>();
	
	//set organization models
	public void setOrganizationModels(HashMap<String, Petrinet> org2pns)
	{
		this.org2PN=org2pns;
	}
	
	//get all organizations
	public Set<String> getAllOrganizations()
	{
		return org2PN.keySet();
	}
	
	//get the pn for an organization
	public Petrinet getOrganizationModel(String org) 
	{
		return org2PN.get(org);
	}
/*	public class Gettmin{
		private String name;
		private int tmin;
		public Gettmin(String name,int tmin) {
			this.name = name;
			this.tmin = tmin;
		}
		public String getName() {
			return name;
		}
		public int getTmin() {
			return tmin;
		}
	}*/
}
