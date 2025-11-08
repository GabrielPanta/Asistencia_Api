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
      @Bean CommandLineRunner seed(UsuarioRepository repo, BCryptPasswordEncoder enc){
    return args -> {
      if (repo.findByUsername("encargado")==null){
        var u=new Usuario();
          u.setUsername("Gabriel Panta");
          u.setPassword(enc.encode("123456"));
          u.setRol(Rol.ENCARGADO); 
           
           repo.save(u);
      }
      if (repo.findByUsername("control")==null){
        var u=new Usuario(); 
        u.setUsername("Joshimar Samame"); 
        u.setPassword(enc.encode("123456")); 
        u.setRol(Rol.CONTROL_ASISTENCIA); 
        
        repo.save(u);
      }
    };
  }
}
