package net.degrendel;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.DefaultPersistenceDelegate;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPicker;
import com.google.zxing.Result;

import net.degrendel.gui.AboutDialog;
import net.degrendel.gui.ConfigDialog;
import net.degrendel.gui.ScanJpanel;

public class StartWebApp {

	private JFrame frmAppStartApp;
	private JTextField textFieldAppNum;
	private JPanel buttonPanel = new JPanel();
	private final Action actionScan = new ActionScan();
	@SuppressWarnings("serial")
	private Action actionCopy = new AbstractAction("Copy") {
		public void actionPerformed(ActionEvent arg0) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection strSel = new StringSelection(getAppnumber(false));
			clipboard.setContents(strSel, null);
		}
	};
	private ActionCfgList actionCfgList;

	/**
	 * @param actionCfgList
	 *            the actionCfgList to set
	 */
	public void setActionCfgList(ActionCfgList actionCfgList) {
		this.actionCfgList = actionCfgList;
		saveActionCfgList(actionCfgList, prefFile);
		initButtonPanel();
	}

	private JPopupMenu popupMenu;
	private File prefFile = new File(System.getenv("APPDATA"), PREF_FILE_NAME);
	private MouseListener mousePopupListner = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			mayBeShowPopup(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mayBeShowPopup(e);
		}

		private void mayBeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopup(e);
			}
		}
	};

	static final String PREF_FILE_NAME = "StartWebApp/StartWebApp.cfg";
	private JPanel mainPanel;
	private JButton btnScan;
	private JLabel lblApplicationNumber;
	private JButton btnCopy;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
			if ("Windows".equals(info.getName())) {
				try {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					StartWebApp window = new StartWebApp();
					window.frmAppStartApp.pack();
					window.frmAppStartApp.setVisible(true);
					window.RequestFocusToTextfield();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void RequestFocusToTextfield() {
		textFieldAppNum.requestFocusInWindow();
	}

	/**
	 * Create the application.
	 */
	public StartWebApp() {

		// gui init
		initialize();
		initButtonPanel();
	}

	private void initButtonPanel() {

		btnCopy = new JButton("Copy");
		btnCopy.addMouseListener(mousePopupListner);
		btnCopy.setAction(actionCopy);

		mainPanel.add(btnCopy);
		mainPanel.add(buttonPanel);

		this.buttonPanel.removeAll();
		// get Action Config
		this.actionCfgList = getActionCfgList();
		// set on top
		this.frmAppStartApp.setAlwaysOnTop(actionCfgList.isOnTop());

		// create action
		List<Action> listOfAction = new LinkedList<Action>();
		for (Iterator<ActionCfg> iterator = actionCfgList.iterator(); iterator.hasNext();) {
			ActionCfg actionCfg = iterator.next();
			listOfAction.add(new MyAction(actionCfg));
		} // Create button action
		for (Iterator<Action> iterator = listOfAction.iterator(); iterator.hasNext();) {
			Action action = (Action) iterator.next();
			JButton jButton = new JButton(action);
			jButton.addMouseListener(mousePopupListner);
			this.buttonPanel.add(jButton);
		}
		buttonPanel.validate();
		buttonPanel.repaint();
	}

	private ActionCfgList getActionCfgList() {
		if (this.actionCfgList != null) {
			return actionCfgList;
		}
		if (prefFile.exists()) {
			XMLDecoder dec;
			ActionCfgList actionCfgList = null;
			try {
				dec = new XMLDecoder(new BufferedInputStream(new FileInputStream(prefFile)));
				actionCfgList = (ActionCfgList) dec.readObject();
				dec.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			this.actionCfgList = actionCfgList;
			return actionCfgList;
		} else {
			ActionCfgList actionCfgList = getDefaultActionCfgList();
			saveActionCfgList(actionCfgList, prefFile);
			JOptionPane.showMessageDialog(null,
					"a new config file as been created : \n" + prefFile + "\n it can be modify to change the button",
					"info", JOptionPane.INFORMATION_MESSAGE);
			this.actionCfgList = actionCfgList;
			return actionCfgList;
		}
	}

	private void saveActionCfgList(ActionCfgList actionList, File pref_file) {
		FileOutputStream fileOutputStream = null;
		File path = pref_file.getParentFile();
		path.mkdirs();
		try {
			fileOutputStream = new FileOutputStream(pref_file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		XMLEncoder e = new XMLEncoder(new BufferedOutputStream(fileOutputStream));
		String[] properties = { "name", "link" };
		e.setPersistenceDelegate(ActionCfg.class, new DefaultPersistenceDelegate(properties));
		e.writeObject(actionList);
		e.close();

	}

	static public ActionCfgList getDefaultActionCfgList() {

		XMLDecoder dec;
		ActionCfgList actionCfgList = null;

		InputStream inputStream = StartWebApp.class.getClassLoader().getResourceAsStream("StartWebApp.cfg");

		dec = new XMLDecoder(new BufferedInputStream(inputStream));
		actionCfgList = (ActionCfgList) dec.readObject();
		dec.close();
		return actionCfgList;
	}

	private String getAppnumber(Boolean forceScan) {
		String appNum = textFieldAppNum.getText();
		if (forceScan || (appNum == null) || (appNum.equals(""))) {
			this.actionScan.actionPerformed(null);
			appNum = textFieldAppNum.getText();
		}
		appNum = appNum.replaceAll(" ", "");
		return appNum;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAppStartApp = new JFrame();
		frmAppStartApp.getContentPane().addMouseListener(mousePopupListner);
		frmAppStartApp.setResizable(false);
		frmAppStartApp.setTitle("App Start App");
		frmAppStartApp.setBounds(100, 100, 460, 69);
		frmAppStartApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new JPanel();

		btnScan = new JButton("Scan");
		btnScan.addMouseListener(mousePopupListner);
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		btnScan.setAction(actionScan);

		lblApplicationNumber = new JLabel("Application number:");

		textFieldAppNum = new JTextField();
		textFieldAppNum.addMouseListener(mousePopupListner);
		textFieldAppNum.setColumns(10);
		buttonPanel.setBorder(null);
		buttonPanel.addMouseListener(mousePopupListner);
		buttonPanel.setLayout(new GridLayout(1, 0, 0, 0));

		mainPanel.add(btnScan);
		mainPanel.add(lblApplicationNumber);
		mainPanel.add(textFieldAppNum);

		frmAppStartApp.getContentPane().add(mainPanel);
	}

	private void showPopup(MouseEvent e) {
		if (popupMenu == null) {
			popupMenu = new JPopupMenu();
			JMenuItem menuConfig = new JMenuItem("Config");
			menuConfig.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					ConfigDialog configDialog = new ConfigDialog(StartWebApp.this);
					configDialog.setActionCfgList(getActionCfgList());
					configDialog.setVisible(true);
				}
			});
			popupMenu.add(menuConfig);
			JMenuItem menuAbout = new JMenuItem("About");
			menuAbout.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					AboutDialog dial = new AboutDialog();
					dial.setVisible(true);
				}
			});
			popupMenu.add(menuAbout);
		}
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	@SuppressWarnings("serial")
	private class MyAction extends AbstractAction {

		ActionCfg actionCfg;

		public MyAction(ActionCfg actionCfg) {
			super(actionCfg.getName());
			this.actionCfg = actionCfg;
		}

		public void actionPerformed(ActionEvent e) {
			String appnumber = getAppnumber(actionCfg.getForceScan());
			if (this.actionCfg.getToCopy()) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringSelection strSel = new StringSelection(appnumber);
				clipboard.setContents(strSel, null);
			}

			// links
			String[] linksStr = actionCfg.getURIs(appnumber);

			for (int i = 0; i < linksStr.length; i++) {
				String link = linksStr[i];
				try {
					Logger logger = LoggerFactory.getLogger(StartWebApp.class);
					logger.info("Try to open: " + link);
					Desktop.getDesktop().browse(new URI(link));
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					String text = "the link is not correct : " + link;
					JOptionPane.showMessageDialog(null, text, "error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	@SuppressWarnings("serial")
	private class ActionScan extends AbstractAction implements PropertyChangeListener {

		private JDialog jDialog;
		private List<Webcam> webCamList;
		private ScanJpanel scanJpanel;

		public ActionScan() {
			putValue(NAME, "Scan");
			putValue(SHORT_DESCRIPTION, "scan application nummer");
		}

		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName() == ScanJpanel.RESULT) {
				ScanJpanel scanJpanel = (ScanJpanel) evt.getSource();

				Result result = scanJpanel.getResult();
				if (result != null) {
					textFieldAppNum.setText(result.getText());
				}
				jDialog.dispose();
				scanJpanel.getWebcam().close();
			}
		}

		public void actionPerformed(ActionEvent e) {
			jDialog = new JDialog(frmAppStartApp, true);

			webCamList = Webcam.getWebcams();
			int webCamIndex = actionCfgList.getWebCamIndex();

			if (webCamIndex > webCamList.size() - 1) {
				webCamIndex = webCamList.size() - 1;
				actionCfgList.setWebCamIndex(webCamIndex);
				saveActionCfgList(actionCfgList, prefFile);
			}
			if (webCamIndex < 0) {
				webCamIndex = 0;
				actionCfgList.setWebCamIndex(webCamIndex);
				saveActionCfgList(actionCfgList, prefFile);
			}

			for (Webcam webcam : webCamList) {
				System.out.println("webcam:" + webcam.getName());
			}

			initscanJpanel();

			jDialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowDeactivated(WindowEvent e) {
					scanJpanel.stop();
					super.windowClosed(e);
				}
			});
			jDialog.pack();
			jDialog.setVisible(true);
		}

		private void initscanJpanel() {
			int webCamIndex = actionCfgList.getWebCamIndex();
			scanJpanel = new ScanJpanel(webCamList.get(webCamIndex));
			scanJpanel.setLayout(new BorderLayout());
			scanJpanel.addPropertyChangeListener(this);
			scanJpanel.setRotate(actionCfgList.isWebcamRotated());

			if (webCamList.size() > 1) {
				WebcamPicker picker = new WebcamPicker();
				picker.setSelectedIndex(webCamIndex);

				scanJpanel.add(picker, BorderLayout.SOUTH);
				picker.addItemListener(new ItemListener() {

					public void itemStateChanged(ItemEvent e) {
						Webcam webcam = scanJpanel.getWebcam();
						if ((e.getItem() != webcam) && (e.getStateChange() == ItemEvent.SELECTED)) {
							scanJpanel.stop();
							scanJpanel.removePropertyChangeListener(ActionScan.this);
							jDialog.getContentPane().remove(scanJpanel);

							webcam.close();

							int webCamIndex = webCamList.indexOf(e.getItem());
							actionCfgList.setWebCamIndex(webCamIndex);
							saveActionCfgList(actionCfgList, prefFile);
							initscanJpanel();
							scanJpanel.revalidate();
						}
					}
				});
			}
			jDialog.getContentPane().add(scanJpanel);
		}
	}
}
