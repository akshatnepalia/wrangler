package io.cdap.wrangler.plugin;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.test.RecipeTester;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsDirectiveTest {

    @Test
    public void testAggregation() throws Exception {
        List<Row> input = Arrays.asList(
            new Row("data_transfer_size", "10MB").add("response_time", "1500ms"),
            new Row("data_transfer_size", "5MB").add("response_time", "2.5s")
        );

        String[] recipe = new String[] {
            "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
        };

        List<Row> results = RecipeTester.run(recipe, input);

        Assert.assertEquals(1, results.size());

        double expectedSizeMB = 15.0;
        double expectedTimeSec = 4.0;

        Row result = results.get(0);
        Assert.assertEquals(expectedSizeMB, (double) result.getValue("total_size_mb"), 0.01);
        Assert.assertEquals(expectedTimeSec, (double) result.getValue("total_time_sec"), 0.01);
    }
}
