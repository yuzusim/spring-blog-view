package shop.mtcoding.blog.love;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(LoveRepository.class)
@DataJpaTest
public class LoveRepositoryTest {

    @Autowired
    private LoveRepository loveRepository;

    @Test
    public void findLove_test(){
        // given
        int boardId = 9;
        int sessionUserId = 1;

        // when
        loveRepository.findLove(boardId, sessionUserId);
    }
}