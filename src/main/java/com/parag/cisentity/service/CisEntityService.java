package com.parag.cisentity.service;

import com.parag.cisentity.dao.CisEntityDao;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CisEntityService {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CisEntityDao cisEntityDao;

    public Map<String, Object> getData(Map<String, Object> params) {
        var productName = (String) params.get("productName");
        var templateName = (String) params.get("templateName");
        var json = getQuries(productName, templateName);
        var jsonObject = new JSONObject(json);
        var queries = jsonObject.getJSONArray("queries");
        var result = new HashMap<String, Object>();
        queries.iterator().forEachRemaining(q -> {
            var query = new JSONObject(q.toString().replace('"', '\"'));
            log.info("query : " + query);
            try {
                executeQuery(query, params, result);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        return result;
    }

    private void executeQuery(JSONObject query, Map<String, Object> params, Map<String, Object> result) throws IOException {
        var sql = query.getString("query");
        var resultType = query.getString("resultType");
        var map = cisEntityDao.getData(sql, params, resultType);
        result.put(query.getString("returnKey"), map);
        try {
            if(result.get(query.getString("returnKey")) instanceof Map){
                Map<String, Object> returnKey = (Map<String, Object>) result.get(query.getString("returnKey"));
                executeQuery(query.getJSONObject("subQuery"), params, returnKey);
            }else if(result.get(query.getString("returnKey")) instanceof List){
                var returnKeys = (List)result.get(query.getString("returnKey"));
                returnKeys.forEach(returnKey-> {
                    try {
                        executeQuery(query.getJSONObject("subQuery"), params, (Map<String, Object>) returnKey);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (JSONException ex) {
            log.info("do nothing ....");
        }
    }

    private String getQuries(String productName, String templateName) {
        var filePath = "classpath:queries/%s/%s.json".formatted(productName, templateName);
        var resource = resourceLoader.getResource(filePath);
        String json = null;
        try {
            json = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return json;
    }

}
