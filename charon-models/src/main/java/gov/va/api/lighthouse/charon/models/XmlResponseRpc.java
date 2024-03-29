package gov.va.api.lighthouse.charon.models;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class XmlResponseRpc {

  /** Deserialize the XML response from an RPC into expected rpc response class. */
  @SneakyThrows
  public <T> T deserialize(String xmlResponse, Class<T> clazz) {
    if (xmlResponse == null) {
      throw new RpcModelExceptions.InvalidRpcResponse("Vista response is null");
    }
    try {
      return new XmlMapper()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .readValue(xmlResponse, clazz);
    } catch (Exception e) {
      throw new RpcModelExceptions.InvalidRpcResponse(
          "Failed to deserialize to " + clazz.getSimpleName(), e);
    }
  }
}
