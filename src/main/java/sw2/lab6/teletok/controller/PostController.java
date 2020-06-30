package sw2.lab6.teletok.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sw2.lab6.teletok.entity.Post;
import sw2.lab6.teletok.repository.PostRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    PostRepository postRepository;

    @GetMapping(value = {"", "/"})
    public String listPost(){
        return "post/list";
    }

    @GetMapping("/post/new")
    public String newPost(){
        return "post/new";
    }

    @PostMapping("/post/save")
    public String savePost() {
        return "redirect:/";
    }

    @GetMapping("/post/file/{media_url}")
    public String getFile() {
        return "";
    }

    @ResponseBody
    @GetMapping("/post/{id}}")
    public ResponseEntity viewPost(@PathVariable("id") String idStr,
                           @RequestParam(value = "token",required = false) String tok) {

        HashMap <String,Object> hashMap = new HashMap<>();
        HttpStatus estadoHTTP = HttpStatus.BAD_REQUEST;

        try{

            Integer idUser = Integer.parseInt(idStr);
            Integer token = Integer.parseInt(tok);

            List<Post> postList = postRepository.buscarPostPorIdPostyToken(token,idUser);

            estadoHTTP = HttpStatus.OK;

            return new ResponseEntity(postList,estadoHTTP);

        }catch (NumberFormatException e){

            hashMap.put("estado","error");
            hashMap.put("msg","El id y el token deben ser numeros!!!");

            return new ResponseEntity(hashMap, estadoHTTP);

        }



    }

    @PostMapping("/post/comment")
    public String postComment() {
        return "";
    }

    @PostMapping("/post/like")
    public String postLike() {
        return "";
    }


    @ResponseBody
    @GetMapping(value = "/product/list",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity obtenerProductos(@RequestParam("query") String query){

        HashMap<String,Object> hashMap = new HashMap<>();
        HttpStatus estadoHTTP = HttpStatus.BAD_REQUEST;




        if(query=="" || query==null){

            estadoHTTP = HttpStatus.OK;

            return new ResponseEntity(postRepository.findAll(), estadoHTTP);

        }else{

            List<Post> postList = postRepository.buscarPostPorPedazoDeNombre(query,query);

            if(postList.isEmpty()){

                Post[] posts = {};
                return new ResponseEntity(posts, estadoHTTP);


            }else{

                estadoHTTP = HttpStatus.OK;

                return new ResponseEntity(postList,estadoHTTP);

            }
        }
    }
}
