/**
  * Copyright (c) 2019 ANSYS and/or its affiliates. All rights reserved.
  * ANSYS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  * 
*/
package com.ansys.cluster.monitor.data.interfaces;

import java.io.Serializable;

import com.russ.test.DetailedInfoProp;


/**
 * 
 * @author rmartine
 * @since
 */
public interface ClusterNodeInterface extends Serializable {

	public DetailedInfoProp getDetailedInfoProp();
	public String getSummary();
	public String toString();
	public String getName();
	public void addState(StateAbstract state);
	public String getMetaData();

}
