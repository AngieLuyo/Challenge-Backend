package challenge.service.impl;

import challenge.model.Calculo;
import challenge.model.NotificacionRequest;
import challenge.rabbitmq.RabbitMQMessageProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CalculoServiceTest {

    @InjectMocks
    CalculoService calculoService;
    @Mock
    RestTemplate restTemplate;
    @Mock
    JsonService jsonService;
    @Mock
    RabbitMQMessageProducer rabbitMQMessageProducer;
    @Mock
    CacheManager cacheManager;
    @Mock
    private Cache cache;
    private static final String CACHE_KEY = "latestValue";

    @BeforeEach
    void setUp() {
        //calculoService = new CalculoService(restTemplate, jsonService, rabbitMQMessageProducer, cacheManager);
        MockitoAnnotations.openMocks(this);
        when(cacheManager.getCache("mock")).thenReturn(cache);
    }

    //Prueba que "calcular" devuelve el resultado correcto cuando el servicio externo responde bien
    @Test
    void testCalcular_ConServicioExternoExitoso() {
        // Configuración de datos de prueba
        Calculo request = generarCalculo();
        when(restTemplate.getForObject(anyString(), eq(Double.class))).thenReturn(0.1); // Mock = 10%

        // Ejecutar método
        double resultado = calculoService.calcular(request, "/test-endpoint");

        // Verificación: (2 + 3) * (1 + 0.1) = 5.5
        assertEquals(5.5, resultado, 0.0001);
    }

    // Prueba que "calcular" usa caché si el servicio externo falla
    @Test
    void testCalcular_ConServicioExternoCaido_UsaCache() {
        // Configuración de datos de prueba
        Calculo request = generarCalculo();

        when(restTemplate.getForObject(anyString(), eq(Double.class))).thenThrow(new RuntimeException("Falla el servicio externo"));
        when(cache.get(CACHE_KEY, Double.class)).thenReturn(0.2); // Mock de caché con 20%

        // Ejecutar método
        double resultado = calculoService.calcular(request, "/test-endpoint");

        // Verificación: (2 + 3) * (1 + 0.2) = 6
        assertEquals(6, resultado, 0.0001);
    }

    // Prueba que "calcular" devuelve -1 si el servicio externo falla y la caché está vacía
    @Test
    void testCalcular_ConServicioExternoCaido_SinCache() {
        // Configuración de datos de prueba
        Calculo request = generarCalculo();

        when(restTemplate.getForObject(anyString(), eq(Double.class))).thenThrow(new RuntimeException("Falla el servicio externo"));
        when(cache.get(CACHE_KEY, Double.class)).thenReturn(null); // Caché vacía

        // Ejecutar método
        double resultado = calculoService.calcular(request, "/test-endpoint");

        // Verificación: Debe devolver -1
        assertEquals(-1, resultado);
    }

    // Prueba que "invocarMock" almacena el valor en caché correctamente
    @Test
    void testInvocarMock_AlmacenaEnCache() {
        when(restTemplate.getForObject(anyString(), eq(Double.class))).thenReturn(0.3);

        double resultado = calculoService.invocarMock();

        // Verificar que se almacena en caché
        verify(cache).put(CACHE_KEY, 0.3);
        assertEquals(0.3, resultado);
    }

    // Prueba que "invocarMock" usa caché si el servicio externo falla
    @Test
    void testInvocarMock_UsaCacheSiServicioExternoFalla() {
        when(restTemplate.getForObject(anyString(), eq(Double.class))).thenThrow(new RuntimeException("Error"));
        when(cache.get(CACHE_KEY, Double.class)).thenReturn(0.4);

        double resultado = calculoService.invocarMock();

        assertEquals(0.4, resultado);
    }

    // Prueba que "registrarHistorial" envía un mensaje a RabbitMQ
    @Test
    void testRegistrarHistorial_EnvioRabbitMQ() {
        Calculo request = generarCalculo();
        String endpoint = "/api/test";
        double resultado = 10.0;
        String error = "";

        // Mockear la conversión JSON
        when(jsonService.convertToJsonNode(request)).thenReturn(null);
        when(jsonService.convertToJsonNode(resultado)).thenReturn(null);

        // Llamar al método
        calculoService.registrarHistorial(request, resultado, endpoint, error);

        // Verificar que se envió el mensaje a RabbitMQ
        verify(rabbitMQMessageProducer, times(1)).publish(any(NotificacionRequest.class), eq("internal.exchange"), eq("internal.notification.routing-key"));
    }

    Calculo generarCalculo() {
        Calculo calculo = new Calculo();
        calculo.setNum1(2);
        calculo.setNum2(3);
        return calculo;
    }
}