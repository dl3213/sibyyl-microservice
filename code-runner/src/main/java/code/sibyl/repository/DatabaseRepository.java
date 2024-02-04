package code.sibyl.repository;

import code.sibyl.domain.Database;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface DatabaseRepository extends ReactiveCrudRepository<Database, Long> {
    @Query("SELECT * from t_database ")
    Flux<Database> list();
}
