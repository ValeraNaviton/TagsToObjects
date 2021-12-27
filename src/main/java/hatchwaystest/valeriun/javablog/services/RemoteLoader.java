package hatchwaystest.valeriun.javablog.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import hatchwaystest.valeriun.javablog.PostList;

import java.util.ArrayList;

@Service
public class RemoteLoader {
    public PostList loadByTag(String tag) {
        RestTemplate restTemplate = new RestTemplate();
        String hatchwaysEndPoint = "https://api.hatchways.io/assessment/blog/posts?tag=" + tag;
        ResponseEntity<String> response = restTemplate.getForEntity(hatchwaysEndPoint, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.getBody(), PostList.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.err.println("Couldn't make a record out of given tag");
        }
        return new PostList(new ArrayList<>());
    }

}
