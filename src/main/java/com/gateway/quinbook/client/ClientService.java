package  com.gateway.quinbook.client;
import com.gateway.quinbook.dto.RegisterRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "register-details", url = "http://10.177.68.6:8081")
public interface ClientService {
    @RequestMapping(method= RequestMethod.POST, path="/register")
    void registerUser(@RequestBody RegisterRequestDTO registerRequestDTO);

}
