package com.bereket.monitor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "monitored_api")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoredApi {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;
    private String httpMethod; // GET, POST, etc.
    private int pollingIntervalSec = 60; // default 60s

    // You can add owner/user info later
}
