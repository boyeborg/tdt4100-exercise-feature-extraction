package com.github.boyeborg;

import com.github.openwhale.spritz.ICollector;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;
import org.eclipse.emf.ecore.EObject;

public class EditCenterOfMassCollector implements ICollector<EObject> {

	private Map<Long, Integer> edits = new TreeMap<>();
	private double result;

	private static long firstEdit = Long.MAX_VALUE;
	private static long lastEdit = Long.MIN_VALUE;
	private static Map<Long, Integer> globalEdits = new TreeMap<>();

	@Override
	public void process() {
		int totalEditSize = globalEdits.values().stream().reduce(Integer::sum).get();
		double percent = 0.001;
		
		int firstDay = -1;
		int lastDay = -1;
		
		int totalDays = getDay(lastEdit);
		
		double[] globalDistribution = new double[totalDays + 1];
		
		// initialize distribution
		for (int i = 0; i < totalDays + 1; i++) {
			globalDistribution[i] = 0;
		}
		
		// create distribution
		for (Entry<Long, Integer> entry : globalEdits.entrySet()) {
			globalDistribution[getDay(entry.getKey())] += entry.getValue();
		}
		
		// find first day
		for (int i = 0; i < totalDays + 1; i++) {
			if (globalDistribution[i] >= totalEditSize * percent) {
				firstDay = i;
				break;
			}
		}
		
		// find last day
		for (int i = totalDays; i >= 0; i--) {
			if (globalDistribution[i] >= totalEditSize * percent) {
				lastDay = i;
				break;
			}
		}
		
		Map<Long, Integer> newEdits = new TreeMap<>();
		
		for (Entry<Long, Integer> entry : edits.entrySet()) {
			int day = getDay(entry.getKey());
			if (day >= firstDay && day <= lastDay) {
				newEdits.put(entry.getKey(), entry.getValue());
			}
		}
		
		int newTotalDays = lastDay - firstDay + 1;

		final double[] val = {0.0, 0.0};
		
		for (Entry<Long, Integer> entry : newEdits.entrySet()) {
			val[0] += Math.abs(entry.getValue());
			val[1] += (getDay(entry.getKey()) - firstDay) * entry.getValue();
		}

		if (val[0] > 0 && newTotalDays > 0) {
			result = val[1] / (val[0] * newTotalDays);
		} else {
			result = 1.0;
		}
	}

	@Override
	public void addEvent(EObject event) {
		if (event instanceof JdtSourceEditEventImpl) {
			JdtSourceEditEventImpl editEvent = (JdtSourceEditEventImpl) event;
			edits.put(editEvent.getTimestamp(), editEvent.getSizeMeasure());
			globalEdits.put(editEvent.getTimestamp(), editEvent.getSizeMeasure());
			
			firstEdit = Math.min(editEvent.getTimestamp(), firstEdit);
			lastEdit = Math.max(editEvent.getTimestamp(), lastEdit);
		}
	}

	@Override
	public String getResult() {
		return String.format("%.3f", result);
	}

	private int getDay(long timestamp) {
		return (int) TimeUnit.DAYS.convert(timestamp - firstEdit, TimeUnit.MILLISECONDS);
	}

}
