package com.github.boyeborg;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Delta;
import com.github.difflib.patch.Patch;
import com.github.openwhale.spritz.ICollector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;
import no.hal.learning.exercise.junit.impl.JunitTestEventImpl;

import org.eclipse.emf.ecore.EObject;

public class WorkAfterCompletionCollector implements ICollector<EObject> {

	private boolean isCompleted = false;
	private long completedTimestamp = Long.MAX_VALUE;
	private List<JdtSourceEditEventImpl> edits = new ArrayList<>();
	private int result = 0;

	@Override
	public void process() {
		if (!isCompleted) {
			return;
		}

		edits.sort(new Comparator<JdtSourceEditEventImpl>() {
			@Override
			public int compare(JdtSourceEditEventImpl o1, JdtSourceEditEventImpl o2) {
				return (int) (o1.getTimestamp() - o2.getTimestamp());
			}
		});

		JdtSourceEditEventImpl previousEdit = null;

		for (JdtSourceEditEventImpl edit : edits) {
			if (edit.getSourceCode() == null) {
				// Don't know why this sometimes happens
				continue;
			}
			
			if (edit.getTimestamp() < completedTimestamp) {
				continue;
			}

			List<String> current = Arrays.asList(edit.getSourceCode().split("\n"));

			if (previousEdit == null) {
				result += current.size();
			} else {
				List<String> previous = Arrays.asList(previousEdit.getSourceCode().split("\n"));

				try {
					Patch<String> patch = DiffUtils.diff(previous, current);
					for (Delta<String> delta : patch.getDeltas()) {
						result += delta.getOriginal().getLines().size();
					}
				} catch (DiffException e) {
					e.printStackTrace();
				}
			}
			previousEdit = edit;
		}

	}

	@Override
	public void addEvent(EObject event) {
		if (event instanceof JdtSourceEditEventImpl) {
			JdtSourceEditEventImpl editEvent = (JdtSourceEditEventImpl) event;
			edits.add(editEvent);
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
		return Integer.toString(result);
	}

}
