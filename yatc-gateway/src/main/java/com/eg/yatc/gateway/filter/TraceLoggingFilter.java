//package com.eg.yatc.gateway.filter;
//
//
//
//
//import io.micrometer.tracing.Span;
//import io.micrometer.tracing.Tracer;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//
//@Component
//
//public class TraceLoggingFilter implements GlobalFilter, Ordered {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(TraceLoggingFilter.class);
//
//    private final Tracer tracer;
//
//    public TraceLoggingFilter(Tracer tracer) {
//        this.tracer = tracer;
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange,
//                             GatewayFilterChain chain) {
//
//        Span span = tracer.currentSpan();
//
//        if (span != null) {
//            LOGGER.info("Incoming request: method={}, path={}, traceId={}, spanId={}",
//                    exchange.getRequest().getMethod(),
//                    exchange.getRequest().getURI().getPath(),
//                    span.context().traceId(),
//                    span.context().spanId());
//        } else {
//            LOGGER.info("Incoming request: method={}, path={}, no active span",
//                    exchange.getRequest().getMethod(),
//                    exchange.getRequest().getURI().getPath());
//        }
//
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return Ordered.LOWEST_PRECEDENCE;
//    }
//}
