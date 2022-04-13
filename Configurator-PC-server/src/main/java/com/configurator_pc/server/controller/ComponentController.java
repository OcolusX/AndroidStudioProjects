package com.configurator_pc.server.controller;

import com.configurator_pc.server.model.*;
import com.configurator_pc.server.model.Currency;
import com.configurator_pc.server.repository.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ComponentController {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private ComponentRepository componentRepository;
    @Autowired
    private ComponentAttributeRepository componentAttributeRepository;
    @Autowired
    private AttributeTypeRepository attributeTypeRepository;
    @Autowired
    private ComponentOfStoreRepository componentOfStoreRepository;
    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private StoreRepository storeRepository;

    @GetMapping("/components")
    public List<Component> getAll() {
        Iterable<Component> components = componentRepository.findAll();
        List<Component> list = new LinkedList<>();
        components.forEach(list::add);
        return list;
    }

    @GetMapping(value = "/components", params = "id")
    public ResponseEntity<Object> getById(@RequestParam(value = "id") int id) {
        Optional<Component> optionalComponent = componentRepository.findById(id);
        if (optionalComponent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Component component = optionalComponent.get();
        JSONObject jsonComponent = new JSONObject();
        jsonComponent.put("name", component.getName());
        jsonComponent.put("attributes", getAttributeList(component.getId()));
        jsonComponent.put("prices", getPriceList(component.getId()));
        return new ResponseEntity<>(jsonComponent, HttpStatus.OK);
    }

    @GetMapping(value = "/components/{type}", params = {"from_index", "to_index"})
    public ResponseEntity<Object> getAllByTypeId(
            @PathVariable int type,
            @RequestParam(value = "from_index", defaultValue = "0") int fromIndex,
            @RequestParam(value = "to_index", defaultValue = "" + Integer.MAX_VALUE) int toIndex) {
        if (fromIndex < 0 || fromIndex > toIndex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<Component> components = componentRepository.findAllByTypeIdOrderById(type);
        if (fromIndex >= components.size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        components = components.subList(fromIndex, Math.min(toIndex + 1, components.size() - 1));

        JSONArray componentList = new JSONArray();
        for(Component component : components) {
            JSONObject jsonComponent = new JSONObject();
            jsonComponent.put("name", component.getName());
            jsonComponent.put("attributes", getAttributeList(component.getId()));
            jsonComponent.put("prices", getPriceList(component.getId()));
            componentList.add(jsonComponent);
        }
        return new ResponseEntity<>(componentList, HttpStatus.OK);
    }

    @GetMapping(value = "/components/{type}")
    public ResponseEntity<Object> getAllByTypeId(@PathVariable int type) {
        return getAllByTypeId(type, 0, (int) componentRepository.countAllByTypeId(type));
    }

    @GetMapping("components/size")
    public long getAllSize() {
        return componentRepository.count();
    }

    @GetMapping("components/size/{type}")
    public long getAllSizeByTypeId(@PathVariable int type) {
        return componentRepository.countAllByTypeId(type);
    }

    private Map<String, String> getAttributeList(int componentId) {
        List<ComponentAttribute> componentAttributeList = componentAttributeRepository.findAllByComponentId(componentId);
        Map<String, String> attributeList = new HashMap<>();
        for (ComponentAttribute componentAttribute : componentAttributeList) {
            Optional<AttributeType> optionalAttributeType = attributeTypeRepository.findById(componentAttribute.getAttributeId());
            if (optionalAttributeType.isEmpty()) {
                return null;
            }
            attributeList.put(optionalAttributeType.get().getName(), componentAttribute.getValue());
        }
        return attributeList;
    }

    private List<Map<String, String>> getPriceList(int componentId) {
        List<ComponentOfStore> componentOfStoreList = componentOfStoreRepository.findAllByComponentId(componentId);
        List<Map<String, String>> priceList = new LinkedList<>();
        for (ComponentOfStore componentOfStore : componentOfStoreList) {
            Price price = priceRepository.findFirstByComponentOfStoreIdOrderByDate(componentOfStore.getId());
            Currency currency = currencyRepository.findById(price.getCurrencyId()).get();
            Store store = storeRepository.findById(componentOfStore.getStoreId()).get();
            Map<String, String> priceAttributes = new HashMap<>();
            priceAttributes.put("store", store.getName());
            priceAttributes.put("url", componentOfStore.getUrl());
            priceAttributes.put("price", "" + price.getPrice());
            priceAttributes.put("currency", currency.getCurrency());
            priceAttributes.put("date", dateFormat.format(price.getDate()));

            priceList.add(priceAttributes);
        }
        return priceList;
    }
}