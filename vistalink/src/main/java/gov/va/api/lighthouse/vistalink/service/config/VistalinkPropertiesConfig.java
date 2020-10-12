package gov.va.api.lighthouse.vistalink.service.config;

import com.google.common.base.Splitter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Builder
public class VistalinkPropertiesConfig {

  @Bean
  VistalinkProperties load(@Value("vistalink.properties") String vistalinkProperties) {
    Properties p = new Properties();
    try {
      p.load(new FileInputStream(new File(vistalinkProperties)));
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "The vistalink.properties file cannot be found and is required.");
    }
    List<ConnectionDetails> vistalinkDetails =
        p.entrySet().stream()
            .map(
                e -> {
                  var parts = Splitter.on(':').splitToList(e.getValue().toString());
                  if (parts.size() != 3) {
                    throw new IllegalArgumentException("Cannot parse vistalink.properties.");
                  }
                  return ConnectionDetails.builder()
                      .host(parts.get(0))
                      .port(Integer.parseInt(parts.get(1)))
                      .divisionIen(parts.get(2))
                      .build();
                })
            .collect(Collectors.toList());
    return VistalinkProperties.builder().vistas(vistalinkDetails).build();
  }
}
