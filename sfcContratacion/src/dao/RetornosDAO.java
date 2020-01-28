package dao;

import conectBD.UConnection;
import entidades.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collection;
import java.util.Vector;

public class RetornosDAO {

	public Collection<interfaceDTO> getLstCasos(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		Vector<interfaceDTO> miLista = new Vector<interfaceDTO>();
		
		String sql = query1();

		try{
			con = UConnection.getConnection();
			pstm = con.prepareStatement(sql);


			rs = pstm.executeQuery();
			
			while(rs.next()){
				interfaceDTO miReg = new interfaceDTO();
				
				miReg.caso = rs.getLong("caso");
				miReg.nro_orden = rs.getLong("nro_orden");
				miReg.tipo_sol = rs.getString("tipo_sol");
				miReg.estado = rs.getInt("estado");
				
				miLista.add(miReg);
			}
			
		}catch(SQLException ex){
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
		
		return miLista;
	}

	public retoOT_DTO getStsOT(long caso, long orden) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		retoOT_DTO miReg = new retoOT_DTO();
		
		String sql = query2();

		try{
			con = UConnection.getConnection();
			pstm = con.prepareStatement(sql);

			pstm.setLong(1, caso);
			pstm.setLong(2, orden);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				miReg.tipo_orden = rs.getString("tipo_orden");
				miReg.numero_orden = rs.getString("numero_orden");
				miReg.mensaje_xnear = rs.getLong("mensaje_xnear");
				miReg.ident_etapa = rs.getString("ident_etapa");
				miReg.status = rs.getString("ots_status");
				miReg.fecha_proc = rs.getString("ots_fecha_proc");
			}
			
		}catch(Exception ex){
			System.out.println("getStsOT()");
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
		
		return miReg;
	}

