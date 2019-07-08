/**
 * 
 */
package com.ansys.cluster.monitor.gui;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * @author rmartine
 * @param <E>
 *
 */
public class ConnectionComboModel extends AbstractListModel<String> implements ComboBoxModel<String> {
	private SGE_MonitorProp mainProps = null;
	private ArrayList<String> lstCluster = new ArrayList<String>();
	private String selected = new String();

	/**
	 * 
	 */
	private static final long serialVersionUID = 5075785134539614555L;

	
	public ConnectionComboModel(SGE_MonitorProp mainProps) {
		
		setMainProps(mainProps);
		findCluster();
		
	}
	
	@Override
	public int getSize() {
		return lstCluster.size();
	}

	@Override
	public String getElementAt(int index) {
		return lstCluster.get(index);
	}

	public void setSelectedItem(Object anItem) {
		selected = (String)anItem;
	}

	@Override
	public Object getSelectedItem() {
		return selected;
	}

	/**
	 * @return the mainProps
	 */
	public SGE_MonitorProp getMainProps() {
		return mainProps;
	}

	/**
	 * @param mainProps the mainProps to set
	 */
	public void setMainProps(SGE_MonitorProp mainProps) {
		this.mainProps = mainProps;
	}

	private void findCluster(){
		boolean cont = true;
		int item = 0;
		
		while(cont) {
			
			String cluster = mainProps.getClusterName(item);
			
			if(cluster !=null) {
				lstCluster.add(cluster);
			}else {
				
				cont = false;
			}
			
			item += 1;
		}
	}
	
}
