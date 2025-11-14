package com.gpanta.Asistencia_app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gpanta.Asistencia_app.model.Asistencia;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
  List<Asistencia> findByFecha(LocalDate fecha);
  List<Asistencia> findByFechaAndEmpresaTrabajador(LocalDate fecha, String empresaTrabajador);
}