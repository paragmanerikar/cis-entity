package com.parag.cisentity.dao;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class CisEntityDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    public Object getData(String sql, Map<String, Object> params, String resultType) throws IOException {
        Object result=null;
        switch (resultType){
            case "Object":
                result =  jdbcTemplate.queryForMap(sql,params);
                break;
            case "List":
                result =  jdbcTemplate.queryForList(sql,params);
                break;
        }
        return result;
    }


}
