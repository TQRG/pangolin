package pt.up.fe.pangolin.eclipse.core.launching;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener2;

public class LaunchesListener implements ILaunchesListener2 {

	private ILaunch currentLaunch = null;

	public boolean isEmpty() {
		return currentLaunch == null;
	}

	public void setCurrentLaunch(ILaunch launch) {
		this.currentLaunch = launch;
	}

	@Override
	public void launchesAdded(ILaunch[] launches) {
	}

	@Override
	public void launchesChanged(ILaunch[] launches) {
	}

	@Override
	public void launchesRemoved(ILaunch[] launches) {
	}

	@Override
	public void launchesTerminated(ILaunch[] launches) {
		for(ILaunch launch : launches) {
			if(launch == currentLaunch) {
				currentLaunch = null;
			}
		}

	}

}
