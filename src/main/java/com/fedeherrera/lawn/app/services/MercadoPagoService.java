package com.fedeherrera.lawn.app.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class MercadoPagoService {

    @Value("${MERCADOPAGO_ACCESS_TOKEN}")
    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> crearPreferencia(String titulo, double precio) {
        String url = "https://api.mercadopago.com/checkout/preferences";

        Map<String, Object> item = new HashMap<>();
        item.put("title", titulo);
        item.put("quantity", 1);
        item.put("currency_id", "ARS");
        item.put("unit_price", precio);

        Map<String, Object> body = new HashMap<>();
        body.put("items", Collections.singletonList(item));
        body.put("back_urls", Map.of(
                // "success", "lawnapp://pago-exitoso",
                "success", "exp://192.168.201.66:8081/pago-exitoso",
                "failure", "lawnapp://pago-fallido"
        ));
        body.put("auto_return", "approved");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            String prefId = (String) responseBody.get("id");
            String initPoint = (String) responseBody.get("init_point");

            Map<String, String> result = new HashMap<>();
            result.put("preferenceId", prefId);
            result.put("init_point", initPoint);

            return result;
        } else {
            throw new RuntimeException("Error creando preferencia de MP: " + response.getStatusCode());
        }
    }
}
