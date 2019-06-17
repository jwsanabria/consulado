package co.gov.cancilleria.miconsulado.rest.client.cms;

import static org.assertj.core.api.Assertions.assertThat;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.gentics.mesh.core.rest.node.NodeListResponse;

import co.gov.cancilleria.miconsulado.MiconsuladogatewayApp;
import co.gov.cancilleria.miconsulado.service.cms.CmsService;
import co.gov.cancilleria.miconsulado.service.cms.impl.CmsServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

/**
* Integration tests for {@link MailService}.
*/
@SpringBootTest(classes = MiconsuladogatewayApp.class)
class CMSServiceIT {
	private CmsService restClient;
	
	@BeforeEach
    public void init() {
        restClient = new CmsServiceImpl();
	}

	@Test
	void testCallCMSService() {
		String info = restClient.getAllCmsNodes();
		assertThat(info).isNotNull();
		//assertThat(info.getData()).isNotNull();
	}
	
	@Test
	void testNavigationRootCMSService() {
		String info = restClient.getCmsNavRoot();
		assertThat(info).isNotNull();
		System.out.println(info);
		//assertThat(info.getData()).isNotNull();
	}

}
