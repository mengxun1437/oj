package online.mengxun.server.repository;

import online.mengxun.server.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuestionRepository extends JpaRepository<Question,String> {
}
