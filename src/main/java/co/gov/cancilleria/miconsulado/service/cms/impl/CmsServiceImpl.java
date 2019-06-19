package co.gov.cancilleria.miconsulado.service.cms.impl;

import co.gov.cancilleria.miconsulado.service.cms.CmsService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gentics.mesh.core.rest.navigation.NavigationElement;
import com.gentics.mesh.core.rest.navigation.NavigationResponse;
import com.gentics.mesh.core.rest.node.FieldMap;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.core.rest.node.NodeResponse;
import com.gentics.mesh.core.rest.node.field.BinaryField;
import com.gentics.mesh.core.rest.node.field.NodeField;
import com.gentics.mesh.core.rest.node.field.list.impl.StringFieldListImpl;
import com.gentics.mesh.parameter.LinkType;
import com.gentics.mesh.parameter.client.NavigationParametersImpl;
import com.gentics.mesh.parameter.client.NodeParametersImpl;
import com.gentics.mesh.rest.client.MeshBinaryResponse;
import com.gentics.mesh.rest.client.MeshRestClient;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.List;


@Service
@JsonIgnoreProperties(ignoreUnknown = true)
public class CmsServiceImpl implements CmsService {


    private String getNavRoot() throws JSONException, IOException {
        MeshRestClient client = MeshRestClient.create("vps206188.vps.ovh.ca", 8080, false);
        client.setLogin("admin", "admin");
        client.login().ignoreElement().blockingAwait();

        //Consulta el CMS

        NavigationResponse nav = client.navroot("miconsulado", "/", new NavigationParametersImpl().setMaxDepth(20)).blockingGet();

        List<NavigationElement> navigationElementList = nav.getChildren();
        System.out.println("size : " + navigationElementList.size());

        //Estructura para el Front-End
        JSONObject structureFrontJson = new JSONObject();

        //Se itera sobre los hijos de la cadena grande


        for (NavigationElement navElem : navigationElementList) {
            JSONArray arrayJson = new JSONArray();

            if (navElem.getNode().getDisplayName().equalsIgnoreCase("Recursos")) {

                List<NavigationElement> childsResourceList = navElem.getChildren();

                for (NavigationElement resourceChild : childsResourceList) {
                    JSONObject objectJson = new JSONObject();
                    JSONArray resourceArrayJson = new JSONArray();

                    NodeListResponse nodes = client.findNodes("miconsulado", new NodeParametersImpl().setResolveLinks(LinkType.SHORT).setLanguages("en")).blockingGet();

                    for (NodeResponse nodeResponse : nodes.getData())
                        if (nodeResponse.getPath().contains("/Recursos/Imagenes/")) {
                            JSONObject imgObjJson = new JSONObject();
                            FieldMap fieldImageMap = nodeResponse.getFields();
                            Collection<String> mapKeys = fieldImageMap.keySet();
                            for (String key : mapKeys) {
                                if (key.equals("imagen")) {
                                    BinaryField imagen = fieldImageMap.getBinaryField(key);
                                    imgObjJson.put("uuid", nodeResponse.getUuid());
                                    MeshBinaryResponse binary = client.downloadBinaryField("miconsulado", nodeResponse.getUuid(), null, "imagen", new NodeParametersImpl().setLanguages("en")).blockingGet();
                                    byte[] bytes = IOUtils.toByteArray(binary.getStream());
                                    Base64.Encoder encoder = Base64.getEncoder();
                                    String imagenEncode = encoder.encodeToString(bytes);
                                    imgObjJson.put("base64", "data:" + binary.getContentType() + ";base64," + imagenEncode);
                                } else {
                                    imgObjJson.put(key, fieldImageMap.getStringField(key));
                                }
                                resourceArrayJson.put(imgObjJson);
                            }

                        }



                    //Se mapean los atributos de los recursos.
                    //System.out.println(nodes.toJson());



                    objectJson.put(resourceChild.getNode().getDisplayName(), resourceArrayJson);
                    arrayJson.put(objectJson);

                }

            } else if (navElem.getNode().getDisplayName().equalsIgnoreCase("Contenido")) {

                List<NavigationElement> childsProcedureList = navElem.getChildren();
                for (NavigationElement procedureChild : childsProcedureList) {
                    JSONObject objectJson = new JSONObject();
                    //Objeto General Procedimientos

                    objectJson.put("nombre", procedureChild.getNode().getDisplayName());
                    objectJson.put("uuid", procedureChild.getNode().getUuid());

                    //componentes - Fields del Procedimiento
                    JSONArray resourceArrayJson = new JSONArray();

                    JSONObject componenteJson = new JSONObject();
                    FieldMap fieldMap = procedureChild.getNode().getFields();
                    Collection<String> mapKeys = fieldMap.keySet();
                    for (String key : mapKeys) {

                        if (key.equals("icono")) {
                            NodeField icon = fieldMap.getNodeField(key);
                            componenteJson.put(key, icon.getUuid());

                        } else if (key.equals("ordenContenido")) {
                            StringFieldListImpl list = fieldMap.getStringFieldList(key);
                            componenteJson.put(key, list.getItems());

                        } else {
                            componenteJson.put(key, fieldMap.getStringField(key));
                        }

                    }

                    resourceArrayJson.put(componenteJson);
                    objectJson.put("componentes", resourceArrayJson);

                    // -----------------------------------------------------------------


                    arrayJson.put(objectJson);
                }


            }


            //Mapea  los child en la nueva estructura
            structureFrontJson.put(navElem.getNode().getDisplayName(), arrayJson);

            //System.out.println("Nodo : "+navElem.getNode().getDisplayName());
        }

        //System.out.println(structureFrontJson.toString());

        //System.out.println("Estructura Completa : "+nav.toJson());

        return structureFrontJson.toString();
    }


    private NodeListResponse getAllNodes() {
        MeshRestClient client = MeshRestClient.create("vps206188.vps.ovh.ca", 8080, false);
        client.setLogin("admin", "admin");
        client.login().ignoreElement().blockingAwait();


        NodeListResponse nodes = client.findNodes("miconsulado", new NodeParametersImpl().setLanguages("en")).blockingGet();
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
