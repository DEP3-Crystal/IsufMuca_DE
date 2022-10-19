import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptors;

import java.util.Arrays;
import java.util.List;

import static org.apache.beam.repackaged.core.org.apache.commons.lang3.StringUtils.trim;

public class ExampleApacheBeam1 {

    private static boolean isStopWord(String word) {

        List<String> stoppedWords = List.of("is", "by");

        return stoppedWords.contains(word);
    }

    public static void start() {
        PipelineOptions options = PipelineOptionsFactory.fromArgs().withValidation().create();

        System.out.println("Options id: " + options.getOptionsId());
        System.out.println("job name :" + options.getJobName());
        System.out.println("runner :" + options.getRunner());
        System.out.println("user agent : " + options.getUserAgent());
        System.out.println("temp locations :" + options.getTempLocation());
        System.out.println("unique names :" + options.getStableUniqueNames());

        Pipeline pipeline = Pipeline.create(options);

        String inputFilePath = "src/main/resources/words";
        String outputFilePath = "src/main/resources/output";

        PCollection<KV<String, Long>> wordCount = pipeline
                .apply("(1) Read all lines",
                        TextIO.read().from(inputFilePath))
                .apply("(2) Flatmap to a list of words",
                        FlatMapElements.into(TypeDescriptors.strings())
                                .via(line -> Arrays.asList(line.split("\\s"))))
                .apply("(3) Lowercase all",
                        MapElements.into(TypeDescriptors.strings())
                                .via(word -> word.toLowerCase()))
                .apply("(4) Trim punctuations",
                        MapElements.into(TypeDescriptors.strings())
                                .via(word -> trim(word)))
                .apply("(5) Filter stopwords",
                        Filter.by(word -> !isStopWord(word)))
                .apply("(6) Count words",
                        Count.perElement());

        wordCount.apply(MapElements.into(TypeDescriptors.strings())
                        .via(count -> count.getKey() + " --> " + count.getValue()))
                .apply(TextIO.write().to(outputFilePath));

        pipeline.run().waitUntilFinish();
    }

    public void testMapElements() {

        PipelineOptions options = PipelineOptionsFactory.create();

        Pipeline pipeline = Pipeline.create(options);

        PCollection<String> lines = pipeline.apply(Create.of("alfa", "beta", "gama"))
                .apply(MapElements.via(new SimpleFunction<String, String>() {

                    @Override
                    public String apply(String name) {
                        return name + " is a wonderful name !";
                    }
                }));

    }
}
