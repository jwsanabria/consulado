package co.gov.cancilleria.miconsulado.rest.client.cms;

import java.util.ArrayList;

public class Node {
	private String uuid;
	Creator CreatorObject;
	private String created;
	Editor EditorObject;
	private String edited;
	private String language;
	AvailableLanguages AvailableLanguagesObject;
	ArrayList<Object> tags = new ArrayList<Object>();
	Project ProjectObject;
	ChildrenInfo ChildrenInfoObject;
	Schema SchemaObject;
	private boolean container;
	private String displayField;
	Fields FieldsObject;
	ArrayList<Object> breadcrumb = new ArrayList<Object>();
	private String version;
	Permissions PermissionsObject;

	// Getter Methods

	public String getUuid() {
		return uuid;
	}

	public Creator getCreator() {
		return CreatorObject;
	}

	public String getCreated() {
		return created;
	}

	public Editor getEditor() {
		return EditorObject;
	}

	public String getEdited() {
		return edited;
	}

	public String getLanguage() {
		return language;
	}

	public AvailableLanguages getAvailableLanguages() {
		return AvailableLanguagesObject;
	}

	public Project getProject() {
		return ProjectObject;
	}

	public ChildrenInfo getChildrenInfo() {
		return ChildrenInfoObject;
	}

	public Schema getSchema() {
		return SchemaObject;
	}

	public boolean getContainer() {
		return container;
	}

	public String getDisplayField() {
		return displayField;
	}

	public Fields getFields() {
		return FieldsObject;
	}

	public String getVersion() {
		return version;
	}

	public Permissions getPermissions() {
		return PermissionsObject;
	}

	// Setter Methods

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setCreator(Creator creatorObject) {
		this.CreatorObject = creatorObject;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public void setEditor(Editor editorObject) {
		this.EditorObject = editorObject;
	}

	public void setEdited(String edited) {
		this.edited = edited;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setAvailableLanguages(AvailableLanguages availableLanguagesObject) {
		this.AvailableLanguagesObject = availableLanguagesObject;
	}

	public void setProject(Project projectObject) {
		this.ProjectObject = projectObject;
	}

	public void setChildrenInfo(ChildrenInfo childrenInfoObject) {
		this.ChildrenInfoObject = childrenInfoObject;
	}

	public void setSchema(Schema schemaObject) {
		this.SchemaObject = schemaObject;
	}

	public void setContainer(boolean container) {
		this.container = container;
	}

	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}

	public void setFields(Fields fieldsObject) {
		this.FieldsObject = fieldsObject;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setPermissions(Permissions permissionsObject) {
		this.PermissionsObject = permissionsObject;
	}
}
