package br.com.agenciacodeplus.socialcron.profiles;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProfilesDao extends JpaRepository<Profile, Long> {

}
