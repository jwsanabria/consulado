package co.gov.cancilleria.miconsulado.rest.client.cms;

import static org.assertj.core.api.Assertions.assertThat;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.gentics.mesh.core.rest.node.NodeListResponse;

import co.gov.cancilleria.miconsulado.MiconsuladogatewayApp;
import static org.assertj.core.api.Assertions.assertThat;

/**
* Integration tests for {@link MailService}.
*/
@SpringBootTest(classes = MiconsuladogatewayApp.class)
class CMSService {
	private RestClientCMS restClient;
	
	@BeforeEach
    public void init() {
        restClient = new RestClientCMS();
	}

	@Test
	void testCallCMSService() {
		NodeListResponse info = restClient.callAllInfoCms();
		assertThat(info).isNotNull();
		assertThat(info.getData()).isNotNull();
		
	}

}
