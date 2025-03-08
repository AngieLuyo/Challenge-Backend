package challenge.service;

import challenge.model.NotificacionRequest;

public interface INotificacionService {
    public boolean enviarNotificacion(NotificacionRequest notificacionRequest);
}
