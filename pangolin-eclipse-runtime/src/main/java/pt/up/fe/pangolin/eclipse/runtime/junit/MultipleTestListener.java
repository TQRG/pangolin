package pt.up.fe.pangolin.eclipse.runtime.junit;

import java.util.ArrayList;

import org.eclipse.jdt.internal.junit.runner.IListensToTestExecutions;
import org.eclipse.jdt.internal.junit.runner.ITestIdentifier;
import org.eclipse.jdt.internal.junit.runner.TestReferenceFailure;

public class MultipleTestListener implements IListensToTestExecutions {
	
	private ArrayList<IListensToTestExecutions> delegates;
	
	public MultipleTestListener(IListensToTestExecutions... listeners) {
		this.delegates = new ArrayList<IListensToTestExecutions>();
		
		for(IListensToTestExecutions listener : listeners) {
			this.delegates.add(listener);
		}
	}
	
	@Override
	public void notifyTestFailed(TestReferenceFailure failure) {
		for(IListensToTestExecutions delegate : this.delegates) {
			delegate.notifyTestFailed(failure);
		}
	}

	@Override
	public void notifyTestStarted(ITestIdentifier test) {
		for(IListensToTestExecutions delegate : this.delegates) {
			delegate.notifyTestStarted(test);
		}
	}

	@Override
	public void notifyTestEnded(ITestIdentifier test) {
		for(IListensToTestExecutions delegate : this.delegates) {
			delegate.notifyTestEnded(test);
		}
	}

}
