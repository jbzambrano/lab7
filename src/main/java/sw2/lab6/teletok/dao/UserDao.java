package sw2.lab6.teletok.dao;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class UserDao {


    public boolean autenticarUsuario(String username , String password) {

        RestTemplate restTemplate = new RestTemplate();

        HashMap<String,Object> responseMap = restTemplate.getForObject(
                "http://localhost:8080/clase11ej2ServidorRest/product/"+id,
                HashMap.class);

        if(responseMap.get("estado").equals("ok")){
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.convertValue(responseMap.get("producto"),Product.class);
            return product;
        }else{
            return null;
        }

    }



}
