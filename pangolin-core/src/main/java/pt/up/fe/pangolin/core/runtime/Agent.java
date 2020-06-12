package pt.up.fe.pangolin.core.runtime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

import javassist.*;
import pt.up.fe.pangolin.core.AgentConfigs;
import pt.up.fe.pangolin.core.instrumentation.ClassTransformer;
import pt.up.fe.pangolin.core.instrumentation.Pass;
import pt.up.fe.pangolin.core.runtime.Collector;

public class Agent {

	public static void premain(String agentArgs, Instrumentation inst) {

		AgentConfigs agentConfigs = new AgentConfigs();
		System.out.println(System.getProperty("java.class.path"));
		Collector.start(agentConfigs.getEventListener());
		ClassTransformer transformer = new ClassTransformer(agentConfigs.getInstrumentationPasses());
		inst.addTransformer(transformer);

		Runtime.getRuntime().addShutdownHook(
			new Thread() {
				public void run() { Collector.instance().endSession(); }
			}
		);

		PangolinFrame.startFrame();
	}

}
