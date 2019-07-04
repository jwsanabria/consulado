package co.gov.cancilleria.miconsulado.web.rest.cms.api;

import co.gov.cancilleria.miconsulado.service.cms.CmsService;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/cmsapi")
public class CmsResource {


    private final CmsService cmsService;

    CmsResource(CmsService cmsService) {
        this.cmsService = cmsService;
    }

    /**
     * Metodo que devuelve la cadena completa de nodos del CMS
     *
     * @return
     */
    @GetMapping(value = "/nodes", produces = "application/json")
    public String getAllCmsNodes() {
        return cmsService.getAllCmsNodes();
    }


    /**
     * Metodo que devuelve la navegacion desde miconsulado
     *
     * @return
     */
    @GetMapping(value = "/navigation", produces = "application/json")
    public String getNavRootCms() throws JSONException, IOException {
        cmsService.setMaxDepth(20);
        return cmsService.getCmsNavRoot().toString();
    }


}

