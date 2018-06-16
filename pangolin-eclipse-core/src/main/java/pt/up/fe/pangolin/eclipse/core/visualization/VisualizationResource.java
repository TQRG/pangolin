package pt.up.fe.pangolin.eclipse.core.visualization;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class VisualizationResource {

	public static String get() {
		try {
			Bundle bundle = Platform.getBundle("pt.up.fe.pangolin.eclipse.ui");
			URL url = bundle.getEntry("resources/visualization/index.html");
			return FileLocator.resolve(url).toString();
		} catch (Exception e) {
			return null;
		}
	}


}
