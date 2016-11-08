package pt.up.fe.pangolin.eclipse.core.launching;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchDelegate;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

@SuppressWarnings({"unchecked", "rawtypes"})
public class VMArgsLaunchConfiguration implements ILaunchConfiguration {

	private static final String VMA_KEY = IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS;
	private final ILaunchConfiguration delegate;
	private final String extraVMArg;

	public VMArgsLaunchConfiguration(ILaunchConfiguration delegate, String extraVMArg) {
		this.delegate = delegate;
		this.extraVMArg = extraVMArg;
	}

	@Override
	public boolean hasAttribute(String attributeName) throws CoreException {
		return VMA_KEY.equals(attributeName) || delegate.hasAttribute(attributeName);
	}

	@Override
	public String getAttribute(String attributeName, String defaultValue) throws CoreException {
		if (VMA_KEY.equals(attributeName)) {
			return getVMArguments();
		} else {
			return delegate.getAttribute(attributeName, defaultValue);
		}
	}

	private String getVMArguments() throws CoreException {
		final String original = delegate.getAttribute(VMA_KEY, "");

		if (original.length() > 0) {
			return original + " " +extraVMArg;
		} else {
			return extraVMArg;
		}
	}

	public boolean isWorkingCopy() {
		return false;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return delegate.getAdapter(adapter);
	}

	@Override
	public boolean contentsEqual(ILaunchConfiguration configuration) {
		return delegate.contentsEqual(configuration);
	}

	@Override
	public ILaunchConfigurationWorkingCopy copy(String name)
			throws CoreException {
		return delegate.copy(name);
	}

	@Override
	public void delete() throws CoreException {
		delegate.delete();
	}

	@Override
	public boolean exists() {
		return delegate.exists();
	}

	@Override
	public boolean getAttribute(String attributeName, boolean defaultValue)
			throws CoreException {
		return delegate.getAttribute(attributeName, defaultValue);
	}

	@Override
	public int getAttribute(String attributeName, int defaultValue)
			throws CoreException {
		return delegate.getAttribute(attributeName, defaultValue);
	}

	@Override
	public List getAttribute(String attributeName, List defaultValue)
			throws CoreException {
		return delegate.getAttribute(attributeName, defaultValue);
	}

	@Override
	public Set getAttribute(String attributeName, Set defaultValue)
			throws CoreException {
		return delegate.getAttribute(attributeName, defaultValue);
	}

	@Override
	public Map getAttribute(String attributeName, Map defaultValue)
			throws CoreException {
		return delegate.getAttribute(attributeName, defaultValue);
	}

	@Override
	public Map getAttributes() throws CoreException {
		return delegate.getAttributes();
	}

	@Override
	public String getCategory() throws CoreException {
		return delegate.getCategory();
	}

	@Override
	public IFile getFile() {
		return delegate.getFile();
	}

	@SuppressWarnings("deprecation")
	@Override
	public IPath getLocation() {
		return delegate.getLocation();
	}

	@Override
	public IResource[] getMappedResources() throws CoreException {
		return delegate.getMappedResources();
	}

	@Override
	public String getMemento() throws CoreException {
		return delegate.getMemento();
	}

	@Override
	public String getName() {
		return delegate.getName();
	}

    @Override
	public Set getModes() throws CoreException {
		return delegate.getModes();
	}

	@Override
	public ILaunchDelegate getPreferredDelegate(Set modes) throws CoreException {
		return delegate.getPreferredDelegate(modes);
	}

	@Override
	public ILaunchConfigurationType getType() throws CoreException {
		return delegate.getType();
	}

	@Override
	public ILaunchConfigurationWorkingCopy getWorkingCopy()
			throws CoreException {
		return delegate.getWorkingCopy();
	}

	@Override
	public boolean isLocal() {
		return delegate.isLocal();
	}

	@Override
	public boolean isMigrationCandidate() throws CoreException {
		return delegate.isMigrationCandidate();
	}

	@Override
	public ILaunch launch(String mode, IProgressMonitor monitor)
			throws CoreException {
		return delegate.launch(mode, monitor);
	}

	@Override
	public ILaunch launch(String mode, IProgressMonitor monitor, boolean build)
			throws CoreException {
		return delegate.launch(mode, monitor, build);
	}

	@Override
	public ILaunch launch(String mode, IProgressMonitor monitor, boolean build,
			boolean register) throws CoreException {
		return delegate.launch(mode, monitor, build, register);
	}

	@Override
	public void migrate() throws CoreException {
		delegate.migrate();
	}

	@Override
	public boolean supportsMode(String mode) throws CoreException {
		return delegate.supportsMode(mode);
	}

	@Override
	public boolean isReadOnly() {
		return delegate.isReadOnly();
	}

}
