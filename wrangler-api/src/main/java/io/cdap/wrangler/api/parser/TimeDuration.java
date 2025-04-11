package io.cdap.wrangler.api.parser;

import java.util.Locale;

public class TimeDuration implements Token {
  private final long milliseconds;

  public TimeDuration(String value) {
    value = value.trim().toLowerCase(Locale.ROOT);
    double number = Double.parseDouble(value.replaceAll("[a-z]+", ""));
    String unit = value.replaceAll("[0-9.]+", "");

    switch (unit) {
      case "ms": milliseconds = (long) number; break;
      case "s":  milliseconds = (long) (number * 1000); break;
      case "m":  milliseconds = (long) (number * 60 * 1000); break;
      case "h":  milliseconds = (long) (number * 60 * 60 * 1000); break;
      default:
        throw new IllegalArgumentException("Unsupported time unit: " + unit);
    }
  }

  public long getMilliseconds() {
    return milliseconds;
  }

  @Override
  public Object value() {
    return milliseconds;
  }

  @Override
  public String toString() {
    return milliseconds + " ms";
  }
}
