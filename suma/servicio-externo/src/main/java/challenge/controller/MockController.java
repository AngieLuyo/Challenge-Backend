package challenge.controller;

import challenge.service.IMockService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mock")
@AllArgsConstructor
public class MockController {
    private final IMockService mockService;

    @GetMapping
    public double obtenerMock() {
        return mockService.obtenerMock();
    }
}
