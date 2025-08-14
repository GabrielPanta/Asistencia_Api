package com.gpanta.Asistencia_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.gpanta.Asistencia_app.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  Usuario findByUsername(String username);
}


