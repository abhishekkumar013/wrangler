package io.cdap.directives.aggregates;

package io.cdap.wrangler.core.directives.aggregates;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.test.TestingRig;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AggregateStatsTest {
  
  @Test
  public void testAggregation() {
    List<Row> rows = new ArrayList<>();
    
    // Create test data
    for (int i = 0; i < 10; i++) {
      Row row = new Row();
      row.add("data_transfer_size", (i + 1) + "KB");
      row.add("response_time", (i * 100) + "ms");
      rows.add(row);
    }
    
    // Expected values: 
    // Sum of 1KB to 10KB = 55KB = 55 * 1024 bytes = 56320 bytes = 0.05375 MB
    // Sum of 0ms to 900ms = 4500ms = 4.5 seconds
    
    String[] recipe = new String[] {
      "aggregate-stats data_transfer_size response_time total_size_mb total_time_sec"
    };
    
    List<Row> results = TestingRig.execute(recipe, rows);
    
    Assert.assertEquals(1, results.size());
    Assert.assertEquals(0.05375, (Double) results.get(0).getValue("total_size_mb"), 0.001);
    Assert.assertEquals(4.5, (Double) results.get(0).getValue("total_time_sec"), 0.001);
  }
}