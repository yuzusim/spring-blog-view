//package shop.mtcoding.blog._core.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.web.SecurityFilterChain;
//
//
//@Configuration //컴퍼넌트 스캔
//// 설정 파일의 역할을 하는 Bean임 (객체)
//
//// ioc 할수 있는 것
//
//// 전략 패턴 : 기본 추상적인 것에 의존한다.
//
//public class SecurityConfig {
//
//    @Bean
//    public WebSecurityCustomizer ignore(){
//        return w -> w.ignoring().requestMatchers("/board/*", "static/**", "h2-console/**");
//    }
//
//    @Bean
//    SecurityFilterChain configure(HttpSecurity http) throws Exception {
//
//        // board/1, board/1/update
//        http.authorizeHttpRequests(a-> {
//            http.csrf(c -> c.disable());
//
//            a.requestMatchers("/user/updateForm", "/board/**").authenticated().anyRequest().permitAll(); // 이 페이지 인증 안되면 못 들어감 그게 아닌 모든 설정은 허용해줘 (문지기에게 설정)
//
//
//        });
//
//        // 인증 필요시 loginForm 으로
//        http.formLogin(f -> {
//            f.loginPage("/loginForm").loginProcessingUrl("/login").defaultSuccessUrl("/").failureForwardUrl("/loginForm"); // 시큐리티 로그인이 등장 만들지 않아도 됨
//            //
//        });
//
//        // 최초의 로그인화면 커스터마이징
//        // loginForm 옮김
//        // 시큐리티 가로채서 커스터마이징
//
//        //
//
//        // 로그인 실패시 failureForwardUrl("/loginForm")
//
//        return http.build();
//
//    }
//
//}



package shop.mtcoding.blog._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration// 메모리에 띄우기 위한 문법
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encoder(){
        return  new BCryptPasswordEncoder();
    }

    // 인증과 상관없이 열어야 하는 주소
    // 주소 설계를 잘해야 함
    @Bean
    public WebSecurityCustomizer ignore(){
        return w -> w.ignoring().requestMatchers("/board/*", "/static/**", "/h2-console/**");
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf(c->c.disable());

        // 주소로 필터링 : 인증이 필요한 페이지를 주소로 구분
        http.authorizeHttpRequests(a -> {
            a.requestMatchers("/user/updateForm", "/board/**").authenticated() // 인증이 필요한 페이지
                    .anyRequest().permitAll(); // 인증이 필요없는 페이지
        });

        // 기본 주소를 우리가 만든 페이지로 변경함
        http.formLogin(f -> {
            // 시큐리티가 들고있는 페이지를 사용할 것
            f.loginPage("/loginForm").loginProcessingUrl("/login").defaultSuccessUrl("/")
                    .failureForwardUrl("/loginForm"); // 실패하면 이동
        });

        return http.build(); // 코드의 변경이 없으면 부모 이름(추상적)으로 리턴할 필요 없음
    }
}
