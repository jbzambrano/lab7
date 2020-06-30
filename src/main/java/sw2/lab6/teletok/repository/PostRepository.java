package sw2.lab6.teletok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sw2.lab6.teletok.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT pos.* FROM teletok.post pos, teletok.user usu WHERE pos.user_id = usu.id AND pos.description = %?1% OR usu.username = %?2%", nativeQuery = true)
    List<Post> buscarPostPorPedazoDeNombre(String name1, String name2);


    @Query(value = "SELECT pos.* FROM teletok.post pos, teletok.user usu, teletok.post_like pl, teletok.token tok WHERE usu.id = pl.user_id AND pl.post_id = pos.id AND tok.user_id = usu.id AND tok.id = 1 AND pos.id = ?2", nativeQuery = true)
    List<Post> buscarPostPorIdPostyToken(Integer token, Integer idPost);

}
