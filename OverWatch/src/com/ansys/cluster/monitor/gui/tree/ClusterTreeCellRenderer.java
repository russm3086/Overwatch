/**
 * 
 */
package com.ansys.cluster.monitor.gui.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import com.ansys.cluster.monitor.data.SGE_DataConst;
import com.ansys.cluster.monitor.data.interfaces.AnsQueueAbstract;
import com.ansys.cluster.monitor.data.interfaces.ClusterNodeAbstract;
import com.ansys.cluster.monitor.data.interfaces.StateAbstract;
import com.ansys.cluster.monitor.data.state.HostState;
import com.ansys.cluster.monitor.data.state.JobState;
import com.ansys.cluster.monitor.gui.GUI_Const;
import com.russ.util.nio.ResourceLoader;

/**
 * @author rmartine
 *
 */
public class ClusterTreeCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {
	private final String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	private boolean selected;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4973235028983695396L;

	/**
	 * 
	 */
	public ClusterTreeCellRenderer() {
		// TODO Auto-generated constructor stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.
	 * JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		// TODO Auto-generated method stub

		this.selected = selected;
		JLabel label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setPreferredSize(new Dimension(200, 25));
		label.setBorder(getBorderSelect());

		Object object = ((DefaultMutableTreeNode) value).getUserObject();

		if (!(object instanceof String)) {

			ClusterNodeAbstract node = (ClusterNodeAbstract) object;
			logger.finer("Processing tree node: " + node);
			StateAbstract state = node.getState();

			label.setToolTipText(node.getToolTip());

			try {
				switch (node.getClusterType()) {

				case SGE_DataConst.clusterTypeJob:
					return jobStateProcessing(state, node);

				case SGE_DataConst.clusterTypeHost:
					return hostStateProcessing(state, node);

				case SGE_DataConst.clusterTypeQueue:
				case SGE_DataConst.clusterTypeQuota:
					return queueStateProcessing(state, node);

				}

			} catch (IOException e) {

				logger.log(Level.FINER, "IO Error", e);
			}
			label.setText(node.getName());

		} else {

			label.setText((String) object);
		}

		return label;
	}

	private Component queueStateProcessing(StateAbstract state, ClusterNodeAbstract node) throws IOException {

		AnsQueueAbstract queue = (AnsQueueAbstract) node;
		Component component = null;

		if (queue.getMembersType().equalsIgnoreCase(SGE_DataConst.clusterTypeHost)
				|| queue.getMembersType().equalsIgnoreCase(SGE_DataConst.clusterTypeQuota)) {
			int resAvailable;
			int resTotal;
			int resUnavailable;

			if (node.isVisualNode()) {

				resAvailable = queue.getSessionAvailable();
				resTotal = queue.getSessionTotal();
				resUnavailable = queue.getSessionUnavailable();

			} else if (queue.getMembersType().equalsIgnoreCase(SGE_DataConst.clusterTypeQuota)) {

				resAvailable = queue.getQuotaLimit() - queue.getQuotaUsage();
				resTotal = queue.getQuotaLimit();
				resUnavailable = queue.getQuotaUsage();
			} else {

				resAvailable = queue.getCoreAvailable();
				resTotal = queue.getCoreTotal();
				resUnavailable = queue.getCoreUnavailable();
			}

			JPanel panel = new ProgressBar(node.getQueueName(), resAvailable, resUnavailable, resTotal,
					queue.getUnitRes(), queue.getToolTip());

			panel.setBorder(getBorderSelect());
			component = panel;

		} else {

			String text = new String();

			if (queue.getMembersType().equalsIgnoreCase(SGE_DataConst.clusterTypeJob)) {
				text = queue.getName() + " - " + queue.size() + " job(s)";
			} else {
				text = queue.getName();
			}

			JLabel lblNewLabel = new JLabel(text);
			lblNewLabel.setPreferredSize(new Dimension(150, 25));
			lblNewLabel.setToolTipText(queue.getToolTip());
			lblNewLabel.setBorder(getBorderSelect());
			component = lblNewLabel;
		}

		return component;
	}

