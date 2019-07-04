package co.gov.cancilleria.miconsulado.service.cms.impl;

import co.gov.cancilleria.miconsulado.config.ApplicationProperties;
import co.gov.cancilleria.miconsulado.service.cms.CmsService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gentics.mesh.core.rest.navigation.NavigationElement;
import com.gentics.mesh.core.rest.navigation.NavigationResponse;
import com.gentics.mesh.core.rest.node.FieldMap;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.core.rest.node.NodeResponse;
import com.gentics.mesh.core.rest.node.field.NodeField;
import com.gentics.mesh.core.rest.node.field.NodeFieldListItem;
import com.gentics.mesh.core.rest.node.field.list.NodeFieldList;
import com.gentics.mesh.parameter.client.NavigationParametersImpl;
import com.gentics.mesh.parameter.client.NodeParametersImpl;
import com.gentics.mesh.rest.client.MeshBinaryResponse;
import com.gentics.mesh.rest.client.MeshRestClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;


@Service
@JsonIgnoreProperties(ignoreUnknown = true)
@EnableConfigurationProperties(value = ApplicationProperties.class)
public class CmsServiceImpl implements CmsService {

    private int maxDepth;

    private static final String APP_NAME = "miConsulado";
    private static final String ATTR_UUID = "uuid";
    private static final String ATTR_IMAGEN = "imagen";
    private static final String ATTR_CONTENIDO = "contenido";
    private static final String ATTR_RECURSOS = "recursos";
    private static final String ATTR_PROCEDIMIENTOS = "procedimientos";
    private static final String ATTR_NOMBRE = "nombre";
    private static final String ATTR_ORDEN_COMP = "ordenComponentes";
    private static final String ATTR_COMPONENTES = "componentes";
    private static final String ATTR_PROC_COSTOS = "procedimientoCostos";
    private static final String ATTR_ITEM_MENU = "itemMenu";
    private static final String ATTR_UUID_VISTA = "uuidVista";
    private static final String ATTR_TIPO_ESQUEMA = "tipoEsquema";
    private static final String ATTR_COLOR_FONDO = "colorFondo";
    private static final String ATTR_ICONO = "icono";
    private static final String ATTR_ACORDEON = "acordeon";
    private static final String ATTR_TITULO = "titulo";

    @Autowired
    private ApplicationProperties appProperties;


    private LinkedList<JSONObject> listProceduresJson = new LinkedList<JSONObject>();

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
        MeshRestClient client = MeshRestClient.create(appProperties.getCms().getHost(), appProperties.getCms().getPort(), appProperties.getCms().isHttps());
        client.setLogin(System.getenv(appProperties.getCms().getUser()), System.getenv(appProperties.getCms().getPassword()));
        client.login().ignoreElement().blockingAwait();

