package io.cdap.wrangler.plugin;

import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.annotation.Name;
import io.cdap.wrangler.api.annotation.Description;
import io.cdap.wrangler.api.annotation.Scope;
import io.cdap.wrangler.api.annotation.Aggregator;
import io.cdap.wrangler.api.Arguments;
import io.cdap.wrangler.api.ExecutorContext;

import java.util.List;
import java.util.ArrayList;

@Name("aggregate-stats")
@Description("Aggregate byte size and time duration into total size (MB) and total time (s)")
@Scope(Scope.ScopeType.AGGREGATE)
@Aggregator
public class AggregateStatsDirective implements Directive {

    private String sourceSizeCol;
    private String sourceTimeCol;
    private String targetSizeCol;
    private String targetTimeCol;

    @Override
    public void initialize(Arguments args, ExecutorContext context) throws Exception {
        this.sourceSizeCol = args.value("source_size_col");
        this.sourceTimeCol = args.value("source_time_col");
        this.targetSizeCol = args.value("target_size_col");
        this.targetTimeCol = args.value("target_time_col");
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) throws Exception {
        long totalBytes = 0;
        long totalMilliseconds = 0;

        for (Row row : rows) {
            Object sizeVal = row.getValue(sourceSizeCol);
            Object timeVal = row.getValue(sourceTimeCol);

            long bytes = 0;
            if (sizeVal instanceof String) {
                bytes = new io.cdap.wrangler.api.parser.ByteSize((String) sizeVal).getBytes();
            }

            long millis = 0;
            if (timeVal instanceof String) {
                millis = new io.cdap.wrangler.api.parser.TimeDuration((String) timeVal).getMilliseconds();
            }

            totalBytes += bytes;
            totalMilliseconds += millis;
        }

        double totalSizeMB = totalBytes / (1024.0 * 1024.0);
        double totalTimeSeconds = totalMilliseconds / 1000.0;

        Row result = new Row();
        result.add(targetSizeCol, totalSizeMB);
        result.add(targetTimeCol, totalTimeSeconds);

        List<Row> resultList = new ArrayList<>();
        resultList.add(result);
        return resultList;
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}
