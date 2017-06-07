package net.degrendel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ActionCfgList implements Iterable<ActionCfg> {

	private List<ActionCfg> list = new LinkedList<ActionCfg>();
	private boolean onTop = true;
	private boolean webcamRotated = false;
	/**
	 * @return the rotateWebcam
	 */
	public boolean isWebcamRotated() {
		return webcamRotated;
	}

	/**
	 * @param rotateWebcam the rotateWebcam to set
	 */
	public void setWebcamRotated(boolean rotateWebcam) {
		this.webcamRotated = rotateWebcam;
	}

	private int webCamIndex = 0;

	/**
	 * @return the list
	 */
	public List<ActionCfg> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<ActionCfg> list) {
		this.list = list;
	}

	public void addActionCfg(ActionCfg actionCfg) {
		list.add(actionCfg);
	}

	public Iterator<ActionCfg> iterator() {
		return list.iterator();
	}

	public boolean isOnTop() {
		return onTop;
	}

	public void setOnTop(boolean onTop) {
		this.onTop = onTop;
	}

	public int getWebCamIndex() {
		return this.webCamIndex;
	}

	public void setWebCamIndex(int webCamInd) {
		this.webCamIndex = webCamInd;
	}

}
