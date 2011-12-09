package com.knitml.validation.context.impl;

import java.util.ArrayList;
import java.util.List;

import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.PatternEventListener;
import com.knitml.validation.context.ListenerManager;

public class DefaultListenerManager implements ListenerManager {
	List<PatternEventListener> listeners = new ArrayList<PatternEventListener>();

	public void addListener(PatternEventListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public void fireBegin(Object event, KnittingContext context) {
		for (PatternEventListener listener : listeners) {
			if (!(context.getPatternState().isReplayMode())
					|| listener.desiresRepeats(event, context)) {
				listener.begin(event, context);
			}
		}
	}

	public void fireEnd(Object event, KnittingContext context) {
		for (PatternEventListener listener : listeners) {
			if (!(context.getPatternState().isReplayMode())
					|| listener.desiresRepeats(event, context)) {
				listener.end(event, context);
			}
		}
	}
}
