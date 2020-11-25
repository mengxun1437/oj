package online.mengxun.server.repository;

import online.mengxun.server.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ExamRepository extends JpaRepository<Exam,String> {
    List<Exam> findAllByPub(int pub);
}
