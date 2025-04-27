package com.example.project.filter;

import com.example.project.service.impl.VisitCounterService;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@AllArgsConstructor
public class VisitCounterFilter extends OncePerRequestFilter {

    private final VisitCounterService visitCounterService;

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
                                    jakarta.servlet.http.HttpServletResponse response,
                                    jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {
        String requestUri = request.getRequestURI();

        visitCounterService.incrementVisit(requestUri);
        filterChain.doFilter(request, response);

    }
}