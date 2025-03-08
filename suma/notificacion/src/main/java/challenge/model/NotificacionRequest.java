package challenge.model;

import com.fasterxml.jackson.databind.JsonNode;

public record NotificacionRequest(String endpoint, JsonNode parametros, JsonNode respuesta, String error) {
}
