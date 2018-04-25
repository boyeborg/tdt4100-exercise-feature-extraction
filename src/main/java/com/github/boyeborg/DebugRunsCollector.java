package com.github.boyeborg;

import com.github.openwhale.spritz.ICollector;
import no.hal.learning.exercise.jdt.impl.JdtLaunchEventImpl;
import org.eclipse.emf.ecore.EObject;

public class DebugRunsCollector implements ICollector<EObject> {

	private int numDebugRuns = 0;

	static double L2norm = 0;

	@Override
	public void process() {
		L2norm += Math.pow(numDebugRuns, 2);
	}

	@Override
	public void addEvent(EObject event) {
		if (event instanceof JdtLaunchEventImpl) {
			if (((JdtLaunchEventImpl) event).getMode().equals("debug")) {
				numDebugRuns++;
			}
		}
	}

	@Override
	public String getResult() {
		double normalizedResult = numDebugRuns / Math.sqrt(L2norm);
		return String.format("%.3f", normalizedResult);
	}

}
