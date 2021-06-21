package ch.aaap.assignment.data;

import java.util.Set;

public record District(String number, String name, Set<PoliticalCommunity> politicalCommunities) {
}
