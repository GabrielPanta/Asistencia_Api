package com.gpanta.Asistencia_app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import com.gpanta.Asistencia_app.model.Asistencia;
import com.gpanta.Asistencia_app.repository.AsistenciaRepository;
import com.gpanta.Asistencia_app.service.ExcelService;

@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaController {
  private final AsistenciaRepository repo;
  private final ExcelService excel;

  public AsistenciaController(AsistenciaRepository r, ExcelService e) {
    repo = r;
    excel = e;
  }

  @PostMapping("/importar")
  public ResponseEntity<?> importar(@RequestParam("file") MultipartFile file,
      @RequestParam("fecha") String fecha) throws IOException {
    excel.importar(file, LocalDate.parse(fecha));
    return ResponseEntity.ok(Map.of("msg", "Importado"));
  }

  @GetMapping("/{fecha}")
  public List<Asistencia> listar(@PathVariable String fecha) {
    return repo.findByFecha(LocalDate.parse(fecha));
  }

  // Actualiza solo observación (ambos roles)
  @PutMapping("/{id}/observacion")
  public Asistencia setObs(@PathVariable Long id, @RequestBody Map<String, String> body) {
    var a = repo.findById(id).orElseThrow();
    a.setObservacion(body.get("observacion"));
    return repo.save(a);
  }

  // Solo ENCARGADO: actualizar registro completo
  @PutMapping("/{id}")
  public Asistencia update(@PathVariable Long id, @RequestBody Asistencia nuevo, Authentication auth) {
    // Seguridad adicional: validar rol desde auth si quieres
    var a = repo.findById(id).orElseThrow();
    a.setCodigo(nuevo.getCodigo());
    a.setApellidosNombres(nuevo.getApellidosNombres());
    a.setDni(nuevo.getDni());
    a.setRegimen(nuevo.getRegimen());
    a.setFechaIngreso(nuevo.getFechaIngreso());
    a.setHoraIngreso(nuevo.getHoraIngreso());
    a.setTotalHoras(nuevo.getTotalHoras());
    a.setZona(nuevo.getZona());
    a.setRuta(nuevo.getRuta());
    a.setCuadrilla(nuevo.getCuadrilla());
    a.setLabor(nuevo.getLabor());
    a.setObservacion(nuevo.getObservacion());
    return repo.save(a);
  }

  @GetMapping("/exportar/{fecha}")
  public ResponseEntity<byte[]> exportar(@PathVariable String fecha) throws IOException {
    var out = excel.exportar(LocalDate.parse(fecha));
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_" + fecha + ".xlsx")
        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(out.toByteArray());
  }
}
