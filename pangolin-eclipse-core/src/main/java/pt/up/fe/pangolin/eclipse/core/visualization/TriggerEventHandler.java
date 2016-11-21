package pt.up.fe.pangolin.eclipse.core.visualization;

import org.eclipse.swt.browser.BrowserFunction;

import pt.up.fe.pangolin.core.flexjson.JSONDeserializer;
import pt.up.fe.pangolin.core.model.Node;
import pt.up.fe.pangolin.core.model.NodeUtils;
import pt.up.fe.pangolin.eclipse.core.Configuration;
import pt.up.fe.pangolin.eclipse.core.launching.ExecutionDescription;

public class TriggerEventHandler extends BrowserFunction {

	private VisualizationBrowser vb;
	
	public TriggerEventHandler(VisualizationBrowser vb) {
		super(vb.getBrowser(), "triggerEvent");
		this.vb = vb;
	}

	@Override
	public Object function(Object[] args) {
		if(args.length == 1 && args[0] instanceof String) {
			String str = (String) args[0];

			try {
				EventDescription e = new JSONDeserializer<EventDescription>().deserialize(str,EventDescription.class);
				handleEvent(e);
			}
			catch (Exception e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	private void handleEvent(EventDescription e) {
		if("click".equals(e.getType())) {
			if(e.getNodeId() != null) {
				try {
					ExecutionDescription d = Configuration.get().getExecutionDescription();
					Node n = d.getSpectrum().getTree().getNode(e.getNodeId());
					String classname = NodeUtils.getClassName(n);

					if(classname != null) {
						int linenumber = NodeUtils.getLineNumber(n);
						if(linenumber != -1) {
							OpenEditorAction.openElement(vb.getProject(), classname, linenumber);
						}
						else {
							OpenEditorAction.openElement(vb.getProject(), classname);
						}
					}
				} catch (Exception ex) {}


			}
		}
	}
}