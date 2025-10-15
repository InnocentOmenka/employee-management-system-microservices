package com.innocodes.api_gateway.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class OpenApiServerRewriteTransformer {

    private final ObjectMapper objectMapper;

    public OpenApiServerRewriteTransformer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Mono<JsonNode> transform(JsonNode input, String gatewayUrl) {
        try {
            ObjectNode root = (ObjectNode) input;
            if (root.has("servers") && root.get("servers").isArray()) {
                ArrayNode servers = (ArrayNode) root.get("servers");
                servers.removeAll(); // Clear existing servers
                ObjectNode newServer = objectMapper.createObjectNode();
                newServer.put("url", gatewayUrl);
                newServer.put("description", "Proxied via API Gateway");
                servers.add(newServer);
            }
            return Mono.just(root);
        } catch (Exception e) {
            System.err.println("Failed to rewrite OpenAPI spec: " + e.getMessage());
            return Mono.just(input); // Return original if error
        }
    }
}