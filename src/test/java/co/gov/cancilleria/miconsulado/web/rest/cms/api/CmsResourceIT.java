package co.gov.cancilleria.miconsulado.web.rest.cms.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import co.gov.cancilleria.miconsulado.MiconsuladogatewayApp;
import co.gov.cancilleria.miconsulado.service.cms.CmsService;

@SpringBootTest(classes = MiconsuladogatewayApp.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
class CmsResourceIT {
	@Autowired
    CmsService cmsService;

    CmsResource cmsResource;

    @BeforeEach
    public void setup() {
        cmsResource = new CmsResource(cmsService);
    }

    @Test
    void testGetAllCmsNodes() {
        String response = cmsResource.getAllCmsNodes();
        assertThat(response).isNotNull();
    }


    @Test
    void testGetNavRootCms() throws JSONException, IOException {
        String response = cmsResource.getNavRootCms();
        assertThat(response).isNotNull();
    }

}