	public Boolean setStatus(retoOT_DTO reg, long Caso, long Orden, String sJsonRta) {
		Connection con = null;
		PreparedStatement pstm = null;
		
		String sql = query3();
	
		try{
			con = UConnection.getConnection();
			pstm = con.prepareStatement(sql);

			pstm.setInt(1, reg.iSts);
			pstm.setString(2, reg.DescriSts);
			pstm.setString(3, reg.fecha_proc);
			pstm.setString(4, sJsonRta);
			
			pstm.setLong(5, Caso);
			pstm.setLong(6, Orden);
			
			pstm.executeUpdate();
			
		}catch(Exception ex){
			System.out.println("setStatus()");
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}finally{
			try{
				if(pstm != null) pstm.close();
			}catch(Exception ex){
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}		
		
		return true;
	}
	
	public periodosDTO getPeriodoDT() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		periodosDTO miReg = new periodosDTO();
		
		String sql = query4("DT");

		try{
			con = UConnection.getConnection();
			pstm = con.prepareStatement(sql);

			rs = pstm.executeQuery();

			if(rs.next()) {
				miReg.dtFechaDesde = rs.getString("fDesde");
				miReg.dtFechaHasta = rs.getString("fhasta");
			}

		}catch(Exception ex){
			System.out.println("getPeriodoDT()");
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
		
		return miReg;
	}

	public Collection<traceCarpetasDTO> getUbicaMensaje(Long lNroMsg) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		Vector<traceCarpetasDTO> miLista = new Vector<traceCarpetasDTO>();
		String sql = query5();

		try{
			con = UConnection.getConnection();
			pstm = con.prepareStatement(sql);
			pstm.setLong(1, lNroMsg);
			
			rs = pstm.executeQuery();

			while(rs.next()) {
				traceCarpetasDTO miReg = new traceCarpetasDTO();
				
				miReg.dtFecha = rs.getString("fecha");
				miReg.sCarpeta = rs.getString("carpeta_destino");
				
				miLista.add(miReg);
			}

		}catch(Exception ex){
			System.out.println("getUbicaMensaje()");
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
		
		return miLista;
	}
	
	public String getTextonMensaje(Long lNroMsg) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String	sTexton="";
		String	sLinea="";


		String sql = query6();

		try{
			con = UConnection.getConnection();
			pstm = con.prepareStatement(sql);
			pstm.setLong(1, lNroMsg);
			
			rs = pstm.executeQuery();

			while(rs.next()) {
				sLinea = rs.getString("texton");
				sTexton += sLinea.trim();
			}

		}catch(Exception ex){
			System.out.println("getTextonMensaje()");
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
		
		return sTexton;
	}

	public retoOT_DTO getCtoSegen(Long lNroCaso, Long lNroOrden){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		retoOT_DTO miReg = new retoOT_DTO();
		
		String sql = query7();

		try{
			con = UConnection.getConnection();
			pstm = con.prepareStatement(sql);
			pstm.setLong(1, lNroCaso);
			pstm.setLong(2, lNroOrden);
			pstm.setLong(3, lNroCaso);
			pstm.setLong(4, lNroOrden);
			
			rs = pstm.executeQuery();
			
			if(rs.next()){
				miReg.co_numero = rs.getLong("nro_contacto");
				miReg.co_sucursal = rs.getString("suc_contacto");
				miReg.mensaje_xnear = rs.getLong("se_mensaje");
				miReg.rol_actual = rs.getString("rol_actual");
				miReg.status=rs.getString("etapa");
				miReg.ident_etapa = rs.getString("estado");
				miReg.fecha_proc = rs.getString("fecha_modif");
				miReg.sResultadoContacto = rs.getString("rm_cod_resp");
			}
			
		}catch(SQLException ex){
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
		
		return miReg;
	}
	
	public costoOT_DTO getCostoOT(Long lNroMensaje) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		costoOT_DTO miReg = new costoOT_DTO();
		
		String sql = query8();

		try{
			con = UConnection.getConnection();
			pstm = con.prepareStatement(sql);
			pstm.setLong(1, lNroMensaje);
			
			rs = pstm.executeQuery();

			if(rs.next()) {
				miReg.bExiste=true;
				miReg.dCostoMat = rs.getDouble("costo_materiales");
				miReg.dCostoMo = rs.getDouble("costo_mano_obra");
				miReg.dCostoTotal = rs.getDouble("costo_total");
			}else {
				miReg.bExiste = false;
			}

		}catch(Exception ex){
			System.out.println("getCostoOT()");
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
		return miReg;
	}
	
	private String query1() {
		String sql;
		
		sql = "SELECT i.caso, i.nro_orden, i.tipo_sol, i.estado ";
		sql += "FROM sfc_interface i, ";
		sql += "WHERE i.tarifa = 'T1' ";
		sql += "AND i.estado BETWEEN 1 AND 2 ";
		
		return sql;
	}
	
	
	private String query2() {
		String sql;
		
		sql = "SELECT o.tipo_orden, o.numero_orden, o.mensaje_xnear, o.ident_etapa, ";
		sql += "h1.ots_status, h1.ots_fecha_proc ";
		sql += "FROM orden o, ot_mac om, ot_hiseven h1 ";
		sql += "WHERE o.sfc_caso = ? ";
		sql += "AND o.sfc_nro_orden = ? ";
		sql += "AND om.ot_mensaje_xnear = o.mensaje_xnear ";
		sql += "AND h1.ots_nro_orden = om.ot_nro_orden ";
		sql += "AND h1.ots_fecha_proc = (SELECT MAX(h2.ots_fecha_proc) ";
		sql += "	FROM ot_hiseven h2 ";
		sql += " 	WHERE h2.ots_nro_orden = om.ot_nro_orden) ";
		
		return sql;
	}
	
	private String query3() {
		String sql;
		
		sql = "UPDATE sfc_interface SET ";
		sql += "estado = ?, ";
		sql += "descri_estado = ?, ";
		sql += "fecha_estado = ?, ";
		sql += "data_out = ? ";
		sql += "WHERE caso = ? ";
		sql += "AND orden = ? ";

		return sql;
	}
	
	private String query4(String sTipo) {
		String sql;
		
		if(sTipo.equals("DT")) {
			sql = "SELECT TO_CHAR(TODAY, '%Y-%m-%d') || ' 00:00:00' fdesde, ";
			sql += "TO_CHAR(TODAY, '%Y-%m-%d') || ' 23:59:59' fhasta ";
			sql += "FROM dual ";
		}else {
			sql = "SELECT TODAY fdesde, TODAY fhasta ";
			sql += "FROM dual ";
		}

		return sql;
	}

	private String query5() {
		String sql;
		
		sql = "SELECT fecha, carpeta_destino ";
		sql += "FROM xnear2:historia ";
		sql += "WHERE servidor = 1 ";
		sql += "AND mensaje = ? ";
		sql += "AND fecha BETWEEN ? AND ? ";
		sql += "ORDER BY fecha DESC ";

		return sql;
	}

	private String query6() {
		String sql;
		
		sql = "SELECT texton, pagina ";
		sql += "FROM xnear2:pagina ";
		sql += "WHERE servidor = 1 ";
		sql += "AND mensaje = ? ";
		sql += "ORDER BY pagina ASC ";

		return sql;
	}

	private String query7() {
		String sql;
		
		sql = "SELECT c.co_numero nro_contacto, "; 
		sql += "c.co_suc_contacto suc_contacto, "; 
		sql += "s.se_mensaje, ";
		sql += "m.rol_actual, ";
		sql += "m.fecha_modif, ";
		sql += "m.etapa, ";
		sql += "m.estado, ";
		sql += "rta.rm_cod_resp ";
		sql += "FROM contacto:ct_contacto c, contacto:ct_motivo mo, "; 
		sql += "contacto:ct_segen s, OUTER xnear2:mensaje m, OUTER contacto:ct_rta_motivos rta ";
		sql += "WHERE c.sfc_caso = ? ";
		sql += "AND c.sfc_nro_orden = ? ";
		sql += "AND mo.mo_co_numero = c.co_numero ";
		sql += "AND mo.mo_suc_contacto = c.co_suc_contacto ";
		sql += "AND mo.mo_principal = '1' ";
		sql += "AND s.se_co_numero = c.co_numero ";
		sql += "AND s.se_suc_contacto = c.co_suc_contacto ";
		sql += "AND m.servidor = 1 ";
		sql += "AND m.mensaje = s.se_mensaje ";
		sql += "AND rta.rm_co_numero = c.co_numero ";
		sql += "AND rta.rm_suc_contacto = c.co_suc_contacto ";
		sql += "AND rta.rm_cod_motivo = mo.mo_cod_motivo ";
		sql += "AND rta.rm_cod_mot_empresa = mo.mo_cod_mot_empresa ";
		
		sql += "UNION ";

		sql += "SELECT c.cf_numero nro_contacto, ";  
		sql += "c.cf_suc_contacto suc_contacto, ";  
		sql += "s.se_mensaje, ";
		sql += "m.rol_actual, ";
		sql += "m.fecha_modif, ";
		sql += "m.etapa, ";
		sql += "m.estado, ";
		sql += "rta.rm_cod_resp "; 
		sql += "FROM contacto:ct_contacto_final c, contacto:ct_motivo_final mo, ";  
		sql += "contacto:ct_segen s, OUTER xnear2:mensaje m, OUTER contacto:ct_rta_motivos rta "; 
		sql += "WHERE c.sfc_caso = ? ";
		sql += "AND c.sfc_nro_orden = ? ";
		sql += "AND mo.mf_co_numero = c.cf_numero "; 
		sql += "AND mo.mf_suc_contacto = c.cf_suc_contacto "; 
		sql += "AND mo.mf_principal = '1' ";
		sql += "AND s.se_co_numero = c.cf_numero "; 
		sql += "AND s.se_suc_contacto = c.cf_suc_contacto "; 
		sql += "AND m.servidor = 1 "; 
		sql += "AND m.mensaje = s.se_mensaje "; 
		sql += "AND rta.rm_co_numero = c.cf_numero ";
		sql += "AND rta.rm_suc_contacto = c.cf_suc_contacto "; 
		sql += "AND rta.rm_cod_motivo = mo.mf_cod_motivo ";
		sql += "AND rta.rm_cod_mot_empresa = mo.mf_cod_mot_empresa "; 

		return sql;
	}
	

	private String query8() {
		String sql;
		
		sql = "SELECT costo_mano_obra, costa_materiales, costo_total ";
		sql += "FROM rec_interc ";
		sql += "WHERE origen = 'MAC' ";
		sql += "AND nro_mensaje = ? ";
		sql += "AND estado = 'EJECUTADO' ";
				
		return sql;
	}
	
}
