package co.gov.cancilleria.miconsulado.service.cms;

import com.gentics.mesh.core.rest.node.NodeListResponse;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.io.IOException;

public interface CmsService {
	
	String getAllCmsNodes();
    
    String getCmsNavRoot() throws JSONException, IOException;

}
