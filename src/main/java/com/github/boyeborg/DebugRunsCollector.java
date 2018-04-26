package com.github.boyeborg;

import com.github.openwhale.spritz.ICollector;

import no.hal.learning.exercise.jdt.impl.JdtLaunchEventImpl;

import org.eclipse.emf.ecore.EObject;

public class DebugRunsCollector implements ICollector<EObject> {

	private int numDebugRuns = 0;

	@Override
	public void process() {}

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
		return Integer.toString(numDebugRuns);
	}

}
