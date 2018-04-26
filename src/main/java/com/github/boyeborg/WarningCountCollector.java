package com.github.boyeborg;

import com.github.openwhale.spritz.ICollector;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;

import org.eclipse.emf.ecore.EObject;

public class WarningCountCollector implements ICollector<EObject> {

	private long latestEditTimestamp = 0;
	private int warningCount = 0;

	@Override
	public void process() {}

	@Override
	public void addEvent(EObject event) {
		if (event instanceof JdtSourceEditEventImpl) {
			JdtSourceEditEventImpl editEvent = (JdtSourceEditEventImpl) event;
			if (editEvent.getTimestamp() > latestEditTimestamp) {
				latestEditTimestamp = editEvent.getTimestamp();
				warningCount = editEvent.getWarningCount();
			}
		}
	}

	@Override
	public String getResult() {
		return Integer.toString(Math.abs(warningCount));
	}
}
