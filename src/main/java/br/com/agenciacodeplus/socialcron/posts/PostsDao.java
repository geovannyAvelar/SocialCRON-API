package br.com.agenciacodeplus.socialcron.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsDao extends JpaRepository<Post, Long> {

}
