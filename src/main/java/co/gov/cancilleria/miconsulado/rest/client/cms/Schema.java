package co.gov.cancilleria.miconsulado.rest.client.cms;

public class Schema {
	private String name;
	private String uuid;
	private String version;
	private String versionUuid;

	// Getter Methods

	public String getName() {
		return name;
	}

	public String getUuid() {
		return uuid;
	}

	public String getVersion() {
		return version;
	}

	public String getVersionUuid() {
		return versionUuid;
	}

	// Setter Methods

	public void setName(String name) {
		this.name = name;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setVersionUuid(String versionUuid) {
		this.versionUuid = versionUuid;
	}
}