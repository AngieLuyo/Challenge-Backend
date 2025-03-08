package challenge.rabbitmq;

import challenge.model.NotificacionRequest;
import challenge.service.impl.NotificacionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NotificacionConsumer {
    private final NotificacionService notificacionService;

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(NotificacionRequest notificacionRequest) {
        log.info("Consumed {} from queue", notificacionRequest);
        notificacionService.enviarNotificacion(notificacionRequest);
    }
}
