/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Informes.Rips;

import BaseDeDatos.gestorMySQL;
import Modelo.MediosDePago;
import Modelo.Rips.Consulta;
import Modelo.Rips.Factura;
import Modelo.Rips.Servicios;
import Modelo.Rips.Usuario;
import Utilidades.Parametros;
import Utilidades.Utilidades;
import static Utilidades.Utilidades.mostrarMensaje;
import static Utilidades.Utilidades.stringify;
import Utilidades.datosUsuario;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;
import javax.swing.JOptionPane;

/**
 *
 * @author PC
 */
public class InformeJson {

    private gestorMySQL resultquery = new gestorMySQL();
    private static final String EFECTIVO = "Efectivo";
    private static final String TARJETA = "Tarjeta";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    private static final String TIPO_DOCUMENTO_DOCTOR = "CC";
    private static final String DOCUMENTO_DOCTOR = "79601299";
    private static final String RS = "RS";
    private static final String TIPO_USUARIO = "12";
    private static final String CODIGO_PAIS_RESIDENCIA = "170";
    private static final String CODIGO_PAIS_ORIGEN = "170";
    private static final String CODIGO_MUNICIPIO = "47001";
    private static final String CODIGO_ZONA_RESIDENCIA = "01";
    private static final String INCAPACIDAD = "NO";
    private static final String CODIGO_PRESTADOR = "470010100901";
    private static final String CODIGO_CONSULTA = "890322";
    private static final String MODALIDAD_GRUPO_SERVICIO_TECSAL = "01";
    private static final String GRUPO_SERVICIO = "01";
    private static final String FINALIDAD_TECNOLOGIA_SALUD = "16";
    private static final String CAUSA_MOTIVO_ATENCION = "38";
    private static final String CODIGO_DIAGNOSTICO_PPAL = "K029";
    private static final String TIPO_DIAGNOSTICO_PPAL = "01";
    private static final String CONCEPTO_RECAUDO = "05";
    private static final Integer CODIGO_SERVICIO = 338;

    private String Encode() {
        String cifrado = formatter.format(new Date()) + System.currentTimeMillis();
        return cifrado;
    }

    public void GenerarInforme(
            int categoria, int informe, Map<String, String> params
    ) {

        String encode = Encode();
        String nombre = "RP" + "_" + encode + ".json";
        String ruta = Parametros.dirInformesRips + nombre;

        String contenido = getInformeRips(params);
        Utilidades.crearArchivo(ruta, contenido);
        int result = JOptionPane.showConfirmDialog(null, "¿Desea abrir el documento?");
        if (result == JOptionPane.YES_OPTION) {
            try {
                Desktop.getDesktop().open(new File(ruta));
            } catch (IOException ex) {
                mostrarMensaje("No se pudo abrir el archivo "+nombre);
            }
        }
    }

