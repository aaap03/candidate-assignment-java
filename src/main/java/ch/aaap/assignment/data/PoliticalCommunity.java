package ch.aaap.assignment.data;

import java.time.LocalDate;
import java.util.Set;

public record PoliticalCommunity(String number,
                                 String name,
                                 String shortName,
                                 LocalDate lastUpdate,
                                 Set<PostalCommunity> postalCommunities) {
}

