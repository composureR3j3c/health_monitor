package com.bereket.monitor.controller;

import com.bereket.monitor.model.ApiMetric;
import com.bereket.monitor.model.MonitoredApi;
import com.bereket.monitor.repo.ApiMetricRepository;
import com.bereket.monitor.repo.MonitoredApiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
public class MetricsController {
    private final ApiMetricRepository metricRepo;
    private final MonitoredApiRepository apiRepo;

    @GetMapping("/latest/{apiId}")
    public List<ApiMetric> latest(@PathVariable Long apiId) {
        MonitoredApi api = apiRepo.findById(apiId).orElseThrow();
        return metricRepo.findTop100ByApiOrderByTimestampDesc(api);
    }

    @GetMapping("/history/{apiId}")
    public List<ApiMetric> history(@PathVariable Long apiId) {
        MonitoredApi api = apiRepo.findById(apiId).orElseThrow();
        return metricRepo.findByApiOrderByTimestampDesc(api);
    }
}
