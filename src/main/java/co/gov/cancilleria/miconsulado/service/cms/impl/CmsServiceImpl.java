package co.gov.cancilleria.miconsulado.service.cms.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gentics.mesh.core.rest.navigation.NavigationElement;
import com.gentics.mesh.core.rest.navigation.NavigationResponse;
import com.gentics.mesh.core.rest.node.FieldMap;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.core.rest.node.field.NodeField;
import com.gentics.mesh.core.rest.node.field.NodeFieldListItem;
import com.gentics.mesh.core.rest.node.field.list.NodeFieldList;
import com.gentics.mesh.parameter.client.NavigationParametersImpl;
import com.gentics.mesh.parameter.client.NodeParametersImpl;
import com.gentics.mesh.rest.client.MeshBinaryResponse;
import com.gentics.mesh.rest.client.MeshRestClient;

import co.gov.cancilleria.miconsulado.config.ApplicationProperties;
import co.gov.cancilleria.miconsulado.service.cms.CmsService;

@Service
@JsonIgnoreProperties(ignoreUnknown = true)
@EnableConfigurationProperties(value = ApplicationProperties.class)
public class CmsServiceImpl implements CmsService {
	private int maxDepth;

	static final String APP_NAME = "miConsulado";
	static final String ATTR_UUID = "uuid";
	static final String ATTR_IMAGEN = "imagen";
	static final String ATTR_CONTENT = "contenido";
	static final String ATTR_RESOURCES = "recursos";
	static final String ATTR_PROCEDURES = "procedimientos";
	static final String ATTR_NAME = "nombre";
	static final String ATTR_ORDER_COMP = "ordenComponentes";
	static final String ATTR_COMPONENTS = "componentes";
	static final String ATTR_PROC_COSTS = "procedimientoCostos";
	static final String ATTR_ITEM_MENU = "itemMenu";
	static final String ATTR_UUID_VIEW = "uuidVista";
	static final String ATTR_TYPE_SQUEMA = "tipoEsquema";
	static final String ATTR_COLOR_BACKGROUND = "colorFondo";
	static final String ATTR_ICON = "icono";
	static final String ATTR_ACCORDION = "acordeon";
	static final String ATTR_TITLE = "titulo";

	@Autowired
	private ApplicationProperties appProperties;

	private LinkedList<JSONObject> listProceduresJson;

	Map<String, String> bufferMap = new HashMap<String, String>();

