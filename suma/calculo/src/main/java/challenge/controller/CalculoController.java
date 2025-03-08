package challenge.controller;

import challenge.model.Calculo;
import challenge.service.ICalculoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/calculo")
public class CalculoController {
    private final ICalculoService calculoService;
    private final HttpServletRequest request;

    public CalculoController(ICalculoService calculoService, HttpServletRequest request) {
        this.calculoService = calculoService;
        this.request = request;
    }

    @PostMapping
    public ResponseEntity<?> calcularPorcentaje(@RequestBody Calculo calculoRequest) {
        double resultado = calculoService.calcular(calculoRequest, request.getRequestURI());
        if (resultado == -1)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
