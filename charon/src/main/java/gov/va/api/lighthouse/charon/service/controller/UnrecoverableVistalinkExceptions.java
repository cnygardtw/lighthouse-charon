package gov.va.api.lighthouse.charon.service.controller;

import lombok.experimental.UtilityClass;

@UtilityClass
final class UnrecoverableVistalinkExceptions {

  static class UnrecoverableVistalinkException extends RuntimeException {
    UnrecoverableVistalinkException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  static class BadRpcContext extends UnrecoverableVistalinkException {
    BadRpcContext(String rpcContext, Throwable cause) {
      super(rpcContext, cause);
    }
  }
}