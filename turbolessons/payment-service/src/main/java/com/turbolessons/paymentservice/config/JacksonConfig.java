package com.turbolessons.paymentservice.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.stripe.model.HasId;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        
        // Configure Jackson features
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        // Register modules
        objectMapper.registerModule(new JavaTimeModule());
        
        // Register Stripe module
        objectMapper.registerModule(stripeModule());
        
        return objectMapper;
    }
    
    /**
     * Creates a Jackson module that handles serialization of Stripe objects
     */
    @Bean
    public SimpleModule stripeModule() {
        SimpleModule module = new SimpleModule("StripeModule");
        
        // Add serializer for all StripeObject classes
        module.addSerializer(StripeObject.class, new StripeObjectSerializer());
        
        return module;
    }
    
    /**
     * Generic serializer for Stripe objects that handles circular references
     * and extracts only the necessary fields
     */
    public static class StripeObjectSerializer extends JsonSerializer<StripeObject> {
        
        @Override
        public void serialize(StripeObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeNull();
                return;
            }
            
            // Start object
            gen.writeStartObject();
            
            // Always include id and object type if available
            if (value instanceof HasId) {
                gen.writeStringField("id", ((HasId) value).getId());
            }
            
            try {
                Method getObjectMethod = value.getClass().getMethod("getObject");
                Object object = getObjectMethod.invoke(value);
                if (object != null) {
                    gen.writeStringField("object", object.toString());
                }
            } catch (Exception e) {
                // Ignore if method doesn't exist
            }
            
            // Use reflection to get all getter methods and write their values
            // This avoids circular references by only including primitive values
            for (Method method : value.getClass().getMethods()) {
                String methodName = method.getName();
                
                // Only process getter methods with no parameters
                if (methodName.startsWith("get") && 
                    !methodName.equals("getClass") && 
                    !methodName.equals("getRawJsonObject") &&
                    !methodName.equals("getResponseGetter") &&
                    method.getParameterCount() == 0) {
                    
                    try {
                        String fieldName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
                        Object fieldValue = method.invoke(value);
                        
                        // Skip null values and circular references
                        if (fieldValue != null && !(fieldValue instanceof ApiResource)) {
                            // For collections, we need special handling
                            if (fieldValue instanceof Map) {
                                gen.writeObjectField(fieldName, fieldValue);
                            } else if (fieldValue instanceof Iterable) {
                                gen.writeFieldName(fieldName);
                                gen.writeStartArray();
                                for (Object item : (Iterable<?>) fieldValue) {
                                    if (item instanceof StripeObject) {
                                        // Recursively serialize nested Stripe objects
                                        serialize((StripeObject) item, gen, serializers);
                                    } else {
                                        gen.writeObject(item);
                                    }
                                }
                                gen.writeEndArray();
                            } else {
                                gen.writeObjectField(fieldName, fieldValue);
                            }
                        }
                    } catch (Exception e) {
                        // Skip fields that can't be serialized
                    }
                }
            }
            
            // End object
            gen.writeEndObject();
        }
    }
}
