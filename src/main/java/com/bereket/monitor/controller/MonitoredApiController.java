package com.bereket.monitor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bereket.monitor.model.MonitoredApi;
import com.bereket.monitor.repo.MonitoredApiRepository;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/apis")
@RequiredArgsConstructor
public class MonitoredApiController {
    private final MonitoredApiRepository repo;

    @GetMapping
    public List<MonitoredApi> list() { return repo.findAll(); }

    @PostMapping
    public MonitoredApi create(@RequestBody MonitoredApi api) { return repo.save(api); }

    @GetMapping("/{id}")
    public ResponseEntity<MonitoredApi> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
