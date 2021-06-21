package ch.aaap.assignment.boot;

import ch.aaap.assignment.data.*;
import ch.aaap.assignment.raw.*;
import ch.aaap.assignment.raw.CSVUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableSet;

public class ParallelModel extends ModelInitializer {


  public ParallelModel() {
    initModel();
  }

  protected void initModel() {

    var politicalCommunities = CSVUtil.getPoliticalCommunities();
    var postalCommunities = CSVUtil.getPostalCommunities();

    // Stage 1 - postalCommunities
    allPostalCommunities = postalCommunities.parallelStream()
            .map(p -> new PostalCommunity(p.zipCode(), p.zipCodeAddition(), p.name()))
            .collect(Collectors.toUnmodifiableSet());

    for (var politicalComm : politicalCommunities) {

      // Stage 2 - politicalCommunities
      var postalInPoliticalComm = postalCommunities.parallelStream()
              .filter(p -> p.politicalCommunityNumber().equalsIgnoreCase(politicalComm.number()))
              .map(p -> new PostalCommunity(p.zipCode(), p.zipCodeAddition(), p.name()))
              .collect(Collectors.toUnmodifiableSet());

      PoliticalCommunity selectedPoliticalComm = new PoliticalCommunity(politicalComm.number(), politicalComm.name(), politicalComm.shortName(), politicalComm.lastUpdate(), postalInPoliticalComm);
      allPoliticalCommunities.add(selectedPoliticalComm);

      extractDistrictsAndCantons(allDistricts, allCantons, politicalComm, selectedPoliticalComm);
    }

    model = new Model(unmodifiableSet(allPoliticalCommunities), allPostalCommunities, unmodifiableSet(allCantons), unmodifiableSet(allDistricts));
  }


  /**
   * extract all districts and cantons
   */
  protected void extractDistrictsAndCantons(Set<District> allDistricts, Set<Canton> allCantons, CSVPoliticalCommunity politicalComm, PoliticalCommunity selectedPoliticalComm) {
    // Stage 3 - Districts
    Predicate<District> IfDistrictExistStrategy = d -> d.number().equalsIgnoreCase(politicalComm.districtNumber());

    var districtExist = allDistricts.parallelStream().filter(IfDistrictExistStrategy).findFirst();
    if (districtExist.isEmpty()) {
      District d = new District(politicalComm.districtNumber(), politicalComm.districtName(), new HashSet<>());
      d.politicalCommunities().add(selectedPoliticalComm);
      allDistricts.add(d);

      extractDistricts(allCantons, politicalComm, d);
    } else
      districtExist.get().politicalCommunities().add(selectedPoliticalComm);
  }

  /**
   * extract all cantons
   */
  protected void extractDistricts(Set<Canton> allCantons, CSVPoliticalCommunity politicalComm, District d) {
    // Stage 4 - Cantons
    Predicate<Canton> CantonExistStrategy = c -> c.code().equalsIgnoreCase(politicalComm.cantonCode());

    var cantonExist = allCantons.parallelStream().filter(CantonExistStrategy).findFirst();
    if (cantonExist.isEmpty()) {
      Canton c = new Canton(politicalComm.cantonCode(), politicalComm.cantonName(), new HashSet<>());
      c.districts().add(d);
      allCantons.add(c);
    } else
      cantonExist.get().districts().add(d);
  }


  public Model getModel() {
    return model;
  }

}
