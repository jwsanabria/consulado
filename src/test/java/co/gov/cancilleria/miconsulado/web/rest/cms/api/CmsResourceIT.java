package co.gov.cancilleria.miconsulado.web.rest.cms.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import co.gov.cancilleria.miconsulado.MiconsuladogatewayApp;
import co.gov.cancilleria.miconsulado.service.cms.CmsService;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MiconsuladogatewayApp.class)
class CmsResourceIT {
	@Autowired
	private CmsService cmsService;
	
	CmsResource cmsResource;
	
	@BeforeEach
    public void setup() {
        cmsResource = new CmsResource(cmsService);
    }

	@Test
	void testGetAllCmsNodes() {
		String response=cmsResource.getAllCmsNodes();
		assertThat(response).isNotNull();
		//System.out.println(response);
	}
	
	
	@Test
	void testGetNavRootCms() throws JSONException, IOException {
		String response=cmsResource.getNavRootCms();
		assertThat(response).isNotNull();
		//System.out.println(response);
	}

}
