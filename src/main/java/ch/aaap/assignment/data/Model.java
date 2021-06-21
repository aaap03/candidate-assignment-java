package ch.aaap.assignment.data;

import java.util.Set;

public record Model(Set<PoliticalCommunity> politicalCommunities,
                    Set<PostalCommunity> postalCommunities,
                    Set<Canton> cantons,
                    Set<District> districts) {
}
