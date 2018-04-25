package com.github.boyeborg;

import com.github.openwhale.spritz.ICollector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;

import org.eclipse.emf.ecore.EObject;

public class TotalTimeCollector implements ICollector<EObject> {

	private List<Double> timestamps = new ArrayList<>();
	private double maxPauseTimeInMillis;
	private long result;

	private static double L2norm = 0;

	public TotalTimeCollector(double maxPauseTimeInMillis) {
		this.maxPauseTimeInMillis = maxPauseTimeInMillis;
	}

	@Override
	public void process() {
		Collections.sort(timestamps);
		
		double prevTime = -1;
		double totalTime = 0;
		
		for (double time : timestamps) {
			if (prevTime != -1) {
				totalTime += Math.min(time - prevTime, maxPauseTimeInMillis);
			}
			
			prevTime = time;
		}

		result = Math.round(totalTime / 1000.0);
		
		L2norm += Math.pow(result, 2);
	}

	@Override
	public void addEvent(EObject event) {
		if (event instanceof JdtSourceEditEventImpl) {
			double currTime = ((JdtSourceEditEventImpl) event).getTimestamp();
			timestamps.add(currTime);
		}
	}

	@Override
	public String getResult() {
		double normalizedResult = result / Math.sqrt(L2norm);
		return String.format("%.3f", normalizedResult);
	}
	
}
