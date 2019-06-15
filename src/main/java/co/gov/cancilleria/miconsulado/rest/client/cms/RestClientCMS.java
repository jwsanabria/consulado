package co.gov.cancilleria.miconsulado.rest.client.cms;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.core.rest.node.NodeResponse;
import com.gentics.mesh.parameter.client.NodeParametersImpl;
import com.gentics.mesh.rest.client.MeshRestClient;

@JsonIgnoreProperties(ignoreUnknown = true)
@Service
public class RestClientCMS {
	
	@Autowired
    private RestTemplate restTemplate;
	
	public NodeListResponse callAllInfoCms() {
		MeshRestClient client = MeshRestClient.create("vps206188.vps.ovh.ca", 8080, false);
		client.setLogin("admin", "admin");
		client.login().ignoreElement().blockingAwait();

		NodeListResponse nodes = client.findNodes("miconsulado", new NodeParametersImpl().setLanguages("en")).blockingGet();
		for (NodeResponse nodeResponse : nodes.getData()) {
			System.out.println(nodeResponse.getUuid());
			System.out.println(nodeResponse.getFields().getStringField("name").getString());
		}
		
		
		
		/*String plainCreds = "admin:admin";
		String url = "http://vps206188.vps.ovh.ca:8080/api/v2/miconsulado/navroot/?maxDepth=20";
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		ResponseEntity<InfoCMS> response = restTemplate.exchange(url, HttpMethod.GET, request, InfoCMS.class);
		InfoCMS info = response.getBody();
		
		return info;*/
		return nodes;
	}

}
