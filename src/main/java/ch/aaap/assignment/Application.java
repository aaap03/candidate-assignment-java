package ch.aaap.assignment;

import ch.aaap.assignment.boot.*;
import ch.aaap.assignment.data.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


import static java.util.stream.Collectors.*;

// JDK 16
public class Application {

  private final Model model;
  private static final Boolean sequential = true;

  public Application(Boolean isParallel) {
    ModelInitializer modelInit;
    modelInit = isParallel ? new ParallelModel() : new ConcurrentModel();
    model = modelInit.getModel();
  }

  public static void main(String[] args) {
    new Application(sequential);
  }

  /**
   * @return model
   */
  public Model getModel() {
    return model;
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of political communities in given canton
   */
  public long getAmountOfPoliticalCommunitiesInCanton(String cantonCode) {
    var canton = getCantonByCantonCode(cantonCode);
    return canton.districts().stream()
            .mapToInt(e -> e.politicalCommunities().size())
            .sum();
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return amount of districts in given canton
   */
  public long getAmountOfDistrictsInCanton(String cantonCode) {
    var canton = getCantonByCantonCode(cantonCode);
    return canton.districts().size();
  }

  /**
   * @param cantonCode of a canton (e.g. ZH)
   * @return list of districts names in a given canton
   */
  public List<String> getAllDistrictsNamesInCanton(String cantonCode) {
    var canton = getCantonByCantonCode(cantonCode);
    return canton.districts().stream().map(District::name).toList();
  }


  /**
   * @param number district number
   * @return list of postal names in a given district
   */
  public List<String> getAllPostalCommunitiesInDistrict(String number) {
    return model.districts().stream()
            .filter(d -> d.number().equalsIgnoreCase(number))
            .map(District::politicalCommunities)
            .flatMap(p -> p.stream().map(PoliticalCommunity::postalCommunities)).toList()
            .stream().flatMap(Collection::stream)
            .map(PostalCommunity::name)
            .distinct().toList();


  }

  /**
   * @param districtNumber of a district (e.g. 101)
   * @return amount of districts in given canton
   */
  public long getAmountOfPoliticalCommunitiesInDistrict(String districtNumber) {

    Optional<Set<PoliticalCommunity>> politicalComm = model.districts().stream()
            .filter(d -> d.number().equalsIgnoreCase(districtNumber))
            .findFirst()
            .map(District::politicalCommunities);
    if (politicalComm.isPresent())
      return politicalComm.get().size();

    throw new IllegalArgumentException("District number not valid: " + districtNumber);
  }

  /**
   * @param zipCode 4 digit zip code
   * @return district that belongs to specified zip code
   */
  public Set<String> getDistrictsForZipCode(String zipCode) {
    return model.districts().stream().filter(d -> d.politicalCommunities().stream()
            .anyMatch(political -> political.postalCommunities().stream()
                    .anyMatch(postal -> postal.zipCode().equalsIgnoreCase(zipCode))))
            .map(District::name)
            .collect(toUnmodifiableSet());
  }

  /**
   * @param postalCommunityName name
   * @return lastUpdate of the political community by a given postal community name
   */
  public LocalDate getLastUpdateOfPoliticalCommunityByPostalCommunityName(String postalCommunityName) {
    return model.politicalCommunities().stream()
            .filter(political -> political.postalCommunities().stream()
                    .anyMatch(postal -> postal.name().equals(postalCommunityName)))
            .findFirst()
            .map(PoliticalCommunity::lastUpdate)
            .orElse(LocalDate.MIN);
  }

  /**
   * https://de.wikipedia.org/wiki/Kanton_(Schweiz)
   *
   * @return amount of canton
   */
  public long getAmountOfCantons() {
    return model.cantons().size();
  }

  /**
   * getAmountOfDistricts
   */
  public long getAmountOfDistricts() {
    return model.districts().size();
  }

  /**
   * getAmountOfPoliticalCommunities
   */
  public long getAmountOfPoliticalCommunities() {
    return model.politicalCommunities().size();
  }

  /**
   * getAmountOfPostalCommunities
   */
  public long getAmountOfPostalCommunities() {
    return model.postalCommunities().size();
  }

  /**
   * https://de.wikipedia.org/wiki/Kommunanz
   *
   * @return amount of political communities without postal communities
   */
  public long getAmountOfPoliticalCommunityWithoutPostalCommunities() {
    return model.politicalCommunities().stream().filter(p -> p.postalCommunities().size() == 0).count();
  }

  /**
   * return political communities names that don't have postal communities
   */
  public Set<String> getPoliticalCommunityNamesWithoutPostalCommunities() {
    return model.politicalCommunities().stream()
            .filter(p -> p.postalCommunities().size() == 0)
            .map(PoliticalCommunity::name)
            .collect(Collectors.toUnmodifiableSet());
  }

  public long getAmountOfPoliticalCommunityWithMoreThanAmountOfPostalCommunities(int amount) {
    return model.politicalCommunities().stream().filter(p -> p.postalCommunities().size() >= amount).count();
  }

  public long getAmountOfPostalCommunitiesInCanton(String cantonCode) {
    Set<PostalCommunity> AllPostal = getCantonByCantonCode(cantonCode)
            .districts().stream()
            .flatMap(d -> d.politicalCommunities().stream().map(PoliticalCommunity::postalCommunities))
            .flatMap(Collection::stream)
            .collect(toUnmodifiableSet());

    return AllPostal.size();
  }




  private Canton getCantonByCantonCode(String cantonCode) {
    var canton = model.cantons().stream().filter(c -> c.code().equalsIgnoreCase(cantonCode)).findFirst();

    if (canton.isPresent())
      return canton.get();
    throw new IllegalArgumentException("Canton code is not valid: " + cantonCode);

  }


}
