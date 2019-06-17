package co.gov.cancilleria.miconsulado.service.cms;

import co.gov.cancilleria.miconsulado.rest.client.cms.RestClientCMS;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import org.springframework.stereotype.Service;

@Service
public class CmsService {

    //Se invoca el cliente desde el Servicio
    RestClientCMS restClient  = new RestClientCMS();


    public String getAllCmsNodes(){
        NodeListResponse cmsNodes = restClient.getAllCmsNodes();
        return cmsNodes.toJson();
    }



}
