package net.degrendel.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class AboutDialog extends JDialog {

	
	private static final long serialVersionUID = 8103133740529042448L;
	
	private final JPanel contentPanel = new JPanel();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AboutDialog dialog = new AboutDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutDialog() {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("license/notice");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuffer text = new StringBuffer();
		String ligne;
		try {
			while((ligne = reader.readLine()) != null) {
				text.append(ligne + "\n");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				JTextArea textArea = new JTextArea();
				scrollPane.setViewportView(textArea);
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				textArea.setText(text.toString());
				textArea.setEditable(false);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						dispose();
						
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
			}
		}
	}

}
