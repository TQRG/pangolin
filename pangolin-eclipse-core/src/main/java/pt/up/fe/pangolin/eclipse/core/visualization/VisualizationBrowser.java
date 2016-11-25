package pt.up.fe.pangolin.eclipse.core.visualization;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class VisualizationBrowser {

	private IJavaProject currentProject = null;
	private ViewPart viewPart = null;
	private String url = null;

	private static String lastJsonMessage = null;

	public static VisualizationBrowser newBrowser(Composite parent, ViewPart viewPart) {
		return new VisualizationBrowser(parent, viewPart);
	}

	public static void revealView() {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().
					getActivePage().showView("pt.up.fe.pangolin.eclipse.ui.visualizationView");
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Browser browser;

	private VisualizationBrowser(Composite parent, ViewPart viewPart) {
		this.browser = new Browser(parent, SWT.NONE);
		this.viewPart = viewPart;
		this.browser.setJavascriptEnabled(true);
		new TriggerEventHandler(this);
		
		this.browser.addProgressListener(new ProgressListener() {

			@Override
			public void changed(ProgressEvent event) {
			}

			@Override
			public void completed(ProgressEvent event) {
				callInitializeVisualization();
			}
		});

		this.url = VisualizationResource.get();
		try {
			setUrl();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void callInitializeVisualization(final IJavaProject project, final String jsonMessage) {
		currentProject = project;
		lastJsonMessage = jsonMessage;
		
		setUrl();
	}

	private void callInitializeVisualization() {
		if (lastJsonMessage != null) {
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					browser.execute("initializeVisualization('"+lastJsonMessage+"')");
				}
			});
		}
	}
	
	public void maximize() {
		setViewState(IWorkbenchPage.STATE_MAXIMIZED);
	}

	private void setViewState(final int state) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				int currentState = page.getPartState(page.getReference(viewPart));
				if(currentState != state) {
					page.activate(viewPart);
					page.setPartState(page.getReference(viewPart), state);
				}
			}
		});
	}

	public void setUrl() {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				browser.setUrl(url);
			}
		});
	}

	public void setFocus() {
		browser.setFocus();
	}

	public void dispose() {
		browser.dispose();
	}

	public Browser getBrowser() {
		return browser;
	}

	public IJavaProject getProject() {
		return currentProject;
	}
}
