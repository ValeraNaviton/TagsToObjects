package hatchwaystest.valeriun.javablog.endpoints;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class RequestValidator {

    public String validateRequest(String tagList, String sortingAttribute, String direction) {
        if (tagList == null || tagList.isEmpty()) {
            return "{\"error\":\"Tags parameter is required\"}";
        }
        Set<String> validDirections = new HashSet<>(Arrays.asList("desc", "asc"));
        if (direction.isEmpty() || !validDirections.contains(direction)) {
            return "{\"error\":\"direction parameter is invalid\"}";
        }
        Set<String> validSorts = new HashSet<>(Arrays.asList("likes", "popularity", "reads", "id"));
        if (sortingAttribute.isEmpty() || !validSorts.contains(sortingAttribute)) {
            return "{\"error\":\"sortBy parameter is invalid\"}";
        }
        return null;
    }

}
