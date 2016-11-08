package pt.up.fe.pangolin.eclipse.ui.launching;

import org.eclipse.swt.graphics.Image;

import pt.up.fe.pangolin.eclipse.core.launching.PangolinConfigurationTab;
import pt.up.fe.pangolin.eclipse.ui.Activator;

public class LaunchConfigurationTab extends PangolinConfigurationTab {

	@Override
	public String getName() {
		return "Pangolin";
	}
	
	@Override
	public Image getImage() {
		return Activator.getImage("icons/crowbar.png");
	}

}
