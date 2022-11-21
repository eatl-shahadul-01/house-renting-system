package bd.com.squarehealth.corelibrary.common.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

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

    public static JsonSerializer getInstance() {
        return JSON_SERIALIZER;
    }
}
