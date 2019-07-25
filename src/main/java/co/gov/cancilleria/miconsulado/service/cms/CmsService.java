package co.gov.cancilleria.miconsulado.service.cms;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;


public interface CmsService {
    String ATTR_UUID = "uuid";
    String ATTR_IMAGEN = "imagen";
    String ATTR_CONTENT = "contenido";
    String ATTR_RESOURCES = "recursos";
    String ATTR_PROCEDURES = "procedimientos";
    String ATTR_NAME = "nombre";
    String ATTR_ORDER_COMP = "ordenComponentes";
    String ATTR_COMPONENTS = "componentes";
    String ATTR_PROC_COSTS = "procedimientoCostos";
    String ATTR_ITEM_MENU = "itemMenu";
    String ATTR_UUID_VIEW = "uuidVista";
    String ATTR_TYPE_SQUEMA = "tipoEsquema";
    String ATTR_COLOR_BACKGROUND = "colorFondo";
    String ATTR_ICON = "icono";
    String ATTR_ACCORDION = "acordeon";

	
	String getAllCmsNodes();

    JSONObject getCmsNavRoot(int maxDepth) throws JSONException, IOException;

}
