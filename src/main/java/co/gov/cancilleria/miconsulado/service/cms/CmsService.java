package co.gov.cancilleria.miconsulado.service.cms;

import com.gentics.mesh.core.rest.node.NodeListResponse;

import co.gov.cancilleria.miconsulado.config.ApplicationProperties;
import co.gov.cancilleria.miconsulado.config.CmsProperties;

import org.springframework.boot.configurationprocessor.json.JSONException;

import java.io.IOException;

public interface CmsService {
	void setMaxDepth(int maxDepth);
	
	void setConfiguration(ApplicationProperties properties);
	
	String getAllCmsNodes();
    
    String getCmsNavRoot() throws JSONException, IOException;

}
