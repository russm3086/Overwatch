/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.interfaces.HostInterface;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.state.HostState;

/**
 * 
 * @author rmartine
 * @since
 */
public class Host extends ClusterNodeAbstract implements HostInterface {
	private static final long serialVersionUID = -737300777228526524L;
	private final String sourceClass = this.getClass().getName();
	private final transient Logger logger = Logger.getLogger(sourceClass);
	private ArrayList<Job> listJob = new ArrayList<Job>();

	/**
	 * 
	 */
	public Host(NodeProp nodeProp) {
		// TODO Auto-generated constructor stub
		super(nodeProp);
		setName(nodeProp.getHostname());
		addState(HostState.Normal);
		processAllMem();
		statusCheck();
		setClusterType(SGE_DataConst.clusterTypeHost);

	}

	private void statusCheck() {

		addState(HostState.parseCode(nodeProp.getState()), HostState.Normal);

		if (nodeProp.getSlotTotal() > 0) {
			if (nodeProp.getSlotUsed() == nodeProp.getSlotTotal()) {

				addState(HostState.MaxedSlotUsed);
			}

			if (nodeProp.getSlotTotal() == nodeProp.getSlotReserved()) {

				addState(HostState.MaxedSlotReserved);
			}
		}

		if (nodeProp.getNp_load_avg() >= 5) {

			addState(HostState.HighCpuLoad);
		}

		if (nodeProp.getSlotTotal() <= 0) {

			addState(HostState.NoSLotsAllocated);
		}

		setAvailabilty();
	}

	private void setAvailabilty() {

		StateAbstract state = getState();
		if ((state.between(HostState.Suspended, HostState.MaxedSlotReserved))
				|| (state.between(HostState.Unknown, HostState.Error)) || (isExclusive())) {

			setNodeAvailable(false);
		}
	}

	private void processAllMem() {
		String availableMemory = "NA";

		try {
			String memoryUnit = findMemoryUnit(nodeProp.getMemTotal());
			double memTotal = processMemory(nodeProp.getMemTotal());
			double memUsed = processMemory(nodeProp.getMemUsed());
			double avalMemory = memTotal - memUsed;
			availableMemory = avalMemory + memoryUnit;

			nodeProp.setMemFreeNum(avalMemory);
			nodeProp.setMemTotalNum(memTotal);
			nodeProp.setMemUseNumd(memUsed);

		} catch (NumberFormatException e) {

			logger.log(Level.FINE, "Erorr processing memory", e);
		}

		nodeProp.setMemFree(availableMemory);
	}

	private String findMemoryUnit(String memory) {
		String memoryUnit = "M";

		Pattern pattern = Pattern.compile("([^\\d.])");
		Matcher matcher = pattern.matcher(nodeProp.getMemTotal());

		if (matcher.find()) {
			memoryUnit = matcher.group(0);
		}
		return memoryUnit;
	}

	private double processMemory(String str) {

		String result = removeNonDigits(str);
		double mem = strToDouble(result);
		return mem;
	}

	private String removeNonDigits(String str) {
		String result = str.replaceAll("[^\\d.]", "");
		return result;
	}

	private double strToDouble(String str) throws NumberFormatException {
		double output = 0;
		try {
			output = Double.parseDouble(str);
		} catch (NumberFormatException e) {
			logger.log(Level.FINER, "Error converting to double", e);
		}

		return output;
	}

	public String toString() {

		return getName();
	}

	@Override
	public double getLoad() {

		return nodeProp.getNp_load_avg();
	}

