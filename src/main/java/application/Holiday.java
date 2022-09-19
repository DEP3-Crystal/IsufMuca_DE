package application;

import client.ApiClient;
import client.ApiException;
import client.api.PublicHolidayApi;
import client.model.PublicHolidayV3Dto;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Holiday {

    @Builder(toBuilder = true)
    @Data
    static class CountryInfo {
        private String name;
        private String code;
        private int numberOfHolidays;
    }


    public static void main(String[] args) throws IOException, ApiException {
        // Common pattern in java IS
        // 1 Open resource (FILE, SOCKET, STREAM)
        // Use it
        // CLOSE IT
        // Res r1 = openRes();
        // try {
        //    r1.xx();
        //    ..
        // } finally {
        //   r1.close();
        // }
        // try (Res r1=openRes()) {
        //  ... use r1
        // }
        InputStream is = Holiday.class.getResourceAsStream("/cc.csv");
        try (CSVParser csvParser = new CSVParser(new InputStreamReader(is, StandardCharsets.UTF_8), CSVFormat.DEFAULT)) {
            List<CountryInfo> countries = csvParser.stream()
                    .map(csvRecord -> CountryInfo.builder()
                            .name(csvRecord.get(0))
                            .code(csvRecord.get(1))
                            .build())
                    .collect(Collectors.toList());
            System.out.println(countries);
        }

        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("https://date.nager.at");
        PublicHolidayApi publicHolidayApi = new PublicHolidayApi(apiClient);
        List<PublicHolidayV3Dto> res = publicHolidayApi.publicHolidayPublicHolidaysV3(2022, "AL");
        System.out.println(res);


        /*              My part of the code            */

        Integer year = 2022;

        InputStream file = Holiday.class.getResourceAsStream("/cc.csv");
        try (CSVParser csvParser = new CSVParser(new InputStreamReader(file, StandardCharsets.UTF_8), CSVFormat.DEFAULT)) {
            List<CountryInfo> countries = csvParser.stream()
                    .map(csvRecord -> CountryInfo.builder()
                            .name(csvRecord.get(0))
                            .code(csvRecord.get(1))
                            .build())
                    .collect(Collectors.toList());

           //getting values of holiday count
            countries = countries.stream().parallel().map(country -> {
                try {
                    List<PublicHolidayV3Dto> holidays = publicHolidayApi.publicHolidayPublicHolidaysV3(year, country.code);
                    country.setNumberOfHolidays(holidays.size());
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }
                return country;
            }).collect(Collectors.toList());

         //   System.out.println(countries);

            Optional<CountryInfo> min= countries.stream().parallel().min((o1,o2)->o1.getNumberOfHolidays() - o2.getNumberOfHolidays());

            Optional<CountryInfo> max = countries.stream().parallel().max((o1,o2)->o1.getNumberOfHolidays() - o2.getNumberOfHolidays());


            System.out.println("The minimum number of holidays is: "+min.get().getNumberOfHolidays()+" in country of "+min.get().getName());
            System.out.println("The maximum number of holidays is: "+max.get().getNumberOfHolidays()+" in country of "+max.get().getName());

        }
    }
}
