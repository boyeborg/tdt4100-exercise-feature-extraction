package com.github.boyeborg;

import com.github.openwhale.spritz.ICollector;

import no.hal.learning.exercise.jdt.impl.JdtLaunchEventImpl;

import org.eclipse.emf.ecore.EObject;

class DebuggerUsedCollector implements ICollector<EObject> {

	int debuggerUsed = 0;

	@Override
	public void process() {}

	@Override
	public void addEvent(EObject event) {
		if (debuggerUsed == 0 && event instanceof JdtLaunchEventImpl) {
			
			JdtLaunchEventImpl jdtLauchEvent = (JdtLaunchEventImpl) event;
			
			if (jdtLauchEvent.getMode().equals("debug")) {
				debuggerUsed = 1;
			}
		}
	}

	@Override
	public String getResult() {
		return Integer.toString(debuggerUsed);
	}

}
