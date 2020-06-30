package sw2.lab6.teletok.controller;

import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sw2.lab6.teletok.entity.Post;
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
    TokenRepository tokenRepository;

    @Autowired
    PostRepository postRepository;

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

    @GetMapping("/post/{id}")
    public String viewPost() {
        return "post/view";
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
    @PostMapping(value="/ws/post/comment",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity comentarPost(@RequestParam(value = "token", required = true) String token,
                                       @RequestParam(value = "postId", required = true) int postId,
                                       @RequestParam(value = "message", required = true) String message,
                                       @ResponseBody PostComment postComment){

        HashMap<String, Object> responseMap = new HashMap<>();

        Optional<Token> tokenValidacion = Optional.ofNullable(tokenRepository.findByCode(token));
        Optional<Post> postValidacion = postRepository.findById(postId);

        Post post = postValidacion.get();

        Optional<PostComment> commentValidacion = Optional.ofNullable(postCommentRepository.findByPostId(postId));

        PostComment postCommentValidacion = commentValidacion.get();

        if (tokenValidacion.isPresent()){
            if(postRepository.existsById(postId)){
                responseMap.put("status", "COMMENT_CREATED");
                responseMap.put("commentId", post.getId());
                postCommentValidacion.setMessage(message);
                postCommentRepository.save(postCommentValidacion);
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
