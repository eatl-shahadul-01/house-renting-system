package bd.com.squarehealth.corelibrary.common.json;

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
}
