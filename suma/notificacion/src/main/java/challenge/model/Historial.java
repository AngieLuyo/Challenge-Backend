package challenge.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "historial")
public class Historial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private String endpoint;

    @Column(columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private JsonNode parametros;

    @Column(columnDefinition = "jsonb")
    @Type(JsonBinaryType.class)
    private JsonNode respuesta;

    private String error;
}
