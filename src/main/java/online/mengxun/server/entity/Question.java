package online.mengxun.server.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class Question {
    @Id
    private String id;
    private String creator;
    private String name;
    private String detail;
    private Integer diff;
    private Date create_at;
    private Date update_at;
}
