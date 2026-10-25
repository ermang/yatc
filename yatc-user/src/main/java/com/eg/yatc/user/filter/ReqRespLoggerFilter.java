//package com.eg.yatc.user.filter;
//
//import jakarta.annotation.Nonnull;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.Enumeration;
//import java.util.List;
//
//@Component
//public class ReqRespLoggerFilter extends OncePerRequestFilter {
//
//        private static final Logger LOGGER = LoggerFactory.getLogger(ReqRespLoggerFilter.class);
//
//        @Override
//        protected void doFilterInternal(
//                HttpServletRequest request,
//                HttpServletResponse response,
//                FilterChain filterChain)
//                throws ServletException, IOException {
//
//            ContentCachingRequestWrapper wrappedRequest =
//                    new ContentCachingRequestWrapper(request);
//
//            long start = System.nanoTime();
//
//            try {
//                filterChain.doFilter(request, response);
//            } finally {
//                long durationMs =
//                        (System.nanoTime() - start) / 1_000_000;
//
//                String headersConcatenated = extractHedersConcatenated(request);
//
//                byte[] requestBodyAsByteArray = wrappedRequest.getContentAsByteArray();
//
//                String requestBody = new String(
//                        requestBodyAsByteArray,
//                        Charset.forName(
//                                wrappedRequest.getCharacterEncoding() != null
//                                        ? wrappedRequest.getCharacterEncoding()
//                                        : "UTF-8"
//                        )
//                );
//
////                ObjectMapper objectMapper = new ObjectMapper();
////
////                JsonNode jsonNode = objectMapper.readTree(requestBody);
////
////                jsonNode.con
//
//                LOGGER.info("HTTP->{} Headers->{} RequestURI->{} RequestBody->{} status={} durationMs={}",
//                        request.getMethod(),
//                        headersConcatenated,
//                        request.getRequestURI(),
//                        requestBody,
//                        response.getStatus(),
//                        durationMs
//                );
//            }
//        }
//
//    @Nonnull
//    private static String extractHedersConcatenated(HttpServletRequest request) {
//        Enumeration<String> headerNames = request.getHeaderNames();
//        List<String> headers = new ArrayList<>();
//
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//
//            if ("Authorization".equalsIgnoreCase(headerName)) {
//                continue;
//            }
//
//            headers.add(headerName + ":" + request.getHeader(headerName));
//        }
//
//        String headersConcatenated = String.join(", ", headers);
//        return headersConcatenated;
//    }
//
//}
