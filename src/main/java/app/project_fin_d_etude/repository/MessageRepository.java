package app.project_fin_d_etude.repository;

import app.project_fin_d_etude.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByLuOrderByDateEnvoiDesc(boolean lu);

    List<Message> findAllByOrderByDateEnvoiDesc();
}
