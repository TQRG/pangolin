package pt.up.fe.pangolin.core.runtime;

import pt.up.fe.pangolin.core.events.EventListener;
import pt.up.fe.pangolin.core.events.MultiEventListener;
import pt.up.fe.pangolin.core.exporter.Exporter;
import pt.up.fe.pangolin.core.model.Node;
import pt.up.fe.pangolin.core.model.Tree;
import pt.up.fe.pangolin.core.model.Node.Type;
import pt.up.fe.pangolin.core.runtime.ProbeGroup.HitProbe;
import pt.up.fe.pangolin.core.spectrum.Spectrum;
import pt.up.fe.pangolin.core.spectrum.SpectrumBuilder;
import pt.up.fe.pangolin.core.spectrum.diagnosis.SFL;

import java.util.Arrays;

public class Collector {

	private static Collector collector;

	private MultiEventListener listener;
	private Tree tree;
	private HitVector hitVector;
	private SpectrumBuilder builder;
	private String exportDir;

	public static Collector instance() {
		return collector;
	}

	public static void start(EventListener listener, String dir) {
		if (collector == null) {
			collector = new Collector(listener, dir);
		}
	}

	private Collector(EventListener listener, String dir) {
		this.listener = new MultiEventListener();
		this.builder = new SpectrumBuilder();
		addListener(this.builder);
//		addListener(listener);
		if (dir == null)
			dir = Exporter.getTemporaryDir();
		exportDir = dir;

		this.tree = new Tree();
		this.hitVector = new HitVector();
	}

	public void addListener(EventListener listener) {
		if (listener != null) {
			this.listener.add(listener);
		}
	}

	public SpectrumBuilder getBuilder() {
		return this.builder;
	}

	public synchronized Node createNode(Node parent, String name, Type type, int line) {
		Node node = tree.addNode(name, type, parent.getId(), line);
		listener.addNode(node.getId(), name, type, parent.getId(), line);
		return node;
	}

	public synchronized HitProbe createHitProbe(String groupName, int nodeId) {
		HitProbe p = hitVector.registerProbe(groupName, nodeId);
		listener.addProbe(p.getId(), p.getNodeId());
		return p;
	}

	public synchronized void endTransaction(String transactionName, boolean isError) {
		listener.endTransaction(transactionName, hitVector.get(), isError); //hitVector.get()
	}

	public synchronized void startTransaction() {
		hitVector.reset();
	}

	public synchronized void endSession() {
		// tree.print();
		listener.endSession();
		Spectrum spectrum = builder.getSpectrum();

		Exporter.exportAnalysis(spectrum, exportDir);

	}
	
	public synchronized boolean[] getHitVector (String className) {
        return hitVector.get(className);
    }
	
	public synchronized boolean existsHitVector (String className) {
		return hitVector.existsHitVector(className);
	}

	public Node getRootNode() {
		return tree.getRoot();
	}

}
