package pt.up.fe.pangolin.eclipse.core.visualization;

public class EventDescription {

	private String type;
	private Integer nodeId;
	
	public EventDescription() {
	}
	
	public EventDescription(String type, Integer nodeId) {
		this.type = type;
		this.nodeId = nodeId;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}
	
	public String getType() {
		return type;
	}
	
	public Integer getNodeId() {
		return nodeId;
	}
}
