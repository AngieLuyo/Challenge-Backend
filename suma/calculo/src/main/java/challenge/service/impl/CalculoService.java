package challenge.service.impl;

import challenge.model.Calculo;
import challenge.model.NotificacionRequest;
import challenge.rabbitmq.RabbitMQMessageProducer;
import challenge.service.ICalculoService;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CalculoService implements ICalculoService {
    private final RestTemplate restTemplate;
    private JsonService jsonService;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final CacheManager cacheManager;
    private static final String CACHE_KEY = "latestValue";

    public double calcular(Calculo request, String endpoint) {
        double mock = invocarMock();
        double resultado = -1;
        String error = "";
        if (mock == -1)
            error = "Servicio caido";
        else
            resultado = (request.getNum1() + request.getNum2()) * (1 + mock);

        registrarHistorial(request, resultado, endpoint, error);

        return resultado;
    }

    public double invocarMock() {
        try {
            Double response = restTemplate.getForObject("http://SERVICIOEXTERNO:8084/api/v1/mock", Double.class);
            if (response == null)
                throw new RuntimeException("Respuesta nula del servicio externo");

            // Almacenar en caché el resultado con una clave fija
            Cache cache = cacheManager.getCache("mock");
            if (cache != null) {
                cache.put(CACHE_KEY, response);
            }

            return response;
        } catch (Exception e) {
            // Buscar en caché el último valor almacenado
            Cache cache = cacheManager.getCache("mock");
            if (cache != null) {
                Double cachedValue = cache.get(CACHE_KEY, Double.class);
                if (cachedValue != null) {
                    System.out.println("Usando el valor en caché: " + cachedValue);
                    return cachedValue;
                }
            }
            return -1;
        }
    }

    public void registrarHistorial(Calculo request, double resultado, String endpoint, String error) {
        NotificacionRequest notificacionRequest = new NotificacionRequest(
                endpoint,
                jsonService.convertToJsonNode(request),
                jsonService.convertToJsonNode(resultado),
                error
        );

        rabbitMQMessageProducer.publish(
                notificacionRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }
}
