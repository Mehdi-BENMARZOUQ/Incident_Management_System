package projectbp.bp_backend.deserializer;


import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import projectbp.bp_backend.bean.Agence;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import projectbp.bp_backend.bean.Devis;

import java.io.IOException;

public class DevisDeserializer extends StdDeserializer<Devis>{

    public DevisDeserializer() {
        this(null);
    }

    public DevisDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Devis deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.isTextual()) {
            return new Devis(node.asText());
        } else {
            String numero = node.get("numero").asText();
            return new Devis(numero);
        }
    }
}
