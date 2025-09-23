/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Informes.Rips;

import BaseDeDatos.gestorMySQL;
import Utilidades.Parametros;
import Utilidades.Utilidades;
import java.awt.Desktop;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author PC
 */
public class InformeJson {
 
    private gestorMySQL resultquery = new gestorMySQL();
    private static final String EFECTIVO = "Efectivo";
    private static final String TARJETA = "Tarjeta";
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    
    public String Encode() {
        String cifrado = format.format(new Date()) + System.currentTimeMillis();
        return cifrado;
    }
    
    public void GenerarInforme(int categoria, int informe, Map<String, String> params){
        
        String encode = Encode();
        String ruta = Parametros.dirInformesRips + "RP" + "_" + encode + ".json";
            
        String contenido = getInformeRips(params);
        
        int result = JOptionPane.showConfirmDialog(null, "¿Desea abrir el documento?");
            if (result == JOptionPane.YES_OPTION) {
                Desktop.getDesktop().open(new File(ruta));
            }
    }

    private String getInformeRips(Map<String, String> params) {
        String consulta = "", add = "", add2 = "";
        if (!params.get("tipo").equals("Todos")) {
            add2 = " mp.`pk_tipo_pago`='" + params.get("tipo") + "' AND ";
        }
        if (params.get("rHist").equals("false")) {
            add = " a.`fecha_pago` BETWEEN '" + params.get("fini") + "' AND '" + params.get("ffin") + "' AND";
        }
        
        consulta = "SELECT\n" +
                    "a.fecha_pago AS FECHA_ATENCION,\n" +
                    "pe.pfk_tipo_documento AS TIPO_DOCUMENTO,\n" +
                    "pe.pk_persona AS DOCUMENTO,\n" +
                    "pe.sexo as SEXO,\n" +
                    "CONCAT_WS(' ', pe.`primer_nombre`, IFNULL(pe.`segundo_nombre`,''), pe.`primer_apellido`, IFNULL(pe.`segundo_apellido`,'')) PACIENTE,\n" +
                    "pe.fecha_de_nacimiento as FECHA_NACIMIENTO\n" +
                    "FROM facturas a \n" +
                    "JOIN pagos p ON p.pk_pago = a.`numero` \n" +
                    "JOIN modo_pago mp ON mp.`pfk_pago`=p.`pk_pago` \n" +
                    "join pagosxconceptos pxc on pxc.pfk_pago=p.pk_pago and pxc.pfk_paciente=p.pfk_paciente\n" +
                    "join conceptos c on c.pk_concepto  = pxc.pfk_concepto \n" +
                    "JOIN personas pe ON a.`pfk_paciente`=CONCAT(pe.pfk_tipo_documento,pe.pk_persona)\n" +
                    "WHERE "+ add2 + add +
                    " a.estado='pagado' and  c.fk_tipo_concepto=1\n" +                    
                    "order by a.pfk_paciente;";
        
        List<Map<String, String>> listaDatos = resultquery.ListSQL(consulta);
        
        List<Map<String, String>> usuarios = Utilidades.data_list(1, listaDatos, new String[]{"DOCUMENTO"});
    
    }
}
