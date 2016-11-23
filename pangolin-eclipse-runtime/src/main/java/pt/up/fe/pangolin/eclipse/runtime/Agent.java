package pt.up.fe.pangolin.eclipse.runtime;

import java.lang.instrument.Instrumentation;

import pt.up.fe.pangolin.core.instrumentation.ClassTransformer;
import pt.up.fe.pangolin.core.runtime.Collector;

public class Agent {

	public static void premain(String agentArgs, Instrumentation inst) {

		EclipseAgentConfigs agentConfigs = EclipseAgentConfigs.deserialize(agentArgs);
		if (agentConfigs == null) {
			return;
		}
		
		Collector.start(agentConfigs.getEventListener());
		ClassTransformer transformer = new ClassTransformer(agentConfigs.getInstrumentationPasses());
		inst.addTransformer(transformer);

		Runtime.getRuntime().addShutdownHook(
			new Thread() { 
				public void run() { Collector.instance().endSession(); } 
			}
		);
		
		if (agentConfigs.islocalJavaApplication()) {
			PangolinFrame.startFrame();
		}
	}

}