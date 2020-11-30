/**
 * 
 */
package com.ansys.cluster.monitor.data.state;

import com.ansys.cluster.monitor.data.interfaces.StateParserInterface;

/**
 * @author rmartine
 *
 */
public class JobStateParser implements StateParserInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5009245317587990022L;

	/**
	 * 
	 */
	public JobStateParser() {
		
	}

	/* (non-Javadoc)
	 * @see com.ansys.cluster.monitor.data.StateParserInterface#parseCode(char)
	 */
	@Override
	public JobState parseCode(char code) {

		switch (code) {

		case 'r':
			return JobState.RunningState;

		case 't':
			return JobState.Transferring;

		case 'd':
			return JobState.Deletion;

		case 'E':
			return JobState.Error;

		case 'R':
			return JobState.Restarted;

		case 'T':
			return JobState.SuspendedThreshold;

		case 'w':
			return JobState.Waiting;

		case 'h':
			return JobState.Hold;

		case 'S':
			return JobState.SuspendedAuto;

		case 's':
			return JobState.SuspendedManual;

		case 'z':
			return JobState.Zombie;

		case 'q':
			return JobState.Queued;

		}
		return JobState.Unknown;

	}

}
