package ch.aaap.assignment.boot;

import ch.aaap.assignment.data.*;
import ch.aaap.assignment.raw.*;

import java.util.HashSet;
import java.util.Set;

public abstract class ModelInitializer {

  protected static Model model = null;    //supplier

  static Set<Canton> allCantons = new HashSet<>();
  static Set<District> allDistricts = new HashSet<>();
  static Set<PoliticalCommunity> allPoliticalCommunities = new HashSet<>();
  static Set<PostalCommunity> allPostalCommunities = new HashSet<>();

  public abstract Model getModel();

  protected abstract void extractDistrictsAndCantons(Set<District> allDistricts, Set<Canton> allCantons, CSVPoliticalCommunity politicalComm, PoliticalCommunity selectedPoliticalComm);

  protected abstract void extractDistricts(Set<Canton> allCantons, CSVPoliticalCommunity politicalComm, District d);

}
