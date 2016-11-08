package pt.up.fe.pangolin.eclipse.runtime.junit;

import org.eclipse.jdt.internal.junit.runner.IListensToTestExecutions;
import org.eclipse.jdt.internal.junit.runner.ITestIdentifier;
import org.eclipse.jdt.internal.junit.runner.TestReferenceFailure;

public class VerboseTestListener implements IListensToTestExecutions {

	@Override
	public void notifyTestFailed(TestReferenceFailure failure) {
		System.out.println("Test failed.");
	}

	@Override
	public void notifyTestStarted(ITestIdentifier test) {
		System.out.println("Test " + test.getName() + " started.");
	}

	@Override
	public void notifyTestEnded(ITestIdentifier test) {
		System.out.println("Test " + test.getName() + " ended.");
	}

}
