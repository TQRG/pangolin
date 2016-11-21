package pt.up.fe.pangolin.eclipse.core.launching;

import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;

import pt.up.fe.pangolin.eclipse.core.Configuration;
import pt.up.fe.pangolin.eclipse.core.PluginAgentConfigs;

public class PangolinLaunchDelegate implements ILaunchConfigurationDelegate2, IExecutableExtension {

	protected ILaunchConfigurationDelegate launchDelegate;
	protected ILaunchConfigurationDelegate2 launchDelegate2;

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		String launchtype = config.getAttribute("type");
		launchDelegate = getLaunchDelegate(launchtype);
		if (launchDelegate instanceof ILaunchConfigurationDelegate2) {
			launchDelegate2 = (ILaunchConfigurationDelegate2) launchDelegate;
		}
	}

	private ILaunchConfigurationDelegate getLaunchDelegate(String launchtype) throws CoreException {
		ILaunchConfigurationType type = DebugPlugin.getDefault().getLaunchManager()
				.getLaunchConfigurationType(launchtype);
		if (type != null) {
			return type.getDelegates(Collections.singleton(ILaunchManager.RUN_MODE))[0].getDelegate();
		}
		
		return null;
	}
	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		if(launchDelegate != null) {
			System.out.println("Launch configuration type id: " + configuration.getType().getIdentifier());
			
			ILaunchConfiguration newConfiguration = configuration;
			
			Configuration pluginConfiguration = Configuration.get();
			if (pluginConfiguration.getLaunchesListener().isEmpty()) {
				
				IJavaProject project = null;
				
				if(launchDelegate2 != null && launchDelegate2 instanceof AbstractJavaLaunchConfigurationDelegate) {
					project = ((AbstractJavaLaunchConfigurationDelegate)launchDelegate2).getJavaProject(configuration);
				}
				
				PluginAgentConfigs agentConfigs = PluginAgentConfigs.getConfigs(configuration);
				agentConfigs.setPort(pluginConfiguration.getServerPort());
				
				newConfiguration = new VMArgsLaunchConfiguration(configuration, Configuration.get().getAgentArg(agentConfigs));
				pluginConfiguration.getLaunchesListener().setCurrentLaunch(launch);
				
				pluginConfiguration.setExecutionDescription(new ExecutionDescription(configuration.getType(), project));
				
				//set last execution in pluginConfiguration ( agentConfigs, launch , project(?) )
			}
			
			launchDelegate.launch(newConfiguration, ILaunchManager.RUN_MODE, launch, monitor);	
		}
	}
	
	@Override
	public ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
		if (launchDelegate2 != null) {
			launchDelegate2.getLaunch(configuration, ILaunchManager.RUN_MODE);
		}
		return null;
	}

	@Override
	public boolean buildForLaunch(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		if (launchDelegate2 != null) {
			launchDelegate2.buildForLaunch(configuration, ILaunchManager.RUN_MODE, monitor);
		}
		return true;
	}

	@Override
	public boolean finalLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		if (launchDelegate2 != null) {
			launchDelegate2.finalLaunchCheck(configuration, ILaunchManager.RUN_MODE, monitor);
		}
		return true;
	}

	@Override
	public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		if (launchDelegate2 != null) {
			launchDelegate2.preLaunchCheck(configuration, ILaunchManager.RUN_MODE, monitor);
		}
		return true;
	}

}
