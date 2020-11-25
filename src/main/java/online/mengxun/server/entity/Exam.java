package online.mengxun.server.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Exam {
    @Id
    private String id;
    private String name;
    private String creator;
    private Date start_time;
    private Date end_time;
    private Integer state;
    private Integer pub;
    private Date create_at;
    private Date update_at;
}
