package shop.mtcoding.blog._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;


import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration // 컴퍼넌트 스캔(메모리에 띄우기 위한 문법) : 설정파일은 configuration을 붙여야함
public class SecurityConfig {
    // 시큐리티를 위한 필터가 체인이 걸려있음 이 필터체인(묶여있는거) 수정할거임
    // ioc 할수 있는 것 : @Controller, @Component, @Configuration, @Service, @Bean

    @Bean // 시큐리티가 로그인 할때
    public BCryptPasswordEncoder encoder(){
        return  new BCryptPasswordEncoder(); // IoC 등록, 시큐리티가 로그인할때 어떤 해시로 비교해야하는지 알게됨
    }

    // 인증과 상관없이 열어야 하는 주소
    // 주소 설계를 잘해야 함
    @Bean
    public WebSecurityCustomizer ignore(){ //정적자원 시큐리티 필터에서 제외시키기
        return w -> w.ignoring().requestMatchers("/static/**", "/h2-console/**");
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf(c->c.disable());

        // 주소로 필터링 : 인증이 필요한 페이지를 주소로 구분
        http.authorizeHttpRequests(a -> {
            a.requestMatchers(RegexRequestMatcher.regexMatcher("/board/\\d+")).permitAll()
                    .requestMatchers("/user/**", "/board/**").authenticated() // 인증이 필요한 페이지
                    .anyRequest().permitAll(); // 인증이 필요없는 페이지
        });



        // 기본 주소를 우리가 만든 페이지로 변경함
        // 인증 필요시 loginForm 으로
        http.formLogin(f -> {
            // 시큐리티가 들고있는 페이지를 사용할 것
            f.loginPage("/loginForm").loginProcessingUrl("/login").defaultSuccessUrl("/")
                    .failureForwardUrl("/loginForm"); // 실패하면 이동
        });

        return http.build(); // 코드의 변경이 없으면 부모 이름(추상적)으로 리턴할 필요 없음
    }
}
