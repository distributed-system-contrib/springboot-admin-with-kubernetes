package io.github.hamelmoon;


import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.ServiceResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cloud.kubernetes.discovery.KubernetesDiscoveryClient;
import org.springframework.cloud.kubernetes.discovery.KubernetesDiscoveryProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KubernetesDiscoveryClientFilterTest {

  @Mock
  private KubernetesClient kubernetesClient;

  @Mock
  private KubernetesDiscoveryProperties properties;

  @Mock
  private MixedOperation<Service, ServiceList, DoneableService, ServiceResource<Service, DoneableService>> serviceOperation;

  @InjectMocks
  private KubernetesDiscoveryClient underTest;

  @Test
  public void testFilteredServices() {
    List<String> springBootServiceNames = Arrays.asList("serviceA", "serviceB","TEST");
    List<Service> services = createSpringBootServiceByName(springBootServiceNames);

    // Add non spring boot service
    Service service = new Service();
    ObjectMeta objectMeta = new ObjectMeta();
    objectMeta.setName("ServiceNonSpringBoot");
    service.setMetadata(objectMeta);
    services.add(service);

    ServiceList serviceList = new ServiceList();
    serviceList.setItems(services);
    when(serviceOperation.list())
        .thenReturn(serviceList);
    when(kubernetesClient.services()).thenReturn(serviceOperation);

    when(properties.getFilter()).thenReturn("metadata.additionalProperties['spring-boot']");

    List<String> filteredServices = underTest.getServices();

    System.out.println("testFilteredServices Services: " + filteredServices);
    assertEquals(springBootServiceNames, filteredServices);

  }

  @Test
  public void testFilteredServicesByPrefix() {
    List<String> springBootServiceNames = Arrays.asList("serviceA", "serviceB", "serviceC");
    List<Service> services = createSpringBootServiceByName(springBootServiceNames);

    // Add non spring boot service
    Service service = new Service();
    ObjectMeta objectMeta = new ObjectMeta();
    objectMeta.setName("anotherService");
    service.setMetadata(objectMeta);
    services.add(service);

    ServiceList serviceList = new ServiceList();
    serviceList.setItems(services);
    when(serviceOperation.list())
        .thenReturn(serviceList);
    when(kubernetesClient.services()).thenReturn(serviceOperation);

    when(properties.getFilter()).thenReturn("metadata.name.startsWith('service')");

    List<String> filteredServices = underTest.getServices();

    System.out.println("testFilteredServicesByPrefix Services: " + filteredServices);
    assertEquals(springBootServiceNames, filteredServices);

  }

  @Test
  public void testFilteredServicesByPortName() {
    List<String> springBootServiceNames = Arrays.asList("serviceA", "serviceB", "TEST");
    List<Service> services = createSpringBootServiceByName(springBootServiceNames);

    ServiceList serviceList = new ServiceList();
    serviceList.setItems(services);
    when(serviceOperation.list())
        .thenReturn(serviceList);
    when(kubernetesClient.services()).thenReturn(serviceOperation);

    when(properties.getFilter()).thenReturn("spec.ports?.?[name.equals('management-tcp')].toString().indexOf('management-tcp') > 0");

    List<String> filteredServices = underTest.getServices();

    System.out.println("testFilteredServicesByPortName Services: " + filteredServices);
    assertEquals(Arrays.asList("TEST"), filteredServices);

  }

  @Test
  public void testNoExpression() {
    List<String> springBootServiceNames = Arrays.asList("serviceA", "serviceB", "serviceC");
    List<Service> services = createSpringBootServiceByName(springBootServiceNames);

    ServiceList serviceList = new ServiceList();
    serviceList.setItems(services);
    when(serviceOperation.list())
        .thenReturn(serviceList);
    when(kubernetesClient.services()).thenReturn(serviceOperation);

    when(properties.getFilter()).thenReturn("");

    List<String> filteredServices = underTest.getServices();

    System.out.println("Filtered Services: " + filteredServices);
    assertEquals(springBootServiceNames, filteredServices);

  }

  private List<Service> createSpringBootServiceByName(List<String> serviceNames) {
    List<Service> serviceCollection = new ArrayList<>(serviceNames.size());

    for (String serviceName : serviceNames) {

      if(serviceName.equals("TEST")){
        Service service = new Service();
        ObjectMeta objectMeta = new ObjectMeta();
        objectMeta.setName("TEST");
        objectMeta.setAdditionalProperty("spring-boot", "true");
        ServiceSpec serviceSpec = new ServiceSpec();
        List<ServicePort> portList = new ArrayList<ServicePort>();
        ServicePort t = new ServicePort();
        t.setName("management-tcp");
        portList.add(t);
        serviceSpec.setPorts(portList);
        service.setSpec(serviceSpec);
        service.setMetadata(objectMeta);
        serviceCollection.add(service);
      }
      else{
        Service service = new Service();
        ObjectMeta objectMeta = new ObjectMeta();
        objectMeta.setName(serviceName);
        objectMeta.setAdditionalProperty("spring-boot", "true");
        ServiceSpec serviceSpec = new ServiceSpec();
        List<ServicePort> portList = new ArrayList<ServicePort>();
        ServicePort t = new ServicePort();
        t.setName("management-pro");
        portList.add(t);
        serviceSpec.setPorts(portList);
        service.setSpec(serviceSpec);
        service.setMetadata(objectMeta);
        serviceCollection.add(service);

      }
    }


    return serviceCollection;
  }

}