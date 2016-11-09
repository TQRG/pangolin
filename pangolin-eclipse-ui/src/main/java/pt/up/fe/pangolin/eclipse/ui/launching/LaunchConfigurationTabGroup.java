package pt.up.fe.pangolin.eclipse.ui.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;

public class LaunchConfigurationTabGroup implements ILaunchConfigurationTabGroup, IExecutableExtension {

	private ILaunchConfigurationTabGroup delegate;
	
	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		delegate = createDelegate(config.getAttribute("type"));
	}
	
	private ILaunchConfigurationTabGroup createDelegate(String type) throws CoreException {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionpoint = registry.getExtensionPoint("org.eclipse.debug.ui.launchConfigurationTabGroups");
		IConfigurationElement[] tabGroupConfigs = extensionpoint.getConfigurationElements();
		IConfigurationElement element = getElement(type,tabGroupConfigs);
		if (element == null) {
			return null;
		} else {
			return (ILaunchConfigurationTabGroup) element.createExecutableExtension("class");
		}
	}
	
	private IConfigurationElement getElement(String type, IConfigurationElement[] tabGroupConfigs) {
		IConfigurationElement element = null;

		for (IConfigurationElement tabGroupConfig : tabGroupConfigs) {
			if (type.equals(tabGroupConfig.getAttribute("type"))) {
				IConfigurationElement[] modeConfigs = tabGroupConfig.getChildren("launchMode");
				if (modeConfigs.length == 0) {
					element = tabGroupConfig;
				}
				for (final IConfigurationElement config : modeConfigs) {
					if (ILaunchManager.RUN_MODE.equals(config.getAttribute("mode"))) {
						element = tabGroupConfig;
						return element;
					}
				}
			}
		}

		return element;
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		if(delegate != null) {
			delegate.createTabs(dialog, mode);
		}
	}

	@Override
	public ILaunchConfigurationTab[] getTabs() {
		if(delegate != null) {
			return insertTab(delegate.getTabs(), new LaunchConfigurationTab());
		}
		return null;
	}
	
	protected ILaunchConfigurationTab[] insertTab(
			ILaunchConfigurationTab[] delegateTabs,
			ILaunchConfigurationTab tab) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[delegateTabs.length + 1];
		tabs[0] = delegateTabs[0];
		tabs[1] = tab;
		System.arraycopy(delegateTabs, 1, tabs, 2, delegateTabs.length - 1);
		return tabs;
	}

	@Override
	public void dispose() {
		if (delegate != null) {
			delegate.dispose();
		}
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		if(delegate != null) {
			delegate.setDefaults(configuration);
		}
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		if(delegate != null) {
			delegate.initializeFrom(configuration);
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		if(delegate != null) {
			delegate.performApply(configuration);
		}
	}

	@Override
	public void launched(ILaunch launch) {

	}

}
