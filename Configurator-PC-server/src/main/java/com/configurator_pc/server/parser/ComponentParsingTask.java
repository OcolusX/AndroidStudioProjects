package com.configurator_pc.server.parser;

import com.configurator_pc.server.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class ComponentParsingTask implements Runnable {

    // Поля, заполняемы методом parseComponent для последующего занесения их в базу данных методом save
    protected Component component;
    protected final List<AttributeType> attributeTypes = new ArrayList<>();
    protected final List<ComponentAttribute> componentAttributes = new ArrayList<>();
    protected final List<Store> stores = new ArrayList<>();
    protected final List<ComponentOfStore> componentOfStores = new ArrayList<>();
    protected final List<Price> prices = new ArrayList<>();

    // Используется для работы с БД
    private final static SessionFactory sessionFactory;
    static {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(serviceRegistry).getMetadataBuilder().build();
        sessionFactory = metadata.buildSessionFactory();
    }

    public ComponentParsingTask(int componentTypeId) {
        this.component = new Component(componentTypeId, "");
    }

    @Override
    public void run() {
        parseComponent();
        save();
    }

    // Парсит страницу с товаром и заполняет соответствующие данные о товаре
    protected abstract void parseComponent();

    // Сохраняет заполненные в процессе работы метода parseComponent данные о товаре в базу данных
    private void save() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        // Сначала сохраняем в БД компонент, если его ещё нет в БД
        Component component = (Component) session.createQuery("from " + Component.class.getName() + " where type_id = :typeId AND name = :name")
                .setParameter("typeId", this.component.getTypeId())
                .setParameter("name", this.component.getName())
                .uniqueResult();
        int componentId = component == null ? (int) session.save(this.component) : component.getId();

        // Затем сохраняем (или обновляем) в БД списки атрибутов
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
                session.flush();
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
                    session.flush();
                } else {
                    String value = componentAttributes.get(i).getValue();
                    if (!componentAttribute.getValue().equals(value)) {
                        componentAttribute.setValue(value);
                        session.update(componentAttribute);
                        session.flush();
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

                session.flush();
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
                session.flush();
            }
        }

        transaction.commit();
    }

}
