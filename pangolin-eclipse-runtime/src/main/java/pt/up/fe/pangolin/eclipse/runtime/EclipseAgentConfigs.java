package pt.up.fe.pangolin.eclipse.runtime;

import java.util.List;

import flexjson.JSON;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import pt.up.fe.pangolin.core.AgentConfigs;
import pt.up.fe.pangolin.core.instrumentation.Pass;
import pt.up.fe.pangolin.eclipse.runtime.instrumentation.InjectJDTJunitListenerPass;

public class EclipseAgentConfigs extends AgentConfigs {
	
	public static enum Runtime {
		junit,
		localJavaApplication
	}
	private Runtime runtime = Runtime.localJavaApplication;
	
	public void setRuntime (Runtime runtime) {
		this.runtime = runtime;
	}
	
	public Runtime getRuntime () {
		return runtime;
	}
	
	@JSON(include = false)
	@Override
	public List<Pass> getInstrumentationPasses() {
		addPrefixToFilter("org.eclipse");
		
		switch(runtime) {
		case junit:
			prepareJunitRuntime();
			break;
		case localJavaApplication:
		default:
			prepareLocalJavaApplicationRuntime();
			break;
		}
		return super.getInstrumentationPasses();
	}

	private void prepareLocalJavaApplicationRuntime() {
	}

	private void prepareJunitRuntime() {
		addPrefixToFilter("junit", "org.junit");
		prependPass(new InjectJDTJunitListenerPass());
	}
	
	public String serialize () {
		return new JSONSerializer().exclude("*.class").deepSerialize(this);
	}

	public static EclipseAgentConfigs deserialize (String str) {
		try {
			return new JSONDeserializer<EclipseAgentConfigs> ().deserialize(str, EclipseAgentConfigs.class);
		}
		catch (Throwable t) {
			return null;
		}
	}

	public boolean islocalJavaApplication() {
		return Runtime.localJavaApplication == runtime;
	}
	
}
