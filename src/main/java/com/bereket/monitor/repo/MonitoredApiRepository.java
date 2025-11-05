package com.bereket.monitor.repo;

import com.bereket.monitor.model.MonitoredApi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MonitoredApiRepository extends JpaRepository<MonitoredApi, Long> {
    List<MonitoredApi> findByHttpMethod(String method);
}
