package com.configurator_pc.server.controller;

import com.configurator_pc.server.controller.request_body.ConfigurationRequestBody;
import com.configurator_pc.server.model.*;
import com.configurator_pc.server.model.Currency;
import com.configurator_pc.server.repository.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ComponentAndConfigurationController {

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
    @Autowired
    private ConfigurationRepository configurationRepository;
    @Autowired
    private ComponentOfConfigurationRepository componentOfConfigurationRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/components", params = "id")
    public ResponseEntity<Object> getById(@RequestParam(value = "id") int id) {
        Optional<Component> optionalComponent = componentRepository.findById(id);
        if (optionalComponent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Component component = optionalComponent.get();
        JSONObject jsonComponent = new JSONObject();
        jsonComponent.put("id", component.getId());
        jsonComponent.put("name", component.getName());
        jsonComponent.put("description", component.getDescription());
        jsonComponent.put("type_id", component.getTypeId());
        jsonComponent.put("image", component.getImage());
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
            return new ResponseEntity<>(new ArrayList<Component>(), HttpStatus.OK);
        }
        components = components.subList(fromIndex, Math.min(toIndex + 1, components.size() - 1));

        JSONArray componentList = new JSONArray();
        for (Component component : components) {
            JSONObject jsonComponent = new JSONObject();
            jsonComponent.put("id", component.getId());
            jsonComponent.put("name", component.getName());
            jsonComponent.put("description", component.getDescription());
            jsonComponent.put("type_id", type);
            jsonComponent.put("image", component.getImage());
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

    @GetMapping("/components/size")
    public long getAllSize() {
        return componentRepository.count();
    }

    @GetMapping("/components/size/{type}")
    public long getAllSizeByTypeId(@PathVariable int type) {
        return componentRepository.countAllByTypeId(type);
    }


    @GetMapping("/configurations/{id}")
    public ResponseEntity<Object> getAllByCreatorId(@PathVariable int id) {
        List<Configuration> configurations = configurationRepository.findAllByCreatorId(id);
        JSONArray configurationList = new JSONArray();
        for (Configuration configuration : configurations) {
            JSONObject jsonConfiguration = new JSONObject();
            jsonConfiguration.put("id", configuration.getId());
            jsonConfiguration.put("name", configuration.getName());
            jsonConfiguration.put("creator", userRepository.findById(configuration.getCreatorId()).get());
            List<ComponentOfConfiguration> componentOfConfigurations =
                    componentOfConfigurationRepository.findAllByConfigurationId(configuration.getId());

            JSONArray componentList = new JSONArray();
            for (ComponentOfConfiguration componentOfConfiguration : componentOfConfigurations) {
                ResponseEntity<Object> responseComponent = getById(componentOfConfiguration.getComponentId());
                if (responseComponent.getStatusCode() == HttpStatus.OK) {
                    componentList.add(responseComponent.getBody());
                }
            }
            jsonConfiguration.put("componentList", componentList);
            configurationList.add(jsonConfiguration);
        }
        return new ResponseEntity<>(configurationList, HttpStatus.OK);
    }


    @PostMapping(value = "/configurations", consumes = {"application/json"})
    public ResponseEntity<Object> saveConfiguration(@RequestBody ConfigurationRequestBody requestBody) {
        int creatorId = requestBody.getCreatorId();
        if(userRepository.findById(creatorId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        int id = requestBody.getId();
        Optional<Configuration> byId = configurationRepository.findById(id);
        Configuration configuration;
        if (byId.isPresent()) {
            configuration = byId.get();
            configuration.setName(requestBody.getName());
        } else {
            configuration = new Configuration(requestBody.getName(), creatorId);
        }
        configuration = configurationRepository.save(configuration);
        id = configuration.getId();

        List<ComponentOfConfiguration> componentOfConfigurationList = new LinkedList<>();
        for (Integer componentId : requestBody.getComponentIdList()) {
            if (componentRepository.findById(componentId).isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            componentOfConfigurationList.add(new ComponentOfConfiguration(componentId, id));
        }
        componentOfConfigurationRepository.deleteAll(
                componentOfConfigurationRepository.findAllByConfigurationId(id)
        );
        componentOfConfigurationRepository.saveAll(componentOfConfigurationList);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/configurations/{id}")
    public ResponseEntity<Object> deleteConfigurationById(@PathVariable int id) {
        Optional<Configuration> byId = configurationRepository.findById(id);
        if(byId.isPresent()) {
            componentOfConfigurationRepository.deleteAll(componentOfConfigurationRepository.findAllByConfigurationId(id));
            configurationRepository.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
            if(price == null) {
                continue;
            }

            Optional<Currency> byIdCurrency = currencyRepository.findById(price.getCurrencyId());
            Currency currency = byIdCurrency.isPresent() ? byIdCurrency.get() : new Currency("RUB");

            Optional<Store> byIdStore = storeRepository.findById(componentOfStore.getStoreId());
            Store store = byIdStore.isPresent() ? byIdStore.get() : new Store("StoreName");

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