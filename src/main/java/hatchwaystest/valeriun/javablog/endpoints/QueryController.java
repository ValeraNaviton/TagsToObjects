package hatchwaystest.valeriun.javablog.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import hatchwaystest.valeriun.javablog.Post;
import hatchwaystest.valeriun.javablog.PostList;
import hatchwaystest.valeriun.javablog.services.CachedTagLoaders;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.*;

@RestController
public class QueryController {

    public QueryController(CachedTagLoaders cachedLoaders) {
        this.cachedLoaders = cachedLoaders;
    }

    @Autowired
    private CachedTagLoaders cachedLoaders;

    @GetMapping(value = "/api/posts", produces = "application/json")
    String findAll(HttpServletResponse httpResponse,
                   @RequestParam(required = false) String tags,
                   @RequestParam(required = false, defaultValue = "id") String sortBy,
                   @RequestParam(required = false, defaultValue = "asc") String direction
    ) {
        String errorBody = validateRequest(tags, sortBy, direction);

        if (errorBody != null) {
            httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return errorBody;
        }

        List<String> tagArray = Arrays.asList(tags.split(","));

        List<Future<PostList>> promises = cachedLoaders.spawnLoaders(tagArray);
        List<Post> allPosts = new ArrayList<>();

        for (Future<PostList> promise : promises) {
            try {
                allPosts.addAll(promise.get().getListOfPosts());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        allPosts = removeDuplicates(allPosts);
        sortPosts(allPosts, sortBy, direction);
        try {
            return new ObjectMapper().writeValueAsString(new PostList(allPosts));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }

    // if return null - no errors found. Otherwise - body of error.
    private String validateRequest(String tags, String sortBy, String direction) {
        if (tags == null || tags.isEmpty()) {
            return "{\"error\":\"Tags parameter is required\"}";
        }
        Set<String> validDirections = new HashSet<>(Arrays.asList("desc", "asc"));
        if (direction.isEmpty() || !validDirections.contains(direction)) {
            return "{\"error\":\"direction parameter is invalid\"}";
        }
        Set<String> validSorts = new HashSet<>(Arrays.asList("likes", "popularity", "reads", "id"));
        if (sortBy.isEmpty() || !validSorts.contains(sortBy)) {
            return "{\"error\":\"sortBy parameter is invalid\"}";
        }
        return null;
    }

    private List<Post> removeDuplicates(List<Post> unfiltered) {
        List<Post> result = new ArrayList<>();
        for (Post element : unfiltered) {
            if (!result.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }

    private void sortPosts(List<Post> unsorted, String sortBy, String direction) {
        unsorted.sort((o1, o2) -> {
            int ascResult = 0;
            switch (sortBy) {
                case "likes":
                    ascResult = Integer.compare(o1.getLikes(), o2.getLikes());
                    break;
                case "popularity":
                    ascResult = Double.compare(o1.getPopularity(), o2.getPopularity());
                    break;
                case "reads":
                    ascResult = Integer.compare(o1.getReads(), o2.getReads());
                    break;
                case "id":
                    ascResult = Integer.compare(o1.getId(), o2.getId());
                    break;
            }
            if (direction.equals("asc")) {
                return ascResult;
            } else {
                return -ascResult;
            }
        });
    }
}
