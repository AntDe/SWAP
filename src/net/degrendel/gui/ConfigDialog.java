package net.degrendel.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.degrendel.ActionCfgList;
import net.degrendel.StartWebApp;

@SuppressWarnings("serial")
public class ConfigDialog extends JDialog {

	private ConfigGui configGui = new ConfigGui();
	private StartWebApp startWebApp;

	/**
	 * Create the dialog.
	 */
	public ConfigDialog(StartWebApp startWebApp) {
		this.startWebApp = startWebApp;
		setAlwaysOnTop(true);
		setBounds(100, 100, 754, 300);
		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(configGui, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						generateActionCfgList();
						dispose();
					}
				});
				buttonPane.add(okButton);

				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				{
					JButton btnApply = new JButton("Apply");
					btnApply.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							generateActionCfgList();
						}

					});
					buttonPane.add(btnApply);
				}
				buttonPane.add(cancelButton);
			}
		}
	}

	protected void generateActionCfgList() {
		ActionCfgList acl = configGui.generateActionCfgList();
		startWebApp.setActionCfgList(acl);
	}

	public void setActionCfgList(ActionCfgList actionCfgList) {
		configGui.setActionCfgList(actionCfgList);
	}

}
