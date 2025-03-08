package challenge.service.impl;

import challenge.model.Mock;
import challenge.service.IMockService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MockService implements IMockService {
    private final Mock mock;

    public double obtenerMock() {
        return mock.getPorcentaje();
    }
}
