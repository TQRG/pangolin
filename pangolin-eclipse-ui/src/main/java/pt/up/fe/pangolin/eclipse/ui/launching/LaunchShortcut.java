package pt.up.fe.pangolin.eclipse.ui.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;

public class LaunchShortcut implements ILaunchShortcut, IExecutableExtension
{
	private String delegateType;
	private ILaunchShortcut delegate;

	@Override
	public void launch(ISelection selection, String mode) {
		ILaunchShortcut delegate = getDelegate();
		if (delegate != null) {
			delegate.launch(selection, mode);
		}
	}

	@Override
	public void launch(IEditorPart editor, String mode) {
		ILaunchShortcut delegate = getDelegate();
		if (delegate != null) {
			delegate.launch(editor, mode);
		}
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		delegateType = String.valueOf(data);
	}
	
	private ILaunchShortcut getDelegate() {
		if (delegate == null) {
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = registry.getExtensionPoint(IDebugUIConstants.PLUGIN_ID, IDebugUIConstants.EXTENSION_POINT_LAUNCH_SHORTCUTS);
			IConfigurationElement[] configs = extensionPoint.getConfigurationElements();
			
			for (IConfigurationElement config : configs) {
				String configID = config.getAttribute("id");
				if (delegateType.equals(configID)) {
					try {
						delegate = (ILaunchShortcut) config.createExecutableExtension("class");
					} catch (CoreException e) {}
					break;
				}
			}
		}
		return delegate;
	}

}