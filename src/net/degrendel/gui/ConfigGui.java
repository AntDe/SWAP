package net.degrendel.gui;

import javax.swing.JPanel;
import java.util.Iterator;
import javax.swing.JScrollPane;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import net.degrendel.ActionCfg;
import net.degrendel.ActionCfgList;
import net.degrendel.StartWebApp;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class ConfigGui extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5639205969100307498L;
	private JScrollPane scrollPane;
	private JPanel cfgPanel;
	private JCheckBox chckbxAlwayOnTop;
	private JCheckBox chckbxRotateWebCam;

	/**
	 * Create the panel.
	 */
	public ConfigGui() {

		scrollPane = new JScrollPane();

		cfgPanel = new JPanel();
		scrollPane.setViewportView(cfgPanel);
		cfgPanel.setLayout(new BoxLayout(cfgPanel, BoxLayout.PAGE_AXIS));

		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cfgPanel.add(new ActionCfgGui());
				validate();
			}
		});
		buttonPanel.add(btnAdd);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(buttonPanel, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE).addComponent(
										scrollPane, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE))
						.addGap(0)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE).addGap(1)
						.addComponent(buttonPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)));

		JButton btnDefault = new JButton("Default");
		btnDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ActionCfgList acl = StartWebApp.getDefaultActionCfgList();
				setActionCfgList(acl);
			}
		});
		buttonPanel.add(btnDefault);
		
		chckbxAlwayOnTop = new JCheckBox("Alway on Top");
		buttonPanel.add(chckbxAlwayOnTop);
		
		chckbxRotateWebCam = new JCheckBox("Rotate Webcam");
		buttonPanel.add(chckbxRotateWebCam);
		setLayout(groupLayout);
	}

	public void setActionCfgList(ActionCfgList actionCfgList) {
		cfgPanel.removeAll();
		repaint();
		for (Iterator<ActionCfg> iterator = actionCfgList.iterator(); iterator.hasNext();) {
			ActionCfg actionCfg = iterator.next();
			ActionCfgGui actionCfgGui = new ActionCfgGui();
			actionCfgGui.setActionCfg(actionCfg);
			cfgPanel.add(actionCfgGui);
		}
		chckbxAlwayOnTop.setSelected(actionCfgList.isOnTop());
		chckbxRotateWebCam.setSelected(actionCfgList.isWebcamRotated());
		validate();
	}

	public ActionCfgList generateActionCfgList() {
		ActionCfgList acl = new ActionCfgList();
		acl.setOnTop(chckbxAlwayOnTop.isSelected());
		acl.setWebcamRotated(chckbxRotateWebCam.isSelected());
		for (Component c : cfgPanel.getComponents()) {
			if (c instanceof ActionCfgGui) {
				ActionCfgGui aCfgGui = (ActionCfgGui) c;
				ActionCfg actionCfg = aCfgGui.getActionCfg();
				if (!actionCfg.getName().isEmpty() && !actionCfg.getLink().isEmpty()) {
					acl.addActionCfg(actionCfg);
				}
			}
		}
		return acl;
	}
}
