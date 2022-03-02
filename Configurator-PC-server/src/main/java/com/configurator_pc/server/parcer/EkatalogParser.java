package com.configurator_pc.server.parcer;


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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class EkatalogParser {

    private final Map<Integer, String> componentsRefs = new HashMap<>();

    public EkatalogParser() {
        try (FileReader reader = new FileReader("src/main/resources/componentType.json")) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray componentTypes = (JSONArray) jsonObject.get("componentTypes");

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

                componentsRefs.put(id, href);
            }

            transaction.commit();
            session.close();

        } catch (ParseException | IOException exception) {
            exception.printStackTrace();
        }
    }

    public void parse() {
        for (int id : componentsRefs.keySet()) {
            parseList(id, componentsRefs.get(id));
        }
    }

    private void parseList(int componentTypeId, String url) {
        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("https://www.google.com")
                    .get();

            Elements elements = document.select("body > div.common-table-div.s-width > table > tbody > tr > td.main-part-content")
                    .get(0).children().get(3).select("> div");

            ExecutorService executorService = Executors.newWorkStealingPool();
            for (Element element : elements) {
                Elements e = element.select("> div > table > tbody > tr > td.model-short-info > table > tbody > tr > td:nth-child(1) > a");
                if (e.size() == 0) {
                    continue;
                }
                Element component = e.get(0);
                String href = "https://www.e-katalog.ru" + component.attr("href");
                int id = Integer.parseInt(component.attr("data-idgood"));

                ComponentParser componentParser = new ComponentParser(href, id, componentTypeId);
                executorService.execute(componentParser);
            }
            elements = document.select("#pager_next");
            if (elements.size() != 0) {
                executorService.shutdown();
                if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
                parseList(componentTypeId, "https://www.e-katalog.ru" + elements.attr("href"));
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
