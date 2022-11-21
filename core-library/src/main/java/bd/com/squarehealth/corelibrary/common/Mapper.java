package bd.com.squarehealth.corelibrary.common;

import bd.com.squarehealth.corelibrary.common.json.JsonSerializerImpl;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Mapper {

    default String toJson() {
        JsonSerializer jsonSerializer = JsonSerializerImpl.getInstance();
        String jsonContent = jsonSerializer.serialize(this);

        return jsonContent;
    }

    default Map<String, Object> removeAttributesFromMap(Map<String, Object> map, @Nullable List<String> attributesToIgnore) {
        if (attributesToIgnore == null || attributesToIgnore.size() == 0) { return map; }

        for (String attribute : attributesToIgnore) {
            map.remove(attribute);
        }

        return map;
    }

    default Map<String, Object> toMap() {
        JsonSerializer jsonSerializer = JsonSerializerImpl.getInstance();
        String jsonContent = jsonSerializer.serialize(this);
        Map<String, Object> map = jsonSerializer.deserialize(jsonContent, new HashMap<String, Object>(0).getClass());

        return map;
    }

    default Map<String, Object> toMap(@Nullable List<String> attributesToIgnore) {
        Map<String, Object> map = removeAttributesFromMap(toMap(), attributesToIgnore);

        return map;
    }

    default <Type> Type toObject(Class<Type> classType) {
        return toObject(classType, null);
    }

    default <Type> Type toObject(Class<Type> classType, @Nullable List<String> attributesToIgnore) {
        Map<String, Object> map = toMap();
        Type object = fromMap(map, classType, attributesToIgnore);

        return object;
    }

    default Mapper fromObject(Object object) {
        return fromObject(object, null);
    }

    default Mapper fromObject(Object object, @Nullable List<String> attributesToIgnore) {
        JsonSerializer jsonSerializer = JsonSerializerImpl.getInstance();
        String jsonContent = jsonSerializer.serialize(object);

        // if there are attributes to ignore, we'll deserialize to a Map and remove the attributes...
        if (attributesToIgnore != null && attributesToIgnore.size() > 0) {
            Map<String, Object> map = jsonSerializer.deserialize(jsonContent, new HashMap<String, Object>(0).getClass());
            map = removeAttributesFromMap(map, attributesToIgnore);
            jsonContent = jsonSerializer.serialize(map);
        }

        Mapper mapper = jsonSerializer.deserialize(jsonContent, this.getClass());

        return mapper;
    }

    default <Type> Type fromMap(Map<String, Object> map, Class<Type> classType) {
        JsonSerializer jsonSerializer = JsonSerializerImpl.getInstance();
        String jsonContent = jsonSerializer.serialize(map);
        Type object = jsonSerializer.deserialize(jsonContent, classType);

        return object;
    }

    default <Type> Type fromMap(Map<String, Object> map, Class<Type> classType, @Nullable List<String> attributesToIgnore) {
        map = removeAttributesFromMap(map, attributesToIgnore);

        return fromMap(map, classType);
    }
}
