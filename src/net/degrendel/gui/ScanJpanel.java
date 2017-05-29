package net.degrendel.gui;

import java.awt.image.BufferedImage;

import javax.xml.transform.Transformer;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamEvent;
import com.github.sarxos.webcam.WebcamListener;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.datatransfer.Transferable;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

@SuppressWarnings("serial")
public class ScanJpanel extends WebcamPanel {

	// private final Action actStartStop = new StartStopAction();
	private Result result;
	private boolean rotate = false;

	/**
	 * @param rotate
	 *            the rotate to set
	 */
	public void setRotate(boolean rotate) {
		this.rotate = rotate;
	}

	// private PropertyChangeSupport mPcs = new PropertyChangeSupport(this);
	public static final String RESULT = "result";

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (rotate) {
			g2.translate(this.getWidth(), this.getHeight());
			g2.scale(-1, -1);
		}
		super.paint(g2);
		g2.setColor(Color.RED);
		g2.draw(new Line2D.Double(0, getHeight() / 2, getWidth(), getHeight() / 2));
	}

	/**
	 * @return the result
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * Create the panel.
	 */
	public ScanJpanel() {
		this(Webcam.getDefault());
	}

	public ScanJpanel(Webcam webcam) {
		super(webcam, false);

		System.out.println("new ScanJpanel :" + webcam.getName());
		Dimension[] dimArray = webcam.getViewSizes();
		// for (Dimension dim : dimArray) {
		// System.out.println("view size" + dim);
		// }
		webcam.setViewSize(dimArray[dimArray.length - 1]);
		start();
		webcam.addWebcamListener(new WebcamListner());
	}

	// public void addPropertyChangeListener(PropertyChangeListener listener) {
	// mPcs.addPropertyChangeListener(listener);
	// }
	//
	// public void removePropertyChangeListener(PropertyChangeListener listener)
	// {
	// mPcs.removePropertyChangeListener(listener);
	// }

	private class WebcamListner implements WebcamListener {

		public void webcamOpen(WebcamEvent we) {
			// TODO Auto-generated method stub

		}

		public void webcamClosed(WebcamEvent we) {
			// TODO Auto-generated method stub

		}

		public void webcamDisposed(WebcamEvent we) {
			// TODO Auto-generated method stub

		}

		public void webcamImageObtained(WebcamEvent we) {
			BufferedImage image = we.getImage();
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			result = null;
			try {
				result = new MultiFormatReader().decode(bitmap);
				firePropertyChange(RESULT, null, result);
			} catch (ReaderException re) {
				// System.out.println("CamJpanel.WebcamListner.webcamImageObtained()"
				// + re);
			}
		}
	}

}
