package pt.up.fe.pangolin.eclipse.runtime.junit;

import org.eclipse.jdt.internal.junit.runner.IListensToTestExecutions;
import org.eclipse.jdt.internal.junit.runner.ITestIdentifier;
import org.eclipse.jdt.internal.junit.runner.TestReferenceFailure;

import pt.up.fe.pangolin.core.runtime.Collector;

public class InstrumentationTestListener implements IListensToTestExecutions {

	private boolean isError = false;
	
	@Override
	public void notifyTestFailed(TestReferenceFailure failure) {
		isError = true;
	}

	@Override
	public void notifyTestStarted(ITestIdentifier test) {
		isError = false;
		Collector.instance().startTransaction();
	}

	@Override
	public void notifyTestEnded(ITestIdentifier test) {
		Collector.instance().endTransaction(test.getName(), this.isError);
	}

	public static IListensToTestExecutions newListener(IListensToTestExecutions listener) {
		return new MultipleTestListener(listener, new InstrumentationTestListener());
	}
	
}
