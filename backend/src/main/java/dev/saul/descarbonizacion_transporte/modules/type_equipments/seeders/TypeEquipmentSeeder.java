package dev.saul.descarbonizacion_transporte.modules.type_equipments.seeders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.DataField;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.TypeEquipmentEntity;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.entity.TypeField;
import dev.saul.descarbonizacion_transporte.modules.type_equipments.repository.TypeEquipmentRepository;

@Service
public class TypeEquipmentSeeder {
    @Autowired
    private TypeEquipmentRepository repository;

    public void seedTypeEquipments() {
        List<TypeEquipmentEntity> typeEquipments = new ArrayList<>();

        // Carro Eléctrico
        TypeEquipmentEntity carroElectrico = new TypeEquipmentEntity();
        carroElectrico.setName("Carro Eléctrico");
        carroElectrico.setDescription("Vehículo eléctrico de cuatro ruedas para transporte personal o comercial.");
        carroElectrico.setDataFields(Arrays.asList(
                DataField.builder()
                        .name("Marca")
                        .description("Marca del vehículo")
                        .typeField(TypeField.TEXTFIELD)
                        .required(true)
                        .minimum(1)
                        .maximum(50)
                        .build(),
                DataField.builder()
                        .name("Modelo")
                        .description("Modelo del vehículo")
                        .typeField(TypeField.TEXTFIELD)
                        .required(true)
                        .minimum(1)
                        .maximum(50)
                        .build(),
                DataField.builder()
                        .name("Año de fabricación")
                        .description("Año en que fue fabricado el vehículo")
                        .typeField(TypeField.NUMBER)
                        .required(true)
                        .minimum(1990)
                        .maximum(LocalDateTime.now().getYear())
                        .build(),
                DataField.builder()
                        .name("Capacidad de batería (kWh)")
                        .description("Capacidad total de la batería en kilovatios hora")
                        .typeField(TypeField.NUMBER)
                        .required(true)
                        .minimum(10)
                        .maximum(200)
                        .build(),
                DataField.builder()
                        .name("Autonomía (km)")
                        .description("Distancia máxima que puede recorrer con una carga completa")
                        .typeField(TypeField.NUMBER)
                        .required(true)
                        .minimum(50)
                        .maximum(600)
                        .build(),
                DataField.builder()
                        .name("Número de plazas")
                        .description("Cantidad de personas que puede transportar")
                        .typeField(TypeField.NUMBER)
                        .required(true)
                        .minimum(1)
                        .maximum(7)
                        .build(),
                DataField.builder()
                        .name("Color")
                        .description("Color del vehículo")
                        .typeField(TypeField.COLOR)
                        .required(false)
                        .build()));
        typeEquipments.add(carroElectrico);

        // Triciclo Eléctrico
        TypeEquipmentEntity tricicloElectrico = new TypeEquipmentEntity();
        tricicloElectrico.setName("Triciclo Eléctrico");
        tricicloElectrico.setDescription("Vehículo eléctrico de tres ruedas para transporte ligero y urbano.");
        tricicloElectrico.setDataFields(Arrays.asList(
                DataField.builder()
                        .name("Marca")
                        .description("Marca del triciclo")
                        .typeField(TypeField.TEXTFIELD)
                        .required(true)
                        .minimum(1)
                        .maximum(50)
                        .build(),
                DataField.builder()
                        .name("Modelo")
                        .description("Modelo del triciclo")
                        .typeField(TypeField.TEXTFIELD)
                        .required(true)
                        .minimum(1)
                        .maximum(50)
                        .build(),
                DataField.builder()
                        .name("Año de fabricación")
                        .description("Año en que fue fabricado el triciclo")
                        .typeField(TypeField.NUMBER)
                        .required(true)
                        .minimum(1990)
                        .maximum(LocalDateTime.now().getYear())
                        .build(),
                DataField.builder()
                        .name("Capacidad de batería (kWh)")
                        .description("Capacidad total de la batería en kilovatios hora")
                        .typeField(TypeField.NUMBER)
                        .required(true)
                        .minimum(5)
                        .maximum(50)
                        .build(),
                DataField.builder()
                        .name("Autonomía (km)")
                        .description("Distancia máxima que puede recorrer con una carga completa")
                        .typeField(TypeField.NUMBER)
                        .required(true)
                        .minimum(30)
                        .maximum(200)
                        .build(),
                DataField.builder()
                        .name("Capacidad de carga (kg)")
                        .description("Peso máximo que puede transportar")
                        .typeField(TypeField.NUMBER)
                        .required(true)
                        .minimum(50)
                        .maximum(300)
                        .build(),
                DataField.builder()
                        .name("Color")
                        .description("Color del vehículo")
                        .typeField(TypeField.COLOR)
                        .required(false)
                        .build()));
        typeEquipments.add(tricicloElectrico);

        // Panel Solar
        TypeEquipmentEntity panelSolar = new TypeEquipmentEntity();
        panelSolar.setName("Panel Solar");
        panelSolar.setDescription("Dispositivo que convierte la luz solar en energía eléctrica.");
        panelSolar.setDataFields(Arrays.asList(
                DataField.builder()
                        .name("Marca")
                        .description("Marca del panel solar")
                        .typeField(TypeField.TEXTFIELD)
                        .required(true)
                        .minimum(1)
                        .maximum(50)
                        .build(),
                DataField.builder()
                        .name("Modelo")
                        .description("Modelo del panel solar")
                        .typeField(TypeField.TEXTFIELD)
                        .required(true)
                        .minimum(1)
                        .maximum(50)
                        .build(),
                DataField.builder()
                        .name("Potencia nominal (W)")
                        .description("Potencia máxima en vatios")
                        .typeField(TypeField.NUMBER)
                        .required(true)
                        .minimum(50)
                        .maximum(500)
                        .build(),
                DataField.builder()
                        .name("Eficiencia (%)")
                        .description("Porcentaje de conversión de energía")
                        .typeField(TypeField.NUMBER)
                        .required(true)
                        .minimum(10)
                        .maximum(25)
                        .build(),
                DataField.builder()
                        .name("Dimensiones (m²)")
                        .description("Área del panel solar en metros cuadrados")
                        .typeField(TypeField.NUMBER)
                        .required(true)
                        .minimum(0)
                        .maximum(5)
                        .build(),
                DataField.builder()
                        .name("Peso (kg)")
                        .description("Peso del panel solar")
                        .typeField(TypeField.NUMBER)
                        .required(false)
                        .minimum(1)
                        .maximum(50)
                        .build(),
                DataField.builder()
                        .name("Fecha de instalación")
                        .description("Fecha en que fue instalado el panel")
                        .typeField(TypeField.DATE)
                        .required(false)
                        .minimumDateTime(LocalDateTime.of(2000, 1, 1, 0, 0))
                        .maximumDateTime(LocalDateTime.now())
                        .build()));
        typeEquipments.add(panelSolar);

        for (TypeEquipmentEntity typeEquipmentEntity : typeEquipments) {
            Optional<TypeEquipmentEntity> tp = repository.findByName(typeEquipmentEntity.getName());
            if (tp.isEmpty()) {
                repository.save(typeEquipmentEntity);
            }
        }
    }
}
