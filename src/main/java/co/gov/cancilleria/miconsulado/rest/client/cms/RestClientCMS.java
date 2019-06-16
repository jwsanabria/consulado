package co.gov.cancilleria.miconsulado.rest.client.cms;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.core.rest.node.NodeResponse;
import com.gentics.mesh.parameter.client.NodeParametersImpl;
import com.gentics.mesh.rest.client.MeshRestClient;

@JsonIgnoreProperties(ignoreUnknown = true)
@Service
public class RestClientCMS {
	
	public NodeListResponse callAllInfoCms() {
		MeshRestClient client = MeshRestClient.create("vps206188.vps.ovh.ca", 8080, false);
		client.setLogin("admin", "admin");
		client.login().ignoreElement().blockingAwait();

		NodeListResponse nodes = client.findNodes("miconsulado", new NodeParametersImpl().setLanguages("en")).blockingGet();
		for (NodeResponse nodeResponse : nodes.getData()) {
			System.out.println(nodeResponse.getUuid());
			System.out.println(nodeResponse.getFields().getStringField("name").getString());
		}
		
		return nodes;
	}

}
