package online.mengxun.server.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Teacher {
    @Id
    private String id;
    private String name;
    private String password;
    private Integer sex;
    private Integer age;
    private String email;
    private Date create_at;
    private Date update_at;
}
