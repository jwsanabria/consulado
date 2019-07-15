package co.gov.cancilleria.miconsulado.service.cms.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gentics.mesh.core.rest.navigation.NavigationResponse;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.parameter.client.NavigationParametersImpl;
import com.gentics.mesh.parameter.client.NodeParametersImpl;
import com.gentics.mesh.rest.client.MeshBinaryResponse;
import com.gentics.mesh.rest.client.MeshRestClient;

import co.gov.cancilleria.miconsulado.config.ApplicationProperties;

@Service
@Scope("prototype")
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
public class GetMeshService {
	
	static final String APP_NAME = "miConsulado";
	static final String ATTR_TITLE = "titulo";
	static final String ATTR_IMAGEN = "imagen";
	
	
	private ApplicationProperties appProperties;
	
	private MeshRestClient client;
	
	@Autowired
	public GetMeshService(ApplicationProperties appProperties) {
		super();
		this.appProperties = appProperties;
	}
	
	private void createConnection() {
		this.client = MeshRestClient.create(this.appProperties.getCms().getHost(),
				this.appProperties.getCms().getPort(), this.appProperties.getCms().isHttps());
		this.client.setLogin(System.getenv(this.appProperties.getCms().getUser()),
				System.getenv(this.appProperties.getCms().getPassword()));
		this.client.login().ignoreElement().blockingAwait();
	}
	

	private void closeConnection() {
		client.logout();
		client.close();
	}
	

	public NavigationResponse getNavigation() {
		createConnection();
		NavigationResponse response = client.navroot(APP_NAME, "/", new NavigationParametersImpl().setIncludeAll(true)).blockingGet();
		closeConnection();
		return response;
	}
	
	public MeshBinaryResponse downloadBinaryField(String uuid) {
		createConnection();
		MeshBinaryResponse response = client.downloadBinaryField(APP_NAME, uuid, null, ATTR_IMAGEN, new NodeParametersImpl().setLanguages("en"))
				.blockingGet();
		closeConnection();
		return response;
	}
	
	public NodeListResponse findNodes() {
		createConnection();
		NodeListResponse response = client.findNodes(APP_NAME, new NodeParametersImpl().setLanguages("en")).blockingGet();
		closeConnection();
		return response;
	}

}
