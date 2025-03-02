package ca.metricalsky.yt.comments.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Filters.CLACKS_OVERHEAD)
public class ClacksOverheadFilter extends OncePerRequestFilter {

    private static final String X_CLACKS_OVERHEAD = "X-Clacks-Overhead";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        response.addHeader(X_CLACKS_OVERHEAD, "GNU Terry Pratchett");
        filterChain.doFilter(request, response);
    }
}
