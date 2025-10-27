package br.com.alldirect.luan.todolist.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);

        var authorization = request.getHeader("Authorization");

        var authEncodede = authorization.substring("basic".length()).trim();


        byte[] authDecode =  Base64.getDecoder().decode(authEncodede);

        var authString = new String(authDecode);

        System.out.println("Authorization");
        System.out.println(authorization);

        String[] credentials = authString.split(":");
        String userName = credentials[0];
        String password = credentials[1];

        System.out.println(authString);

        System.out.println("usuario: "+userName);
        System.out.println("senha: "+password);
    }
}
