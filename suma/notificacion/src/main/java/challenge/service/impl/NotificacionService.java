package challenge.service.impl;

import challenge.model.Historial;
import challenge.model.NotificacionRequest;
import challenge.repository.HistorialRepository;
import challenge.service.INotificacionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificacionService implements INotificacionService {

    private final HistorialRepository historialRepository;

    public boolean enviarNotificacion(NotificacionRequest notificacionRequest) {
        historialRepository.save(
                Historial.builder()
                        .endpoint(notificacionRequest.endpoint())
                        .parametros(notificacionRequest.parametros())
                        .respuesta(notificacionRequest.respuesta())
                        .error(notificacionRequest.error())
                        .build()
        );
        return false;
    }
}