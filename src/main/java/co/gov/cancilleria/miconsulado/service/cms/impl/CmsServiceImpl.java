package co.gov.cancilleria.miconsulado.service.cms.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gentics.mesh.core.rest.navigation.NavigationElement;
import com.gentics.mesh.core.rest.navigation.NavigationResponse;
import com.gentics.mesh.core.rest.node.FieldMap;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.core.rest.node.NodeResponse;
import com.gentics.mesh.core.rest.node.field.BinaryField;
import com.gentics.mesh.core.rest.node.field.NodeField;
import com.gentics.mesh.core.rest.node.field.NodeFieldListItem;
import com.gentics.mesh.core.rest.node.field.list.NodeFieldList;
import com.gentics.mesh.parameter.ParameterProvider;
import com.gentics.mesh.parameter.client.NavigationParametersImpl;
import com.gentics.mesh.parameter.client.NodeParametersImpl;
import com.gentics.mesh.rest.client.MeshBinaryResponse;
import com.gentics.mesh.rest.client.MeshRestClient;

import co.gov.cancilleria.miconsulado.config.ApplicationProperties;
import co.gov.cancilleria.miconsulado.config.CmsProperties;
import co.gov.cancilleria.miconsulado.service.cms.CmsService;


@Service
@JsonIgnoreProperties(ignoreUnknown = true)
@EnableConfigurationProperties(value = ApplicationProperties.class)
public class CmsServiceImpl implements CmsService {
	
	private int maxDepth;
	
	@Autowired
	private ApplicationProperties appProperties;
	

    private JSONArray arrayProceduresJson = new JSONArray();
    
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
        client.setLogin(System.getenv("MICONSULADO_CMS_USER"), System.getenv("MICONSULADO_CMS_PASS"));
        client.login().ignoreElement().blockingAwait();
        
