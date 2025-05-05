package dev.saul.descarbonizacion_transporte.global.listeners;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import dev.saul.descarbonizacion_transporte.global.entity.GenericEntity;

import java.time.LocalDateTime;
import org.springframework.lang.NonNull;

@Component
public class MongoAuditListener extends AbstractMongoEventListener<Object> {

    @Override
    public void onBeforeConvert(@NonNull BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        if (source instanceof GenericEntity) {
            GenericEntity entity = (GenericEntity) source;

            if (entity.getCreatedAt() == null) {
                entity.setCreatedAt(LocalDateTime.now());
            }
            entity.setUpdatedAt(LocalDateTime.now());
        }
    }
}