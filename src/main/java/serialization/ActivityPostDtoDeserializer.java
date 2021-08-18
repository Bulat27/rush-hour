package serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.prime.rush_hour.dtos.ActivityPostDto;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.Duration;

@JsonComponent
public class ActivityPostDtoDeserializer extends JsonDeserializer<ActivityPostDto> {

    @Override
    public ActivityPostDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String name =  (node.get("name").textValue());
        Duration duration = Duration.ofMinutes((node.get("duration").longValue()));
        Double price = (node.get("price").doubleValue());

        return new ActivityPostDto(name, duration, price);
    }
}
