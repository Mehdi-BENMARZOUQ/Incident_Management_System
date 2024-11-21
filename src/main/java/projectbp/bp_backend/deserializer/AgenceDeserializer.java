package projectbp.bp_backend.deserializer;


import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import projectbp.bp_backend.bean.Agence;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;


public class AgenceDeserializer extends StdDeserializer<Agence>{

    public AgenceDeserializer() {
        this(null);
    }

    public AgenceDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Agence deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.isTextual()) {
            return new Agence(node.asText());
        } else {
            String nom = node.get("nom").asText();
            return new Agence(nom);
        }
    }
}
