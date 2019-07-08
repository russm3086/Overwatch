/**
 * 
 */
package com.ansys.cluster.monitor.data.state;

import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.interfaces.StateParserInterface;

/**
 * @author rmartine
 *
 */
public class HostStateParser implements StateParserInterface {

	/**
	 * 
	 */
	public HostStateParser() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ansys.cluster.monitor.data.interfaces.StateParserInterface#parseCode(
	 * char)
	 */
	@Override
	public StateAbstract parseCode(char code) {
		// TODO Auto-generated method stub

		switch (code) {

		case 'a':
			return HostState.AlarmThreshold;

		case 'A':
			return HostState.Alarm;

		case 'C':
			return HostState.SuspendedCalendar;

		case 's':
			return HostState.Suspended;

		case 'S':
			return HostState.SuspendedAuto;

		case 'd':
			return HostState.DisabledManually;

		case 'D':
			return HostState.DisabledAuto;

		case 'E':
			return HostState.Error;

		case 'u':
			return HostState.Unknown;

		}

		return HostState.Normal;

	}

}
