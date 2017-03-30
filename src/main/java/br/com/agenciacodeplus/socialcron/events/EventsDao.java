package br.com.agenciacodeplus.socialcron.events;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface EventsDao extends JpaRepository<Event, Long> {

}
