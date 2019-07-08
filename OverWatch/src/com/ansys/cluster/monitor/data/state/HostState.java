/**
 * 
 */
package com.ansys.cluster.monitor.data.state;

import java.awt.Color;
import java.util.TreeMap;

import com.ansys.cluster.monitor.data.interfaces.StateAbstract;

/**
 * @author rmartine
 *
 */
public class HostState extends StateAbstract {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1659897637112303205L;
	private static final String AlarmThresholdDesc = "Alarm state (because of load threshold or also when host is not reachable)";
	private static final String AlarmDesc = "Alarm state";
	private static final String UnknownDesc = "Unknown state: The execution daemon is not reachable.";
	private static final String SuspendedCalendarDesc = "Calendar suspended";
	private static final String SuspendedDesc = "Suspended";
	private static final String SuspendedAutoDesc = "Automatically suspended";
	private static final String DisabledManuallyDesc = "Manually disabled (qmod -d)";
	private static final String DisabledAutoDesc = "Automatically disabled";
	private static final String ErrorDesc = "Error state";
	private static final String NormalOperationDesc = "The host is running under normal operation";
	private static final String MaxedSlotUsedDesc = "The host is running with Maxed Slot(s) Utilized";
	private static final String MaxedSlotReservedDesc = "The host is running with all slots reserved";
	private static final String HighCpuLoadDesc = "The host is running with a high cpu load";
	private static final String NoSLotsAllocatedDesc = "The host has noconfigured slots for usage";
	private static final String ExclsiveDesc = "The host is running in exclusive running";

	/**
	 * @param name
	 * @param value
	 * @param description
	 */
	public HostState(String name, int value, String description, Color color) {
		super(name, value, description, color);
		// TODO Auto-generated constructor stub
	}

	public static TreeMap<Integer, StateAbstract> parseCode(String code) {

		TreeMap<Integer, StateAbstract> store = parseCode(code, new HostStateParser());

		return store;
	}

	public static final HostState Normal = new HostState("Normal", 1500, NormalOperationDesc, Color.GREEN);

	public static final HostState Error = new HostState("Error", 1400, ErrorDesc, Color.RED);

	public static final HostState Unknown = new HostState("Unknown", 1300, UnknownDesc, Color.RED);

	public static final HostState AlarmThreshold = new HostState("Alarm State Threshold", 1200, AlarmThresholdDesc,
			Color.RED);

	public static final HostState Alarm = new HostState("Alarm State", 1100, AlarmDesc, Color.RED);

	public static final HostState MaxedSlotReserved = new HostState("Maxed Slots Reserved", 1000, MaxedSlotReservedDesc,
			Color.RED);

	public static final HostState Exclusive = new HostState("Exclusive Usage", 900, ExclsiveDesc, Color.RED);

	public static final HostState NoSLotsAllocated = new HostState("No slots allocated", 800, NoSLotsAllocatedDesc,
			Color.RED);

	public static final HostState MaxedSlotUsed = new HostState("Maxed Slots", 700, MaxedSlotUsedDesc, Color.RED);

	public static final HostState DisabledManually = new HostState("Manually disabled", 600, DisabledManuallyDesc,
			Color.RED);

	public static final HostState DisabledAuto = new HostState("Automatically disabled", 500, DisabledAutoDesc,
			Color.RED);

	public static final HostState SuspendedCalendar = new HostState("Calendar suspended", 400, SuspendedCalendarDesc,
			Color.YELLOW);

	public static final HostState SuspendedAuto = new HostState("Automatically suspended", 300, SuspendedAutoDesc,
			Color.YELLOW);

	public static final HostState Suspended = new HostState("Suspended", 200, SuspendedDesc, Color.YELLOW);

	public static final HostState HighCpuLoad = new HostState("High Cpu Load", 100, HighCpuLoadDesc, Color.YELLOW);

}
