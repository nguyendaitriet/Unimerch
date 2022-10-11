package com.unimerch.service.impl;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ConfigurationServiceImpl implements InitializingBean {

    @Value("${root.dir}")
    private String dirPath;
    @Value("${uni.backend.configFileName}")
    private String backendFileName;
    @Value("${uni.app.configFileName}")
    private String appFileName;

    private String backendPricePattern;

    private String appsConfigString;

    @Override
    public void afterPropertiesSet() throws Exception {
        try (FileReader reader =
                     new FileReader(String.format("%s%s", dirPath, backendFileName))) {
            JSONParser jsonParser = new JSONParser();
            JSONObject regexObject = (JSONObject) jsonParser.parse(reader);
            JSONObject pricePatternObject = (JSONObject) regexObject.get("regex");
            backendPricePattern = (String) pricePatternObject.get("pricePattern");
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        appsConfigString = new String(Files.readAllBytes(Paths.get(dirPath, appFileName)));
    }

    public boolean updateAppsConfig(String newAppsConfigString) {
        return updateConfigString(newAppsConfigString, appFileName);
    }

    public boolean updateBackendConfig(String newBackendConfigString) {
        return updateConfigString(newBackendConfigString, backendFileName);
    }

    private boolean updateConfigString(String newConfigString, String backendFileName) {
        try( FileWriter file = new FileWriter(String.format("%s%s", dirPath, backendFileName))) {
            file.write(newConfigString);
            file.flush();
            appsConfigString = new String(Files.readAllBytes(Paths.get(dirPath, backendFileName)));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String getAppsConfigString() {
        return appsConfigString;
    }

    public String getBackendPricePattern() {
        return backendPricePattern;
    }
}
