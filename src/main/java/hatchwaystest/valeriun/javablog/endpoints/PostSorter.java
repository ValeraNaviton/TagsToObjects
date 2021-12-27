package hatchwaystest.valeriun.javablog.endpoints;

import hatchwaystest.valeriun.javablog.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostSorter {

    public void sortPosts(List<Post> unsorted, String sortBy, String direction) {
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
