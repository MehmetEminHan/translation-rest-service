package com.neuroval.translationApi.rest.json;

import com.neuroval.translationApi.model.response.Response;
import com.neuroval.translationApi.services.json.JsonOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("translation/validation/json")
public class JsonController {

    @Autowired
    private JsonOperations jsonOperations;

    private Response response;

    @PostMapping("/upload")
    public Response uploadJson(@RequestParam("file") MultipartFile file) throws Exception {
        response = new Response();
        response.setMessage("Json file is serialized!");
        response.setStatus("Successful");
        response.setData(jsonOperations.loadJsonAsMap(file));

        return response;
    }

    @PostMapping("/returnTargetLanguage")
    public Response returnTargetLanguage() throws Exception {
        response = new Response();
        response.setMessage("Json file is serialized!");
        response.setStatus("Successful");
        response.setData(jsonOperations.getJson().getTargetLanguage());

        return response;
    }
}
