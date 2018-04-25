package com.github.boyeborg;

import com.github.openwhale.spritz.ICollector;
import no.hal.learning.exercise.junit.impl.JunitTestProposalImpl;
import org.eclipse.emf.ecore.EObject;

public class CompletionCollector implements ICollector<EObject> {

	private double completion = 0.0;

	@Override
	public void process() {
		
	}

	@Override
	public void addEvent(EObject event) {
		if (event instanceof JunitTestProposalImpl) {
			completion = Math.max(((JunitTestProposalImpl) event).getCompletion(), completion);
		}
	}

	@Override
	public String getResult() {
		return String.format("%.3f", completion);
	}
	
}
