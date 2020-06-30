package sw2.lab6.teletok.controller;


import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
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
import sw2.lab6.teletok.entity.PostComment;
import sw2.lab6.teletok.entity.PostLike;
import sw2.lab6.teletok.entity.Token;
import sw2.lab6.teletok.repository.PostCommentRepository;
import sw2.lab6.teletok.repository.PostLikeRepository;
import sw2.lab6.teletok.repository.PostRepository;
import sw2.lab6.teletok.repository.TokenRepository;

import java.util.HashMap;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    PostRepository postRepository;

    TokenRepository tokenRepository;


    @Autowired
    PostCommentRepository postCommentRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

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
    @PostMapping(value="/ws/post/comment",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity comentarPost(@RequestParam(value = "token", required = true) String token,
                                       @RequestParam(value = "postId", required = true) int postId,
                                       @RequestParam(value = "message", required = true) String message){

        HashMap<String, Object> responseMap = new HashMap<>();

        Optional<Token> tokenValidacion = Optional.ofNullable(tokenRepository.findByCode(token));
        Optional<Post> postValidacion = postRepository.findById(postId);

        Post post = postValidacion.get();

        Optional<PostComment> commentValidacion = Optional.ofNullable(postCommentRepository.findByPostId(postId));

        PostComment postCommentValidacion = commentValidacion.get();

        if (tokenValidacion.isPresent()){
            if(postRepository.existsById(postId)){
                postCommentValidacion.setMessage(message);
                postCommentRepository.save(postCommentValidacion);
                responseMap.put("status", "COMMENT_CREATED");
                responseMap.put("commentId", post.getId());
                return new ResponseEntity(responseMap, HttpStatus.OK);
            }else{
                responseMap.put("error","POST_NOT_FOUND");
                return new ResponseEntity(responseMap, HttpStatus.NOT_FOUND);
            }
        } else{
            responseMap.put("error","TOKEN_INVALID");
            return new ResponseEntity(responseMap, HttpStatus.NOT_FOUND);
        }

    }


    @ResponseBody
    @PostMapping(value="/ws/post/like",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity likearPost(@RequestParam(value = "token", required = true) String token,
                                     @RequestParam(value = "postId", required = true) int postId){


        HashMap<String, Object> responseMap = new HashMap<>();
        Optional<Token> tokenValidacion = Optional.ofNullable(tokenRepository.findByCode(token));
        Optional<Post> postValidacion = postRepository.findById(postId);
        Optional<PostLike> likeValidacion = Optional.ofNullable(postLikeRepository.findByPostId(postId));

        PostLike postLike = likeValidacion.get();


        if (tokenValidacion.isPresent()){
            if(postRepository.existsById(postId)){
                if (likeValidacion.isPresent()){
                    responseMap.put("error","LIKE_ALREADY_EXISTS");
                    return new ResponseEntity(responseMap, HttpStatus.NOT_FOUND);
                }else {
                    postLikeRepository.save(postLike);
                    responseMap.put("status", "LIKE_CREATED");
                    responseMap.put("likeId", postLike.getId());
                    return new ResponseEntity(responseMap, HttpStatus.OK);
                }
            }else{
                responseMap.put("error","POST_NOT_FOUND");
                return new ResponseEntity(responseMap, HttpStatus.NOT_FOUND);
            }
        } else{
            responseMap.put("error","TOKEN_INVALID");
            return new ResponseEntity(responseMap, HttpStatus.NOT_FOUND);
        }

    }
}
