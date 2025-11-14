package com.gpanta.Asistencia_app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.gpanta.Asistencia_app.model.Rol;
import com.gpanta.Asistencia_app.model.Usuario;
import com.gpanta.Asistencia_app.repository.UsuarioRepository;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seed(UsuarioRepository repo, BCryptPasswordEncoder enc) {
        return args -> {

            if (repo.findByUsername("encargado_verfrut") == null) {
                var u = new Usuario();
                u.setUsername("encargado_verfrut");
                u.setPassword(enc.encode("123456"));
                u.setRol(Rol.ENCARGADO);
                u.setEmpresaId(14);
                repo.save(u);
            }

            if (repo.findByUsername("control_verfrut") == null) {
                var u = new Usuario();
                u.setUsername("control_verfrut");
                u.setPassword(enc.encode("123456"));
                u.setRol(Rol.CONTROL_ASISTENCIA);
                u.setEmpresaId(14);
                repo.save(u);
            }

            if (repo.findByUsername("encargado_rapel") == null) {
                var u = new Usuario();
                u.setUsername("encargado_rapel");
                u.setPassword(enc.encode("123456"));
                u.setRol(Rol.ENCARGADO);
                u.setEmpresaId(9);
                repo.save(u);
            }

            if (repo.findByUsername("control_rapel") == null) {
                var u = new Usuario();
                u.setUsername("control_rapel");
                u.setPassword(enc.encode("123456"));
                u.setRol(Rol.CONTROL_ASISTENCIA);
                u.setEmpresaId(9);
                repo.save(u);
            }
        };
    }
}
