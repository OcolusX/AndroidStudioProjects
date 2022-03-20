package com.configurator_pc.server.parser;

import com.configurator_pc.server.model.ComponentType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Parser {
    private final Map<Integer, String> componentRefs = new HashMap<>();

    protected static final String resourcesPath = "src/main/resources/";

    public Parser(String componentTypesJSON) {
        try (FileReader reader = new FileReader(resourcesPath + componentTypesJSON)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray componentTypes = (JSONArray) jsonObject.get("component_types");

            StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            for (Object _type : componentTypes) {
                JSONObject type = (JSONObject) _type;
                int id = (int) (long) type.get("id");
                String name = (String) type.get("name");
                String href = (String) type.get("href");

                ComponentType componentType = session.get(ComponentType.class, id);
                if (componentType == null) {
                    session.save(new ComponentType(id, name));
                } else if (!componentType.getName().equals(name)) {
                    componentType.setName(name);
                    session.update(componentType);
                }

                componentRefs.put(id, href);
            }

            transaction.commit();
            session.close();

        } catch (ParseException | IOException exception) {
            exception.printStackTrace();
        }
    }

    public void parse() {
//        for (int id : componentRefs.keySet()) {
//            parseList(id, componentRefs.get(id));
//        }
        parseList(2, componentRefs.get(2));
    }

    protected abstract void parseList(int componentTypeId, String url);
}
