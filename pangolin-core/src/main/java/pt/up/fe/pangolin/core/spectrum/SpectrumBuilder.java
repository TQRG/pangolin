package pt.up.fe.pangolin.core.spectrum;

import pt.up.fe.pangolin.core.events.EventListener;
import pt.up.fe.pangolin.core.model.Node.Type;

public class SpectrumBuilder implements EventListener {

	protected SpectrumImpl spectrum;
	
	public SpectrumBuilder() {
		resetSpectrum();
	}
	
	public void resetSpectrum() {
		spectrum = new SpectrumImpl();
	}
	
	public Spectrum getSpectrum() {
		return spectrum;
	}
	
	@Override
	public void endTransaction(String transactionName, boolean[] activity, boolean isError) {
		spectrum.addTransaction(transactionName, activity, isError);
	}
	
	@Override
	public void endTransaction(String transactionName, boolean[] activity, int hashCode, boolean isError) {
		spectrum.addTransaction(transactionName, activity, hashCode, isError);
	}

	@Override
	public void addNode(int id, String name, Type type, int parentId, int line) {
		spectrum.getTree().addNode(name, type, parentId, line);
	}

	@Override
	public void addProbe(int id, int nodeId) {
		spectrum.addProbe(id, nodeId);
	}

	@Override
	public void endSession() {
	}

}
