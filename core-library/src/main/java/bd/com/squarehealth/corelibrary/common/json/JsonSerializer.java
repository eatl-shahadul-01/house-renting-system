package bd.com.squarehealth.corelibrary.common.json;

import java.util.List;
import java.util.Map;

public interface JsonSerializer {

    /**
     * Serializes an object to JSON string.
     * @param data Data to be serialized.
     * @return Returns JSON representation of the object.
     */
    String serialize(Object data);

    /**
     * Deserializes JSON string to Object.
     * @param json JSON data that needs to be deserialized.
     * @return Returns the deserialized object.
     */
    <Type> Type deserialize(String json, Class<Type> type);

    /**
     * Populates a map from the given object.
     * @param object Object of which the map shall be populated.
     * @return Returns a map containing the attributes of the given object.
     * @param <Type> Type of the object.
     */
    <Type> Map<String, Object> toMap(Type object);

    /**
     * Populates a map from the given object.
     * @param object Object of which the map shall be populated.
     * @param attributesToIgnore List of attributes that shall be ignored.
     * @return Returns a map containing the attributes of the given object.
     * @param <Type> Type of the object.
     */
    <Type> Map<String, Object> toMap(Type object, List<String> attributesToIgnore);

    /**
     * Transforms one object into another.
     * @param object Object that needs to be transformed.
     * @param classType Type of class the object needs to be transformed into.
     * @return Returns the transformed object.
     * @param <TypeA> Type of the object to be transformed.
     * @param <TypeB> Type of the transformed object.
     */
    <TypeA, TypeB> TypeB transform(TypeA object, Class<TypeB> classType);

    /**
     * Transforms one object into another.
     * @param object Object that needs to be transformed.
     * @param classType Type of class the object needs to be transformed into.
     * @return Returns the transformed object.
     * @param <TypeA> Type of the object to be transformed.
     * @param <TypeB> Type of the transformed object.
     */
    <TypeA, TypeB> TypeB transform(TypeA object, Class<TypeB> classType, List<String> attributesToIgnore);

    <Type> Type fromMap(Map<String, Object> map, Class<Type> classType);

    <Type> Type fromMap(Map<String, Object> map, Class<Type> classType, List<String> attributesToIgnore);
}
