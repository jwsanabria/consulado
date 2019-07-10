package co.gov.cancilleria.miconsulado.service.cms;

import java.io.IOException;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import co.gov.cancilleria.miconsulado.config.ApplicationProperties;

public interface CmsService {
	void setMaxDepth(int maxDepth);
	
	void setConfiguration(ApplicationProperties properties);
	
	String getAllCmsNodes();

    JSONObject getCmsNavRoot() throws JSONException, IOException;

}
