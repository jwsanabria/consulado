package co.gov.cancilleria.miconsulado.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Miconsuladogateway.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	CmsProperties cms;

	public CmsProperties getCms() {
		return cms;
	}

	public void setCms(CmsProperties cms) {
		this.cms = cms;
	}
	

	@Override
	public String toString() {
		return "ApplicationProperties [cms=" + cms +  "]";
	}
	
	
	
}
