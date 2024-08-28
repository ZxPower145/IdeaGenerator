package org.nexus.ideagenerator.core.controllers;


import org.nexus.ideagenerator.core.models.Api;
import org.nexus.ideagenerator.core.repository.ApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/api")
@CrossOrigin("http://127.0.0.1:8080")
public class ApiController<T> {
    private final ApiRepository apiRepository;

    @Autowired
    public ApiController(ApiRepository apiRepository) {
        this.apiRepository = apiRepository;
    }

    @GetMapping(value = "/get/all")
    public ResponseEntity<List<Api>> getAllApis(){
        return ResponseEntity.ok(apiRepository.findAll());
    }

    @DeleteMapping(value = "/delete/{id}")
    public HttpStatus delete(@PathVariable long id) {
        if (apiRepository.existsById(id)) {
            apiRepository.deleteById(id);
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    @PostMapping(value = "/add")
    public HttpStatus add(@RequestBody T apiEntity) {
        if (apiEntity instanceof List<?>) {
            for (int i = 0; i < ((List<?>) apiEntity).size(); i++) {
                apiRepository.save((Api) ((List<?>) apiEntity).get(i));
            }
        } else if (apiEntity instanceof Api) {
            apiRepository.save((Api) apiEntity);
        } else {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.CREATED;
    }

    @GetMapping(value = "/get/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> allCategories = new ArrayList<>();
        for (Api api : apiRepository.findAll()) {
            if (!allCategories.contains(api.getCategory())) {
                allCategories.add(api.getCategory());
            };
        }
        return ResponseEntity.ok(allCategories);
    }
}
