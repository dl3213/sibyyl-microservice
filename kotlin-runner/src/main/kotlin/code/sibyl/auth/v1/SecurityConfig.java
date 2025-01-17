//package code.sibyl.auth.v1;
//
//import code.sibyl.auth.v1.rest.*;
//import code.sibyl.auth.v1.sys.SysAuthEntryPoint;
//import code.sibyl.auth.v1.sys.SysAuthFailureHandler;
//import code.sibyl.auth.v1.sys.SysLogoutSuccessHandler;
//import code.sibyl.auth.v1.rest.RestAccessDeniedHandler;
//import code.sibyl.auth.v1.rest.RestAuthenticationEntryPoint;
//import code.sibyl.filter.ReactiveRequestContextFilter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
//import org.springframework.security.web.server.context.ReactorContextWebFilter;
//import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
//
//import java.util.Base64;
//
///**
// * spring security webflux版配置
// *
// * <dependency>
// *             <groupId>org.springframework.boot</groupId>
// *             <artifactId>spring-boot-starter-security</artifactId>
// *         </dependency>
// *         <dependency>
// *             <groupId>org.springframework.security</groupId>
// *             <artifactId>spring-security-test</artifactId>
// *             <scope>test</scope>
// *         </dependency>
// *         <dependency>
// *             <groupId>org.thymeleaf.extras</groupId>
// *             <artifactId>thymeleaf-extras-springsecurity6</artifactId>
// *         </dependency>
// *
// */
//
//@Configuration
//@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity(useAuthorizationManager=true)
//@Slf4j
//public class SecurityConfig {
//
//
//    /**
//     * 前后端分离 todo
//     *
//     * @param http
//     * @return
//     */
//    @Order(0)
//    @Bean
//    public SecurityWebFilterChain restSecurityFilterChain(ServerHttpSecurity http) {
//        // @formatter:off
//        http
//                .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/api/rest/**"))
//                .authorizeExchange((authorize) ->
//                        authorize
//                                //.pathMatchers("/login","/logout").permitAll()
//                                .anyExchange().authenticated());
//        http.csrf(csrf -> csrf.disable());
//        http.httpBasic(e -> e.disable());
//        http.headers(header -> header.frameOptions(f->f.disable()));
//        http.exceptionHandling(exceptionHandling ->
//                exceptionHandling
//                        .accessDeniedHandler(new RestAccessDeniedHandler()) // 权限处理
//                        .authenticationEntryPoint(new RestAuthenticationEntryPoint()) // 未认证的处理,返回json
//        );
//        http
//                .formLogin(form-> form
//                                .loginPage("/login-view")
//                        .requiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/api/rest/login"))// 注意请求方法及Content-Type,Content-Type默认x-www-form-urlencoded
////                        .authenticationEntryPoint(new RestServerAuthenticationEntryPoint())// todo 限制到 post + json
////                        .authenticationManager(new RestReactiveAuthenticationManager())
//                        .authenticationSuccessHandler(new RestAuthSuccessHandler())
//                        .authenticationFailureHandler(new RestAuthFailureHandler())
//                );
//        http.logout(logout->logout.logoutUrl("/api/rest/logout")); // ReactorContextWebFilter
//        // @formatter:on
////        http.securityContextRepository()
//        return http.build();
//    }
//
//    private final static String loginView = "/sign-in.html";
//
//    /**
//     * 前后端不分离
//     *
//     * @param http
//     * @return
//     */
//    @Order(1)
//    @Bean
//    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
//        // @formatter:off
//       http
//                .authorizeExchange((authorize) -> authorize
//                                .pathMatchers("/admin/**").hasAnyAuthority("admin:api")
//                                .pathMatchers("/user/**").hasAnyAuthority("user:api")
//                        .pathMatchers(
//                                "/**",
//                                "/favicon.ico",
//                                loginView,
//                                "/css/**",
//                                "/js/**",
//                                "/font/**",
//                                "/img/**",
//                                "/dist/**",
//                                "/file/**",
//                                "/database/socket/**",
//                                "/noAuth/**",
//                                "/default/**",
//                                "/api/external/**",
//                                "/api/kotlin/**",
//                                "/eos/**"
//                        )
//                        .permitAll()
//                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
//                        .anyExchange().authenticated()
//                );
//        http.csrf(csrf -> csrf.disable());
//        http.httpBasic(e -> e.disable());
//        http.headers(header -> header.frameOptions(f->f.disable()));
//        http.exceptionHandling(exceptionHandling ->
//                exceptionHandling
//                        .authenticationEntryPoint(new SysAuthEntryPoint(loginView)) // 未认证的处理,跳转到登录页面
//                        .accessDeniedHandler(new RestAccessDeniedHandler()) //权限失败 处理
//        );
//        http
//                .formLogin((form)-> form
//                        .loginPage(loginView)
//                        .requiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/login"))
////                                .authenticationManager(new SysAuthenticationManager()) //  todo
////                        .authenticationSuccessHandler(new RestAuthSuccessHandler()) // 前后端不分离需要跳转到首页
//                        .authenticationFailureHandler(new SysAuthFailureHandler(loginView, "登录失败~~~")) // 前后端不分离需要跳转到登录页并加提示 todo
//                );
////        http.addFilterAt(new LoginFilter, ); // 自定义登录操作 ??? 邮箱手机登录可以在ReactiveUserDetailsService中完成
//        http.logout(logout->logout.logoutUrl("/logout").logoutSuccessHandler(new SysLogoutSuccessHandler(loginView, "用户已退出")));
//        //http.securityContextRepository(customServerSecurityContextRepository());
//        // @formatter:on
//
//        SessionConfig securityContextRepository = new SessionConfig();
//        securityContextRepository.setCacheSecurityContext(true);
//        http.securityContextRepository(securityContextRepository);
//
////        http.addFilterAfter(new ReactorContextWebFilter(securityContextRepository), SecurityWebFiltersOrder.AUTHENTICATION); // ReactorContextWebFilter;
////        http.addFilterAfter(new ReactiveRequestContextFilter(), SecurityWebFiltersOrder.FIRST); // ReactorContextWebFilter;
//        return http.build();
//    }
//
////    @Bean
////    public ReactorContextWebFilter reactorContextWebFilter(){
////        return new ReactorContextWebFilter();
////    }
//
//    /**
//     * 临时
//     *
//     * @return
//     */
//    @Bean
//    public MapReactiveUserDetailsService userDetailsService() {
//        // @formatter:off
////        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin-sibyl-202410")
//                .passwordEncoder(passwordEncoder()::encode)
//                .authorities("admin:api","user:api")
////                .authorities(new SimpleGrantedAuthority("admin:api"),new SimpleGrantedAuthority("user:api"))
////                .roles("admin","user")//有前缀，最好不要用
//                .build();
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("user-sibyl-202410")
//                .passwordEncoder(passwordEncoder()::encode)
//                .authorities(new SimpleGrantedAuthority("user:api"))// 角色-菜单  权限-接口
////                .roles("user")// 角色-菜单  权限-接口
//                .build();
//        // @formatter:on
//        return new MapReactiveUserDetailsService(admin, user);
//    }
//
//    /**
//     *  todo
//     * @return
//     */
////    @Bean
////    public ServerSecurityContextRepository customServerSecurityContextRepository() {
////        return NoOpServerSecurityContextRepository.getInstance();
////    }
//
//    /**
//     * 密码加密解密
//     *
//     * @return
//     */
//    @Bean
//    @Primary
//    public PasswordEncoder passwordEncoder() {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
////        NoPasswordEncoder encoder = new NoPasswordEncoder();
//        return encoder;
//    }
//}