	@Override
	public void setStatus() {
		// TODO Auto-generated method stub
		int exclusiveSlots = 0;
		if (getSlotReserved() == 0) {
			exclusiveSlots = nodeProp.getSlotTotal();
		}

		status = "Free Slots: " + exclusiveSlots + "  Mem Free: " + decimalFormatter.format(nodeProp.getMemFreeNum()) + "  Load: "
				+ decimalFormatter.format(nodeProp.getNp_load_avg());
	}

	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		String summary = "Hostname: " + nodeProp.getHostname();
		summary += "\nLoad: \t\t" + decimalFormatter.format(nodeProp.getNp_load_avg());
		summary += "\nSlot Total: \t\t" + nodeProp.getSlotTotal();
		summary += "\nSlot Reserved: \t\t" + getSlotReserved();
		summary += "\nSlot Used: \t\t" + nodeProp.getSlotUsed();
		summary += "\nMemory Total: \t\t" + nodeProp.getMemTotal();
		summary += "\nMemory Used: \t\t" + decimalFormatter.format(nodeProp.getMemUsedNum());
		summary += "\nMemory Free: \t\t" + decimalFormatter.format(nodeProp.getMemFreeNum());
		summary += "\nCore #: \t\t" + nodeProp.getM_Core();
		summary += "\nState: \t\t" + nodeProp.getState();
		summary += "\nState Description:\t\t" + getStateDescriptions();
		summary += "\nQueue : \t\t" + nodeProp.getHostQueueName();
		summary += "\nJob:\n" + getJobs();

		return summary;
	}

	private String getJobs() {
		StringBuffer sb = new StringBuffer();

		for (Job job : listJob) {
			sb.append(job.getStatus() + "\n");
		}

		return sb.toString();
	}

	@Override
	public ArrayList<Job> getListJob() {
		return listJob;
	}

	@Override
	public int JobCount() {
		return listJob.size();
	}

	@Override
	public void addJob(Job job) {
		setBoExclusive(job.isExclusive());

		if (isExclusive()) {
			addState(HostState.Exclusive);
			setAvailabilty();
		}

		listJob.add(job);
	}

	@Override
	public void setListJob(ArrayList<Job> listJob) {
		this.listJob = listJob;
	}

	@Override
	public void addState(StateAbstract state) {
		addState(state, HostState.Normal);
	}

	@Override
	public double getAvgLoad() {
		return nodeProp.getNp_load_avg();
	}

	@Override
	public int getSlotTotal() {
		return nodeProp.getSlotTotal();
	}

	@Override
	public int getSlotReserved() {
		return nodeProp.getSlotReserved();
	}
	
	@Override
	public int getSlotUsed() {
		return nodeProp.getSlotUsed();
	}
	
	@Override
	public String getMemTotal() {
		return nodeProp.getMemTotal();
	}
	
	@Override
	public double getMemTotalNum() {
		return nodeProp.getMemTotalNum();
	}
	
	@Override
	public String getMemUsedNumStr() {
		return decimalFormatter.format(getMemUsedNum());
	}
	
	@Override
	public double getMemUsedNum() {
		return nodeProp.getMemUsedNum();
	}
	
	@Override
	public double getMemFreeNum() {
		return nodeProp.getMemFreeNum();
	}

	@Override
	public String getMemFreeNumStr() {
		return decimalFormatter.format(getMemFreeNum());
	}
	
	@Override
	public int getMachineCore() {
		return nodeProp.getM_Core();		
	}
	
	@Override
	public double getNp_load_avg() {
		return nodeProp.getNp_load_avg();
	}
	
	@Override
	public String getMetaData() {
		StringBuffer output = new StringBuffer();
		output.append(super.getMetaData());
		output.append("\nLoad: " + getAvgLoad());
		output.append("\nCore Total: " + getSlotTotal());
		output.append("\nCore Reserved: " + getSlotReserved());
		output.append("\nCore Used: " + getSlotUsed());
		output.append("\nMemory Total: " + getMemTotal());
		output.append("\nMemory Used: " + getMemUsedNumStr());
		output.append("\nMemory Free: " + getMemFreeNumStr());
		output.append("\nCore Machine: " + getMachineCore());
		output.append("\nJob:\n" + getJobs());

		return output.toString();
	}

}
