package hatchwaystest.valeriun.javablog;

import java.util.List;
import java.util.Objects;

public class Post {

    private String author;
    private int authorId;
    private int id;
    private int likes;
    private double popularity;
    private int reads;
    private List<String> tags;

    public Post(String author, int authorId, int id, int likes, double popularity, int reads, List<String> tags) {
        this.author = author;
        this.authorId = authorId;
        this.id = id;
        this.likes = likes;
        this.popularity = popularity;
        this.reads = reads;
        this.tags = tags;
    }

    public Post() {
    }

    @Override
    public String toString() {

        return "Post{ id=" + id + '}';
    }

    public String getAuthor() {

        return author;
    }

    public void setAuthor(String author) {

        this.author = author;
    }

    public int getAuthorId() {

        return authorId;
    }

    public void setAuthorId(int authorId) {

        this.authorId = authorId;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getLikes() {

        return likes;
    }

    public void setLikes(int likes) {

        this.likes = likes;
    }

    public double getPopularity() {

        return popularity;
    }

    public void setPopularity(double popularity) {

        this.popularity = popularity;
    }

    public int getReads() {

        return reads;
    }

    public void setReads(int reads) {

        this.reads = reads;
    }

    public List<String> getTags() {

        return tags;
    }

    public void setTags(List<String> tags) {

        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
