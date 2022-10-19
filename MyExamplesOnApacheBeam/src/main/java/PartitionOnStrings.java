import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Partition;
import org.apache.beam.sdk.values.PCollectionList;

import java.util.List;

public class PartitionOnStrings {

    public static void main(String[] args) {

        List<String> listOfWords = List.of("alfa-lakto-albumine", "beta-lakto-globulin", "went", "gone");

        PipelineOptions opt = PipelineOptionsFactory.create();

        Pipeline pipe = Pipeline.create(opt);

        PCollectionList<String> list = pipe.apply(Create.of(listOfWords)).apply(Partition.of(2, new Partition.PartitionFn<String>() {
            @Override
            public int partitionFor(String elem, int numPartitions) {
                if (elem.trim().length() > 10) {
                    return 1;
                }
                return 0;
            }
        }));

        list.get(0).apply(ParDo.of(new DoFn<String, Void>() {

            @ProcessElement
            public void processElement(ProcessContext context) {

                System.out.println("Less then 10 characters word: " + context.element());
            }
        }));


        list.get(1).apply(ParDo.of(new DoFn<String, Void>() {
            @ProcessElement
            public void processElement(ProcessContext context) {

                System.out.println("More then 10 characters word: " + context.element());
            }
        }));

        pipe.run();
    }
}
