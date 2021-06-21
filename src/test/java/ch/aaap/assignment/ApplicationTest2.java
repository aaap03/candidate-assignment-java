package ch.aaap.assignment;


import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("ApplicationTest2 Class")
class ApplicationTest2 {
  static Application sut;
  static long startTestTime, endTestTime;

  @BeforeAll
  static void BeforeAll() {
    System.out.println("Before All tests 😬");
    startTestTime = System.nanoTime();

    String property = System.getProperty("java.version").split("\\.")[0];
    Assumptions.assumeTrue(Integer.parseInt(property) > 15);

    sut = new Application(true);
  }

  @BeforeEach
  void BeforeEach(TestInfo testName) {
    System.out.println("Before: " + testName.getTestMethod().get().getName() + " in " + this);
  }

  @AfterEach
  void AfterEach(TestInfo testName) {
    System.out.println("After: " + testName.getTestMethod().get().getName() + " in " + this);
  }

  @Test
  void testModel() {
    assertAll(
            () -> assertEquals(26, sut.getAmountOfCantons(), "Correct amount of cantons"),
            () -> assertEquals(143, sut.getAmountOfDistricts(), "Correct amount of districts"),
            () -> assertEquals(2215, sut.getAmountOfPoliticalCommunities(), "Correct amount of political communities"),
            () -> assertEquals(4064, sut.getAmountOfPostalCommunities(), "Correct amount of postal communities"));
  }

  @Test
  public void returnsCorrectAmountOfPoliticalCommunitiesInCanton() {
    assertAll(
            () -> assertEquals(162, sut.getAmountOfPoliticalCommunitiesInCanton("ZH"), "Correct amount of political communities in canton Zürich"),
            () -> assertEquals(20, sut.getAmountOfPoliticalCommunitiesInCanton("AR"), "Correct amount of political communities in canton Appenzell Innerrhoden"),
            () -> assertEquals(6, sut.getAmountOfPoliticalCommunitiesInCanton("AI"), "Correct amount of political communities in canton Appenzell Ausserrhoden"),
            () -> assertEquals(346, sut.getAmountOfPoliticalCommunitiesInCanton("BE"), "Correct amount of political communities in canton Bern")
    );
  }

  @Test
  public void returnsCorrectAmountOfDistrictsInCanton() {
    assertAll(
            () -> assertEquals(12, sut.getAmountOfDistrictsInCanton("ZH"), "Correct amount of districts in canton with short code 'ZH'"),
            () -> assertEquals(11, sut.getAmountOfDistrictsInCanton("GR"), "Correct amount of districts in canton with short code 'GR'"));
  }

  @Test
  public void returnAllDistrictsNamesInCanton() {
    List<String> expectedZH = List.of("Bezirk Horgen", "Bezirk Dielsdorf", "Bezirk Hinwil", "Bezirk Uster", "Bezirk Dietikon", "Bezirk Meilen", "Bezirk Zürich", "Bezirk Pfäffikon", "Bezirk Bülach", "Bezirk Andelfingen", "Bezirk Affoltern", "Bezirk Winterthur");
    List<String> expectedNE = List.of("Canton de Neuchâtel");
    List<String> expectedAR = List.of("Bezirk Vorderland", "Bezirk Hinterland", "Bezirk Mittelland");

    assertAll(
            () -> assertTrue(expectedZH.containsAll(sut.getAllDistrictsNamesInCanton("ZH"))),
            () -> assertTrue(expectedNE.containsAll(sut.getAllDistrictsNamesInCanton("NE"))),
            () -> assertTrue(expectedAR.containsAll(sut.getAllDistrictsNamesInCanton("AR"))),
            () -> assertNotSame(List.of("Bla bla"), sut.getAllDistrictsNamesInCanton("AR"))
    );
  }

  @Test
  public void returnsAmountOfPoliticalCommunitiesInDistrict() {
    assertEquals(14, sut.getAmountOfPoliticalCommunitiesInDistrict("101"), "Correct amount of political communities in in district with number '101'");
  }


  @Test
  public void returnsAllPostalCommunitiesInDistrict() {
    assertEquals(21, sut.getAllPostalCommunitiesInDistrict("101").size(), "Correct amount of postal communities in in district with number '101'");
  }

