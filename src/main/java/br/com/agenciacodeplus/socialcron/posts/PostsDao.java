package br.com.agenciacodeplus.socialcron.posts;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PostsDao extends JpaRepository<Post, Long> {

}
