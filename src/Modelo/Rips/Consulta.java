/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Rips;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


/**
 *
 * @author PC
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Consulta {
    private String codPrestador;
    private String fechaInicioAtencion;
    private String numAutorizacion;
    private String codConsulta;
    private String modalidadGrupoServicioTecSal;
    private String grupoServicios;
    private int codServicio;
    private String finalidadTecnologiaSalud;
    private String causaMotivoAtencion;
    private String codDiagnosticoPrincipal;
    private String codDiagnosticoRelacionado1;
    private String codDiagnosticoRelacionado2;
    private String codDiagnosticoRelacionado3;
    private String tipoDiagnosticoPrincipal;
    private String tipoDocumentoIdentificacion;
    private String numDocumentoIdentificacion;
    private double vrServicio;
    private String conceptoRecaudo;
    private double valorPagoModerador;
    private String numFEVPagoModerador;
    private int consecutivo;
}