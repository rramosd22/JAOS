/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Rips;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

/**
 *
 * @author PC
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {
    private String tipoDocumentoIdentificacion;
    private String numDocumentoIdentificacion;
    private String tipoUsuario;
    private String fechaNacimiento;
    private String codSexo;
    private String codPaisResidencia;
    private String codMunicipioResidencia;
    private String codZonaTerritorialResidencia;
    private String incapacidad;
    private String codPaisOrigen;
    private int consecutivo;
    private Servicios servicios;
}
