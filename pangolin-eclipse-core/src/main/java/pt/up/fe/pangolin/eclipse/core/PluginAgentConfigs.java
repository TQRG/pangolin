package pt.up.fe.pangolin.eclipse.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import pt.up.fe.pangolin.core.flexjson.JSONDeserializer;
import pt.up.fe.pangolin.eclipse.runtime.EclipseAgentConfigs;

public class PluginAgentConfigs extends EclipseAgentConfigs {

	public static final String PANGOLIN_CONFIGS = "pt.up.fe.pangolin.eclipse.core.agent_configs";
	
	public static PluginAgentConfigs deserialize(String str) {
		try {
			return new JSONDeserializer<PluginAgentConfigs>().deserialize(str, PluginAgentConfigs.class);
		} catch (Throwable t) {
			return null;
		}
	}
	
	public static PluginAgentConfigs getConfigs(ILaunchConfiguration configuration) {
		PluginAgentConfigs pc = null;
		
		try {
			String str = configuration.getAttribute(PluginAgentConfigs.PANGOLIN_CONFIGS, "");
			if(!"".equals(str)) {
				pc = PluginAgentConfigs.deserialize(str);
			}
			if(pc == null) pc = new PluginAgentConfigs();
		} catch (CoreException e) {
			pc = new PluginAgentConfigs();
		}
		
		try {
			EclipseAgentConfigs.Runtime runtime = 
					"org.eclipse.jdt.junit.launchconfig".equals(configuration.getType().getIdentifier()) ? 
							EclipseAgentConfigs.Runtime.junit : EclipseAgentConfigs.Runtime.localJavaApplication;
			pc.setRuntime(runtime);
		} catch (CoreException e) {
		}
		
		return pc;
	}
}
