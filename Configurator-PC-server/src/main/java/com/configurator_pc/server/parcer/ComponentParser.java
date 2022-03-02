package com.configurator_pc.server.parcer;

import com.configurator_pc.server.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ComponentParser implements Runnable {

    private final String url;
    private final int componentId;
    private final int componentTypeId;

    private Component component;
    private final List<AttributeType> attributeTypes = new ArrayList<>();
    private final List<ComponentAttribute> componentAttributes = new ArrayList<>();
    private final List<Store> stores = new ArrayList<>();
    private final List<ComponentOfStore> componentOfStores = new ArrayList<>();
    private final List<Price> prices = new ArrayList<>();

    private final static StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();

    public ComponentParser(String url, int componentId, int componentTypeId) {
        this.url = url;
        this.componentId = componentId;
        this.componentTypeId = componentTypeId;
    }

    @Override
    public void run() {
        parseComponent();
        save();
    }

    private void parseComponent() {
        try {
            Document document = connect(url);
            if (document == null) {
                throw new NullPointerException();
            }

            String name = document.select("#top-page-title > h1").text();
            this.component = new Component(componentId, componentTypeId, name);

            Elements elements = document.select("#item_bl_" + componentId + " > div:nth-child(2) > div:nth-child(2) > div > div");
            for (Element element : elements) {
                String[] attr = element.text().split(":");
                attributeTypes.add(new AttributeType(attr[0]));
                componentAttributes.add(new ComponentAttribute(attr[1]));
            }

            elements = document.select("#item_sm_wb_" + componentId + " > table > tbody > tr");
            for (Element element : elements) {
                for (Element el : element.children()) {
                    String className = el.className();
                    if (className.contains("model-shop-name")) {
                        Element e = el.select("> div > a").get(0);
                        String ref = e.attr("onmouseover").split("\"")[1];

                        Document refDocument = connect(ref);
                        if (refDocument == null) {
                            throw new NullPointerException();
                        }

                        ref = refDocument.select("div > a").attr("href");
                        if (ref.equals("") || ref.equals("#") || ref.equals("/")) {
                            ref = refDocument.location();
                        }

                        stores.add(new Store(e.select("> u").text()));
                        componentOfStores.add(new ComponentOfStore(ref));

                        el = el.nextElementSibling();
                        String stringPrice = el.select("> a").text();
                        stringPrice = stringPrice.substring(0, stringPrice.length() - 2).replaceAll("\\s", "");
                        prices.add(new Price(Float.parseFloat(stringPrice), 1, new Date(System.currentTimeMillis())));
                    }
                }

            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    private void save() {
        Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Component component = session.get(Component.class, componentId);
        if (component == null) {
            session.persist(this.component);
        } else if (!component.equals(this.component)) {
            session.update(component);
        }

        for (int i = 0; i < attributeTypes.size(); i++) {
            AttributeType attributeType = attributeTypes.get(i);
            AttributeType dbAttributeType = (AttributeType) session.createQuery("from " + AttributeType.class.getName() + " where name = :attributeName")
                    .setParameter("attributeName", attributeType.getName())
                    .uniqueResult();

            if (dbAttributeType == null) {
                ComponentAttribute componentAttribute = componentAttributes.get(i);
                componentAttribute.setComponentId(componentId);
                componentAttribute.setAttributeId((int) session.save(attributeType));
                session.persist(componentAttribute);
            } else {
                ComponentAttribute componentAttribute = (ComponentAttribute) session.createQuery("from " + ComponentAttribute.class.getName() + " where component_id = :componentId and attribute_id = :attributeId")
                        .setParameter("componentId", componentId)
                        .setParameter("attributeId", dbAttributeType.getId())
                        .uniqueResult();
                if (componentAttribute == null) {
                    componentAttribute = componentAttributes.get(i);
                    componentAttribute.setComponentId(componentId);
                    componentAttribute.setAttributeId(dbAttributeType.getId());
                    session.persist(componentAttribute);
                } else {
                    String value = componentAttributes.get(i).getValue();
                    if (!componentAttribute.getValue().equals(value)) {
                        componentAttribute.setValue(value);
                        session.update(componentAttribute);
                    }
                }
            }
        }

        for (int i = 0; i < stores.size(); i++) {
            Store store = stores.get(i);
            Store dbStore = (Store) session.createQuery("from " + Store.class.getName() + " where name = :storeName")
                    .setParameter("storeName", store.getName())
                    .uniqueResult();
            if (dbStore == null) {
                ComponentOfStore componentOfStore = componentOfStores.get(i);
                componentOfStore.setComponentId(componentId);
                componentOfStore.setStoreId((int) session.save(store));

                Price price = prices.get(i);
                price.setComponentOfStoreId((int) session.save(componentOfStore));
                session.persist(price);
            } else {
                ComponentOfStore componentOfStore = (ComponentOfStore) session.createQuery("from " + ComponentOfStore.class.getName() + " where component_id = :componentId and store_id = :storeId")
                        .setParameter("componentId", componentId)
                        .setParameter("storeId", dbStore.getId())
                        .uniqueResult();
                if (componentOfStore == null) {
                    componentOfStore = componentOfStores.get(i);
                    componentOfStore.setComponentId(componentId);
                    componentOfStore.setStoreId(dbStore.getId());
                    session.persist(componentOfStore);
                } else {
                    String url = componentOfStores.get(i).getUrl();
                    if (!componentOfStore.getUrl().equals(url)) {
                        componentOfStore.setUrl(url);
                        session.update(componentOfStore);
                    }
                }
                Price price = prices.get(i);
                price.setComponentOfStoreId(componentOfStore.getId());
                session.persist(price);
            }
        }

        transaction.commit();
    }

    private Document connect(String url) {
        try {
            Thread.sleep(150);
            return Jsoup.connect(url)
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("https://www.google.com")
                    .get();
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
