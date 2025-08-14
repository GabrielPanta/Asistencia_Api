package com.gpanta.Asistencia_app.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends GenericFilter{
    private final JwtUtil jwt;

  public JwtFilter(JwtUtil jwt){ this.jwt = jwt; }

  @Override 
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    String auth = request.getHeader("Authorization");
    if (StringUtils.hasText(auth) && auth.startsWith("Bearer ")) {
      try {
        var claims = jwt.parse(auth.substring(7)).getBody();
        var role = (String) claims.get("role");
        var authToken = new UsernamePasswordAuthenticationToken(
            claims.getSubject(), null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      } catch (Exception ignored) { }
    }
    chain.doFilter(req, res);
  }
}
