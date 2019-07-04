package co.gov.cancilleria.miconsulado.web.rest.cms.api;

import co.gov.cancilleria.miconsulado.MiconsuladogatewayApp;
import co.gov.cancilleria.miconsulado.config.ApplicationProperties;
import co.gov.cancilleria.miconsulado.service.cms.CmsService;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MiconsuladogatewayApp.class)
class CmsResourceIT {
    @Autowired
    private CmsService cmsService;

    @Autowired
    private ApplicationProperties appProperties;

    CmsResource cmsResource;

    @BeforeEach
    public void setup() {
        cmsService.setConfiguration(appProperties);
        cmsService.setMaxDepth(20);
        cmsResource = new CmsResource(cmsService);
    }

    @Test
    void testGetAllCmsNodes() {
        String response = cmsResource.getAllCmsNodes();
        assertThat(response).isNotNull();
        //System.out.println(response);
    }


    @Test
    void testGetNavRootCms() throws JSONException, IOException {
        String response = cmsResource.getNavRootCms();
        assertThat(response).isNotNull();
        System.out.println(response);
    }

}
