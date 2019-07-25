package co.gov.cancilleria.miconsulado.service.cms.impl;

import co.gov.cancilleria.miconsulado.service.cms.CmsService;
import co.gov.cancilleria.miconsulado.service.cms.GetMeshService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gentics.mesh.core.rest.navigation.NavigationElement;
import com.gentics.mesh.core.rest.navigation.NavigationResponse;
import com.gentics.mesh.core.rest.node.FieldMap;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.core.rest.node.field.NodeField;
import com.gentics.mesh.core.rest.node.field.NodeFieldListItem;
import com.gentics.mesh.core.rest.node.field.list.NodeFieldList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@Scope("prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsServiceImpl implements CmsService {

    private final static Logger log = LoggerFactory.getLogger(CmsService.class);

    private GetMeshService clientCms;

    private LinkedList<JSONObject> listProceduresJson;

    //Map<String, String> bufferMap = new HashMap<String, String>();

    @Autowired
    public CmsServiceImpl(GetMeshService clientCms) {
        super();
        this.clientCms = clientCms;
    }


    public JSONObject getCmsNavRoot(int maxDepth) throws JSONException, IOException {

        // Consulta el CMS
        NavigationResponse navRoot = clientCms.getNavigation();

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
                            + clientCms.getBase64ImageResource(nodeResourceItem.getUuid()));

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


    private void buildProcedures(NavigationElement nodeProcedure) throws JSONException {

        JSONObject procedureObject = new JSONObject();

        // ---Datos Generales
        procedureObject.put(ATTR_UUID, nodeProcedure.getUuid());
        procedureObject.put(ATTR_NAME, nodeProcedure.getNode().getDisplayName());


        // --Componentes del Procedimiento
        FieldMap fieldsMap = nodeProcedure.getNode().getFields();
        procedureObject.put(ATTR_COMPONENTS, buildComponents(nodeProcedure, getOrderComponents(fieldsMap)));
        listProceduresJson.addFirst(procedureObject);

    }


    private JSONArray buildComponents(NavigationElement nodeProcedure, List<String> arrayOrderComponents)
        throws JSONException {

        JSONObject[] arrayComponentJson = new JSONObject[arrayOrderComponents.size()];

        //-- Se crear al Array de Componentes ItemMenu
        JSONObject[] arrayItemMenuJson = new JSONObject[arrayOrderComponents.size()];


        // ----Componentes
        Boolean findArrayItems = Boolean.FALSE;

        List<NavigationElement> nodesComponentsList = nodeProcedure.getChildren();

        if (nodesComponentsList != null) {
            for (NavigationElement nodeComponent : nodesComponentsList) {
                JSONObject componentObject = new JSONObject();
                Boolean flag = Boolean.TRUE;

                componentObject.put(ATTR_UUID, nodeComponent.getUuid());
                componentObject.put(ATTR_TYPE_SQUEMA, nodeComponent.getNode().getSchema().getName());


                //-- Nodo ProcedimientoCostos
                if (nodeComponent.getNode().getSchema().getName().equals(ATTR_PROC_COSTS)) {
                    componentObject.put(ATTR_UUID_VIEW, nodeComponent.getUuid());
                }

                //--Revision de los campos del nodo

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
                        //componentObject.put(key, new JSONArray(getOrderComponents(fieldsMap)));
                    } else {
                        componentObject.put(key, fieldsMap.getStringField(key));
                        if (fieldsMap.getStringField(key).getString().equals(ATTR_ACCORDION)) {
                            //---- Componentes del acordeon
                            componentObject.put(ATTR_COMPONENTS, buildComponents(nodeComponent, getOrderComponents(fieldsMap)));
                            flag = Boolean.FALSE;
                        }
                    }

                }

                try {
                    //-- Encuentra un Item Menu - Crea un nuevo array ordenado con solo los Items Menu
                    if (nodeComponent.getNode().getSchema().getName().equals(ATTR_ITEM_MENU)) {
                        componentObject.put(ATTR_UUID_VIEW, nodeComponent.getUuid());
                        if (nodeProcedure.getNode().getFields().hasField("tipo")) {
                            if (!nodeProcedure.getNode().getFields().getStringField("tipo").getString().equals(ATTR_ACCORDION)) {
                                if (arrayOrderComponents.indexOf(nodeComponent.getUuid()) >= 0) {
                                    arrayItemMenuJson[arrayOrderComponents.indexOf(nodeComponent.getUuid())] = componentObject;
                                    findArrayItems = Boolean.TRUE;
                                }

                            }
                        }
                    }

                    //-- Se genera Error cuando el orden de los componentes no es diligenciado en el CMS o se referencian nodos que no estan contenidos
                    if (arrayOrderComponents.indexOf(nodeComponent.getUuid()) >= 0) {
                        arrayComponentJson[arrayOrderComponents.indexOf(nodeComponent.getUuid())] = componentObject;
                    }

                } catch (ArrayIndexOutOfBoundsException exception) {

                    //-- Envio de correo con el error
                    log.info("******* Error en Orden Componentes **********");
                    log.info("Uuid - Componente :" + nodeComponent.getUuid());
                    log.info("Nombre - Componente :" + nodeComponent.getNode().getDisplayName());
                    log.info("Orden Componentes :" + arrayOrderComponents);
                    throw exception;
                }


                if (!nodeComponent.getNode().getChildrenInfo().isEmpty() && flag) {
                    buildProcedures(nodeComponent);
                }

            }

        }


        //---- Convierte el array de elementos
        JSONArray arrayComponentConvertJson;

        if (findArrayItems) {
            //-- Genera el Esquema Menu
            arrayComponentConvertJson = createMenuSchema(arrayComponentJson, arrayItemMenuJson);
        } else {
            arrayComponentConvertJson = new JSONArray(arrayComponentJson);
        }


        return arrayComponentConvertJson;
    }


    private JSONArray createMenuSchema(JSONObject[] arrayComponentJson, JSONObject[] arrayItemsMenuJson) throws JSONException {

        //-- Objeto para Esquema Menu
        JSONObject menuObject = new JSONObject();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        menuObject.put(ATTR_UUID, uuid);
        menuObject.put(ATTR_TYPE_SQUEMA, "menu");
        JSONArray menuItemsArray = removeNullsFrom(new JSONArray(arrayItemsMenuJson));
        menuObject.put(ATTR_COMPONENTS, menuItemsArray);

        //--  Cambia los componentes
        int sizeArray = (arrayComponentJson.length - menuItemsArray.length()) + 1;
        JSONObject[] arrayComponentMenuJson = new JSONObject[sizeArray];


        for (int i = 0; i < arrayComponentMenuJson.length; i++) {
            if (i == 1) {
                arrayComponentMenuJson[i] = menuObject;
            } else {
                arrayComponentMenuJson[i] = arrayComponentJson[i];
            }

        }


        return new JSONArray(arrayComponentMenuJson);
    }


    private JSONArray removeNullsFrom(JSONArray array) throws JSONException {
        JSONArray arrayRemove = new JSONArray();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                Object o = array.get(i);
                if (!(o == null || o == JSONObject.NULL)) {
                    arrayRemove.put(array.get(i));
                    // array.remove(i);
                }
            }
        }


        return arrayRemove;
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
        NodeListResponse nodes = clientCms.findNodes();
        return nodes;
    }

    public String getAllCmsNodes() {
        NodeListResponse cmsNodes = this.getAllNodes();
        return cmsNodes.toJson();
    }

}
