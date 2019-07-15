package co.gov.cancilleria.miconsulado.service.cms;

import java.io.IOException;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public interface CmsService {
	
	String getAllCmsNodes();

    JSONObject getCmsNavRoot(int maxDepth) throws JSONException, IOException;

}
