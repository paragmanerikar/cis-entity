package com.parag.cisentity.controller;

import com.parag.cisentity.service.CisEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cis-entity")
public class CisController {

    @Autowired
    private CisEntityService cisEntityService;
    @PostMapping("/")
    public Map<String, Object> getData(@RequestBody Map<String,Object> params){
        return cisEntityService.getData(params);
    }
}
