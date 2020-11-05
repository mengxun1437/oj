package online.mengxun.server.repository;

import com.sun.org.apache.xpath.internal.operations.Bool;
import online.mengxun.server.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher,String>{
    List<Teacher> findByName(String name);
    Boolean existsByName(String name);
}
