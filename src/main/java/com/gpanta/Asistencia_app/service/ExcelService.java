package com.gpanta.Asistencia_app.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gpanta.Asistencia_app.model.Asistencia;
import com.gpanta.Asistencia_app.repository.AsistenciaRepository;

import java.io.IOException;

@Service
public class ExcelService {
  
  private final AsistenciaRepository repo;

  public ExcelService(AsistenciaRepository repo) {
    this.repo = repo;
  }

  public void importar(MultipartFile file, LocalDate fecha) throws IOException {
    try (var in = file.getInputStream(); var wb = WorkbookFactory.create(in)) {
      Sheet sheet = wb.getSheetAt(0);
      boolean header = true;
      for (Row row : sheet) {
        if (header) {
          header = false;
          continue;
        } // salta cabecera
        if (row == null || row.getCell(0) == null)
          continue;

        var a = new Asistencia();
        a.setFecha(fecha);
        a.setEmpresaTrabajador(getString (row, 0));
        a.setCodigo(getString(row, 1));
        a.setApellidosNombres(getString(row, 2));
        a.setDni(getString(row, 3));
        a.setRegimen(getString(row, 4));
        a.setFechaIngreso(getString(row, 5));
        a.setHoraIngreso(getString(row, 6));
        a.setTotalHoras(getString(row, 7));
        a.setMarcacionIngreso(getString(row, 8));
        a.setMarcacionSalida(getString(row, 9));
        a.setTotalHorasMarcacion(getString(row, 10));
        a.setHistorico(getString(row, 11));
        a.setZona(getString(row, 12));
        a.setCuartel(getString(row, 13));
        a.setPlaca(getString(row, 14));
        a.setRuta(getString(row, 15));
        a.setCodigoBus(getString(row, 16));
        a.setCuadrilla(getString(row, 17));
        a.setLabor(getString(row, 18));
        a.setObservacion(getString(row, 19));
        repo.save(a);
      }
    }
  }

  public void importarPorEmpresa(MultipartFile file, LocalDate fecha, int empresaId) throws IOException {
    try (var in = file.getInputStream(); var wb = WorkbookFactory.create(in)) {
      Sheet sheet = wb.getSheetAt(0);
      boolean header = true;
      for (Row row : sheet) {
        if (header) {
          header = false;
          continue;
        }
        if (row == null || row.getCell(0) == null)
          continue;

        String empresa = getString(row, 0);
        if (!coincideEmpresa(empresa, empresaId))
          continue; // se omite si no corresponde a la empresa del encargado o control

        var a = new Asistencia();
        a.setFecha(fecha);
        a.setEmpresaTrabajador(empresa);
        a.setCodigo(getString(row, 1));
        a.setApellidosNombres(getString(row, 2));
        a.setDni(getString(row, 3));
        a.setRegimen(getString(row, 4));
        a.setFechaIngreso(getString(row, 5));
        a.setHoraIngreso(getString(row, 6));
        a.setTotalHoras(getString(row, 7));
        a.setMarcacionIngreso(getString(row, 8));
        a.setMarcacionSalida(getString(row, 9));
        a.setTotalHorasMarcacion(getString(row, 10));
        a.setHistorico(getString(row, 11));
        a.setZona(getString(row, 12));
        a.setCuartel(getString(row, 13));
        a.setPlaca(getString(row, 14));
        a.setRuta(getString(row, 15));
        a.setCodigoBus(getString(row, 16));
        a.setCuadrilla(getString(row, 17));
        a.setLabor(getString(row, 18));
        a.setObservacion(getString(row, 19));
        repo.save(a);
      }
    }
  }

  private boolean coincideEmpresa(String nombreEmpresa, int empresaId) {
    if (empresaId == 9 && nombreEmpresa.equalsIgnoreCase("9")) return true;
    if (empresaId == 14 && nombreEmpresa.equalsIgnoreCase("14")) return true;
    return false;
  }

  @SuppressWarnings("deprecation")
  private String getString(Row r, int idx) {
    Cell c = r.getCell(idx);
    if (c == null)
      return null;
    c.setCellType(CellType.STRING);
    return c.getStringCellValue().trim();
  }

