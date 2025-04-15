package io.cdap.wrangler.api.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeDuration extends Token {
  private static final Pattern PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*(ns|ms|s|m|h|d)");
  private final double value;
  private final String unit;

  public TimeDuration(String text) {
    super(text);
    Matcher matcher = PATTERN.matcher(text);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid time duration format: " + text);
    }
    this.value = Double.parseDouble(matcher.group(1));
    this.unit = matcher.group(2);
  }

  public long getNanoseconds() {
    switch (unit) {
      case "ns":
        return (long) value;
      case "ms":
        return (long) (value * 1_000_000);
      case "s":
        return (long) (value * 1_000_000_000);
      case "m":
        return (long) (value * 60 * 1_000_000_000L);
      case "h":
        return (long) (value * 60 * 60 * 1_000_000_000L);
      case "d":
        return (long) (value * 24 * 60 * 60 * 1_000_000_000L);
      default:
        throw new IllegalStateException("Unknown time unit: " + unit);
    }
  }

  public double getMilliseconds() {
    return getNanoseconds() / 1_000_000.0;
  }

  public double getSeconds() {
    return getNanoseconds() / 1_000_000_000.0;
  }

  public double getMinutes() {
    return getSeconds() / 60.0;
  }

  public double getHours() {
    return getMinutes() / 60.0;
  }

  public String getUnit() {
    return unit;
  }

  public double getValue() {
    return value;
  }
}