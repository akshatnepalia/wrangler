package io.cdap.wrangler.api.parser;

import java.util.Locale;

public class ByteSize implements Token {
  private final long bytes;

  public ByteSize(String value) {
    value = value.trim().toUpperCase(Locale.ROOT);
    double number = Double.parseDouble(value.replaceAll("[A-Z]+", ""));
    String unit = value.replaceAll("[0-9.]+", "");

    switch (unit) {
      case "B":  bytes = (long) number; break;
      case "KB": bytes = (long) (number * 1024); break;
      case "MB": bytes = (long) (number * 1024 * 1024); break;
      case "GB": bytes = (long) (number * 1024 * 1024 * 1024); break;
      case "TB": bytes = (long) (number * 1024L * 1024L * 1024L * 1024L); break;
      default:
        throw new IllegalArgumentException("Unsupported byte size unit: " + unit);
    }
  }

  public long getBytes() {
    return bytes;
  }

  @Override
  public Object value() {
    return bytes;
  }

  @Override
  public String toString() {
    return bytes + " B";
  }
}
