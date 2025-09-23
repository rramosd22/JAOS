/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Rips;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

/**
 *
 * @author PC
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Factura {
    private String numDocumentoIdObligado;
    private String numFactura;
    private String tipoNota;
    private String numNota;
    private List<Usuario> usuarios;
}
