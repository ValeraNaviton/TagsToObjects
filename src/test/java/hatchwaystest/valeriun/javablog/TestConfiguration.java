package hatchwaystest.valeriun.javablog;

import hatchwaystest.valeriun.javablog.endpoints.Deduplicator;
import hatchwaystest.valeriun.javablog.endpoints.PostSorter;
import hatchwaystest.valeriun.javablog.endpoints.QueryController;
import hatchwaystest.valeriun.javablog.endpoints.RequestValidator;
import hatchwaystest.valeriun.javablog.services.CachedTagLoaders;
import hatchwaystest.valeriun.javablog.services.RemoteLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Arrays;


@Configuration
@EnableWebMvc
public class TestConfiguration implements WebMvcConfigurer {

    private static RemoteLoader TEST_LOADER = new RemoteLoader() {
        @Override
        public PostList loadByTag(String tag) {
            switch (tag) {
                case "science":
                    return new PostList(Arrays.asList(
                            new Post("Isaac Newton", 1, 1, 12, 4.99, 1000, Arrays.asList("science", "math", "technology")),
                            new Post("Albert Einstein", 2, 2, 13, 4.98, 1001, Arrays.asList("science", "math", "technology"))
                    ));
                case "math":
                    return new PostList(Arrays.asList(
                            new Post("Isaac Newton", 1, 1, 12, 4.99, 1000, Arrays.asList("science", "math", "technology")),
                            new Post("Albert Einstein", 2, 2, 13, 4.98, 1001, Arrays.asList("science", "math", "technology"))
                    ));
                case "startups":
                    return new PostList(Arrays.asList(
                            new Post("Elon Musk", 11, 11, 2, 1.0, 1, Arrays.asList("startups", "technology")),
                            new Post("John Doe", 13, 13, 2, 1.0, 1, Arrays.asList("startups")),
                            new Post("Jeff Bezos", 12, 12, 1, 0.99, 0, Arrays.asList("startups", "technology"))
                    ));
                case "technology":
                    return new PostList(Arrays.asList(
                            new Post("Isaac Newton", 1, 1, 12, 4.99, 1000, Arrays.asList("science", "math", "technology")),
                            new Post("Albert Einstein", 2, 2, 13, 4.98, 1001, Arrays.asList("science", "math", "technology"))
                    ));
                default:
                    return new PostList(new ArrayList<>());
            }
        }
    };

    @Autowired
    private ServletContext ctx;

    @Bean
    public CachedTagLoaders getCachedLoaders() {
        return new CachedTagLoaders(TEST_LOADER);
    }

    @Bean
    public Deduplicator getDeduplicator() {
        return new Deduplicator();
    }

    @Bean
    public PostSorter getSorter() {
        return new PostSorter();
    }

    @Bean
    public RequestValidator geRequestValidator() {
        return new RequestValidator();
    }

    @Bean
    public RemoteLoader buildTestLoader() {
        return TEST_LOADER;
    }

    @Bean
    public QueryController getControllerLoader() {
        return new QueryController();
    }

}