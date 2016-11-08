package pt.up.fe.pangolin.core.instrumentation.matchers;

import javassist.CtClass;
import javassist.CtMethod;

public interface ActionTaker {
    public static enum Action {
        ACCEPT, NEXT, REJECT
    }

    Action getAction (CtClass c);
    Action getAction (CtClass c, CtMethod m);
}