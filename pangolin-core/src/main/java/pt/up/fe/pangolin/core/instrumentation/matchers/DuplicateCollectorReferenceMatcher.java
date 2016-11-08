package pt.up.fe.pangolin.core.instrumentation.matchers;

import javassist.CtClass;
import javassist.CtMethod;
import pt.up.fe.pangolin.core.runtime.Collector;

public class DuplicateCollectorReferenceMatcher implements Matcher {

	@Override
	public boolean matches(CtClass c) {
		return Collector.instance().existsHitVector(c.getName());
	}

	@Override
	public boolean matches(CtClass c, CtMethod m) {
		return matches(c);
	}

}
