package hatchwaystest.valeriun.javablog;

import hatchwaystest.valeriun.javablog.endpoints.QueryController;
import hatchwaystest.valeriun.javablog.services.CachedTagLoaders;
import hatchwaystest.valeriun.javablog.services.RemoteLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;




@Configuration
@EnableWebMvc
public class TestConfiguration implements WebMvcConfigurer {

    @Autowired
    private ServletContext ctx;

    @Bean
    public CachedTagLoaders getCachedLoaders() {
        return new CachedTagLoaders(new RemoteLoader());
    }

    @Bean
    public RemoteLoader getTagLoaders() {
        return new RemoteLoader();
    }

    @Bean
    public QueryController getControllerLoader() {
        return new QueryController(new CachedTagLoaders(new RemoteLoader()));
    }

}