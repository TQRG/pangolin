package pt.up.fe.pangolin.core.events;

import pt.up.fe.pangolin.core.model.Node.Type;

public class NullEventListener implements EventListener {

	@Override
	public void endTransaction(String transactionName, boolean[] activity, boolean isError) {
	}

	@Override
	public void endTransaction(String transactionName, boolean[] activity, int hashCode, boolean isError) {
	}

	@Override
	public void addNode(int id, String name, Type type, int parentId, int line) {
	}

	@Override
	public void addProbe(int id, int nodeId) {
	}

	@Override
	public void endSession() {
	}

}
