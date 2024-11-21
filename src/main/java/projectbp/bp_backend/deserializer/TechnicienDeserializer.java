package projectbp.bp_backend.deserializer;

import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import projectbp.bp_backend.bean.Technicien;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class TechnicienDeserializer extends StdDeserializer<Technicien> {

    public TechnicienDeserializer() {
        this(null);
    }

    public TechnicienDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Technicien deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.isTextual()) {
            return new Technicien(node.asText());
        } else {
            String nom = node.get("nom").asText();
            return new Technicien(nom);
        }
    }
}
