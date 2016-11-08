package pt.up.fe.pangolin.eclipse.runtime.instrumentation;

import javassist.CtClass;
import javassist.CtConstructor;
import pt.up.fe.pangolin.core.instrumentation.Pass;
import pt.up.fe.pangolin.core.instrumentation.StackSizePass;
import pt.up.fe.pangolin.core.instrumentation.matchers.ClassNamesMatcher;
import pt.up.fe.pangolin.core.instrumentation.matchers.Matcher;

public class InjectJDTJunitListenerPass implements Pass {
	
	private static final String STATEMENT = 
			"this.fExecutionListener = pt.up.fe.pangolin.eclipse.runtime.junit.InstrumentationTestListener.newListener(this.fExecutionListener);";

	private Matcher matcher = new ClassNamesMatcher("org.eclipse.jdt.internal.junit.runner.TestExecution");
	
	@Override
	public Outcome transform(CtClass c) throws Exception {
		if (matcher.matches(c)) {
			
			for(CtConstructor ctor : c.getDeclaredConstructors()) {
				ctor.insertAfter(STATEMENT);
			}
			
			new StackSizePass().transform(c);

			return Outcome.FINISH;
		}
		return Outcome.CONTINUE;
	}

}
