package pt.up.fe.pangolin.eclipse.core;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.graphics.Image;

import pt.up.fe.pangolin.core.AgentConfigs;
import pt.up.fe.pangolin.core.events.EventListener;
import pt.up.fe.pangolin.core.events.NullEventListener;
import pt.up.fe.pangolin.eclipse.core.launching.ExecutionDescription;
import pt.up.fe.pangolin.eclipse.core.launching.LaunchesListener;
import pt.up.fe.pangolin.eclipse.core.messaging.InstrumentationServer;
import pt.up.fe.pangolin.eclipse.core.visualization.VisualizationBrowser;
import pt.up.fe.pangolin.eclipse.core.visualization.tree.TransactionViewer;
import pt.up.fe.pangolin.eclipse.runtime.Agent;

public class Configuration {
	
	private String agentPath;
	private InstrumentationServer server;
	private LaunchesListener launchesListener;
	private VisualizationBrowser visualizationBrowser;
	private ExecutionDescription executionDescription;
	private TransactionViewer transactionViewer;
	private IActivator activator;

	private static Configuration configuration;
	public static Configuration get() {
		if(configuration == null)
			configuration = new Configuration();
		return configuration;
	}
	
	public Configuration() {
		prepareAgent();
		try {
			this.server = new InstrumentationServer();
			this.server.start();
		} catch (IOException exception) {
			this.server = null;
		}
		
		launchesListener = new LaunchesListener();
		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(launchesListener);
	}
	
	public int getServerPort() {
		if (this.server == null) {
			return -1;
		}
		return this.server.getPort();
	}
	
	public String getAgentArg(AgentConfigs configs) {
		return " -javaagent:\"" + agentPath + "\"=" + configs.serialize();
	}
	
	private void prepareAgent() {
		URL agentLocation = Agent.class.getProtectionDomain().getCodeSource().getLocation();

		if(agentLocation != null) {
			
			try {
				agentLocation = FileLocator.resolve(agentLocation); // resolve if resource is an OSGi bundle
				String path = agentLocation.getPath();

				/* windows path is something like /C:/blablabla/, we should remove that first slash */
				if (System.getProperty("os.name").toLowerCase().contains("win")) {
					path = path.replaceFirst("/", "");
				}

				if(path.endsWith(".jar")) {
					agentPath = path;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public LaunchesListener getLaunchesListener() {
		return launchesListener;
	}
	
	public void setVisualizationBrowser(VisualizationBrowser browser) {
		visualizationBrowser = browser;
	}
	
	public void initializeVisualization(IJavaProject project, String jsonMessage) {
		VisualizationBrowser.revealView();
		if (visualizationBrowser != null) {
			visualizationBrowser.callInitializeVisualization(project, jsonMessage);
		}
	}
	
	public ExecutionDescription getExecutionDescription() {
		return executionDescription;
	}

	public void setExecutionDescription(ExecutionDescription executionDescription) {
		this.executionDescription = executionDescription;
	}
	
	public EventListener getEventListener() {
		if (executionDescription == null) {
			return new NullEventListener();
		}
		return executionDescription;
	}

	public void setTransactionViewer(TransactionViewer tv) {
		this.transactionViewer = tv;
	}

	public TransactionViewer getTransactionViewer() {
		TransactionViewer.revealView();
		return transactionViewer;
	}

	public void registerActivator(IActivator activator) {
		this.activator = activator;
	}

	public Image getImage(String path) {
		if (this.activator != null) {
			return this.activator.getImageFromPath(path);
		}
		return null;
	}
}
