package com.github.boyeborg;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;

import com.github.openwhale.spritz.ICollector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import no.hal.learning.exercise.jdt.impl.JdtSourceEditEventImpl;

import org.eclipse.emf.ecore.EObject;

public class ComplexityCollector implements ICollector<EObject> {

	private Map<String, Integer> lf = new HashMap<>();

	private Map<String, List<JdtSourceEditEventImpl>> edits = new HashMap<>();
	private int complexity = 0;

	/**
	 * Cunstructor. Adds complexity from solutions.
	 */
	public ComplexityCollector() {
		lf.put("objectstructures.Person", 26);
		lf.put("objectstructures.Partner", 6);
		lf.put("objectstructures.StopWatch", 13);
		lf.put("objectstructures.StopWatchManager", 11);
		lf.put("objectstructures.Tweet", 6);
		lf.put("objectstructures.TwitterAccount", 5);
		lf.put("objectstructures.Card", 5);
		lf.put("objectstructures.CardDeck", 7);
		lf.put("objectstructures.CardHand", 3);
		lf.put("encapsulation.Card", 5);
		lf.put("encapsulation.CardDeck", 6);
		lf.put("debugging.StringMergingIterator", 8);
		lf.put("interfaces.StringGridImpl", 6);
		lf.put("interfaces.StringGrid", 0);
		lf.put("interfaces.StringGridIterator", 6);
		lf.put("interfaces.BinaryComputingIterator", 10);
		lf.put("interfaces.Named", 0);
		lf.put("interfaces.NamedComparator", 1);
		lf.put("interfaces.Person1", 0);
		lf.put("interfaces.Person2", 0);
		lf.put("interfaces.Card", 6);
		lf.put("interfaces.CardHand", 3);
		lf.put("interfaces.CardDeck", 12);
		lf.put("interfaces.CardComparator", 6);
		lf.put("interfaces.CardContainer", 0);
		lf.put("interfaces.CardContainerIterator", 1);
		lf.put("interfaces.twitter.Tweet", 8);
		lf.put("interfaces.twitter.TweetsCountComparator", 0);
		lf.put("interfaces.twitter.TwitterAccount", 7);
		lf.put("interfaces.twitter.FollowersCountComparator", 0);
		lf.put("interfaces.twitter.UserNameComparator", 0);
		lf.put("patterns.observable.StockIndex", 3);
		lf.put("patterns.observable.StockListener", 0);
		lf.put("patterns.observable.Stock", 5);
		lf.put("patterns.observable.HighscoreListProgram", 4);
		lf.put("patterns.observable.HighscoreListListener", 0);
		lf.put("patterns.observable.HighscoreList", 6);
		lf.put("patterns.observable.SmartStock", 4);
		lf.put("patterns.observable.ObservableList", 7);
		lf.put("patterns.observable.ObservableListListener", 0);
		lf.put("patterns.observable.ObservableHighscoreList", 4);
		lf.put("patterns.observable.ObservableHighscoreListProgram", 4);
		lf.put("patterns.delegation.StreamLogger", 0);
		lf.put("patterns.delegation.FilteringLogger", 3);
		lf.put("patterns.delegation.ILogger", 0);
		lf.put("patterns.delegation.DistributingLogger", 1);
		lf.put("patterns.delegation.office.Clerk", 0);
		lf.put("patterns.delegation.office.Employee", 0);
		lf.put("patterns.delegation.office.Manager", 3);
		lf.put("patterns.delegation.office.Printer", 2);
		lf.put("inheritance.CreditAccount", 8);
		lf.put("inheritance.DebitAccount", 2);
		lf.put("inheritance.SavingsAccount2", 6);
		lf.put("inheritance.AbstractAccount", 6);
		lf.put("inheritance.Train", 6);
		lf.put("inheritance.CargoCar", 0);
		lf.put("inheritance.PassengerCar", 0);
		lf.put("inheritance.TrainCar", 0);
		lf.put("inheritance.ForeldreSpar", 3);
		lf.put("inheritance.BSU", 6);
		lf.put("inheritance.SavingsAccount", 8);
		lf.put("inheritance.CardDeck", 6);
		lf.put("inheritance.CardHand", 0);
		lf.put("inheritance.CardContainerImpl", 5);
		lf.put("inheritance.CardContainer", 0);
		lf.put("inheritance.Account", 0);
		lf.put("inheritance.RPNCalculator", 2);
		lf.put("inheritance.CalculatorProgram", 14);
		lf.put("inheritance.SimpleCalculator", 22);
	}

