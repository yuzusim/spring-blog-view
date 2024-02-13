package shop.mtcoding.blog.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncodeTest {
    @Test
    public void encode_test() {
        BCryptPasswordEncoder en = new BCryptPasswordEncoder();
        String rawPassword = "1234";

        String encPassword = en.encode(rawPassword);
        System.out.println(encPassword);
    }
}

// $2a$10$jLBB8QsP3tAk6pzX4yztfuH0U4todLkW7oQwmQpeS5fm0pefYLK2y


// 할 때마다 값이 다름