    private String getInformeRips(Map<String, String> params) {
        String mediosDePago = Stream.of(
                new MediosDePago(
                        EFECTIVO, Boolean.valueOf(params.get("efectivo"))
                ),
                new MediosDePago(
                        TARJETA, Boolean.valueOf(params.get("tarjeta"))
                )
        )
                .filter(MediosDePago::estaSeleccionado)
                .map(MediosDePago::toString)
                .collect(joining(","));

        String consulta = "SELECT\n"
                + "a.fecha_pago AS FECHA_ATENCION,\n"
                + "a.valor_fact as VALOR_FACTURA,\n"
                + "pe.pfk_tipo_documento AS TIPO_DOCUMENTO,\n"
                + "pe.pk_persona AS DOCUMENTO,\n"
                + "pe.sexo as SEXO,\n"
                + "CONCAT_WS(' ', pe.`primer_nombre`, IFNULL(pe.`segundo_nombre`,''), pe.`primer_apellido`, IFNULL(pe.`segundo_apellido`,'')) PACIENTE,\n"
                + "pe.fecha_de_nacimiento as FECHA_NACIMIENTO\n"
                + "FROM facturas a \n"
                + "JOIN pagos p ON p.pk_pago = a.`numero` \n"
                + "JOIN modo_pago mp ON mp.`pfk_pago`=p.`pk_pago` \n"
                + "join pagosxconceptos pxc on pxc.pfk_pago=p.pk_pago and pxc.pfk_paciente=p.pfk_paciente\n"
                + "join conceptos c on c.pk_concepto  = pxc.pfk_concepto \n"
                + "JOIN personas pe ON a.`pfk_paciente`=CONCAT(pe.pfk_tipo_documento,pe.pk_persona)\n"
                + "WHERE  a.estado='pagado' and c.fk_tipo_concepto=1 and \n"
                + "mp.pk_tipo_pago in(" + mediosDePago + ") and \n"
                + "a.`fecha_pago` BETWEEN '" + params.get("fini") + "' AND '" + params.get("ffin") + "';";

        Integer consecutivo = getConsecutivo();
        Integer consecutivoServicio = 0;

        List<Map<String, String>> datos = resultquery.ListSQL(consulta);

        List<Map<String, String>> usuariosDB = Utilidades.data_list(
                1, datos, new String[]{"DOCUMENTO"}
        );

        if (datos.isEmpty()) {
            return "";
        }

        Factura factura = new Factura();
        factura.setNumDocumentoIdObligado(DOCUMENTO_DOCTOR);
        factura.setTipoNota(RS);
        factura.setNumNota(consecutivo.toString());
        List<Usuario> usuarios = new ArrayList<>();

        for (int i = 0; i < usuariosDB.size(); i++) {
            Map<String, String> map = usuariosDB.get(i);
            List<Map<String, String>> data = Utilidades.data_list(
                    3, datos,
                    new String[]{"DOCUMENTO<->" + map.get("DOCUMENTO")}
            );

            usuarios.add(Usuario.builder()
                    .tipoDocumentoIdentificacion(map.get("TIPO_DOCUMENTO"))
                    .numDocumentoIdentificacion(map.get("DOCUMENTO"))
                    .fechaNacimiento(map.get("FECHA_NACIMIENTO"))
                    .codSexo(map.get("SEXO"))
                    .tipoUsuario(TIPO_USUARIO)
                    .codPaisResidencia(CODIGO_PAIS_RESIDENCIA)
                    .codMunicipioResidencia(CODIGO_MUNICIPIO)
                    .codZonaTerritorialResidencia(CODIGO_ZONA_RESIDENCIA)
                    .codPaisOrigen(CODIGO_PAIS_ORIGEN)
                    .incapacidad(INCAPACIDAD)
                    .consecutivo(i + 1)
                    .servicios(getServiciosConConsultas(
                            data, consecutivoServicio
                    ))
                    .build());

            consecutivoServicio += data.size();
        }
        factura.setUsuarios(usuarios);
        
        actualizarConsecutivo();

        return stringify(factura);
    }

    private Integer getConsecutivo() {
        return Integer.parseInt(
                resultquery.unicoDato(
                        "select consecutivo + 1  from consecutivo_rips"
                )
        );
    }

    private void actualizarConsecutivo() {
        try {
            ArrayList<String> consultas = new ArrayList<String>();
            consultas.add(
                    "UPDATE consecutivo_rips SET consecutivo=consecutivo + 1,"
                            + "usuario='" + datosUsuario.datos.get(0)[0] + "';"
            );
            resultquery.EnviarConsultas(consultas);
        } catch (Exception ex) {
            
        }
    }

    private Servicios getServiciosConConsultas(
            List<Map<String, String>> data, Integer consecutivo
    ) {
        Servicios servicios = new Servicios();
        List<Consulta> consultas = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            Map<String, String> map = data.get(i);
            consultas.add(Consulta.builder()
                    .consecutivo(consecutivo + i + 1)
                    .codPrestador(CODIGO_PRESTADOR)
                    .codConsulta(CODIGO_CONSULTA)
                    .modalidadGrupoServicioTecSal(MODALIDAD_GRUPO_SERVICIO_TECSAL)
                    .grupoServicios(GRUPO_SERVICIO)
                    .codServicio(CODIGO_SERVICIO)
                    .finalidadTecnologiaSalud(FINALIDAD_TECNOLOGIA_SALUD)
                    .causaMotivoAtencion(CAUSA_MOTIVO_ATENCION)
                    .codDiagnosticoPrincipal(CODIGO_DIAGNOSTICO_PPAL)
                    .tipoDiagnosticoPrincipal(TIPO_DIAGNOSTICO_PPAL)
                    .tipoDocumentoIdentificacion(TIPO_DOCUMENTO_DOCTOR)
                    .numDocumentoIdentificacion(DOCUMENTO_DOCTOR)
                    .conceptoRecaudo(CONCEPTO_RECAUDO)
                    .vrServicio(new BigDecimal(map.get("VALOR_FACTURA")))
                    .valorPagoModerador(BigDecimal.ZERO)
                    .build());
        }
        servicios.setConsultas(consultas);
        return servicios;
    }

}
