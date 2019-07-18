package co.gov.cancilleria.miconsulado.service.cms;

import com.gentics.mesh.core.rest.navigation.NavigationResponse;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.rest.client.MeshBinaryResponse;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;


public interface GetMeshService {

    static final String APP_NAME = "miConsulado";
    static final String ATTR_TITLE = "titulo";
    static final String ATTR_IMAGEN = "imagen";
    static final String IMAGE_RESOURCE_CMS_BY_UUID_CACHE = "cmsImageResources";

    NavigationResponse getNavigation();

    MeshBinaryResponse downloadBinaryField(String uuid);

    NodeListResponse findNodes();

    @Cacheable(cacheNames = IMAGE_RESOURCE_CMS_BY_UUID_CACHE)
    String getBase64ImageResource(String uuid)throws IOException;


}
