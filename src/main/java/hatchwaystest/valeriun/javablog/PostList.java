package hatchwaystest.valeriun.javablog;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostList {
    @JsonProperty(value = "posts")
    private List<Post> listOfPosts = new ArrayList<>();
}
