package ch.aaap.assignment.raw;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.*;

import ch.aaap.assignment.data.*;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import static java.util.Collections.unmodifiableSet;


public class CSVUtil {

  private static final String POLITICAL_COMMUNITY_FILE = "/GDE_from_be-b-00.04-agv-01.xlsx.csv";
  private static final String POSTAL_COMMUNITY_FILE = "/PLZ6_from_do-t-09.02-gwr-37.xlsx.csv";

  private CSVUtil() {
  }

  public static Set<CSVPoliticalCommunity> getPoliticalCommunities() {
    try {
      InputStream is = CSVUtil.class.getResourceAsStream(POLITICAL_COMMUNITY_FILE);
      Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
      CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

      Set<CSVPoliticalCommunity> data = new HashSet<>();

      for (final CSVRecord record : parser) {
        data.add(new CSVPoliticalCommunity(record.get("GDENR"),
                record.get("GDENAME"),
                record.get("GDENAMK"),
                record.get("GDEKT"),
                record.get("GDEKTNA"),
                record.get("GDEBZNR"),
                record.get("GDEBZNA"),
                LocalDate.parse(record.get("GDEMUTDAT"), formatter)));
      }
      parser.close();
      return unmodifiableSet(data);
    } catch (IOException e) {
      throw new RuntimeException("Could not parse political communities csv", e);
    }
  }

  public static Set<CSVPostalCommunity> getPostalCommunities() {

    try {
      InputStream is = CSVUtil.class.getResourceAsStream(POSTAL_COMMUNITY_FILE);
      Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
      CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());

      Set<CSVPostalCommunity> data = new HashSet<>();

      for (final CSVRecord record : parser) {
        data.add(new CSVPostalCommunity(record.get("PLZ4"),
                record.get("PLZZ"),
                record.get("PLZNAMK"),
                record.get("KTKZ"),
                record.get("GDENR"),
                record.get("GDENAMK")));
      }

      parser.close();
      return unmodifiableSet(data);
    } catch (IOException e) {
      throw new RuntimeException("could not parse postal community csv", e);
    }
  }


  public static void generateCSVFileFromObject(Set<Canton> canton, String fileName) {
    @JsonPropertyOrder({"code", "name", "numOfDistricts"})
    record CSVCanton(String code, String name, Integer numOfDistricts) {
    }
    File csvOutfile = new File(fileName);

    List<CSVCanton> csvWriteData = canton.stream()
            .map(c -> new CSVCanton(c.code(), c.name(), c.districts().size())).toList();

    var mapper = new CsvMapper();
    mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

    CsvSchema schema = CsvSchema.builder().setUseHeader(true)
            .addColumn("code")
            .addColumn("name")
            .addColumn("numOfDistricts").build();

    ObjectWriter writer = mapper.writerFor(CSVCanton.class).with(schema);
    try {
      writer.writeValues(csvOutfile).writeAll(csvWriteData);
    } catch (IOException e) {
      System.out.println(e);
    }


  }





  public static void generateJsonFromCsv(File file) {

    @JsonPropertyOrder({"code", "name", "numOfDistricts"})
    record CSVCanton(String code, String name, Integer numOfDistricts) {
    }

    CsvSchema schema = CsvSchema.emptySchema().withHeader();
    CsvMapper csvMapper = new CsvMapper();
    ObjectReader oReader = csvMapper.readerFor(CSVCanton.class).with(schema);
    try {
      MappingIterator<CSVCanton> cantonsCsvContent = oReader.readValues(file);
      new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true)
              .writeValue(new File("src/main/resources/cantons_new.json"), cantonsCsvContent.readAll());
      System.out.println(cantonsCsvContent.readAll());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }







}

