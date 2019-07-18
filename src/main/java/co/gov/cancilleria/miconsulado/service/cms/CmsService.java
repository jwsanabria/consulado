package co.gov.cancilleria.miconsulado.service.cms;

import java.io.IOException;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public interface CmsService {
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

	
	String getAllCmsNodes();

    JSONObject getCmsNavRoot(int maxDepth) throws JSONException, IOException;

}
