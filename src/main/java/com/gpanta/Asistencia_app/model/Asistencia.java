package com.gpanta.Asistencia_app.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity @Getter @Setter
@Table(indexes = {@Index(columnList="fecha")})
public class Asistencia {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate fecha;                 // del reporte
  private String codigo;
  private String apellidosNombres;
  private String dni;
  private String regimen;
  private LocalDate fechaIngreso;
  private LocalTime horaIngreso;
  private String totalHoras;
  private String zona;
  private String ruta;
  private String cuadrilla;
  private String labor;

  private String observacion;
}
