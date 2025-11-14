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
import com.gpanta.Asistencia_app.repository.UsuarioRepository;
import com.gpanta.Asistencia_app.service.ExcelService;

@RestController
@RequestMapping("/api/asistencia")
public class AsistenciaController {

  private final AsistenciaRepository repo;
  private final ExcelService excel;
  private final UsuarioRepository usuarioRepo;

  public AsistenciaController(AsistenciaRepository r, ExcelService e, UsuarioRepository u) {
    repo = r;
    excel = e;
    usuarioRepo = u;
  }

  @PostMapping("/importar/empresa")
  public ResponseEntity<?> importarPorEmpresa(
      @RequestParam("file") MultipartFile file,
      @RequestParam("fecha") String fecha,
      Authentication auth) throws IOException {

    String username = auth.getName();

    int empresaId = getEmpresaIdPorUsuario(username);


    if (empresaId != 9 && empresaId != 14) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(Map.of("error", "No tienes permiso para importar datos para esta empresa"));
    }

    excel.importarPorEmpresa(file, LocalDate.parse(fecha), empresaId);
    return ResponseEntity.ok(Map.of("msg", "Importado para empresa " + empresaId));
  }

  @GetMapping("/empresa/{fecha}")
  public List<Asistencia> listarPorEmpresa(@PathVariable String fecha, Authentication auth) {
    String username = auth.getName();
    int empresaId = getEmpresaIdPorUsuario(username);

    if (empresaId != 9 && empresaId != 14) {
      throw new RuntimeException("No tienes permiso para ver datos de esta empresa");
    }

    return repo.findByFechaAndEmpresaTrabajador(
        LocalDate.parse(fecha),
        String.valueOf(empresaId));
  }

  private int getEmpresaIdPorUsuario(String username) {
    var u = usuarioRepo.findByUsername(username);

    if (u == null) {
      return 0;
    }

    return u.getEmpresaId() != null ? u.getEmpresaId() : 0;
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

  @PutMapping("/{id}/respuestaObservacion")
  public Asistencia setObs(@PathVariable Long id, @RequestBody Map<String, String> body) {
    var a = repo.findById(id).orElseThrow();
    a.setRespuestaObservacion(body.get("respuestaObservacion"));
    return repo.save(a);
  }

  @PutMapping("/{id}")
  public Asistencia update(@PathVariable Long id, @RequestBody Asistencia nuevo, Authentication auth) {

    var a = repo.findById(id).orElseThrow();
    a.setEmpresaTrabajador(nuevo.getEmpresaTrabajador());
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
