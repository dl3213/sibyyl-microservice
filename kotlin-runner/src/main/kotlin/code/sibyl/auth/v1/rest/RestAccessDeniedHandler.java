//package code.sibyl.auth.v1.rest;
//
//import code.sibyl.common.Response;
//import com.alibaba.fastjson2.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//
///**
// * 拒绝授权 处理器
// */
////@Component // 注入之后就会被spring security 获取
//@Slf4j
//public class RestAccessDeniedHandler implements ServerAccessDeniedHandler {
//    @Override
//    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
//        System.err.println("rest 认证失败");
//        exchange.getPrincipal()
//                .doOnSuccess(e -> {
//                    System.err.println(e);
//                    System.err.println(e);
//                })
//                .subscribe();
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value()));
//        byte[] bytes = JSONObject.toJSONString(Response.error(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase())).getBytes(StandardCharsets.UTF_8);
//        DataBuffer buffer = response.bufferFactory().wrap(bytes);
//        return response.writeWith(Mono.just(buffer));
//    }
//}
