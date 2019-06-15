package co.gov.cancilleria.miconsulado.rest.client.cms;

public class Permissions {
	private boolean create;
	private boolean read;
	private boolean update;
	private boolean delete;
	private boolean publish;
	private boolean readPublished;

	// Getter Methods

	public boolean getCreate() {
		return create;
	}

	public boolean getRead() {
		return read;
	}

	public boolean getUpdate() {
		return update;
	}

	public boolean getDelete() {
		return delete;
	}

	public boolean getPublish() {
		return publish;
	}

	public boolean getReadPublished() {
		return readPublished;
	}

	// Setter Methods

	public void setCreate(boolean create) {
		this.create = create;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	public void setReadPublished(boolean readPublished) {
		this.readPublished = readPublished;
	}
}