  public ByteArrayOutputStream exportar(LocalDate fecha) throws IOException {
    var data = repo.findByFecha(fecha);
    Workbook wb = new XSSFWorkbook();
    Sheet sh = wb.createSheet("Reporte " + fecha);
    int r = 0;
    Row head = sh.createRow(r++);
    String[] cols = { "Fecha", "Codigo", "Apellidos y Nombres", "DNI", "Regimen", "Fecha Ingreso", "Hora Ingreso",
        "Total Horas","Marcacion Ingreso", "Marcacion Salida","Total Horas Marcacion","Historico","Zona","Cuartel",
        "Placa", "Ruta","Codigo Bus", "Cuadrilla", "Labor", "Observacion","Respuesta Observacion" };
    for (int i = 0; i < cols.length; i++)
      head.createCell(i).setCellValue(cols[i]);

    for (Asistencia a : data) {
      Row row = sh.createRow(r++);
      int c = 0;
      row.createCell(c++).setCellValue(a.getFecha() != null ? a.getFecha().toString() : "");
      row.createCell(c++).setCellValue(n(a.getCodigo()));
      row.createCell(c++).setCellValue(n(a.getApellidosNombres()));
      row.createCell(c++).setCellValue(n(a.getDni()));
      row.createCell(c++).setCellValue(n(a.getRegimen()));
      row.createCell(c++).setCellValue(a.getFechaIngreso() != null ? a.getFechaIngreso().toString() : "");
      row.createCell(c++).setCellValue(a.getHoraIngreso() != null ? a.getHoraIngreso().toString() : "");
      row.createCell(c++).setCellValue(n(a.getTotalHoras()));
      row.createCell(c++).setCellValue(n(a.getMarcacionIngreso()));
      row.createCell(c++).setCellValue(n(a.getMarcacionSalida()));
      row.createCell(c++).setCellValue(n(a.getTotalHorasMarcacion()));
      row.createCell(c++).setCellValue(n(a.getHistorico()));
      row.createCell(c++).setCellValue(n(a.getZona()));
      row.createCell(c++).setCellValue(n(a.getCuartel()));
      row.createCell(c++).setCellValue(n(a.getPlaca()));
      row.createCell(c++).setCellValue(n(a.getRuta()));
      row.createCell(c++).setCellValue(n(a.getCodigoBus()));  
      row.createCell(c++).setCellValue(n(a.getCuadrilla()));
      row.createCell(c++).setCellValue(n(a.getLabor()));
      row.createCell(c++).setCellValue(n(a.getObservacion()));
      row.createCell(c++).setCellValue(n(a.getRespuestaObservacion()));
    }
    for (int i = 0; i < cols.length; i++)
      sh.autoSizeColumn(i);
    var out = new ByteArrayOutputStream();
    wb.write(out);
    wb.close();
    return out;
  }

  public ByteArrayOutputStream exportarPorEmpresa(LocalDate fecha, int empresaId) throws IOException {

    String empresa = obtenerNombreEmpresa(empresaId);
    if (empresa == null)
        throw new IllegalArgumentException("Empresa no válida para el usuario");

    var data = repo.findByFechaAndEmpresaTrabajador(fecha, empresa);

    Workbook wb = new XSSFWorkbook();
    Sheet sh = wb.createSheet("Reporte " + fecha);
    int r = 0;

    Row head = sh.createRow(r++);
    String[] cols = { "Fecha", "Codigo", "Apellidos y Nombres", "DNI", "Regimen", "Fecha Ingreso", "Hora Ingreso",
        "Total Horas","Marcacion Ingreso", "Marcacion Salida","Total Horas Marcacion","Historico","Zona","Cuartel",
        "Placa", "Ruta","Codigo Bus", "Cuadrilla", "Labor", "Observacion","Respuesta Observacion" };
    for (int i = 0; i < cols.length; i++)
      head.createCell(i).setCellValue(cols[i]);

    for (Asistencia a : data) {
      Row row = sh.createRow(r++);
      int c = 0;
      row.createCell(c++).setCellValue(a.getFecha() != null ? a.getFecha().toString() : "");
      row.createCell(c++).setCellValue(n(a.getCodigo()));
      row.createCell(c++).setCellValue(n(a.getApellidosNombres()));
      row.createCell(c++).setCellValue(n(a.getDni()));
      row.createCell(c++).setCellValue(n(a.getRegimen()));
      row.createCell(c++).setCellValue(a.getFechaIngreso() != null ? a.getFechaIngreso().toString() : "");
      row.createCell(c++).setCellValue(a.getHoraIngreso() != null ? a.getHoraIngreso().toString() : "");
      row.createCell(c++).setCellValue(n(a.getTotalHoras()));
      row.createCell(c++).setCellValue(n(a.getMarcacionIngreso()));
      row.createCell(c++).setCellValue(n(a.getMarcacionSalida()));
      row.createCell(c++).setCellValue(n(a.getTotalHorasMarcacion()));
      row.createCell(c++).setCellValue(n(a.getHistorico()));
      row.createCell(c++).setCellValue(n(a.getZona()));
      row.createCell(c++).setCellValue(n(a.getCuartel()));
      row.createCell(c++).setCellValue(n(a.getPlaca()));
      row.createCell(c++).setCellValue(n(a.getRuta()));
      row.createCell(c++).setCellValue(n(a.getCodigoBus()));  
      row.createCell(c++).setCellValue(n(a.getCuadrilla()));
      row.createCell(c++).setCellValue(n(a.getLabor()));
      row.createCell(c++).setCellValue(n(a.getObservacion()));
      row.createCell(c++).setCellValue(n(a.getRespuestaObservacion()));
    }

    for (int i = 0; i < cols.length; i++)
      sh.autoSizeColumn(i);

    var out = new ByteArrayOutputStream();
    wb.write(out);
    wb.close();
    return out;
}

private String obtenerNombreEmpresa(int id) {
    if (id == 9) return "9";
    if (id == 14) return "14";
    return null;
}

  private String n(String s) {
    return s == null ? "" : s;
  }

  public String extraerEmpresa(MultipartFile file) throws IOException {
try (var in = file.getInputStream(); var wb = WorkbookFactory.create(in)) {
Sheet sheet = wb.getSheetAt(0);
for (Row row : sheet) {
if (row.getRowNum() == 0) continue;
if (row.getCell(0) == null) continue;
return getString(row, 0);
}
}
return null;
}
}
