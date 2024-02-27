package shop.mtcoding.blog.reply;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Table(name="reply_tb")
@Data
@Entity
public class Reply {
    // 하이버네이트 기술

    @Id // PK 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment 전략
    private int id;

    private String comment;
    private int userId;
    private int boardId;

    private LocalDateTime createdAt;


}
