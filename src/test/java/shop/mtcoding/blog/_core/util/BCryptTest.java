package shop.mtcoding.blog._core.util;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class BCryptTest {

    @Test
    public void login_test(){
        String joinPassword = "1234";
        String enc1Password = BCrypt.hashpw(joinPassword, BCrypt.gensalt());
        System.out.println("JOIN : "+enc1Password);

        String loginPassword = "12345";

        // 해쉬 비교 없이 인코딩으로
        // 해쉬 값이랑 순수한 함수로 비교
        boolean test = BCrypt.checkpw(loginPassword, enc1Password);
        System.out.println(test);
    }

    @Test
    public void gensalt_test(){
        String salt = BCrypt.gensalt();
        System.out.println(salt);
    }

    @Test
    public void hashpw_test(){
        String rawPassword ="1234";
        String encPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        System.out.println(encPassword);
    }

}
