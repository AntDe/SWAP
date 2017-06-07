package net.degrendel;

public class ActionCfg {

	private String name;
	private String link;
	private Boolean toCopy = false;
	private Boolean forceScan = false;

	public ActionCfg(String name, String link) {
		super();
		this.name = name;
		this.link = link;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link
	 *            the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	public Boolean getToCopy() {
		return toCopy;
	}

	public void setToCopy(Boolean toCopy) {
		this.toCopy = toCopy;
	}

	/**
	 * @return the forceScan
	 */
	public Boolean getForceScan() {
		return forceScan;
	}

	/**
	 * @param forceScan the forceScan to set
	 */
	public void setForceScan(Boolean forceScan) {
		this.forceScan = forceScan;
	}

	public String[] getURIs(String appnumber) {
		String[] linksArr = this.getLink().replaceAll("\\{appNum\\}", appnumber).split(" +");
		return linksArr;
	}

}
