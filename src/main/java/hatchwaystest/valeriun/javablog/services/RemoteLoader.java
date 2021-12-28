package hatchwaystest.valeriun.javablog.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import hatchwaystest.valeriun.javablog.PostList;

@Service
public class RemoteLoader {
    public PostList loadByTag(String tag) throws RemoteServerAccessException, InvalidRemoteServerDataException{
        RestTemplate restTemplate = new RestTemplate();
        String hatchwaysEndPoint = "https://api.hatchways.io/assessment/blog/posts?tag=" + tag;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(hatchwaysEndPoint, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.getBody(), PostList.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InvalidRemoteServerDataException(e);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RemoteServerAccessException(e);
        }
    }

    public static class RemoteServerAccessException extends Exception {
        public RemoteServerAccessException(Throwable cause) {
            super(cause);
        }
    }

    public static class InvalidRemoteServerDataException extends Exception {
        public InvalidRemoteServerDataException(Throwable cause) {
            super(cause);
        }
    }

}