	@Override
	public void process() {
		for (Entry<String, List<JdtSourceEditEventImpl>> entry : edits.entrySet()) {
			// Sort edits (latest first)
			List<JdtSourceEditEventImpl> editList = entry.getValue();
			editList.sort(Comparator.comparing(JdtSourceEditEventImpl::getTimestamp).reversed());

			for (JdtSourceEditEventImpl edit : editList) {
				String source = edit.getSourceCode();
				
				if (source == null) {
					continue;
				}

				try {
					complexity += calculateComplexity(source) - lf.get(entry.getKey());
					break;
				} catch (ParseProblemException e) {
					continue;
				}
			}
		}
	}

	@Override
	public void addEvent(EObject event) {
		if (event instanceof JdtSourceEditEventImpl) {
			JdtSourceEditEventImpl editEvent = (JdtSourceEditEventImpl) event;
			String className = editEvent.getClassName();

			if (!edits.containsKey(className)) {
				edits.put(className, new ArrayList<>());
			}

			edits.get(className).add(editEvent);
		}
	}

	@Override
	public String getResult() {
		return Integer.toString(complexity);
	}

	private int calculateComplexity(String source) throws ParseProblemException {
		final int[] complexity = new int[]{ 0 };
		CompilationUnit compilationUnit = JavaParser.parse(source);

		complexity[0] += compilationUnit.findAll(ForStmt.class).size();
		complexity[0] += compilationUnit.findAll(ForeachStmt.class).size();
		complexity[0] += compilationUnit.findAll(WhileStmt.class).size();
		complexity[0] += compilationUnit.findAll(DoStmt.class).size();
		complexity[0] += compilationUnit.findAll(BreakStmt.class).size();
		complexity[0] += compilationUnit.findAll(ContinueStmt.class).size();
		complexity[0] += compilationUnit.findAll(SwitchEntryStmt.class).size();
		complexity[0] += compilationUnit.findAll(ConditionalExpr.class).size() * 2;
		complexity[0] += compilationUnit.findAll(ThrowStmt.class).size();
		complexity[0] += compilationUnit.findAll(CatchClause.class).size();
		
		compilationUnit.findAll(TryStmt.class).forEach(stmt -> {
			if (stmt.getFinallyBlock().isPresent()) {
				complexity[0]++;
			}
		});

		compilationUnit.findAll(IfStmt.class).forEach(ifStmt -> {
			complexity[0]++;
			if (ifStmt.getElseStmt().isPresent()) {
				if (!(ifStmt.getElseStmt().get() instanceof IfStmt)) {
					complexity[0]++;
				}
			}
		});

		compilationUnit.findAll(BinaryExpr.class).forEach(be -> {
			if (be.getOperator() == BinaryExpr.Operator.OR) {
				complexity[0]++;
			} else if (be.getOperator() == BinaryExpr.Operator.AND) {
				complexity[0]++;
			}
		});

		compilationUnit.findAll(MethodDeclaration.class).forEach(method -> {
			// Remove all other methods within this method
			method.getChildNodes().forEach(child -> {
				child.findAll(MethodDeclaration.class).forEach(n -> method.remove(n));
			});
			complexity[0] += Math.max(method.findAll(ReturnStmt.class).size() - 1, 0);
		});

		return complexity[0];
	}

}
