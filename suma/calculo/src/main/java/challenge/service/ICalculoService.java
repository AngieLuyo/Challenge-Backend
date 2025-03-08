package challenge.service;

import challenge.model.Calculo;

public interface ICalculoService {
    public double calcular(Calculo request, String endpoint);
}
