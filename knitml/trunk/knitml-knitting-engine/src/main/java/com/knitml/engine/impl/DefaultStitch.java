package com.knitml.engine.impl;

import org.apache.commons.lang.NullArgumentException;

import com.knitml.engine.Stitch;
import com.knitml.engine.StitchOperation;
import static com.knitml.engine.StitchOperation.KNIT;

public class DefaultStitch implements Stitch {

	/**
	 * The previous operation that was performed on the stitch, which was
	 * (ultimately) either a knit-like or purl-like operation as viewed from the
	 * "right" (i.e. outer) side of the stitch.
	 */
	private StitchOperation history = KNIT; // stitch creation == cast on ==
											// knit-like
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
		return (obj instanceof DefaultStitch)
				&& id.equals(((Stitch) obj).getId());
	}

	/**
	 * @see com.knitml.engine.Stitch#getId()
	 */
	public String getId() {
		return id;
	}

	public void restore(Object mementoObj) {
		if (!(mementoObj instanceof DefaultStitchMemento)) {
			throw new IllegalArgumentException(
					"Type to restore must be of type DefaultStitchMemento");
		}
		DefaultStitchMemento memento = (DefaultStitchMemento) mementoObj;
		this.history = memento.getHistory();
	}

	public Object save() {
		return new DefaultStitchMemento(this.history);
	}

	public void recordOperation(StitchOperation operation) {
		// at some point we may want to save the history for more than just the
		// last operation, but we don't need to do that yet
		this.history = operation;
	}

}
