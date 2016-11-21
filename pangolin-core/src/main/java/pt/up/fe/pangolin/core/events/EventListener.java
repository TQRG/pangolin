package pt.up.fe.pangolin.core.events;

import pt.up.fe.pangolin.core.model.Node.Type;

public interface EventListener {

	void endTransaction (String transactionName, boolean[] activity, boolean isError);
	
	void endTransaction (String transactionName, boolean[] activity, int hashCode, boolean isError);

	void addNode(int id, String name, Type type, int parentId, int line);

	void addProbe(int id, int nodeId);
	
	void endSession();
}
