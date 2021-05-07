package dao;
import com.sun.xml.internal.ws.util.StringUtils;
import conectBD.UConnection;
import entidades.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collection;
import java.util.Vector;

public class PruebasDAO {

    public pruebaDTO lanzaGral(String sProcedimiento, String sMotivo, Long lNroCliente) {
        pruebaDTO miPrueba = new pruebaDTO();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String	sNroOrden;
        String  sNvoNroOrden;
        String	sRol="";
        String  sArea="";
        String  sAuxProced="";
        String  xproEstado="";
        Long    nroMensaje;
        String  xproEtapa="";
        int     iCodRetorno;
        String  sDescRetorno="";
        String  sNroOt="";

        System.out.println(String.format("PruebasDAO.LanzaGral recibio %s %s %d", sProcedimiento, sMotivo, lNroCliente));

        if(sProcedimiento.equals("RETCLI")){
            sAuxProced="RET";
        }else{
            sAuxProced="MAN";
        }
        try{
System.out.println("PUnto 0");
            con = UConnection.getConnection();
            pstm = con.prepareStatement(SEL_AREA_DESTINO);
            pstm.setLong(1, lNroCliente);
            pstm.setString(2,sAuxProced);

            rs = pstm.executeQuery();
System.out.println("PUnto 1");
            while(rs.next()) {
                sRol = rs.getString("rol");
                sArea = rs.getString("area");

            }
            rs=null;
            pstm=null;
System.out.println("PUnto 2");
            //Recuperar nro de orden
            pstm = con.prepareStatement(SEL_NRO_ORDEN);
            pstm.setString(1, sAuxProced);
            pstm.setString(2,sArea);
            rs = pstm.executeQuery();
            rs.next();
            sNroOrden=rs.getString(1);
            sNvoNroOrden = sArea.trim() + String.format("%012d",Long.parseLong(sNroOrden.trim())+1);
            //sNvoNroOrden = sArea.trim() + StringUtils.leftPad(String.valueOf(Long.parseLong(sNroOrden) +1), 12, "0");
System.out.println("PUnto 3");
            rs=null;
            pstm=null;

            pstm = con.prepareStatement(SET_NRO_ORDEN);
            pstm.setString(1, sNvoNroOrden);
            pstm.setString(2, sAuxProced);
            pstm.setString(3,sArea);
            pstm.executeUpdate();

            pstm=null;
System.out.println("PUnto 4");
            //Recuperar nro.de mensaje
            pstm = con.prepareStatement(GET_NRO_MENSAJE);
            pstm.setString(1, sProcedimiento);
            rs = pstm.executeQuery();
            rs.next();
            nroMensaje=rs.getLong(2);

            rs=null;
            pstm=null;
System.out.println("PUnto 5");
            //Lanzar Operativo
            if(sProcedimiento.equals("RETCLI")){
System.out.println("PUnto 6 nro.mensaje " + nroMensaje);
                pstm = con.prepareStatement(SET_RETCLI);
                pstm.setString(1, sMotivo);
                pstm.setLong(2, lNroCliente);
                pstm.setLong(3, nroMensaje);
                pstm.setString(4, sNroOrden);

                rs = pstm.executeQuery();
System.out.println("PUnto 7");
                rs.next();
                iCodRetorno = rs.getInt(1);
                sDescRetorno = rs.getString(2);
                sNroOt = rs.getString(3);
System.out.println("PUnto 8");
            }else{
                pstm = con.prepareStatement(SET_MANSER);
                pstm.setString(1, sMotivo);
                pstm.setLong(2, lNroCliente);
                pstm.setLong(3, nroMensaje);
                pstm.setString(4, sNroOrden);

                rs = pstm.executeQuery();
                rs.next();
                iCodRetorno = rs.getInt(1);
                sDescRetorno = rs.getString(2);
                sNroOt = rs.getString(3);
            }
            miPrueba.iCodRetorno = iCodRetorno;
            miPrueba.sDescRetorno = sDescRetorno;
            miPrueba.sNroOrden = sNroOrden;
            miPrueba.sNroOt = sNroOt;

        }catch(Exception ex){
            System.out.println("lanzaGral()");
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }finally{
            try{
                if(rs != null) rs.close();
                if(pstm != null) pstm.close();
            }catch(Exception ex){
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

        return miPrueba;
    }


    private static final String SEL_AREA_DESTINO = "SELECT s.rol, r.area " +
            "FROM cliente c, sucur_centro_op o, sfc_roles s, rol r " +
            "WHERE c.numero_cliente = ? " +
            "and o.cod_centro_op = c.sucursal " +
            "AND s.procedimiento = ? " +
            "AND s.sucursal = o.cod_sucur  " +
            "AND r.rol = s.rol ";

    private static final String SEL_NRO_ORDEN = "SELECT substr(numero, 5) " +
            "FROM numao " +
            "WHERE tipo_orden = ? " +
            "AND area = ? ";


    private static final String SET_NRO_ORDEN = "UPDATE numao SET " +
            "numero = ? " +
            "WHERE tipo_orden = ? " +
            "AND area = ? ";

    private static final String GET_NRO_MENSAJE = "call xpro_crear(1, ?) ";

    private static final String SET_RETCLI = "call sfc_retcli(?, ?, ?, ?) ";

    private static final String SET_MANSER = "call sfc_manser(?, ?, ?, ?) ";
}
