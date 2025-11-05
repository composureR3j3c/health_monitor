package com.bereket.monitor.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.bereket.monitor.model.ApiMetric;
import com.bereket.monitor.model.MonitoredApi;

public interface ApiMetricRepository extends JpaRepository<ApiMetric, Long> {
    List<ApiMetric> findTop100ByApiOrderByTimestampDesc(MonitoredApi api);
    List<ApiMetric> findByApiOrderByTimestampDesc(MonitoredApi api);
}
