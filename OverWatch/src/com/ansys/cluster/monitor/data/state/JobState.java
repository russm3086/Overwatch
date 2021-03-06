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
public class JobState extends StateAbstract {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6007505775115442111L;
	private static final String QueuedDesc = "Job is in a Queued state";
	private static final String RunningDesc = "Running state. The job is running on the execution host.";
	private static final String TransferringDesc = "Job is in a transferring state. The job is sent to the execution host.";
	private static final String DeletionDesc = "The job is in a deletion state. The job is currently deleted by the system.";
	private static final String ErrorDesc = "The job is in an error state.";
	private static final String RestartedDesc = "The job was restarted.";
	private static final String SuspendedThresholdDesc = "The job is in a suspended state because of threshold limitations.";
	private static final String WaitingDesc = "The job is in a waiting state.";
	private static final String HoldDesc = "The job is in a hold state. The hold state prevents scheduling of the job.";
	private static final String SuspendedAutoDesc = "The job is in an automatic suspended state. The job suspension was triggered not directly.";
	private static final String SuspendedManualDesc = "The job is in a manual suspend state. The job suspension was triggered manually.";
	private static final String ZombieDesc = "The job is in a zombie state.";
	private static final String IdleDesc = "The job is in an idle state";
	private static final String UnknownDesc = "Unknown status job";

	public JobState(String name, int value, String description, Color color) {
		super(name, value, description, color);
	}

	public static TreeMap<Integer, StateAbstract> parseCode(String code) {

		TreeMap<Integer, StateAbstract> store = parseCode(code, new JobStateParser());

		return store;
	}

	public static final JobState RunningState = new JobState("Running", 3300, RunningDesc, Color.GREEN);

	public static final JobState Zombie = new JobState("Zombie", 3200, ZombieDesc, Color.RED);

	public static final JobState Idle = new JobState("Idle", 3100, IdleDesc, Color.RED);

	public static final JobState Unknown = new JobState("Unknown", 3000, UnknownDesc, Color.RED);

	public static final JobState Error = new JobState("Error", 2900, ErrorDesc, Color.RED);

	public static final JobState SuspendedThreshold = new JobState("Suspended Threshold", 2800,
			SuspendedThresholdDesc, Color.YELLOW);

	public static final JobState SuspendedAuto = new JobState("Suspended Automatically", 2700, SuspendedAutoDesc, Color.YELLOW);

	public static final JobState SuspendedManual = new JobState("Suspended Manual", 2600, SuspendedManualDesc, Color.YELLOW);

	public static final JobState Transferring = new JobState("Transferring", 2500, TransferringDesc, Color.YELLOW);

	public static final JobState Restarted = new JobState("Restarted", 2400, RestartedDesc, Color.YELLOW);

	public static final JobState Queued = new JobState("Queued", 2300, QueuedDesc, Color.YELLOW);

	public static final JobState Waiting = new JobState("Waiting", 2200, WaitingDesc, Color.YELLOW);

	public static final JobState Hold = new JobState("Hold", 2100, HoldDesc, Color.YELLOW);

	public static final JobState Deletion = new JobState("Deletion", 2000, DeletionDesc, Color.YELLOW);

	
}
