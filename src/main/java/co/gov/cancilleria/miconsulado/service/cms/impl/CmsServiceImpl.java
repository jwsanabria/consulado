package co.gov.cancilleria.miconsulado.service.cms.impl;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gentics.mesh.core.rest.navigation.NavigationResponse;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.parameter.ParameterProvider;
import com.gentics.mesh.parameter.client.NavigationParametersImpl;
import com.gentics.mesh.parameter.client.NodeParametersImpl;
import com.gentics.mesh.rest.client.MeshRestClient;

import co.gov.cancilleria.miconsulado.service.cms.CmsService;


@Service
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsServiceImpl implements CmsService{
	
    
    private NavigationResponse getNavRoot() {
    	MeshRestClient client = MeshRestClient.create("vps206188.vps.ovh.ca", 8080, false);
		client.setLogin("admin", "admin");
		client.login().ignoreElement().blockingAwait();
		
    	NavigationResponse nav = client.navroot("miconsulado", "/", new  NavigationParametersImpl().setMaxDepth(20)).blockingGet();
    	return nav;
    }


	private NodeListResponse getAllNodes() {
		MeshRestClient client = MeshRestClient.create("vps206188.vps.ovh.ca", 8080, false);
		client.setLogin("admin", "admin");
		client.login().ignoreElement().blockingAwait();

		
		NodeListResponse nodes = client.findNodes("miconsulado", new NodeParametersImpl().setLanguages("en")).blockingGet();
		/*for (NodeResponse nodeResponse : nodes.getData()) {
			System.out.println(nodeResponse.getUuid());
			System.out.println(nodeResponse.getFields().getStringField("name").getString());
		}*/
		
		return nodes;
	}


    public String getAllCmsNodes(){
        NodeListResponse cmsNodes = this.getAllNodes();
        
        return cmsNodes.toJson();
    }
    
    public String getCmsNavRoot() {
    	NavigationResponse cmsNavigation = this.getNavRoot();
    	return cmsNavigation.toJson();
    }

}
