package co.gov.cancilleria.miconsulado.rest.client.cms;

import java.util.ArrayList;
import java.util.List;

public class InfoCMS {
	private String uuid;
	Node NodeObject;
	List<Object> children = new ArrayList<Object>();

	// Getter Methods

	public String getUuid() {
		return uuid;
	}

	public Node getNode() {
		return NodeObject;
	}

	// Setter Methods

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setNode(Node nodeObject) {
		this.NodeObject = nodeObject;
	}
}
