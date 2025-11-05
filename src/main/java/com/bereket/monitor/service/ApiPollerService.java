package com.bereket.monitor.service;

import com.bereket.monitor.model.ApiMetric;
import com.bereket.monitor.model.MonitoredApi;
import com.bereket.monitor.repo.ApiMetricRepository;
import com.bereket.monitor.repo.MonitoredApiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiPollerService {
    private final MonitoredApiRepository apiRepo;
    private final ApiMetricRepository metricRepo;
    private final RestTemplate restTemplate;

    // runs every 30 seconds; inside we skip non-GET APIs
    @Scheduled(fixedRate = 30000)
    public void poll() {
        List<MonitoredApi> apis = apiRepo.findAll();
        for (MonitoredApi api : apis) {
            if (!"GET".equalsIgnoreCase(api.getHttpMethod())) continue; // safety
            long start = System.currentTimeMillis();
            int status = 0;
            String snippet = null;
            try {
                ResponseEntity<String> resp = restTemplate.getForEntity(api.getUrl(), String.class);
                status = resp.getStatusCodeValue();
                String body = resp.getBody();
                snippet = body == null ? null : (body.length() > 1000 ? body.substring(0,1000) : body);
            } catch (Exception e) {
                log.warn("Poll failed for {} : {}", api.getUrl(), e.toString());
                status = 0; // or 503
                snippet = e.getMessage();
            }
            long latency = System.currentTimeMillis() - start;
            ApiMetric m = new ApiMetric();
            m.setApi(api);
            m.setStatusCode(status);
            m.setLatencyMs(latency);
            m.setResponseSnippet(snippet);
            m.setTimestamp(LocalDateTime.now());
            metricRepo.save(m);
        }
    }
}