	public void setConfiguration(ApplicationProperties properties) {
		this.appProperties = properties;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	private MeshRestClient getRestClient() {
		MeshRestClient client = MeshRestClient.create(appProperties.getCms().getHost(),
				appProperties.getCms().getPort(), appProperties.getCms().isHttps());
		client.setLogin(System.getenv(appProperties.getCms().getUser()),
				System.getenv(appProperties.getCms().getPassword()));
		client.login().ignoreElement().blockingAwait();

		return client;
	}

	private JSONObject getNavRoot() throws JSONException, IOException {

		// Consulta el CMS
		NavigationResponse navRoot = getRestClient()
				.navroot(APP_NAME, "/", new NavigationParametersImpl().setIncludeAll(true)).blockingGet();

		// Estructura para el Front-End
		JSONObject structureFrontJson = new JSONObject();

		// Se reinicia la lista de procedimientos
		listProceduresJson = new LinkedList<JSONObject>();

		List<NavigationElement> navigationElementList = navRoot.getChildren();

		for (NavigationElement navRootElement : navigationElementList) {
			if (navRootElement.getNode().getDisplayName().toLowerCase().equals(ATTR_RESOURCES)) {
				buildResources(structureFrontJson, navRootElement);

			} else if (navRootElement.getNode().getDisplayName().toLowerCase().equals(ATTR_CONTENT)) {
				// Se iteran los procedimientos
				List<NavigationElement> nodesProcedureList = navRootElement.getChildren();

				// Procedimientos
				for (NavigationElement nodeProcedure : nodesProcedureList) {
					buildProcedures(nodeProcedure);

					JSONArray arrayProceduresJson = new JSONArray(listProceduresJson);
					structureFrontJson.accumulate(ATTR_PROCEDURES, arrayProceduresJson);

				}

			} else {
				// Se verifica tratamiento
			}

		}

		return structureFrontJson;
	}

	private void buildResources(JSONObject structureFrontJson, NavigationElement node)
			throws JSONException, IOException {
		// Se iteran los Recursos
		List<NavigationElement> nodesListResources = node.getChildren();

		// Imagenes y Colores
		for (NavigationElement nodeResource : nodesListResources) {
			JSONObject objectResource = new JSONObject();

			List<NavigationElement> nodesResourceItems = nodeResource.getChildren();

			// Se iteran todos los items de los recursos
			for (NavigationElement nodeResourceItem : nodesResourceItems) {

				JSONObject objectResourceItem = new JSONObject();

				// Se incorpora el UUID
				objectResourceItem.put(ATTR_UUID, nodeResourceItem.getUuid());

				// Se sacan los Campos de los items de los Recursos
				FieldMap fieldsMap = nodeResourceItem.getNode().getFields();
				for (String key : fieldsMap.keySet()) {
					if (key.equals(ATTR_IMAGEN)) {
						objectResourceItem.put("uuid", nodeResourceItem.getUuid());

						// Consulta el CMS
						objectResourceItem.put("base64", "data:" + "image/png" + ";base64,"
								+ getBase64ImageResource(nodeResourceItem.getUuid()));

					} else {
						// Demas Campos diferentes a Imagen
						objectResourceItem.put(key, fieldsMap.getStringField(key));
					}

				}

				objectResource.accumulate(nodeResource.getNode().getDisplayName().toLowerCase(), objectResourceItem);

			}

			structureFrontJson.accumulate(node.getNode().getDisplayName().toLowerCase(), objectResource);
		}

	}

	public String getBase64ImageResource(String uuid) throws IOException {
		String imagenEncode = "";
		if (bufferMap.containsKey(uuid)) {
			imagenEncode = bufferMap.get(uuid);
		} else {

			MeshBinaryResponse binary = getRestClient()
					.downloadBinaryField(APP_NAME, uuid, null, ATTR_IMAGEN, new NodeParametersImpl().setLanguages("en"))
					.blockingGet();
			byte[] bytes = IOUtils.toByteArray(binary.getStream());
			Base64.Encoder encoder = Base64.getEncoder();
			imagenEncode = encoder.encodeToString(bytes);
			bufferMap.put(uuid, imagenEncode);

		}
		return imagenEncode;
	}

	private void buildProcedures(NavigationElement nodeProcedure) throws JSONException {

		JSONObject procedureObject = new JSONObject();

		// ---Datos Generales
		procedureObject.put(ATTR_UUID, nodeProcedure.getUuid());
		procedureObject.put(ATTR_NAME, nodeProcedure.getNode().getDisplayName());

		FieldMap fieldsMap = nodeProcedure.getNode().getFields();
		
		if (fieldsMap.hasField(ATTR_ORDER_COMP)) {
			procedureObject.put(ATTR_ORDER_COMP, new JSONArray(getOrderComponents(fieldsMap)));
		}
		
		// --Componente
		procedureObject.put(ATTR_COMPONENTS, buildComponents(nodeProcedure, getOrderComponents(fieldsMap)));
		listProceduresJson.addFirst(procedureObject);

	}

	private JSONArray buildComponents(NavigationElement nodeProcedure, List<String> arrayOrderComponents)
			throws JSONException {

		JSONObject[] arrayComponentJson = new JSONObject[arrayOrderComponents.size()];
		// ----Componentes

		List<NavigationElement> nodesComponentsList = nodeProcedure.getChildren();
		if (nodesComponentsList != null) {
			for (NavigationElement nodeComponent : nodesComponentsList) {
				JSONObject componentObject = new JSONObject();
				Boolean flag = Boolean.TRUE;

				componentObject.put(ATTR_UUID, nodeComponent.getUuid());
				componentObject.put(ATTR_TYPE_SQUEMA, nodeComponent.getNode().getSchema().getName());
				if (nodeComponent.getNode().getSchema().getName().equals(ATTR_ITEM_MENU)
						|| nodeComponent.getNode().getSchema().getName().equals(ATTR_PROC_COSTS)) {
					componentObject.put(ATTR_UUID_VIEW, nodeComponent.getUuid());
				}

				FieldMap fieldsMap = nodeComponent.getNode().getFields();
				for (String key : fieldsMap.keySet()) {
					if (key.equals(ATTR_COLOR_BACKGROUND)) {
						NodeField nodeColorFondo = fieldsMap.getNodeField(key);
						JSONObject fondoObject = new JSONObject();
						fondoObject.put(ATTR_UUID, nodeColorFondo.getUuid());
						componentObject.put(key, fondoObject);

					} else if (key.equals(ATTR_ICON)) {
						NodeField icon = fieldsMap.getNodeField(key);
						componentObject.put(key, icon.getUuid());

					} else if (key.equals(ATTR_ORDER_COMP)) {
						componentObject.put(key, new JSONArray(getOrderComponents(fieldsMap)));
					}  else {
						componentObject.put(key, fieldsMap.getStringField(key));
						if (fieldsMap.getStringField(key).getString().equals(ATTR_ACCORDION)) {
							componentObject.put(ATTR_COMPONENTS,
									buildComponents(nodeComponent, getOrderComponents(fieldsMap)));
							flag = Boolean.FALSE;
						}
					}

				}

				arrayComponentJson[arrayOrderComponents.indexOf(nodeComponent.getUuid())] = componentObject;

				if (!nodeComponent.getNode().getChildrenInfo().isEmpty() && flag) {
					buildProcedures(nodeComponent);

				}

			}
		}

		return new JSONArray(arrayComponentJson);
	}

	private List<String> getOrderComponents(FieldMap fieldsMap) {
		List<String> listComponentOrder = new ArrayList<String>();

		if (fieldsMap.hasField(ATTR_ORDER_COMP)) {
			NodeFieldList listNode = fieldsMap.getNodeFieldList(ATTR_ORDER_COMP);
			JSONArray arrayOrder = new JSONArray();
			for (NodeFieldListItem nodeOrder : listNode.getItems()) {
				arrayOrder.put(nodeOrder.getUuid());
				listComponentOrder.add(nodeOrder.getUuid());
			}
		}

		return listComponentOrder;
	}

	private NodeListResponse getAllNodes() {
		NodeListResponse nodes = getRestClient().findNodes(APP_NAME, new NodeParametersImpl().setLanguages("en"))
				.blockingGet();
		return nodes;
	}

	public String getAllCmsNodes() {
		NodeListResponse cmsNodes = this.getAllNodes();
		return cmsNodes.toJson();
	}

	public JSONObject getCmsNavRoot() throws JSONException, IOException {
		return this.getNavRoot();
	}

}
