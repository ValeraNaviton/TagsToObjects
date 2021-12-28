package hatchwaystest.valeriun.javablog.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hatchwaystest.valeriun.javablog.services.RemoteLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import hatchwaystest.valeriun.javablog.Post;
import hatchwaystest.valeriun.javablog.PostList;
import hatchwaystest.valeriun.javablog.services.CachedTagLoaders;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@RestController
public class QueryController {

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

        String errorText = validator.validateRequest(tags, sortBy, direction);

        if (errorText != null) {
            return reportServerError(httpResponse, errorText);
        }

        try {
            List<Post> allPosts = cachedLoaders.getByTagList(Arrays.asList(tags.split(",")));
            allPosts = deduplicator.removeDuplicates(allPosts);
            postSorter.sortPosts(allPosts, sortBy, direction);

            return new ObjectMapper().writeValueAsString(new PostList(allPosts));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return reportServerError(httpResponse, "unknown exception occurred: " + e.getMessage());
        } catch (RemoteLoader.InvalidRemoteServerDataException e) {
            e.printStackTrace();
            return reportServerError(httpResponse, "failed to parse 3rd party server data: " + e.getMessage());
        } catch (RemoteLoader.RemoteServerAccessException e) {
            e.printStackTrace();
            return reportServerError(httpResponse, "failed to call 3rd party server: " + e.getMessage());
        }
    }

    private String reportServerError(HttpServletResponse httpResponse, String body) {
        httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return body;
    }
}
