package com.github.boyeborg;

import com.github.openwhale.spritz.ICollector;

import java.util.NoSuchElementException;
import java.util.TreeMap;
import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;
import no.hal.learning.exercise.junit.impl.JunitTestEventImpl;
import org.eclipse.emf.ecore.EObject;

public class WorkAfterCompletionCollector implements ICollector<EObject> {

	boolean isCompleted = false;
	long completedTimestamp = Long.MAX_VALUE;
	TreeMap<Long, Integer> edits = new TreeMap<>();
	int result;

	static double L2norm = 0;

	@Override
	public void process() {

		if (!isCompleted) {
			result = 0;
			return;
		}

		Long startKey = edits.higherKey(completedTimestamp);

		if (startKey == null) {
			result = 0;
			return;
		}
		
		try {
			result = edits.tailMap(startKey, true).values().stream().reduce(Integer::sum).get();
			L2norm += Math.pow(result, 2);
		} catch (NoSuchElementException e) {
			result = 0;
		}
		
	}

	@Override
	public void addEvent(EObject event) {
		if (event instanceof JdtSourceEditEventImpl) {
			JdtSourceEditEventImpl editEvent = (JdtSourceEditEventImpl) event;
			edits.put(editEvent.getTimestamp(), Math.abs(editEvent.getSizeMeasure()));
		} else if (event instanceof JunitTestEventImpl) {
			JunitTestEventImpl testEvent = (JunitTestEventImpl) event;
			if (testEvent.getCompletion() == 1.0) {
				isCompleted = true;
				completedTimestamp = Math.min(completedTimestamp, testEvent.getTimestamp());
			}
		}
	}

	@Override
	public String getResult() {
		double normalizedResult = result / Math.sqrt(L2norm);
		return String.format("%.3f", normalizedResult);
	}

}
