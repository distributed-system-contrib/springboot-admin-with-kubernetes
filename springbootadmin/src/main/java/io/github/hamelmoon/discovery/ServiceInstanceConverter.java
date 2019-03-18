package io.github.hamelmoon.discovery;

import de.codecentric.boot.admin.server.domain.values.Registration;
import org.springframework.cloud.client.ServiceInstance;


public interface ServiceInstanceConverter {
  Registration convert(ServiceInstance instance);
}