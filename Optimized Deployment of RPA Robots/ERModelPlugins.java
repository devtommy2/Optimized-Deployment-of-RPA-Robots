package org.processmining.ERModel.plugins;

import java.text.ParseException;

import javax.swing.JOptionPane;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.processmining.ERModel.visualize.TransformCrossOrganizationBusinessProcessModel2PetriNet;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.InductiveMiner.plugins.IMPetriNet;
import org.processmining.plugins.InductiveMiner.plugins.dialogs.IMMiningDialog;


public class ERModelPlugins {
//	@SuppressWarnings("null")
	@Plugin(
             name = "TimePetri Net Discovery ", 
         			parameterLabels = { "Log" }, 
        			returnLabels = { "EResourseModel" },
        			returnTypes = {EResourseModel.class},
             help = "."
     )	
     @UITopiaVariant(
             affiliation = "SDUT", 
             author = "HuiLing LI", 
             email = "17864309765@163.com"
     )
	
	public EResourseModel  mineGuiPetrinet(UIPluginContext context, XLog log) throws ParseException {
		EResourseModel ermodel = new EResourseModel();
		Time_MAX_MIN time = new Time_MAX_MIN();
				
		IMMiningDialog dialog = new IMMiningDialog(log);
		context.log("Mining...");
	
		Object[] IMP=IMPetriNet.minePetriNet(context, log, dialog.getMiningParameters());
		Petrinet pn =TransformCrossOrganizationBusinessProcessModel2PetriNet.
				addingArtifitialSouceandTargetPlaces((Petrinet) IMP[0], 
						(Marking)IMP[1], (Marking)IMP[2]);
		
		ermodel.setPn(pn);
		ermodel.settime_max_min(time.Time_max_min(log, pn));
		
		return ermodel;
	}
	public static boolean confirmLargeLogs(final UIPluginContext context, XLog log, IMMiningDialog dialog) {
		if (dialog.getVariant().getWarningThreshold() > 0) {
			XEventClassifier classifier = dialog.getMiningParameters().getClassifier();
			XLogInfo xLogInfo = XLogInfoFactory.createLogInfo(log, classifier);
			int numberOfActivities = xLogInfo.getEventClasses().size();
			if (numberOfActivities > dialog.getVariant().getWarningThreshold()) {
				int cResult = JOptionPane.showConfirmDialog(null,
						dialog.getVariant().toString() + " might take a long time, as the event log contains "
								+ numberOfActivities
								+ " activities.\nThe chosen variant of Inductive Miner is exponential in the number of activities.\nAre you sure you want to continue?",
						"Inductive Miner might take a while", JOptionPane.YES_NO_OPTION);

				return cResult == JOptionPane.YES_OPTION;
			}
		}
		return true;
	}
}
	
