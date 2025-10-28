package br.com.alldirect.luan.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.alldirect.luan.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Se não for /tasks ou subcaminhos, deixa passar direto
        if (path == null || !path.startsWith("/tasks")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Ignora requisições OPTIONS (CORS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header");
            return;
        }

        String prefix = "Basic ";
        if (!authHeader.regionMatches(true, 0, prefix, 0, prefix.length())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization type");
            return;
        }

        String base64 = authHeader.substring(prefix.length()).trim();
        String decoded;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            decoded = new String(bytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Base64 token");
            return;
        }

        String[] parts = decoded.split(":", 2);
        if (parts.length != 2) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials format");
            return;
        }

        String userName = parts[0];
        String password = parts[1];

        var user = userRepository.findByUserName(userName);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
            return;
        }

        var result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if (!result.verified) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid password");
            return;
        }

        // Somente chega aqui se o usuário e senha estiverem corretos
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path == null || !path.startsWith("/tasks");
    }
}
