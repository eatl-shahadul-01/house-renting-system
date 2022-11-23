package bd.com.squarehealth.corelibrary.common;

import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializerImpl;

import java.util.List;
import java.util.Map;

public interface Mapper {

    default String toJson() {
        JsonSerializer jsonSerializer = JsonSerializerImpl.getInstance();
        String jsonContent = jsonSerializer.serialize(this);

        return jsonContent;
    }

    default Map<String, Object> toMap() {
        return toMap(null);
    }

    default Map<String, Object> toMap(List<String> attributesToIgnore) {
        JsonSerializer jsonSerializer = JsonSerializerImpl.getInstance();

        return jsonSerializer.toMap(this, attributesToIgnore);
    }

    default <Type> Type toObject(Class<Type> classType) {
        return toObject(classType, null);
    }

    default <Type> Type toObject(Class<Type> classType, List<String> attributesToIgnore) {
        JsonSerializer jsonSerializer = JsonSerializerImpl.getInstance();

        return jsonSerializer.transform(this, classType, attributesToIgnore);
    }

    default Mapper fromObject(Object object) {
        return fromObject(object, null);
    }

    default Mapper fromObject(Object object, List<String> attributesToIgnore) {
        JsonSerializer jsonSerializer = JsonSerializerImpl.getInstance();

        return jsonSerializer.transform(object, this.getClass(), attributesToIgnore);
    }

    default <Type> Type fromMap(Map<String, Object> map, Class<Type> classType) {
        return fromMap(map, classType);
    }

    default <Type> Type fromMap(Map<String, Object> map, Class<Type> classType, List<String> attributesToIgnore) {
        JsonSerializer jsonSerializer = JsonSerializerImpl.getInstance();

        return jsonSerializer.fromMap(map, classType, attributesToIgnore);
    }
}