        return client;
    }

    private String getNavRoot() throws JSONException, IOException {
        
    	List<ParameterProvider> paramsList = new ArrayList<ParameterProvider>();
    	
    	
    	if(maxDepth>0)
    		paramsList.add(new NavigationParametersImpl().setMaxDepth(maxDepth));
    	
    	ParameterProvider[] params = new ParameterProvider[paramsList.size()];
    	
    	//Consulta el CMS
        NavigationResponse navRoot = getRestClient().navroot("miConsulado", "/", new NavigationParametersImpl().setMaxDepth(maxDepth)).blockingGet();


        List<NavigationElement> navigationElementList = navRoot.getChildren();
        System.out.println("size : " + navigationElementList.size());

        //Estructura para el Front-End
        JSONObject structureFrontJson = new JSONObject();


        for (NavigationElement navRootElement : navigationElementList) {

            System.out.println("Elemento - Root : " + navRootElement.getNode().getDisplayName() + " - uuid : " + navRootElement.getNode().getUuid());


            if (navRootElement.getNode().getDisplayName().toLowerCase().equals("recursos")) {

                //Objeto de Recursos
                JSONObject objectRootJson = new JSONObject();

                //Se iteran los Recursos
                NodeListResponse nodesResources = this.getChildNode(navRootElement.getNode().getUuid());

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
                        objectResourceItem.put("uuid", nodeResourceItem.getUuid());

                        //Se sacan los Campos de los items de los Recursos
                        FieldMap fieldsMap = nodeResourceItem.getFields();
                        for (String key : fieldsMap.keySet()) {
                            if (key.equals("imagen")) {
                                BinaryField imagen = fieldsMap.getBinaryField(key);
                                objectResourceItem.put("uuid", nodeResourceItem.getUuid());
                                MeshBinaryResponse binary = getRestClient().downloadBinaryField("miConsulado", nodeResourceItem.getUuid(), null, "imagen", new NodeParametersImpl().setLanguages("en")).blockingGet();
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

                    structureFrontJson.accumulate(navRootElement.getNode().getDisplayName().toLowerCase(), objectResource);
                }


            } else if (navRootElement.getNode().getDisplayName().toLowerCase().equals("contenido")) {

                System.out.println("Procedimiento - Root : " + navRootElement.getNode().getDisplayName() + " - uuid : " + navRootElement.getNode().getUuid());

                //Se iteran los procedimientos
                NodeListResponse nodesProcedure = this.getChildNode(navRootElement.getNode().getUuid());

                //Procedimientos
                for (NodeResponse nodeProcedure : nodesProcedure.getData()) {
                    //structureFrontJson = buildProcedure(nodeProcedure, structureFrontJson);
                    buildProcedures(nodeProcedure);
                    structureFrontJson.accumulate("procedimientos", arrayProceduresJson);

                }


            } else {
                // Se verifica tratamiento
            }


        }


        //System.out.println(structureFrontJson.toString());
        //System.out.println("Estructura Completa : "+nav.toJson());

        return structureFrontJson.toString();
    }


    private void buildProcedures(NodeResponse nodeProcedure) throws JSONException {

        JSONObject procedureObject = new JSONObject();

        //---Datos Generales
        procedureObject.put("uuid", nodeProcedure.getUuid());
        procedureObject.put("nombre", nodeProcedure.getDisplayName());

        FieldMap fieldsMap = nodeProcedure.getFields();
        for (String key : fieldsMap.keySet()) {
            if (key.equals("ordenComponentes")) {
                NodeFieldList listNode = fieldsMap.getNodeFieldList(key);

                JSONArray arrayProcedureOrder= new JSONArray();
                for(NodeFieldListItem nodeProcedureOrder : listNode.getItems()){
                    arrayProcedureOrder.put(nodeProcedureOrder.getUuid());
                }

                procedureObject.put(key, arrayProcedureOrder);

            }
        }

        //--Componente
        procedureObject.put("componentes", buildComponents(nodeProcedure));
        arrayProceduresJson.put(procedureObject);

    }


    private JSONArray buildComponents(NodeResponse nodeProcedure) throws JSONException {


        JSONArray arrayComponentJson = new JSONArray();
        //----Componentes

        JSONObject components = new JSONObject();
        NodeListResponse nodesComponents = this.getChildNode(nodeProcedure.getUuid());
        for (NodeResponse nodeComponent : nodesComponents.getData()) {
            JSONObject componentObject = new JSONObject();
            Boolean flag = Boolean.TRUE;

            componentObject.put("uuid", nodeComponent.getUuid());
            componentObject.put("tipoEsquema", nodeComponent.getSchema().getName());
            if(nodeComponent.getSchema().getName().equals("itemMenu") || nodeComponent.getSchema().getName().equals("procedimientoCostos") ){
                componentObject.put("uuidVista", nodeComponent.getUuid());
                //Se crea un Identificador
                //componentObject.put("uuid", UUID.randomUUID().toString().replaceAll("-", ""));
            }

            FieldMap fieldsMap = nodeComponent.getFields();
            for (String key : fieldsMap.keySet()) {
                if (key.equals("colorFondo")) {
                    NodeField nodeColorFondo = fieldsMap.getNodeField(key);
                    JSONObject fondoObject = new JSONObject();
                    fondoObject.put("uuid", nodeColorFondo.getUuid());
                    componentObject.put(key, fondoObject);

                } else if (key.equals("icono")) {
                    NodeField icon = fieldsMap.getNodeField(key);
                    componentObject.put(key, icon.getUuid());

                } else if (key.equals("ordenComponentes")) {
                    NodeFieldList listNode = fieldsMap.getNodeFieldList(key);

                    JSONArray arrayOrder= new JSONArray();
                    for(NodeFieldListItem nodeOrder : listNode.getItems()){
                        arrayOrder.put(nodeOrder.getUuid());
                    }

                    componentObject.put(key, arrayOrder);

                } else {
                    componentObject.put(key, fieldsMap.getStringField(key));
                    if(fieldsMap.getStringField(key).getString().equals("acordeon")){
                        componentObject.put("componentes",buildComponents(nodeComponent)) ;
                        flag = Boolean.FALSE;
                    }
                }





            }

            arrayComponentJson.put(componentObject);

            if (!nodeComponent.getChildrenInfo().isEmpty() && flag) {
                System.out.println("Tiene Hijos" + nodeComponent.getChildrenInfo().keySet());
                buildProcedures(nodeComponent);

            }

        }


        return arrayComponentJson;
    }



    private NodeListResponse getChildNode(String uuid) {
        NodeListResponse childNodesList = getRestClient().findNodeChildren("miConsulado", uuid, new NodeParametersImpl().setLanguages("en")).blockingGet();
        return childNodesList;

    }



    private NodeListResponse getAllNodes() {
        NodeListResponse nodes = getRestClient().findNodes("miConsulado", new NodeParametersImpl().setLanguages("en")).blockingGet();
		/*for (NodeResponse nodeResponse : nodes.getData()) {
			System.out.println(nodeResponse.getUuid());
			System.out.println(nodeResponse.getFields().getStringField("name").getString());
		}*/

        return nodes;
    }


    public String getAllCmsNodes() {
        NodeListResponse cmsNodes = this.getAllNodes();

        return cmsNodes.toJson();
    }

    public String getCmsNavRoot() throws JSONException, IOException {
        //NavigationResponse cmsNavigation = this.getNavRoot();
        return this.getNavRoot();
    }

}
