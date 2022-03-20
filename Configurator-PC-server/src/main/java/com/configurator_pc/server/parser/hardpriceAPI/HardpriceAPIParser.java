package com.configurator_pc.server.parser.hardpriceAPI;

import com.configurator_pc.server.model.AttributeType;
import com.configurator_pc.server.parser.ComponentParsingTask;
import com.configurator_pc.server.parser.Parser;
import com.configurator_pc.server.parser.ParserThreadPool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HardpriceAPIParser extends Parser {

    private static final String componentTypesJSON = "componentTypes/hardpriceAPI.json";
    private final JSONParser parser;

    public HardpriceAPIParser() {
        super(componentTypesJSON);
        parser = new JSONParser();
    }

    @Override
    protected void parseList(int componentTypeId, String url) {
        try {
            String jsonString = Objects.requireNonNull(ParserThreadPool.connect(url)).ignoreContentType(true).execute().body();
            JSONArray array = (JSONArray) parser.parse(jsonString);
            for (Object object : array) {
                JSONObject jsonObject = (JSONObject) object;
                ComponentParsingTask parsingTask = new HardpriceAPIComponentParsingTask(
                        "https://hardprice.ru" + jsonObject.get("url"),
                        componentTypeId,
                        (String) jsonObject.get("vendor_name")
                );
                ParserThreadPool.parse(parsingTask);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


}