	private Component hostStateProcessing(StateAbstract state, ClusterNodeAbstract node) throws IOException {

		JLabel lblNewLabel = new JLabel();

		if (state.between(HostState.MinorCPUAllocation, HostState.SuspendedCalendar)) {

			lblNewLabel.setToolTipText(state.getDescription() + "\n\t" + node.getToolTip());
			lblNewLabel.setIcon(new ImageIcon(ResourceLoader.load(GUI_Const.Icon_YellowLight_Small_Path)));
		}

		if (state.between(HostState.Unknown, HostState.Error)) {

			lblNewLabel.setToolTipText(HostState.Error.getDescription() + "\n\t" + node.getToolTip());
			lblNewLabel.setIcon(new ImageIcon(ResourceLoader.load(GUI_Const.Icon_Skull_and_Bones_Small_Path)));
		}

		if (state.equals(HostState.Normal)) {
			lblNewLabel.setToolTipText(HostState.Normal.getDescription() + "\n\t" + node.getToolTip());
			lblNewLabel.setIcon(new ImageIcon(ResourceLoader.load(GUI_Const.Icon_GreenLight_Small_Path)));

		}

		if (state.between(HostState.MaxedSlotUsed, HostState.AlarmThreshold)) {

			lblNewLabel.setToolTipText(state.getDescription() + "\n\t" + node.getToolTip());
			lblNewLabel.setIcon(new ImageIcon(ResourceLoader.load(GUI_Const.Icon_RedLight_Small_Path)));

		}

		if (state.between(HostState.DisabledAuto, HostState.DisabledManually)) {

			lblNewLabel.setToolTipText(state.getDescription() + "\n\t" + node.getToolTip());
			lblNewLabel.setIcon(new ImageIcon(ResourceLoader.load(GUI_Const.Icon_Skull_and_Bones_Small_Path)));

		}

		if (state.equals(HostState.MaxedSlotReserved)) {

			lblNewLabel.setToolTipText(HostState.MaxedSlotReserved.getDescription() + "\n\t" + node.getToolTip());

			Font font = lblNewLabel.getFont();
			Hashtable<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();
			map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			font = font.deriveFont(map);
			lblNewLabel.setFont(font);
			lblNewLabel.setIcon(new ImageIcon(ResourceLoader.load(GUI_Const.Icon_RedLight_Small_Path)));
			lblNewLabel.setEnabled(false);
		}

		if (state.equals(HostState.OverProvisioned)) {

			lblNewLabel.setToolTipText(HostState.OverProvisioned.getDescription() + "\n\t" + node.getToolTip());
			lblNewLabel.setIcon(new ImageIcon(ResourceLoader.load(GUI_Const.Icon_Radiation_Small_Path)));
		}

		lblNewLabel.setText(node.toString());

		lblNewLabel.setBorder(getBorderSelect());

		return lblNewLabel;

	}

	private Component jobStateProcessing(StateAbstract state, ClusterNodeAbstract node) throws IOException {

		JLabel label = new JLabel();

		label.setBorder(getBorderSelect());

		if (state.between(JobState.Restarted, JobState.RunningState)) {

			label.setIcon(new ImageIcon(ResourceLoader.load(GUI_Const.Icon_GreenLight_Small_Path)));
			// setToolTipState(label, state.getName(), node.getToolTip());
		}

		if (state.between(JobState.Deletion, JobState.SuspendedThreshold)) {

			label.setIcon(new ImageIcon(ResourceLoader.load(GUI_Const.Icon_YellowLight_Small_Path)));
			// label.setToolTipText("<html>"+ state.getDescription() + "<BR>" +
			// node.getToolTip()+"</html>");

		}

		if (state.equals(JobState.Zombie)) {
			label.setIcon(new ImageIcon(ResourceLoader.load(GUI_Const.Icon_Zombie_Small_Path)));
			// label.setToolTipText(state.getDescription() + "\n\t" + node.getToolTip());
		}

		if (state.equals(JobState.Idle)) {
			label.setIcon(new ImageIcon(ResourceLoader.load(GUI_Const.Icon_BlueLight_Small_Path)));
			// label.setToolTipText(state.getDescription() + "\n\t" + node.getToolTip());
		}

		if (state.between(JobState.SuspendedThreshold, JobState.Unknown)) {

			label.setIcon(new ImageIcon(ResourceLoader.load(GUI_Const.Icon_Skull_and_Bones_Small_Path)));
			// label.setToolTipText(state.getDescription() + "\n\t" + node.getToolTip());
		}

		setToolTipState(label, state.getName(), node.getToolTip());
		label.setText(node.getNodeProp().getJobOwner() + " - " + node.getNodeProp().getJobNumber());

		return label;

	}

	private void setToolTipState(JLabel label, String state, String toolTip) {
		StringBuffer sb = new StringBuffer("<html>");
		sb.append("State: ");
		sb.append(state);
		sb.append("<BR>");
		sb.append(toolTip);
		sb.append("</html>");

		label.setToolTipText(sb.toString());

	}

	private Border getBorderSelect() {
		Border border;
		if (selected) {

			border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true),
					BorderFactory.createEmptyBorder(2, 2, 2, 2));

		} else {

			border = null;
			// border = BorderFactory.createLineBorder(Color.WHITE, 0);
		}
		return border;

	}
}
