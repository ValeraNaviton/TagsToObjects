package hatchwaystest.valeriun.javablog.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hatchwaystest.valeriun.javablog.PostList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class CachedTagLoaders {
    @Autowired
    private RemoteLoader hatchwaysLoader;

    private final ExecutorService executorService = Executors.newFixedThreadPool(32);


    public CachedTagLoaders(RemoteLoader hatchwaysLoader) {
        this.hatchwaysLoader = hatchwaysLoader;
    }

    private final CacheLoader<String, PostList> loader = new CacheLoader<String, PostList>() {
        @Override
        public PostList load(final String tag) {
            return hatchwaysLoader.loadByTag(tag);
        }
    };

    private final LoadingCache<String, PostList> cache = CacheBuilder.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(loader);

    private Future<PostList> getPostsByTag(String tag) {
        return executorService.submit(() -> cache.get(tag));
    }

    public List<Future<PostList>> spawnLoaders(List<String> tagList) {
        List<Future<PostList>> result = new ArrayList<>();
        for (String tag : tagList) {
            Future<PostList> promise = getPostsByTag(tag);
            result.add(promise);
        }
        return result;
    }
}
