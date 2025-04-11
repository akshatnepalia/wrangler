package io.cdap.wrangler.plugin;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.test.RecipeTester;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsDirectiveTest {

    @Test
    public void testAggregateStats() throws Exception {
        List<Row> rows = Arrays.asList(
            new Row("data_transfer_size", "10MB").add("response_time", "1500ms"),
            new Row("data_transfer_size", "5MB").add("response_time", "2.5s")
        );

        String[] recipe = new String[] {
            "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
        };

        List<Row> result = RecipeTester.run(recipe, rows);

        Assert.assertEquals(1, result.size());
        Row output = result.get(0);

        double totalSizeMB = (double) output.getValue("total_size_mb");
        double totalTimeSec = (double) output.getValue("total_time_sec");

        Assert.assertEquals(15.0, totalSizeMB, 0.01);
        Assert.assertEquals(4.0, totalTimeSec, 0.01);
    }
}
