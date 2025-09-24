/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Rips;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import java.math.BigDecimal;
import lombok.Getter;

/**
 *
 * @author PC
 */
@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Consulta {
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
    private BigDecimal vrServicio;
    private String conceptoRecaudo;
    private BigDecimal valorPagoModerador;
    private String numFEVPagoModerador;
    private int consecutivo;
}