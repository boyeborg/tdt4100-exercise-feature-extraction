package com.github.boyeborg;

import com.github.openwhale.spritz.ICollector;

import org.eclipse.emf.ecore.EObject;

class StudentIdCollector implements ICollector<EObject> {

	private String studentId;

	@Override
	public void process() {}

	@Override
	public void addEvent(EObject event) {
		if (event instanceof StudentIdEvent) {
			studentId = ((StudentIdEvent) event).getStudentId();
		}
	}

	@Override
	public String getResult() {
		return studentId;
	}

}
