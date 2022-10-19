import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionList;

import java.util.List;

public class Flatten {
    public static void main(String[] args) {
        PipelineOptions opt = PipelineOptionsFactory.create();
        Pipeline mainPipeline = Pipeline.create(opt);

        PCollection<String> p1 = mainPipeline.apply(Create.of(List.of("text", "word")));

        PCollection<String> p2 = mainPipeline.apply(Create.of(List.of("letters", "characters")));

        PCollectionList<String> list = PCollectionList.of(p1).and(p2);

        PCollection<String> flattenCollection = list.apply(org.apache.beam.sdk.transforms.Flatten.pCollections());

        flattenCollection.apply(ParDo.of(new DoFn<String, Void>() {

            @ProcessElement
            public void processElement(ProcessContext context) {
                System.out.println(context.element());
            }
        }));

        mainPipeline.run();
    }
}
