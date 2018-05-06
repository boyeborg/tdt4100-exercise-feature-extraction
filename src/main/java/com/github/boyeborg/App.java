package com.github.boyeborg;

import com.github.openwhale.spritz.CollectorFactory;
import com.github.openwhale.spritz.EventConsumer;

import java.io.File;
import java.io.FilenameFilter;

import no.hal.learning.exercise.ExercisePackage;
import no.hal.learning.exercise.jdt.JdtPackage;
import no.hal.learning.exercise.junit.JunitPackage;
import no.hal.learning.exercise.util.ExerciseResourceFactoryImpl;
import no.hal.learning.exercise.workbench.WorkbenchPackage;
import no.hal.learning.exercise.workspace.WorkspacePackage;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;


/**
 * Hello world class.
 */
public class App {

	/**
	 * Hello world method.
	 * 
	 * @param args Arguments passed from the command line
	 */
	public static void main(String[] args) {
		// Hack to disable warnings like illegal reflective access
		System.err.close();
		System.setErr(System.out);

		// Create the collector factory
		CollectorFactory<EObject> factory = new CollectorFactory<>();

		// Add collectors
		factory.add(() -> new StudentIdCollector(), "studentId");
		factory.add(() -> new DebuggerUsedCollector(), "debuggerUsed");
		factory.add(() -> new WorkAfterCompletionCollector(), "workAfterCompletion");
		factory.add(() -> new CompletionCollector(), "completion");
		factory.add(() -> new DebugRunsCollector(), "numDebugRuns");
		factory.add(() -> new EditCenterOfMassCollector(), "editCenterOfMass");
		factory.add(() -> new WarningCountCollector(), "numWarnings");
		factory.add(() -> new TotalTimeCollector(10 * 60 * 1000), "totalTime"); // 10 minutes
		factory.add(() -> new NonprofitableWorkSessionCollector(), "numNonprofitableWorkSessions");
		factory.add(() -> new AverageEditSizeCollector(), "averageEditSize");
		factory.add(() -> new ComplexityCollector(), "complexity");

		// Create the event consumer
		EventConsumer<EObject> eventConsumer = new EventConsumer<>(factory);

		// Create a resource set for reading XML files
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(ExercisePackage.eNS_URI, ExercisePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(JdtPackage.eNS_URI, JdtPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(JunitPackage.eNS_URI, JunitPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(WorkspacePackage.eNS_URI, WorkspacePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(WorkbenchPackage.eNS_URI, WorkbenchPackage.eINSTANCE);
		
		resourceSet.getResourceFactoryRegistry()
			.getExtensionToFactoryMap()
			.put("ex", new ExerciseResourceFactoryImpl());
		
		// Get the path to the direcotry of exercises
		String exercisesPath = args[0];

		// Loop through the student folders within the exercise folder
		for (File studentDirectory : new File(exercisesPath).listFiles(File::isDirectory)) {

			// Store the student ID
			String studentId = studentDirectory.getName();

			// Find all exercise XML files
			File[] studentExercises = studentDirectory.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File file, String name) {
					return name.endsWith(".ex");
				}
			});

			// Loop through all exercise files
			for (File studentExercise : studentExercises) {
				// Get the resource containing the events of the XML file
				Resource resource = resourceSet
						.getResource(URI.createURI(studentExercise.getAbsolutePath()), true);

				// Get the content (events) within the file
				TreeIterator<EObject> it = resource.getAllContents();

				// Add a new batch
				eventConsumer.newBatch();

				// Add the student ID
				eventConsumer.addEvent(new StudentIdEvent(studentId));

				// Loop through all events within the file
				while (it.hasNext()) {
					// Get the event
					EObject event = it.next();

					// Do stuff with the event
					eventConsumer.addEvent(event);
				}
			}
		}

		// Process all events
		eventConsumer.process();
		
		// Print out the results
		System.out.println(eventConsumer);
	}
}
