package hatchwaystest.valeriun.javablog.endpoints;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class PingController {

    @GetMapping(value = "/api/ping", produces ="application/json")
    @ResponseStatus(HttpStatus.OK)
    String ping() {
        return "{\"success\":true}";
    }

}
