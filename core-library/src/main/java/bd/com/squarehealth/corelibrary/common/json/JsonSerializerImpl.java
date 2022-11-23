package bd.com.squarehealth.corelibrary.common.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonSerializerImpl implements JsonSerializer {

    private final ObjectMapper objectMapper;
    private static final JsonSerializer JSON_SERIALIZER = new JsonSerializerImpl();

    private JsonSerializerImpl() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public String serialize(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return "{}";
    }

    @Override
    public <Type> Type deserialize(String json, Class<Type> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public <Type> Map<String, Object> toMap(Type object) {
        String jsonContent = serialize(object);
        Map<String, Object> map = deserialize(jsonContent, new HashMap<String, Object>(0).getClass());

        return map;
    }

    @Override
    public <Type> Map<String, Object> toMap(Type object, List<String> attributesToIgnore) {
        Map<String, Object> map = toMap(object);
        map = removeAttributesFromMap(map, attributesToIgnore);

        return map;
    }

    @Override
    public <TypeA, TypeB> TypeB transform(TypeA object, Class<TypeB> classType) {
        return transform(object, classType, null);
    }

    @Override
    public <TypeA, TypeB> TypeB transform(TypeA object, Class<TypeB> classType, List<String> attributesToIgnore) {
        Map<String, Object> map = toMap(object);
        TypeB transformedObject = fromMap(map, classType, attributesToIgnore);

        return transformedObject;
    }

    @Override
    public <Type> Type fromMap(Map<String, Object> map, Class<Type> classType) {
        JsonSerializer jsonSerializer = JsonSerializerImpl.getInstance();
        String jsonContent = jsonSerializer.serialize(map);
        Type object = jsonSerializer.deserialize(jsonContent, classType);

        return object;
    }

    @Override
    public <Type> Type fromMap(Map<String, Object> map, Class<Type> classType, List<String> attributesToIgnore) {
        map = removeAttributesFromMap(map, attributesToIgnore);

        return fromMap(map, classType);
    }

    private static Map<String, Object> removeAttributesFromMap(Map<String, Object> map, List<String> attributesToIgnore) {
        if (attributesToIgnore == null || attributesToIgnore.size() == 0) { return map; }

        for (String attribute : attributesToIgnore) {
            map.remove(attribute);
        }

        return map;
    }

    public static JsonSerializer getInstance() {
        return JSON_SERIALIZER;
    }
}
