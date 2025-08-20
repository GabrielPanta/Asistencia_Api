package com.gpanta.Asistencia_app.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity 
@Getter 
@Setter
@Table(indexes = {@Index(columnList="fecha")})
public class Asistencia {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate fecha;               
  private String codigo;
  private String apellidosNombres;
  private String dni;
  private String regimen;
  private String fechaIngreso;
  private String horaIngreso;
  private String totalHoras;
  private String marcacionIngreso;
  private String marcacionSalida;
  private String totalHorasMarcacion;
  private String historico;
  private String zona;
  private String cuartel;
  private String placa;
  private String ruta;
  private String codigoBus;
  private String cuadrilla;
  private String labor;
  private String observacion;
  private String respuestaObservacion;
}
