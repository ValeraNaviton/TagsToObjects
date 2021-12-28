package hatchwaystest.valeriun.javablog.endpoints;

import hatchwaystest.valeriun.javablog.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Deduplicator {

    public List<Post> removeDuplicates(List<Post> bag) {
        List<Post> result = new ArrayList<>();
        for (Post element : bag) {
            if (!result.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }

}
