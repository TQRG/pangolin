package pt.up.fe.pangolin.eclipse.core.launching;

import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.jdt.core.IJavaProject;

import pt.up.fe.pangolin.core.events.EventListener;
import pt.up.fe.pangolin.core.model.Node.Type;
import pt.up.fe.pangolin.core.model.Tree;
import pt.up.fe.pangolin.core.spectrum.Spectrum;
import pt.up.fe.pangolin.core.spectrum.SpectrumBuilder;
import pt.up.fe.pangolin.eclipse.core.Configuration;

public class ExecutionDescription implements EventListener {

	private SpectrumBuilder delegate;
	private ILaunchConfigurationType type;
	private IJavaProject project;
	
	public ExecutionDescription(ILaunchConfigurationType type, IJavaProject project) {
		this.type = type;
		this.project = project;
		this.delegate = new SpectrumBuilder();
	}

	@Override
	public void endTransaction (String transactionName, boolean[] activity, boolean isError) {
		delegate.endTransaction(transactionName, activity, isError);
	}
	
	@Override
	public void endTransaction (String transactionName, boolean[] activity, int hashCode, boolean isError) {
		delegate.endTransaction(transactionName, activity, hashCode, isError);
	}

	@Override
	public void addNode(int id, String name, Type type, int parentId, int line) {
		delegate.addNode(id, name, type, parentId, line);
	}

	@Override
	public void addProbe(int id, int nodeId) {
		delegate.addProbe(id, nodeId);
	}
	
	@Override
	public void endSession() {
		delegate.endSession();
		diagnose();
	}

	public EventListener getEventListener() {
		return delegate;
	}

	public Spectrum getSpectrum() {
		return delegate.getSpectrum();
	}
	
	private void diagnose() {
		Spectrum s = getSpectrum();
		
		Tree t = s.getTree();
		int size = t.size();

		StringBuilder sb = new StringBuilder("{\"type\":\"visualization\",");
		sb.append(t.toString());
		sb.append(",");
		sb.append("\"scores\":[");
		for(int i = 0; i < size; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append("-1");
		}
		sb.append("]}");
		
		Configuration.get().initializeVisualization(project, sb.toString());
	}
}
