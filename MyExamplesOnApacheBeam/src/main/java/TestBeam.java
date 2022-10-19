import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.SerializableCoder;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class TestBeam {

    private static class GraphicsCard implements Serializable {
        private String gpuName;

        private String company;
        private double price;

        private String manufacturer;

        private int frequencyHz;


        public String getGpuName() {
            return gpuName;
        }

        public void setGpuName(String gpuName) {
            this.gpuName = gpuName;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public int getFrequencyHz() {
            return frequencyHz;
        }

        public void setFrequencyHz(int frequencyHz) {
            this.frequencyHz = frequencyHz;
        }

        public GraphicsCard(String gpuName, String company, double price, String manufacturer, int frequencyHz) {
            this.gpuName = gpuName;
            this.company = company;
            this.price = price;
            this.manufacturer = manufacturer;
            this.frequencyHz = frequencyHz;
        }
    }

    private static class FilterRadeonCardsFn extends DoFn<GraphicsCard, GraphicsCard> {

        @ProcessElement
        public void processElement(@Element GraphicsCard element, OutputReceiver<GraphicsCard> out) {
            if (element.getCompany().contains("Radeon")) {
                out.output(element);
            }
        }
    }

    private static class DisplayFn extends DoFn<GraphicsCard, GraphicsCard> {

        Logger logger = LoggerFactory.getLogger(TestBeam.class);

        @ProcessElement
        public void processElement(ProcessContext context) {
            GraphicsCard gpu = context.element();
            assert gpu != null;
            logger.info("The " + gpu.getGpuName() + " of " + gpu.getCompany() + " costs: " + gpu.getPrice());
            // System.out.println("The " + gpu.getGpuName() + " of " + gpu.getCompany() + " costs: " + gpu.getPrice());
            context.output(context.element());
        }
    }


    public static void main(String[] args) {

        List<GraphicsCard> gpus = List.of(
                new GraphicsCard("gtx 1650", "Nvidia", 245, "Palit", 1645),
                new GraphicsCard("gtx 1050", "Nvidia", 190, "Palit", 1550),
                new GraphicsCard("rx 580", "Radeon", 280, "Msi", 1775)
        );

        try {

            // Java serialization coder for POJOs :  withCoder(SerializableCoder.of(Entity.class)
            // Text coder : setCoder(StringUtf8Coder.of())


            /*  This  is a valid pipeline  */

            PipelineOptions options = PipelineOptionsFactory.create();
            Pipeline pipeline = Pipeline.create(options);

            pipeline.apply(Create.of(gpus).withCoder(SerializableCoder.of(GraphicsCard.class)))
                    .apply(ParDo.of(new DisplayFn()))
                    //      .apply(ParDo.of(new FilterRadeonCardsFn()))
                    .apply(ParDo.of(new DisplayFn()))
                    .apply("Extract company", MapElements.via(new SimpleFunction<GraphicsCard, List<String>>() {
                        @Override
                        public List<String> apply(GraphicsCard element) {
                            return Collections.singletonList(element.getCompany());
                        }
                    }))
                    .apply(Count.perElement())
                    .apply(ParDo.of(new DoFn<KV<List<String>, Long>, Void>() {

                        Logger logger = LoggerFactory.getLogger(TestBeam.class);

                        @ProcessElement
                        public void processElement(ProcessContext context) {

                            logger.info(context.element().getKey().get(0) + " ==> " + context.element().getValue());
                            // System.out.println(context.element().getKey().get(0) + " ==> " + context.element().getValue());

                        }
                    }));

            pipeline.run();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
}
