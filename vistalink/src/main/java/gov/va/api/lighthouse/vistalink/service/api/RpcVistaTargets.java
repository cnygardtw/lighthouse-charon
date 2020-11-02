package gov.va.api.lighthouse.vistalink.service.api;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcVistaTargets {
  private String forPatient;
  private List<String> include;
  private List<String> exclude;

  /** Lazy initializer. */
  public List<String> exclude() {
    if (exclude == null) {
      exclude = new ArrayList<>();
    }
    return exclude;
  }

  /** Lazy initializer. */
  public List<String> include() {
    if (include == null) {
      include = new ArrayList<>();
    }
    return include;
  }
}
