package com.bereket.monitor.logging;

import com.bereket.monitor.model.ApiMetric;
import com.bereket.monitor.model.MonitoredApi;
import com.bereket.monitor.repo.ApiMetricRepository;
import com.bereket.monitor.repo.MonitoredApiRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiLoggingInterceptor implements HandlerInterceptor {

    private final ApiMetricRepository metricRepo;
    private final MonitoredApiRepository apiRepo;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            String path = request.getRequestURI();
            String method = request.getMethod();
            long start = (Long) (request.getAttribute("startTime") == null ? System.currentTimeMillis() : request.getAttribute("startTime"));
            long latency = System.currentTimeMillis() - start;
            int status = response.getStatus();

            // find matching monitored API (simple match by URL)
            apiRepo.findAll().stream()
                .filter(a -> a.getUrl().contains(path) && a.getHttpMethod().equalsIgnoreCase(method))
                .findFirst()
                .ifPresent(api -> {
                    ApiMetric m = new ApiMetric();
                    m.setApi(api);
                    m.setStatusCode(status);
                    m.setLatencyMs(latency);
                    m.setResponseSnippet(null);
                    m.setTimestamp(LocalDateTime.now());
                    metricRepo.save(m);
                });
        } catch (Exception e) {
            log.warn("Failed to save passive metric: {}", e.getMessage());
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }
}
