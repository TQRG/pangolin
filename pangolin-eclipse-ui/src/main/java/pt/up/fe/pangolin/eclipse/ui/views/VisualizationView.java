package pt.up.fe.pangolin.eclipse.ui.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import pt.up.fe.pangolin.eclipse.core.Configuration;
import pt.up.fe.pangolin.eclipse.core.visualization.VisualizationBrowser;

public class VisualizationView extends ViewPart {

	private VisualizationBrowser browser = null;

	@Override
	public void createPartControl(Composite parent) {
		browser = VisualizationBrowser.newBrowser(parent, this);
		Configuration.get().setVisualizationBrowser(browser);
	}

	@Override
	public void setFocus() {
		if (browser != null) {
			browser.setFocus();
		}
	}

	public void dispose() {
		if (browser != null) {
			browser.dispose();
			browser = null;
		}
		super.dispose();
	}
}
