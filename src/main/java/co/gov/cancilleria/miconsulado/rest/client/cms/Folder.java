package co.gov.cancilleria.miconsulado.rest.client.cms;

public class Folder {
	private String schemaUuid;
	private float count;

	// Getter Methods

	public String getSchemaUuid() {
		return schemaUuid;
	}

	public float getCount() {
		return count;
	}

	// Setter Methods

	public void setSchemaUuid(String schemaUuid) {
		this.schemaUuid = schemaUuid;
	}

	public void setCount(float count) {
		this.count = count;
	}
}