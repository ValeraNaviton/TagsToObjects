package hatchwaystest.valeriun.javablog;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PostList {

    @JsonProperty(value = "posts")
    private List<Post> listOfPosts = new ArrayList<>();

    public PostList() { }

    public PostList(List<Post> listOfPosts) {
        this.listOfPosts = listOfPosts;
    }

    public List<Post> getListOfPosts() {
        return listOfPosts;
    }

    public void setListOfPosts(List<Post> posts) {
        this.listOfPosts = posts;
    }
}
