package online.mengxun.server.repository;

import online.mengxun.server.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student,String>{
    List<Student> findByName(String name);
}

