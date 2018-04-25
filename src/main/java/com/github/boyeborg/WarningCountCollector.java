package com.github.boyeborg;

import com.github.openwhale.spritz.ICollector;
import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;
import org.eclipse.emf.ecore.EObject;

public class WarningCountCollector implements ICollector<EObject> {

	private long latestEditTimestamp = 0;
	private int warningCount = 0;

	public static double L2norm = 0;

	@Override
	public void process() {
		L2norm += Math.pow(warningCount, 2);
	}

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
		double normalizedResult = Math.abs(warningCount) / Math.sqrt(L2norm);
		return String.format("%.3f", normalizedResult);
	}
}
