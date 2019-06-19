package co.gov.cancilleria.miconsulado.web.rest.cms.api;

import co.gov.cancilleria.miconsulado.service.cms.CmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/cmsapi")
public class CmsResource {


    
    private final CmsService cmsService;
    
    CmsResource(CmsService cmsService){
    	this.cmsService = cmsService;
    }

    /**
     * Metodo que devuelve la cadena completa de nodos del CMS
     * @return
     */
    @GetMapping("/nodes")
    public String getAllCmsNodes(){
        return cmsService.getAllCmsNodes();
    }
    
    
    /**
     * Metodo que devuelve la navegacion desde miconsulado
     * @return
     */
    @GetMapping("/navigation")
    public String getNavRootCms() throws JSONException, IOException {
        return cmsService.getCmsNavRoot();
    }


}

