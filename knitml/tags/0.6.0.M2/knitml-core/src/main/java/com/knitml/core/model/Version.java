package com.knitml.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Version {

	private static final String CURRENT_VERSION_ID = "0.6";
	private static final Version CURRENT_VERSION = new Version(
			CURRENT_VERSION_ID);
	private static final Map<String, Version> supportedVersions = new HashMap<String, Version>();

	static {
		supportedVersions.put("0.5", new Version("0.5"));
		supportedVersions.put(CURRENT_VERSION_ID, new Version(
				CURRENT_VERSION_ID));
	}

	public static Version getCurrentVersion() {
		return CURRENT_VERSION;
	}

	public static String getCurrentVersionId() {
		return CURRENT_VERSION.getId();
	}
	
	public static Version getVersion(Pattern pattern) {
		return supportedVersions.get(pattern.getVersion());
	}

	public static boolean isSupported(Pattern pattern) {
		return getVersion(pattern) != null; 
	}

	private String id;
	private int majorVersionNumber;
	private int minorVersionNumber;

	private Version(String id) {
		this.id = id;
		StringTokenizer st = new StringTokenizer(id, ".");
		this.majorVersionNumber = Integer.parseInt(st.nextToken());
		this.minorVersionNumber = Integer.parseInt(st.nextToken());
	}

	public String getId() {
		return this.id;
	}

	public int getMajorVersionNumber() {
		return majorVersionNumber;
	}

	public int getMinorVersionNumber() {
		return minorVersionNumber;
	}

}
