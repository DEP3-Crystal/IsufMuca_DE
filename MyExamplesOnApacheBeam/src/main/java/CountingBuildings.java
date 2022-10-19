import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

import java.util.List;

public class CountingBuildings {
    public static void main(String[] args) {

        PipelineOptions opt = PipelineOptionsFactory.create();

        Pipeline pipe = Pipeline.create(opt);

        PCollection<KV<String, Long>> map = pipe.apply(Create.of(List.of(KV.of("tower", "alfa"), KV.of("tower", "beta"), KV.of("skyscraper", "gama"))))
                .apply(Count.perKey());


        map.apply(ParDo.of(new DoFn<KV<String, Long>, Void>() {

            @ProcessElement
            public void processElement(ProcessContext context) {
                System.out.println(context.element().getKey() + ": " + context.element().getValue());
            }
        }));

        pipe.run();

    }
}
