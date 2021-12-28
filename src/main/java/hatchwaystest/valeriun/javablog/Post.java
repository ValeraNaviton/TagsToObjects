package hatchwaystest.valeriun.javablog;


import lombok.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id"})
public class Post {

    private String author;
    private int authorId;
    private int id;
    private int likes;
    private double popularity;
    private int reads;
    private List<String> tags;

}
