package dev.saul.descarbonizacion_transporte.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.saul.descarbonizacion_transporte.modules.type_equipments.seeders.TypeEquipmentSeeder;
import dev.saul.descarbonizacion_transporte.modules.users.seeders.UserSeeder;
import jakarta.annotation.PostConstruct;

@Component
public class AppInitializer {

    @Autowired
    private UserSeeder userSeeder;

    @Autowired
    private TypeEquipmentSeeder typeEquipmentSeeder;

    @PostConstruct
    public void init() {
        userSeeder.seedRoles();
        userSeeder.seedUsers();
        typeEquipmentSeeder.seedTypeEquipments();
    }
}
