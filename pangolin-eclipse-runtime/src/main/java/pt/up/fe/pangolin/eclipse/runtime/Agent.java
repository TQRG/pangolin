package pt.up.fe.pangolin.eclipse.runtime;

import java.lang.instrument.Instrumentation;

import pt.up.fe.pangolin.core.AgentConfigs;
import pt.up.fe.pangolin.core.instrumentation.ClassTransformer;
import pt.up.fe.pangolin.core.runtime.Collector;
import pt.up.fe.pangolin.eclipse.runtime.instrumentation.InjectJDTJunitListenerPass;

public class Agent {

	public static void premain(String agentArgs, Instrumentation inst) {

		AgentConfigs agentConfigs = AgentConfigs.deserialize(agentArgs);
		if (agentConfigs == null) {
			return;
		}
		
		agentConfigs.addPrefixToFilter("org.eclipse", "junit", "org.junit");
		agentConfigs.prependPass(new InjectJDTJunitListenerPass());
		
		Collector.start(agentConfigs.getEventListener());
		ClassTransformer transformer = new ClassTransformer(agentConfigs.getInstrumentationPasses());
		inst.addTransformer(transformer);

		Runtime.getRuntime().addShutdownHook(
			new Thread() { 
				public void run() { Collector.instance().endSession(); } 
			}
		);
		
		System.out.println(agentConfigs.serialize());
	}

}