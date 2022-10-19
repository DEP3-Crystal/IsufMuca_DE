import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.PBegin;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import java.util.List;


public class SchemaTesting {

    public static void main(String[] args) {

        PipelineOptions options = PipelineOptionsFactory.create();

        Pipeline pipe = Pipeline.create(options);

        Schema schema = Schema.builder().addStringField("name").addInt32Field("age")
                .addArrayField("preferences", Schema.FieldType.STRING).build();

        Row row1 = Row.withSchema(schema).addValues("Terry", 22, List.of("Football", "Video-games")).build();

        Row row2 = Row.withSchema(schema).addValues("John", 28, List.of("None")).build();

        PCollection<Row> collection = PBegin.in(pipe).apply(Create.of(row1, row2).withRowSchema(schema));

        collection.apply(MapElements.via(new SimpleFunction<Row, Void>() {

            @Override
            public Void apply(Row rowElement) {
                System.out.println(rowElement);
                return null;
            }
        }));

        pipe.run();

    }
}
