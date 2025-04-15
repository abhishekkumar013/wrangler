package io.cdap.wrangler.api.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ByteSize extends Token {
  private static final Pattern PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*([kKmMgGtTpP]?[bB]|[kK][bB]|[mM][bB]|[gG][bB]|[tT][bB]|[pP][bB])");
  private final double value;
  private final String unit;

  public ByteSize(String text) {
    super(text);
    Matcher matcher = PATTERN.matcher(text);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid byte size format: " + text);
    }
    this.value = Double.parseDouble(matcher.group(1));
    this.unit = matcher.group(2).toUpperCase();
  }

  public long getBytes() {
    switch (unit) {
      case "B":
        return (long) value;
      case "KB":
        return (long) (value * 1024);
      case "MB":
        return (long) (value * 1024 * 1024);
      case "GB":
        return (long) (value * 1024 * 1024 * 1024);
      case "TB":
        return (long) (value * 1024L * 1024L * 1024L * 1024L);
      case "PB":
        return (long) (value * 1024L * 1024L * 1024L * 1024L * 1024L);
      default:
        throw new IllegalStateException("Unknown byte unit: " + unit);
    }
  }

  public double getKilobytes() {
    return getBytes() / 1024.0;
  }

  public double getMegabytes() {
    return getBytes() / (1024.0 * 1024.0);
  }

  public double getGigabytes() {
    return getBytes() / (1024.0 * 1024.0 * 1024.0);
  }

  public String getUnit() {
    return unit;
  }

  public double getValue() {
    return value;
  }
}