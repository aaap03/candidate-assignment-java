package ch.aaap.assignment.data;

import java.util.Set;

public record Canton(String code, String name, Set<District> districts) {
}
