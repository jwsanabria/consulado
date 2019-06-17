package co.gov.cancilleria.miconsulado.web.rest.cms.api;

import co.gov.cancilleria.miconsulado.service.cms.CmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cmsapi")
public class CmsApi {


    @Autowired
    CmsService cmsService;

    /**
     * Metodo que devuelve la cadena completa de nodos del CMS
     * @return
     */

    @GetMapping("/nodes")
    public String getAllCmsNodes(){
        return cmsService.getAllCmsNodes();
    }

}

