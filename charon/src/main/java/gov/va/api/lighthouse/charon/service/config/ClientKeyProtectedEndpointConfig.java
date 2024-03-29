package gov.va.api.lighthouse.charon.service.config;

import static gov.va.api.lighthouse.talos.Responses.unauthorizedAsJson;

import gov.va.api.health.autoconfig.configuration.JacksonConfig;
import gov.va.api.lighthouse.charon.api.RpcResponse;
import gov.va.api.lighthouse.talos.ClientKeyProtectedEndpointFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ClientKeyProtectedEndpointConfig {

  @Bean
  FilterRegistrationBean<ClientKeyProtectedEndpointFilter> clientKeyProtectedEndpointFilter(
      @Value("${charon.rpc.client-keys}") String rpcClientKeysCsv) {
    var registration = new FilterRegistrationBean<ClientKeyProtectedEndpointFilter>();

    List<String> clientKeys;

    if ("disabled".equals(rpcClientKeysCsv)) {
      log.warn(
          "ClientKeyProtectedEndpointFilter is disabled. To enable, "
              + "set charon.rpc.client-keys to a value other than disabled.");

      registration.setEnabled(false);
      clientKeys = List.of();
    } else {
      log.info("ClientKeyProtectedEndpointFilter is enabled.");
      clientKeys = Arrays.stream(rpcClientKeysCsv.split(",")).collect(Collectors.toList());
    }

    registration.setFilter(
        ClientKeyProtectedEndpointFilter.builder()
            .clientKeys(clientKeys)
            .name("RPC Request")
            .unauthorizedResponse(unauthorizedResponse())
            .build());

    registration.setOrder(1);

    registration.addUrlPatterns("/rpc/*", PathRewriteConfig.leadingPath() + "rpc/*");

    return registration;
  }

  @SneakyThrows
  private Consumer<HttpServletResponse> unauthorizedResponse() {
    var response =
        RpcResponse.builder()
            .status(RpcResponse.Status.FAILED)
            .message(Optional.of("Unauthorized: Check the client-key header."))
            .build();
    return unauthorizedAsJson(JacksonConfig.createMapper().writeValueAsString(response));
  }
}
