package online.mengxun.server.repository;

import online.mengxun.server.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;


public interface QuestionRepository extends JpaRepository<Question,String> {
    List<Question> findAllByCreator(String creator);
}
