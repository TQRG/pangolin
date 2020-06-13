package pt.up.fe.pangolin.core.runtime;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

import pt.up.fe.pangolin.core.AgentConfigs;
import pt.up.fe.pangolin.core.exporter.Exporter;
import pt.up.fe.pangolin.core.instrumentation.ClassTransformer;

public class Agent {

	public static void premain(String agentArgs, Instrumentation inst) {

		AgentConfigs agentConfigs = new AgentConfigs();
		Collector.start(agentConfigs.getEventListener(), agentArgs);
		ClassTransformer transformer = new ClassTransformer(agentConfigs.getInstrumentationPasses());
		inst.addTransformer(transformer);

		Runtime.getRuntime().addShutdownHook(
			new Thread() {
				public void run() {
					Collector.instance().endSession();
				}
			}
		);

		PangolinFrame.startFrame();
	}

}