        return client;
    }

    private JSONObject getNavRoot() throws JSONException, IOException {

        //Consulta el CMS
        NavigationResponse navRoot = getRestClient().navroot(APP_NAME, "/", new NavigationParametersImpl().setMaxDepth(maxDepth)).blockingGet();


        List<NavigationElement> navigationElementList = navRoot.getChildren();
        System.out.println("size : " + navigationElementList.size());

        //Estructura para el Front-End
        JSONObject structureFrontJson = new JSONObject();


        for (NavigationElement navRootElement : navigationElementList) {

            System.out.println("Elemento - Root : " + navRootElement.getNode().getDisplayName() + " - uuid : " + navRootElement.getNode().getUuid());


            if (navRootElement.getNode().getDisplayName().toLowerCase().equals(ATTR_RECURSOS)) {

                buildResources(structureFrontJson, navRootElement.getNode());

            } else if (navRootElement.getNode().getDisplayName().toLowerCase().equals(ATTR_CONTENIDO)) {

                System.out.println("Procedimiento - Root : " + navRootElement.getNode().getDisplayName() + " - uuid : " + navRootElement.getNode().getUuid());
                /*FieldMap map = navRootElement.getNode().getFields();
        		Iterator<String> it = map.keySet().iterator();
        		while (it.hasNext()) {
        			String clave = (String) it.next();
        		
        			System.out.println(clave);
        		}*/

                //Se iteran los procedimientos
                NodeListResponse nodesProcedure = this.getChildNode(navRootElement.getNode().getUuid());

                //Procedimientos
                for (NodeResponse nodeProcedure : nodesProcedure.getData()) {
                    //structureFrontJson = buildProcedure(nodeProcedure, structureFrontJson);
                    buildProcedures(nodeProcedure);

                    JSONArray arrayProceduresJson = new JSONArray(listProceduresJson);
                    structureFrontJson.accumulate(ATTR_PROCEDIMIENTOS, arrayProceduresJson);

                }


            } else {
                // Se verifica tratamiento
            }


        }

        return structureFrontJson;
    }


    private void buildResources(JSONObject structureFrontJson, NodeResponse node) throws JSONException, IOException {
        //Objeto de Recursos
        JSONObject objectRootJson = new JSONObject();

        //Se iteran los Recursos
        NodeListResponse nodesResources = this.getChildNode(node.getUuid());

        //Imagenes y Colores
        for (NodeResponse nodeResource : nodesResources.getData()) {

            System.out.println("***** Recurso : " + nodeResource.getDisplayName() + " - uuid : " + nodeResource.getUuid());
            JSONObject objectResource = new JSONObject();

            NodeListResponse nodesResourceItems = this.getChildNode(nodeResource.getUuid());

            //Se iteran todos los items de los recursos
            for (NodeResponse nodeResourceItem : nodesResourceItems.getData()) {

                System.out.println("************ Item Recurso : " + nodeResourceItem.getDisplayName() + " - uuid : " + nodeResourceItem.getUuid());

                JSONObject objectResourceItem = new JSONObject();

                // Se incorpora el UUID
                objectResourceItem.put(ATTR_UUID, nodeResourceItem.getUuid());

                //Se sacan los Campos de los items de los Recursos
                FieldMap fieldsMap = nodeResourceItem.getFields();
                for (String key : fieldsMap.keySet()) {
                    if (key.equals(ATTR_IMAGEN)) {
                        objectResourceItem.put("uuid", nodeResourceItem.getUuid());
                        MeshBinaryResponse binary = getRestClient().downloadBinaryField(APP_NAME, nodeResourceItem.getUuid(), null, ATTR_IMAGEN, new NodeParametersImpl().setLanguages("en")).blockingGet();
                        byte[] bytes = IOUtils.toByteArray(binary.getStream());
                        Base64.Encoder encoder = Base64.getEncoder();
                        String imagenEncode = encoder.encodeToString(bytes);
                        objectResourceItem.put("base64", "data:" + binary.getContentType() + ";base64," + imagenEncode);

                    } else {
                        //Demas Campos diferentes a Imagen
                        objectResourceItem.put(key, fieldsMap.getStringField(key));
                    }

                }

                objectResource.accumulate(nodeResource.getDisplayName().toLowerCase(), objectResourceItem);

            }

            structureFrontJson.accumulate(node.getDisplayName().toLowerCase(), objectResource);
        }

    }


    private void buildProcedures(NodeResponse nodeProcedure) throws JSONException {

        JSONObject procedureObject = new JSONObject();

        //---Datos Generales
        procedureObject.put(ATTR_UUID, nodeProcedure.getUuid());
        procedureObject.put(ATTR_NOMBRE, nodeProcedure.getDisplayName());

        FieldMap fieldsMap = nodeProcedure.getFields();

        if (fieldsMap.hasField(ATTR_ORDEN_COMP)) {
            procedureObject.put(ATTR_ORDEN_COMP, new JSONArray(getOrderComponents(fieldsMap)));
        }

        //--Componente
        procedureObject.put(ATTR_COMPONENTES, buildComponents(nodeProcedure, getOrderComponents(fieldsMap)));
        listProceduresJson.addFirst(procedureObject);

    }


    private JSONArray buildComponents(NodeResponse nodeProcedure, List<String> arrayOrderComponents) throws JSONException {


        JSONObject[] arrayComponentJson = new JSONObject[arrayOrderComponents.size()];
        //----Componentes

        NodeListResponse nodesComponents = this.getChildNode(nodeProcedure.getUuid());
        for (NodeResponse nodeComponent : nodesComponents.getData()) {
            JSONObject componentObject = new JSONObject();
            Boolean flag = Boolean.TRUE;

            componentObject.put(ATTR_UUID, nodeComponent.getUuid());
            componentObject.put(ATTR_TIPO_ESQUEMA, nodeComponent.getSchema().getName());
            if (nodeComponent.getSchema().getName().equals(ATTR_ITEM_MENU) || nodeComponent.getSchema().getName().equals(ATTR_PROC_COSTOS)) {
                componentObject.put(ATTR_UUID_VISTA, nodeComponent.getUuid());
                //Se crea un Identificador
                //componentObject.put("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
            }

            FieldMap fieldsMap = nodeComponent.getFields();
            for (String key : fieldsMap.keySet()) {
                if (key.equals(ATTR_COLOR_FONDO)) {
                    NodeField nodeColorFondo = fieldsMap.getNodeField(key);
                    JSONObject fondoObject = new JSONObject();
                    fondoObject.put(ATTR_UUID, nodeColorFondo.getUuid());
                    componentObject.put(key, fondoObject);

                } else if (key.equals(ATTR_ICONO)) {
                    NodeField icon = fieldsMap.getNodeField(key);
                    componentObject.put(key, icon.getUuid());

                } else if (key.equals(ATTR_ORDEN_COMP)) {

                    componentObject.put(key, new JSONArray(getOrderComponents(fieldsMap)));

                } else {
                    componentObject.put(key, fieldsMap.getStringField(key));
                    if (fieldsMap.getStringField(key).getString().equals(ATTR_ACORDEON)) {
                        componentObject.put(ATTR_COMPONENTES, buildComponents(nodeComponent, getOrderComponents(fieldsMap)));
                        flag = Boolean.FALSE;
                    }
                }

            }

            arrayComponentJson[arrayOrderComponents.indexOf(nodeComponent.getUuid())] = componentObject;

            if (!nodeComponent.getChildrenInfo().isEmpty() && flag) {
                System.out.println("Tiene Hijos" + nodeComponent.getChildrenInfo().keySet());
                buildProcedures(nodeComponent);

            }

        }


        return new JSONArray(arrayComponentJson);
    }


    private List<String> getOrderComponents(FieldMap fieldsMap) {
        List<String> listComponentOrder = new ArrayList<String>();

        if (fieldsMap.hasField(ATTR_ORDEN_COMP)) {
            NodeFieldList listNode = fieldsMap.getNodeFieldList(ATTR_ORDEN_COMP);
            JSONArray arrayOrder = new JSONArray();
            for (NodeFieldListItem nodeOrder : listNode.getItems()) {
                arrayOrder.put(nodeOrder.getUuid());
                listComponentOrder.add(nodeOrder.getUuid());
            }
        }

        return listComponentOrder;
    }


    private NodeListResponse getChildNode(String uuid) {
        NodeListResponse childNodesList = getRestClient().findNodeChildren(APP_NAME, uuid, new NodeParametersImpl().setLanguages("en")).blockingGet();
        /*for (NodeResponse nodeResponse : childNodesList.getData()) {
			System.out.println(nodeResponse.getUuid());
			FieldMap map = nodeResponse.getFields();
			Iterator<String> it = map.keySet().iterator();
			while (it.hasNext()) {
				String clave = (String) it.next();
			}
			if(nodeResponse.getFields().getStringField(ATTR_TITULO)!=null)
				System.out.println(nodeResponse.getFields().getStringField(ATTR_TITULO).getString());
		}*/
        return childNodesList;

    }


    private NodeListResponse getAllNodes() {
        NodeListResponse nodes = getRestClient().findNodes(APP_NAME, new NodeParametersImpl().setLanguages("en")).blockingGet();
        return nodes;
    }


    public String getAllCmsNodes() {
        NodeListResponse cmsNodes = this.getAllNodes();
        return cmsNodes.toJson();
    }

    public JSONObject getCmsNavRoot() throws JSONException, IOException {
        //NavigationResponse cmsNavigation = this.getNavRoot();
        return this.getNavRoot();
    }

}
