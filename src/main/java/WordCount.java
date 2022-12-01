import org.apache.beam.runners.dataflow.DataflowRunner;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class WordCount {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(WordCount.class);
        String bookPath = "gs://documents_buckey_1/alice_in_wonderland.txt";
//        String resultPath = "gs://documents_buckey_1/alice_in_wonderland_result";

        final DataflowPipelineOptions options = PipelineOptionsFactory.as(DataflowPipelineOptions.class);
        options.setProject("ecstatic-magpie-368822");
        options.setRunner(DataflowRunner.class);
        options.setGcpTempLocation("gs://documents_buckey_1/temp");
        options.setRegion("europe-west1");
        options.setJobName("CustomWordCount1");

        Pipeline pipeline = Pipeline.create(options);

        PCollection<Long> wordCount = pipeline.apply(TextIO.read().from(bookPath))
                .apply(ParDo.of(new DoFn<String, String[]>() {
                    @ProcessElement
                    public void processElement(ProcessContext context) {
                        context.output(Objects.requireNonNull(context.element()).split(" "));
                    }
                }))
                .apply(ParDo.of(new DoFn<String[], String>() {
                    @ProcessElement
                    public void processElement(ProcessContext context) {
                        for (String word : Objects.requireNonNull(context.element())) {
                            context.output(word);
                        }
                    }
                }))
                .apply(ParDo.of(new DoFn<String, String>() {
                    @ProcessElement
                    public void processElement(ProcessContext context) {
                        logger.info(context.element());
                        context.output(context.element());
                    }
                }))
                .apply(Count.globally());

        logger.info("Word count: " + String.valueOf(wordCount));
        pipeline.run();
    }
}
