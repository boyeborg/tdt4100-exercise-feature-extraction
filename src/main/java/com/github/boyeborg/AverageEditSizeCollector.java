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

import org.eclipse.emf.ecore.EObject;

public class AverageEditSizeCollector implements ICollector<EObject> {

	List<JdtSourceEditEventImpl> edits = new ArrayList<>();
	List<Integer> editSizes = new ArrayList<>();

	@Override
	public void process() {
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

			List<String> current = Arrays.asList(edit.getSourceCode().split("\n"));
			int editSize = 0;
			
			if (previousEdit != null) {
				List<String> previous = Arrays.asList(previousEdit.getSourceCode().split("\n"));

				try {
					Patch<String> patch = DiffUtils.diff(previous, current);
					for (Delta<String> delta : patch.getDeltas()) {
						editSize += delta.getOriginal().getLines().size();
					}
				} catch (DiffException e) {
					e.printStackTrace();
				}
			} else {
				editSize = current.size();
			}

			editSizes.add(editSize);
			previousEdit = edit;
		}
	}

	@Override
	public void addEvent(EObject event) {
		if (event instanceof JdtSourceEditEventImpl) {
			edits.add((JdtSourceEditEventImpl) event);
		}
	}

	@Override
	public String getResult() {
		double totalEditSize = editSizes.stream().mapToDouble(i -> i).sum();
		int numEdits = editSizes.size();
		
		if (editSizes.size() == 0) {
			return "0.00";
		}

		return String.format("%.2f", totalEditSize / numEdits);
	}

}
