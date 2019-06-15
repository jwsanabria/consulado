package co.gov.cancilleria.miconsulado.rest.client.cms;

public class AvailableLanguages {
	En EnObject;

	// Getter Methods

	public En getEn() {
		return EnObject;
	}

	// Setter Methods

	public void setEn(En enObject) {
		this.EnObject = enObject;
	}

	public class En {
		private boolean published;
		private String version;

		// Getter Methods

		public boolean getPublished() {
			return published;
		}

		public String getVersion() {
			return version;
		}

		// Setter Methods

		public void setPublished(boolean published) {
			this.published = published;
		}

		public void setVersion(String version) {
			this.version = version;
		}
	}
}
