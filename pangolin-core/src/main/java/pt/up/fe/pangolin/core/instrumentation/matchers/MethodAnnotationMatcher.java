package pt.up.fe.pangolin.core.instrumentation.matchers;

import javassist.CtClass;
import javassist.CtMethod;


public class MethodAnnotationMatcher implements Matcher {
    private final String annotation;

    public MethodAnnotationMatcher (String annotation) {
        this.annotation = annotation;
    }

    @Override
    public final boolean matches (CtClass c) {
    	for(CtMethod m : c.getDeclaredMethods()) {
    		if (matches(c, m)) {
    			return true;
    		}
    	}
        return false;
    }

    @Override
    public final boolean matches (CtClass c,
                                  CtMethod m) {
        try {
            return m.hasAnnotation(Class.forName(annotation));
        }
        catch (Exception e) {
            return false;
        }
    }
}