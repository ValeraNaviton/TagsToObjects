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
    private RequestValidator validator;

    @Autowired
    private PostSorter postSorter;

    @Autowired
    private Deduplicator deduplicator;

    @Autowired
    private CachedTagLoaders cachedLoaders;

    @GetMapping(value = "/api/posts", produces = "application/json")
    String findAll(HttpServletResponse httpResponse,
                   @RequestParam(required = false) String tags,
                   @RequestParam(required = false, defaultValue = "id") String sortBy,
                   @RequestParam(required = false, defaultValue = "asc") String direction
    ) {

        String errorBody = validator.validateRequest(tags, sortBy, direction);

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
        allPosts = deduplicator.removeDuplicates(allPosts);
        postSorter.sortPosts(allPosts, sortBy, direction);
        try {
            return new ObjectMapper().writeValueAsString(new PostList(allPosts));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }
}
