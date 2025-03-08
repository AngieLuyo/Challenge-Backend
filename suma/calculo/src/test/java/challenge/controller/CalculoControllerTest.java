package challenge.controller;

import challenge.model.Calculo;
import challenge.service.impl.CalculoService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(CalculoControllerTest.class)
class CalculoControllerTest {

    @InjectMocks
    private CalculoController calculoController;
    @Mock
    private CalculoService calculoService;
    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        Mockito.when(request.getRequestURI()).thenReturn("/api/mock");
        calculoController = new CalculoController(calculoService, request);
    }

    @Test
    void calcularPorcentajeOk() {
        Calculo calculo = generarCalculo();
        when(calculoService.calcular(any(Calculo.class), anyString())).thenReturn(30.0);

        ResponseEntity<?> responseEntity = calculoController.calcularPorcentaje(calculo);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        double clienteResponse = (double) responseEntity.getBody();
        assertEquals(30, clienteResponse);
    }

    @Test
    void calcularPorcentajeError() {
        Calculo calculo = generarCalculo();
        when(calculoService.calcular(any(Calculo.class), anyString())).thenReturn(-1.0);

        ResponseEntity<?> responseEntity = calculoController.calcularPorcentaje(calculo);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    Calculo generarCalculo() {
        Calculo calculo = new Calculo();
        calculo.setNum1(10);
        calculo.setNum2(20);
        return calculo;
    }
}