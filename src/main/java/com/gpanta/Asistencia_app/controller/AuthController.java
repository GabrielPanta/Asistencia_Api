package com.gpanta.Asistencia_app.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gpanta.Asistencia_app.dto.LoginDTO;
import com.gpanta.Asistencia_app.dto.TokenDTO;
import com.gpanta.Asistencia_app.repository.UsuarioRepository;
import com.gpanta.Asistencia_app.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UsuarioRepository userRepo;
  private final BCryptPasswordEncoder encoder;
  private final JwtUtil jwt;

  public AuthController(UsuarioRepository r, BCryptPasswordEncoder e, JwtUtil j) {
    userRepo = r;
    encoder = e;
    jwt = j;
  }

  @PostMapping("/login")
  public TokenDTO login(@RequestBody LoginDTO dto) {
    var u = userRepo.findByUsername(dto.username());
    if (u == null || !encoder.matches(dto.password(), u.getPassword()))
      throw new RuntimeException("Credenciales");
    var token = jwt.generate(u.getUsername(), u.getRol().name());
    return new TokenDTO(token, u.getRol().name(), u.getUsername());
  }
}