  @Test
  public void returnsCorrectDistrictNamesForZipCode() {
    assertAll(
            () -> assertEquals("Bezirk Bülach", sut.getDistrictsForZipCode("8305").iterator().next(), "Correct district name for zip code '8305'"),
            () -> assertEquals("Region Albula", sut.getDistrictsForZipCode("7457").iterator().next(), "Correct district name for zip code '7457'"),
            () -> assertEquals(0, sut.getDistrictsForZipCode("9999").size(), "Expected 0 results"),
            () -> assertEquals(2, sut.getDistrictsForZipCode("8866").size(), "Expected 2 results")
    );
  }

  @Test
  public void returnsCorrectLastUpdateOfMunicipalityByPostalCommunityName() {
    assertAll(
            () -> assertEquals("2016-04-10", sut.getLastUpdateOfPoliticalCommunityByPostalCommunityName("Vergeletto").toString(), "Correct last update of political community by postal community name 'Vergeletto'"),
            () -> assertEquals("2003-01-01", sut.getLastUpdateOfPoliticalCommunityByPostalCommunityName("Urnäsch").toString(), "Correct last update of political community by postal community name 'Urnäsch'"),
            () -> assertEquals("-999999999-01-01", sut.getLastUpdateOfPoliticalCommunityByPostalCommunityName("XXXX").toString(), "Correct last update of political community by postal community name 'XXXX' doesn't exist")
    );
  }

  @Test
  public void returnsAmountOfPoliticalCommunityWithoutPostalCommunities() {
    assertEquals(3, sut.getAmountOfPoliticalCommunityWithoutPostalCommunities(), "Correct amount of political communities without postal communities");
  }

  @Test
  public void returnsNamesOfPoliticalCommunityWithoutPostalCommunities() {
    var expectedRes = Set.of("C'za Cadenazzo/Monteceneri", "C'za Capriasca/Lugano", "Staatswald Galm");
    var result = sut.getPoliticalCommunityNamesWithoutPostalCommunities();
    assertTrue(result.containsAll(expectedRes));

  }

  @Test
  public void returnsAmountOfPoliticalCommunityWithMoreThanAmountOfPostalCommunities() {
    assertEquals(289, sut.getAmountOfPoliticalCommunityWithMoreThanAmountOfPostalCommunities(5), "Correct amount of political communities without postal communities");
  }


  @Test
  @Deprecated
  void returnAmountOfPostalCommunitiesInCanton() {
    assertAll(
            () -> assertEquals(590, sut.getAmountOfPostalCommunitiesInCanton("BE"), "Correct amount of postal communities in Bern"),
            () -> assertEquals(302, sut.getAmountOfPostalCommunitiesInCanton("ZH"), "Correct amount of postal communities in Zürich"),
            () -> assertEquals(590, sut.getAmountOfPostalCommunitiesInCanton("BE"), "Correct amount of postal communities in Bern"),
            () -> assertEquals(318, sut.getAmountOfPostalCommunitiesInCanton("TI"), "Correct amount of postal communities in Ticino"),
            () -> assertEquals(13, sut.getAmountOfPostalCommunitiesInCanton("BS"), "Correct amount of postal communities in Basel-Stadt"),
            () -> assertEquals(30, sut.getAmountOfPostalCommunitiesInCanton("UR"), "Correct amount of postal communities in Uri"),
            () -> assertEquals(267, sut.getAmountOfPostalCommunitiesInCanton("AG"), "Correct amount of postal communities in Aargau")
    );
  }

  @Nested
  class ExceptionHandeling {

    @Test
    public void returnsCorrectAmountOfPoliticalCommunitiesInCanton() {
      assertThrows(IllegalArgumentException.class, () -> sut.getAmountOfPoliticalCommunitiesInCanton("XX"), "Expected IllegalArgumentException");
    }

    @Test
    public void returnsCorrectAmountOfDistrictsInCanton() {
      assertThrows(IllegalArgumentException.class, () -> sut.getAmountOfDistrictsInCanton("XX"), "Expected IllegalArgumentException");
    }

    @Test
    public void returnsAmountOfPoliticalCommunitiesInDistrict() {
      assertThrows(IllegalArgumentException.class, () -> sut.getAmountOfPoliticalCommunitiesInDistrict("9999"), "Expected IllegalArgumentException");
    }
  }


  @AfterAll
  static void AfterAll() {
    endTestTime = System.nanoTime();
    System.out.println("Time taken: " + (endTestTime - startTestTime) / 1.0e9);
    System.out.println("All tests pass 😎");
  }


}

