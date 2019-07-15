package co.gov.cancilleria.miconsulado.rest.client.cms;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import co.gov.cancilleria.miconsulado.MiconsuladogatewayApp;
import co.gov.cancilleria.miconsulado.service.cms.CmsService;
import co.gov.cancilleria.miconsulado.service.cms.impl.CmsServiceImpl;
import co.gov.cancilleria.miconsulado.service.cms.impl.GetMeshService;


@SpringBootTest(classes = {MiconsuladogatewayApp.class})
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
class CMSServiceIT {
	private CmsService restClient;
	
	@Autowired
	private GetMeshService serviceGetMesh;
	
	@BeforeEach
    public void init() {
        restClient = new CmsServiceImpl(serviceGetMesh);        
	}
	
	
	@Test
	void testNavigationRootCMSService() throws JSONException, IOException {
        JSONObject info = restClient.getCmsNavRoot(20);
		assertThat(info).isNotNull();
	}

}
