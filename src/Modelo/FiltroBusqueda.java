/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Map;
import lombok.Data;

/**
 *
 * @author PC
 */
@Data
public class FiltroBusqueda {

    private String textoRegistro;
    private Map<String, String> registro;

    public FiltroBusqueda(Map<String, String> registro) {
        this.textoRegistro = String.join(" ", registro.values());
        this.textoRegistro = this.textoRegistro.toUpperCase();
        this.registro = registro;
    }

}
