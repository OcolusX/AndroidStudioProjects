package com.example.configurator_pc;

import com.example.configurator_pc.model.Component;
import com.example.configurator_pc.model.ComponentType;
import com.example.configurator_pc.model.Configuration;
import com.example.configurator_pc.model.User;

import java.util.List;

public class ServerConnection {

    // Возвращает список конфигураций (сборок) из БД, принадлежащих конкретному пользователю
    public static List<Configuration> getConfigurationList(User user) {
        // TODO : должен возвращать список конфигураций(сборок) для конкретного пользователя

        return null;
    }

    // Возвращает список компонентов из БД конкретного типа,
    // начиная с номера beginIndex и заканчивая номером endIndex;
    // Например, вызов getComponentList(ComponentType.CPU, 0, 49) вернёт первые 50 процессоров из БД
    public static List<Component> getComponentList(ComponentType type, int beginIndex, int endIndex) {
        // TODO : должен возвращать список компонентов конкретного типа

        return null;
    }
}
