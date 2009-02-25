package com.knitml.engine.impl;

import org.apache.commons.lang.NullArgumentException;

import com.knitml.engine.Stitch;

public class DefaultStitch implements Stitch {
	private String id;
	
	public DefaultStitch(String id) {
		if (id == null) {
			throw new NullArgumentException("id");
		}
		this.id = id;
	}

	@Override
	public String toString() {
		return "DefaultStitch " + (id == null ? "#" : id);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof DefaultStitch) && id.equals(((Stitch)obj).getId());
	}

	/**
	 * @see com.knitml.engine.Stitch#getId()
	 */
	public String getId() {
		return id;
	}
	
	
}
