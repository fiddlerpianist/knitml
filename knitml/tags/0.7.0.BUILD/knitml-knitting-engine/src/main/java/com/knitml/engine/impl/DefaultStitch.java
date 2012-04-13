package com.knitml.engine.impl;

import static com.knitml.core.model.operations.StitchNature.KNIT;

import java.text.MessageFormat;

import org.apache.commons.lang.NullArgumentException;

import com.knitml.core.model.operations.StitchNature;
import com.knitml.engine.Stitch;

public class DefaultStitch implements Stitch {

	/**
	 * The previous operation that was performed on the stitch, which was
	 * (ultimately) either a knit-like or purl-like operation as viewed from the
	 * "right" (i.e. outer) side of the stitch.
	 */
	private StitchNature history = KNIT; // stitch creation == cast on ==
											// knit-like
	private String id;

	public DefaultStitch(String id) {
		if (id == null) {
			throw new NullArgumentException("id"); //$NON-NLS-1$
		}
		this.id = id;
	}

	@Override
	public String toString() {
		return MessageFormat.format(Messages.getString("DefaultStitch.TO_STRING"), id); //$NON-NLS-1$
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
					Messages.getString("DefaultStitch.WRONG_MEMENTO_TYPE")); //$NON-NLS-1$
		}
		DefaultStitchMemento memento = (DefaultStitchMemento) mementoObj;
		this.history = memento.getHistory();
	}

	public Object save() {
		return new DefaultStitchMemento(this.history);
	}

	public void recordNature(StitchNature nature) {
		if (nature != null)
		// at some point we may want to save the history for more than just the
		// last operation, but we don't need to do that yet
		this.history = nature;
	}

	public StitchNature getCurrentNature() {
		return this.history;
	}

}
