package com.bereket.monitor.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "api_metric")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiMetric {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MonitoredApi api;

    private int statusCode;
    private long latencyMs;

    @Column(length = 2000)
    private String responseSnippet;

    private LocalDateTime timestamp;
}
