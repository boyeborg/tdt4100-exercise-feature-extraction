package com.github.boyeborg;

import com.github.openwhale.spritz.ICollector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;
import no.hal.learning.exercise.junit.impl.JunitTestEventImpl;

import org.eclipse.emf.ecore.EObject;


public class NonprofitableWorkSessionCollector implements ICollector<EObject> {
	
	List<EObject> events = new ArrayList<>();
	int numberOfNonprofitableWorkSessions = 0;

	@Override
	public void process() {
		events.sort(new Comparator<EObject>() {

			@Override
			public int compare(EObject o1, EObject o2) {
				double timestamp1 = 0;
				double timestamp2 = 0;

				try {
					timestamp1 = ((JdtSourceEditEventImpl) o1).getTimestamp();
				} catch (ClassCastException e) {
					timestamp1 = ((JunitTestEventImpl) o1).getTimestamp();
				}

				try {
					timestamp2 = ((JdtSourceEditEventImpl) o2).getTimestamp();
				} catch (ClassCastException e) {
					timestamp2 = ((JunitTestEventImpl) o2).getTimestamp();
				}

				return (int) (timestamp1 - timestamp2);
			}
		});

		double previousTestScore = 0.0;
		boolean editSinceLastTest = false;

		for (EObject event : events) {
			try {
				JunitTestEventImpl testEvent = (JunitTestEventImpl) event;
				if (editSinceLastTest && testEvent.getCompletion() <= previousTestScore) {
					numberOfNonprofitableWorkSessions++;
					editSinceLastTest = false;
					previousTestScore = testEvent.getCompletion();
				}
			} catch (ClassCastException e) {
				editSinceLastTest = true;
			}
		}

	}

	@Override
	public void addEvent(EObject event) {
		if (event instanceof JdtSourceEditEventImpl || event instanceof JunitTestEventImpl) {
			events.add(event);
		}
	}

	@Override
	public String getResult() {
		return Integer.toString(numberOfNonprofitableWorkSessions);
	}

}
