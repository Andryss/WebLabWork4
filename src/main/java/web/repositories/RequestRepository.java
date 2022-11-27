package web.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.entities.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}
