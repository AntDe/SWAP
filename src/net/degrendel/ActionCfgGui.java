package net.degrendel;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.Container;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class ActionCfgGui extends JPanel {

	private static final long serialVersionUID = -4594918943839666239L;
	private JTextField nameTextField;
	private JTextField linkTextField;
	private JButton btnRemove;
	private JCheckBox chckbxToCopy;

	public void setActionCfg(ActionCfg actionCfg) {
		nameTextField.setText(actionCfg.getName());
		linkTextField.setText(actionCfg.getLink());
		chckbxToCopy.setSelected(actionCfg.getToCopy());
		validate();
	}
	
	public ActionCfg getActionCfg() {
		ActionCfg actionCfg = new ActionCfg(nameTextField.getText(), linkTextField.getText());
		actionCfg.setToCopy(chckbxToCopy.isSelected());
		return actionCfg;
	}
	
	/**
	 * Create the panel.
	 */
	public ActionCfgGui() {

		JLabel lblButtonName = new JLabel("Name:");

		nameTextField = new JTextField();
		nameTextField.setColumns(10);

		JLabel lblLink = new JLabel("Link:");

		linkTextField = new JTextField();
		linkTextField.setColumns(10);

		btnRemove = new JButton("remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Container parent = ActionCfgGui.this.getParent();
				if (parent != null) {
					parent.remove(ActionCfgGui.this);
					parent.revalidate();
				}
			}
		});

		chckbxToCopy = new JCheckBox("to Copy");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout
				.setHorizontalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(
										groupLayout.createSequentialGroup().addContainerGap()
												.addComponent(lblButtonName).addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblLink)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(linkTextField, GroupLayout.DEFAULT_SIZE, 204,
														Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(chckbxToCopy)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnRemove)
												.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addGap(5)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblButtonName)
								.addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblLink)
								.addComponent(linkTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnRemove).addComponent(chckbxToCopy))));
		setLayout(groupLayout);

	}
}
