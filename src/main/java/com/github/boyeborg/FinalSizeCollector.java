package com.github.boyeborg;

import com.github.openwhale.spritz.ICollector;

import java.util.HashMap;
import java.util.Map;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;

import org.eclipse.emf.ecore.EObject;

public class FinalSizeCollector implements ICollector<EObject> {

	private Map<String, JdtSourceEditEventImpl> lastEdits = new HashMap<>();
	private int length = 0;

	@Override
	public void process() {
		for (JdtSourceEditEventImpl edit : lastEdits.values()) {
			if (edit.getSourceCode() != null) {
				length += edit.getSourceCode().split("\n").length;
			}
		}
	}

	@Override
	public void addEvent(EObject event) {
		if (event instanceof JdtSourceEditEventImpl) {
			JdtSourceEditEventImpl editEvent = (JdtSourceEditEventImpl) event;
			String[] classNameSplit = editEvent.getClassName().split("\\.");
			String className = classNameSplit[classNameSplit.length - 1];

			if (editEvent.getSourceCode() == null) {
				return;
			}
			if (lastEdits.containsKey(className)) {
				if (editEvent.getTimestamp() > lastEdits.get(className).getTimestamp()) {
					lastEdits.put(className, editEvent);
				}
			} else {
				lastEdits.put(className, editEvent);
			}
		}
	}

	@Override
	public String getResult() {
		return Integer.toString(length);
	}

}
