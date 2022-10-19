import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Task {
    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.fromArgs(args).create(); //create custom options by args
        Pipeline pipeline = Pipeline.create(options); // create pipeline

        // the below described method
        PCollection<String> output = setupPipeline(pipeline); // Declare a PCollection and initialize it using

        // display elements using a logger
        Logger logger = LoggerFactory.getLogger(Task.class);

        logger.info("Printed in SLF4J");
        logger.info(String.valueOf(output));

        pipeline.run(); // invoke pipeline to run

    }

    static PCollection<String> setupPipeline(Pipeline pipeline) {
        return pipeline.apply(Create.of("Hello Beam"));
    }
    // static method returns a PCollection which contains a single String "Hello Beam"
}
