package pt.up.fe.pangolin.core.events;

import java.util.Arrays;

import pt.up.fe.pangolin.core.model.Node.Type;

public class VerboseEventListener implements EventListener {

	@Override
	public void endTransaction(String transactionName, boolean[] activity, boolean isError) {
		System.out.println("End Transaction " + transactionName + " " + isError + " " + Arrays.toString(activity));
	}

	@Override
	public void endTransaction(String transactionName, boolean[] activity, int hashCode, boolean isError) {
		System.out.println("End Transaction " + transactionName + " " + hashCode + " " + isError  + " " + Arrays.toString(activity));
	}

	@Override
	public void addNode(int id, String name, Type type, int parentId, int line) {
		System.out.println("Add Node " + id + " " + name + " " + type + " " + parentId + " " + line);
	}

	@Override
	public void addProbe(int id, int nodeId) {
		System.out.println("Add Node " + id + " " + nodeId);
	}

	@Override
	public void endSession() {
		System.out.println("End Session");
	}

}
