package dao;

import conectBD.UConnection;
/*
import entidades.dataIncorpoDTO;
import entidades.interfaceDTO;
import entidades.SolSumDTO;
import entidades.MensajeDTO;
*/
import entidades.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collection;
import java.util.Vector;


public class SuministroDAO {

	public Collection<interfaceDTO> getLstSolicitudes(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		Vector<interfaceDTO> miLista = new Vector<interfaceDTO>();
		
		String sql = query8();

		try{
			con = UConnection.getConnection();
			pstm = con.prepareStatement(sql);


			rs = pstm.executeQuery();
			
			while(rs.next()){
				interfaceDTO miReg = new interfaceDTO();
				
				miReg.caso = rs.getLong("caso");
				miReg.nro_orden = rs.getLong("nro_orden");
				miReg.data_in = rs.getString("data_in");
				miReg.tipo_sol = rs.getString("tipo_sol");
				
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
	
	
	public long getNroMensaje(String sProcedimiento){
		Connection con = null;
		PreparedStatement pstm = null;
		CallableStatement cal = null;
		
		ResultSet rs = null;
		
		String sEstado;
		long lNroMensaje=0;
		String sEtapa;
		String sql;
		
		sql = query1(sProcedimiento);
		
		try{
			con = UConnection.getConnection();
			cal = con.prepareCall(sql);
			rs = cal.executeQuery();
			//pstm = con.prepareStatement(sql);
			//rs = pstm.executeQuery();

			while(rs.next()){
				sEstado = rs.getString(1);
				lNroMensaje = rs.getLong(2);
				sEtapa = rs.getString(3);
			}
		}catch(Exception ex){
			System.out.println("getNroMensaje()");
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}finally{
			try{
				if(rs != null) rs.close();
				if(cal != null) cal.close();
			}catch(Exception ex){
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
		
		
		return lNroMensaje;
	}
	
	public String getRolDestino(String sProcedimiento, String sSucursal) {
		String sRol="";
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
	
		String sql;
		
		sql = query2(sProcedimiento, sSucursal);
		
		try{
			con = UConnection.getConnection();
			pstm = con.prepareStatement(sql);

			pstm.setString(1, sProcedimiento);
			pstm.setString(2, sSucursal);

			rs = pstm.executeQuery();
			
			while(rs.next()){
				sRol = rs.getString(1);
			}
			
		}catch(Exception ex){
			System.out.println("getRolDestino()");
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
		return sRol;
	}
	
	public Boolean regSolSumin(dataIncorpoDTO regInter, long lNroMensaje, String sRolOrigen, String sRolDestino) {
		
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String sql;
		long lNroSolicitud;
	
		try{
			con = UConnection.getConnection();
			con.setAutoCommit(false);
		
			//Obtener Nro Solicitud
			sql = query3();
			pstm = con.prepareStatement(sql);
			rs = pstm.executeQuery();
			rs.next();
			lNroSolicitud = rs.getLong(1);
			rs=null;
			pstm=null;

			//Arma Solicitud
			SolSumDTO regSol = new SolSumDTO(lNroSolicitud, lNroMensaje, regInter);
			
			//Armar Mensaje
			MensajeDTO regMen = new MensajeDTO(lNroMensaje, lNroSolicitud, regSol.sDv, sRolOrigen, sRolDestino, "INCORPORACION");
	
			//Grabar Solicitud
			sql = query4(regSol);
			
			pstm = con.prepareStatement(sql);
			pstm.executeUpdate();
			pstm=null;
	
			//Grabar Estado Solicitud
			sql = query5();
			pstm = con.prepareStatement(sql);
			pstm.setLong(1, lNroSolicitud);
			pstm.setString(2, regInter.Sucursal.trim());
			pstm.setString(3, regInter.Motivo.trim());
			pstm.executeUpdate();
			pstm=null;
	
			if(regSol.sTipoFpago.trim()=="D") {
				//Grabar Solicitud_adhpag
				sql = query16();
				pstm = con.prepareStatement(sql);
				pstm.setLong(1, regSol.lNroCliente);
				pstm.setString(2, regSol.sClienteDV.trim());
				pstm.setString(3, regSol.sCodTarjeta.trim());
				pstm.setString(4, regSol.sNroTarjeta.trim());

				pstm.executeUpdate();
				pstm=null;
			}
			
			//Enviar Mensaje
			sql = query6(regMen);
			pstm = con.prepareStatement(sql);
			pstm.execute();
			pstm=null;
	
			//Actualiza Secuen
			sql = query7();
			pstm = con.prepareStatement(sql);
			pstm.setLong(1, lNroSolicitud);
			pstm.executeUpdate();
			pstm=null;
			//pstm.close();

			//Actualiza Caso
			sql = query9();
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, 1);			
			pstm.setLong(2, regInter.Caso);
			pstm.setLong(3, regInter.NroOrden);
			pstm.setString(4, "INCORPORACION");
			pstm.executeUpdate();
			pstm=null;
			
			con.commit();
		}catch(Exception ex){
			System.out.println("regSolSumin()");
			try {
				con.rollback();
			}catch(SQLException exSQL) {
				exSQL.printStackTrace();
			}
			
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
		
		return true;
	}
	
	public Boolean regManRet(dataManserDTO regInter, long lNroMensaje, String sRolOrigen, String sAreaOrigen, String sRolDestino, String sProcedimiento ) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String sql;
		String sNroOrden;
		long   lNroOrden;
		String sNvoNroOrden;
		String sTema;
		String sTrabajo;
	
		if(sProcedimiento == "MANSER") {
			sTema=regInter.sMotivo.substring(0, 3);
			sTrabajo=regInter.sMotivo.substring(3);
		}else {
			sTema=regInter.sMotivo;
			sTrabajo=" ";
		}
		
		try{
			con = UConnection.getConnection();
			con.setAutoCommit(false);
		
			//Armar Mensaje
			MensajeDTO regMen = new MensajeDTO(lNroMensaje, regInter.nroCliente, regInter.Dv, sRolOrigen, sRolDestino, sProcedimiento);
			
			//Enviar Mensaje
			sql = query6(regMen);
			pstm = con.prepareStatement(sql);
			pstm.execute();
			pstm=null;

			//Inserta en Retcli
			sql = query10();
			pstm = con.prepareStatement(sql);
			pstm.setLong(1, regInter.nroCliente);
			if(sProcedimiento=="MANSER") {
				pstm.setString(2, "C");
			}else {
				pstm.setString(2, "R");				
			}
			pstm.executeUpdate();
			pstm=null;
			
			//Obtener Nro.de Orden
			sql = query11();
			pstm = con.prepareStatement(sql);
			if(sProcedimiento=="MANSER") {
				pstm.setString(1, "MAN");
			}else {
				pstm.setString(1, "RET");
			}
			pstm.setString(2, sAreaOrigen);
			rs = pstm.executeQuery();
			rs.next();
			sNroOrden = rs.getString("numero");
			sNroOrden=sNroOrden.trim();
			
			rs=null;
			pstm=null;
			
			//Actualiza Numao
			lNroOrden = Long.parseLong(sNroOrden.trim())+1;
			sNvoNroOrden = String.format("%06d", lNroOrden);
			
			sql = query12();
			pstm = con.prepareStatement(sql);
			pstm.setString(1, sNvoNroOrden);
			if(sProcedimiento=="MANSER") {
				pstm.setString(2, "MAN");
			}else {
				pstm.setString(2, "RET");
			}
			pstm.setString(3, sAreaOrigen);
			pstm.executeUpdate();
			pstm=null;

			//Grabar Orden
			sql = query13();
			pstm = con.prepareStatement(sql);
			if(sProcedimiento=="MANSER") {
				pstm.setString(1, "MAN");
			}else {
				pstm.setString(1, "RET");
			}
			
			pstm.setString(2, sNroOrden.trim());
			pstm.setLong(3, lNroMensaje);
			pstm.setString(4, regInter.Sucursal);
			
			pstm.setString(5, sRolOrigen.trim());
			
			pstm.setString(6, sAreaOrigen.trim());
			pstm.setString(7, sRolOrigen.trim());
			pstm.setString(8, sTema.trim());
			pstm.setString(9, sTrabajo);
			pstm.setLong(10, regInter.nroCliente);
			pstm.setLong(11,regInter.Caso);
			pstm.setLong(12, regInter.NroOrden);
			pstm.executeUpdate();
			pstm=null;
			
			//Actualiza Caso
			sql = query9();
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, 1);
			pstm.setLong(2, regInter.Caso);
			pstm.setLong(3, regInter.NroOrden);
			pstm.setString(4, sProcedimiento);
			pstm.executeUpdate();
			pstm=null;
			
			con.commit();
		}catch(Exception ex){
			System.out.println("regManRet()");
			try {
				con.rollback();
			}catch(SQLException exSQL) {
				exSQL.printStackTrace();
			}
			
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
		
		return true;
	}

	public Boolean regRetcli(dataRetcliDTO regInter, long lNroMensaje, String sRolOrigen, String sAreaOrigen, String sRolDestino, String sProcedimiento ) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String sql;
		String sNroOrden;
		long   lNroOrden;
		String sNvoNroOrden;
		String sTema;
		String sTrabajo;
	
		sTema=regInter.sMotivo;
		sTrabajo=" ";

		
		try{
			con = UConnection.getConnection();
			con.setAutoCommit(false);
		
			//Armar Mensaje
			MensajeDTO regMen = new MensajeDTO(lNroMensaje, regInter.nroCliente, regInter.Dv, sRolOrigen, sRolDestino, sProcedimiento);
			
			//Enviar Mensaje
			sql = query6(regMen);
			
			pstm = con.prepareStatement(sql);
			pstm.execute();
			pstm=null;

			//Inserta en Retcli
			sql = query10();
			pstm = con.prepareStatement(sql);
			pstm.setLong(1, regInter.nroCliente);
			pstm.setString(2, "R");				

			pstm.executeUpdate();
			pstm=null;
			
			//Obtener Nro.de Orden
			sql = query11();
			pstm = con.prepareStatement(sql);
			pstm.setString(1, "RET");

			pstm.setString(2, sAreaOrigen);
			rs = pstm.executeQuery();
			rs.next();
			sNroOrden = rs.getString("numero");
			sNroOrden=sNroOrden.trim();
			
			rs=null;
			pstm=null;
			
			//Actualiza Numao
			lNroOrden = Long.parseLong(sNroOrden.trim())+1;
			sNvoNroOrden = String.format("%06d", lNroOrden);
			
			sql = query12();
			pstm = con.prepareStatement(sql);
			pstm.setString(1, sNvoNroOrden);
			pstm.setString(2, "RET");

			pstm.setString(3, sAreaOrigen);
			pstm.executeUpdate();
			pstm=null;

			//Grabar Orden
			sql = query13();
			pstm = con.prepareStatement(sql);
			pstm.setString(1, "RET");
			
			pstm.setString(2, sNroOrden.trim());
			pstm.setLong(3, lNroMensaje);
			pstm.setString(4, regInter.Sucursal);
			
			pstm.setString(5, sRolOrigen.trim());
			
			pstm.setString(6, sAreaOrigen.trim());
			pstm.setString(7, sRolOrigen.trim());
			pstm.setString(8, sTema.trim());
			pstm.setString(9, sTrabajo);
			pstm.setLong(10, regInter.nroCliente);
			pstm.setLong(11,regInter.Caso);
			pstm.setLong(12, regInter.NroOrden);
			pstm.executeUpdate();
			pstm=null;
			
			//Actualiza Caso
			sql = query9();
			pstm = con.prepareStatement(sql);
			pstm.setInt(1, 1);
			pstm.setLong(2, regInter.Caso);
			pstm.setLong(3, regInter.NroOrden);
			pstm.setString(4, sProcedimiento);
			pstm.executeUpdate();
			pstm=null;
			
			con.commit();
		}catch(Exception ex){
			System.out.println("regRetcli()");
			try {
				con.rollback();
			}catch(SQLException exSQL) {
				exSQL.printStackTrace();
			}
			
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
		
		return true;
	}	
	
	public ClienteDTO getDataCliente(long NroCliente) {
		ClienteDTO miReg = new ClienteDTO();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		String sql;
		
		sql = query14();
		
		try{
			con = UConnection.getConnection();
			pstm = con.prepareStatement(sql);

			pstm.setLong(1, NroCliente);

			rs = pstm.executeQuery();
			
			while(rs.next()){
				miReg.sSucursal = rs.getString("sucursal");
				miReg.sDV = rs.getString("dv_numero_cliente");
			}
			
		}catch(Exception ex){
			System.out.println("getRolDestino()");
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
	
	public String getTrafoSapMac(String sClave, String sCampo, String sValorFC) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String sValorMAC="";
		
		String sql;
		
		sql = query17(sClave.trim(), sCampo.trim());
		
		try{
			con = UConnection.getConnection();
			pstm = con.prepareStatement(sql);
			pstm.setString(1, sValorFC.trim());

			rs = pstm.executeQuery();
			
			if(rs.next()){
				sValorMAC = rs.getString("cod_mac");
			}
			
		}catch(Exception ex){
			System.out.println("getTrafoSapMac()");
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
		return sValorMAC;		
		
	}
	
	

	
	private String query1(String sProcedimiento) {
		String sql;
		
		//sql = "EXECUTE PROCEDURE xpro_crear(1, '" + sProcedimiento + "')";
		sql = "{call xpro_crear(1, '" + sProcedimiento + "')}";
		
		return sql;
	}

	private String query2(String sProcedimiento, String sSucursal) {
		String sql;
		
		sql = "SELECT rol FROM sfc_roles ";
		sql += "WHERE procedimiento = ? ";
		sql += "AND sucursal = ? ";
		
		return sql;
	}
	
	private String query3() {
		String sql;
		
		sql ="SELECT valor + 1 ";
		sql += "FROM secuen ";
		sql += "WHERE codigo = 'SOLSUM' ";
		sql += "FOR UPDATE ";
		
		return sql;

	}
	
	private String query4(SolSumDTO reg) {
		String sql;
		
		sql = "INSERT INTO solicitud ( ";
		sql += "nro_solicitud, ";				//1
		sql += "dv_ss, ";						//2
		sql += "nombre, ";						//3
		sql += "tip_doc, ";						//4
		sql += "nro_doc, ";						//5
		sql += "provincia, ";					//6
		sql += "partido, ";						//7
		sql += "localidad, ";					//8
		sql += "cod_calle, ";					//9
		sql += "nom_calle, ";					//10
		sql += "nro_dir, ";						//11
		sql += "piso_dir, ";					//12
		sql += "depto_dir, ";					//13
		sql += "cod_postal, ";					//14
		sql += "mensaje, ";						//15
		sql += "servidor, ";					//16
		sql += "tipo_iva, ";					//17
		sql += "tipo_venc, ";					//18
		sql += "cod_propiedad, ";				//19
		sql += "ciiu, ";						//20
		sql += "sucursal, ";					//21
		sql += "tipo_reparto, ";				//22
		sql += "estado, ";						//23
		sql += "centro_trans, ";				//24
		sql += "tipo_sum, ";					//25
		sql += "tarifa, ";						//26
		sql += "nro_cuit, ";					//27
		sql += "voltaje_solicitado, ";			//28
		sql += "telefono, ";					//29
		sql += "corr_factint, ";				//30
		sql += "modifica_red, ";				//31
		sql += "rst_fecha_inicio, ";			//32
		sql += "tipo_cliente, ";				//33
		sql += "numero_cliente, ";				//34
		sql += "tipo_fpago, ";					//35
		
		sql += "fp_banco, ";					//36
		sql += "fp_tipocuenta, ";				//37
		sql += "fp_nrocuenta, ";				//38
		
		sql += "dp_nom_provincia, ";			//39
		sql += "dp_nom_localidad, ";			//40
		sql += "dp_nom_calle, ";				//41
		sql += "dp_nro_dir, ";					//42
		sql += "dp_cod_postal, ";				//43
		sql += "dv_numero_cliente, ";			//44
		sql += "sfc_caso, ";					//45
		sql += "sfc_nro_orden ";				//46
		   
		sql += ")VALUES( ";
		sql += reg.lNroSolicitud + ", ";			//1
		sql += "'" + reg.sDv + "', ";				//2
		sql += "'" + reg.sNombre.trim() + "', ";	//3
		if(reg.sTipoDoc == null || reg.sTipoDoc.equals("")) {					//4
			sql += "NULL, ";
		}else {
			sql += "'" + reg.sTipoDoc.trim() + "', ";
		}
		if(reg.lNroDoc <= 0) {						//5
			sql += "0, ";
		}else {
			sql += reg.lNroDoc + ", ";
		}
		if(reg.sProvincia == null || reg.sProvincia.equals("")) {				//6
			sql += "NULL, ";
		}else {
			sql += "'" + reg.sProvincia.trim() + "', ";
		}
		if(reg.sPartido == null || reg.sPartido.equals("")) {					//7
			sql +="NULL, ";
		}else {
			sql += "'" + reg.sPartido.trim() + "', ";
		}
		if(reg.sLocalidad == null || reg.sLocalidad.equals("")) {				//8
			sql +="NULL, ";
		}else {
			sql += "'" + reg.sLocalidad.trim() + "', ";
		}
		if(reg.sCodCalle == null || reg.sCodCalle.equals("")) {					//9
			sql += "NULL, ";
		}else {
			sql += "'" + reg.sCodCalle.trim() + "', ";
		}
		if(reg.sNomCalle == null || reg.sNomCalle.equals("")) {					//10
			sql += "NULL, ";
		}else {
			sql += "'" + reg.sNomCalle.trim() + "', ";
		}
		if(reg.sNroDir == null || reg.sNroDir.equals("")) {					//11
			sql += "NULL, ";
		}else {
			sql += "'" + reg.sNroDir.trim() + "', ";
		}
		if(reg.sPiso == null || reg.sPiso.equals("")) {						//12
			sql += "NULL, ";
		}else {
			sql += "'" + reg.sPiso.trim() + "', ";
		}
		if(reg.sDepto == null || reg.sDepto.equals("")) {					//13
			sql+= "NULL, ";
		}else {
			sql += "'" + reg.sDepto.trim() + "', ";
		}
		if(reg.codPost<=0 ) {						//14
			sql += "NULL, ";
		}else {
			sql += reg.codPost + ", ";
		}
		
		sql += reg.lNroMensaje + ", ";				//15
		
		sql += reg.iServidor + ", ";				//16
		if(reg.sTipoIva == null || reg.sTipoIva.equals("")) {					//17
			sql += "NULL, ";
		}else {
			sql += "'" + reg.sTipoIva.trim() + "', ";
		}
		if(reg.sTipoVenc == null || reg.sTipoVenc.equals("")) {					//18
			sql += "NULL, ";
		}else {
			sql += "'" + reg.sTipoVenc.trim() + "', ";
		}
		if(reg.sCodPropiedad == null || reg.sCodPropiedad.equals("")) {				//19
			sql += "NULL, ";
		}else {
			sql += "'" + reg.sCodPropiedad.trim() + "', ";
		}
		if(reg.sCiiu == null || reg.sCiiu.equals("")) {						//20
			sql += "NULL, ";
		}else {
			sql += "'" + reg.sCiiu.trim() + "', ";
		}
		if(reg.sSucursal.trim()=="" || reg.sSucursal.equals("")) {				//21
			sql += "NULL, ";
		}else {
			sql += "'" + reg.sSucursal.trim() + "', ";
		}

		sql += "'" + reg.sTipoReparto.trim() + "', ";		//22
		sql += "'" + reg.sEstado.trim() + "', ";			//23
		sql += "'" + reg.sCentroTrans.trim() + "', ";		//24
		
		if(reg.sTipoSum.length()==0) {						//25
			sql += "'0', ";
		}else {
			sql += "'" + reg.sTipoSum.trim() + "', ";
		}
		if(reg.Tarifa == null || reg.Tarifa.equals("")) {							//26
			sql += "NULL, ";
		}else {
			sql += "'" + reg.Tarifa.trim() + "', ";
		}
		
		if(reg.nro_cuit.length()==0 || reg.nro_cuit.equals("")) {						//27
			sql += "NULL, ";
		}else {
			sql += "'" + reg.nro_cuit.trim() + "', ";
		}
		if(reg.voltaje_solicitado == null || reg.voltaje_solicitado.equals("")) {				//28
			sql += "NULL, ";
		}else {
			sql += "'" + reg.voltaje_solicitado.trim() + "', ";
		}
		if(reg.telefono == null || reg.telefono.equals("")) {							//29
			sql += "NULL, ";
		}else {
			sql += "'" + reg.telefono.trim() + "', ";	
		}
		
		sql += "0, ";				//corr_factint			//30
		sql += "'N', ";				//modifica_red			//31
		sql += "TODAY, ";			//modifica_red			//32
		if(reg.sTipoCliente == null || reg.sTipoCliente.trim().equals("")) {						//33
			sql += "NULL,";
		}else {
			sql += "'" + reg.sTipoCliente.trim() + "', ";
		}
		if(reg.lNroCliente>0) {								//34
			sql += reg.lNroCliente + ", "; 
		}else {
			sql += "NULL,";
		}
		sql += "'" + reg.sTipoFpago.trim() + "', ";			//35
		if(reg.sTipoFpago.trim().equals("D")){
			sql += "'" + reg.sCodTarjeta.trim() + "', ";	//36
			sql += " 1, ";									//37
			sql += "'" + reg.sNroTarjeta.trim() + "', ";	//38
		}else {
			sql += "NULL, ";
			sql += "NULL, ";
			sql += "NULL, ";
		}
		
		if(reg.dp_nom_provincia == null || reg.dp_nom_provincia.equals("")) {					//39
			sql += "NULL, ";
		}else {
			sql += "'" + reg.dp_nom_provincia.trim() + "', ";
		}
		if(reg.dp_nom_localidad == null || reg.dp_nom_localidad.equals("")) {					//40
			sql += "NULL, ";
		}else {
			sql += "'" + reg.dp_nom_localidad.trim() + "', ";
		}		
		if(reg.dp_nom_calle == null || reg.dp_nom_calle.equals("")) {						//41
			sql += "NULL, ";
		}else {
			sql += "'" + reg.dp_nom_calle.trim() + "', ";				
		}		
		if(reg.dp_nro_dir == null || reg.dp_nro_dir.equals("")) {						//42
			sql += "NULL, ";
		}else {
			sql += "'" + reg.dp_nro_dir.trim() + "', ";
		}		
		if(reg.dp_cod_postal == null || reg.dp_cod_postal.equals("")) {						//43
			sql += "NULL, ";
		}else {
			sql += "'" + reg.dp_cod_postal.trim() + "', ";
		}		
		
		if(reg.sClienteDV.equals("")) {						//44
			sql += "NULL, ";
		}else {
			sql += "'" + reg.sClienteDV.trim() + "', ";
		}
		sql += reg.Caso + ", ";								//45
		sql += reg.NroOrden + ") ";							//46
		
		return sql;
	}
	
	private String query5() {
		String sql;

		sql = "INSERT INTO est_sol ( ";
		sql += "nro_solicitud, ";
		sql += "fecha_generacion, ";
		sql += "sucursal, ";
		sql += "cod_motivo ";
		sql += ")VALUES( ";
		sql += "?, CURRENT, ?, ?) ";
		
		return sql;
	}

	private String query6(MensajeDTO reg) {
		String sql;
		
		sql = "EXECUTE PROCEDURE xpro_enviar( ";

        sql += reg.lMensaje + ", ";
        sql += "'" + reg.sProced.trim() + "', ";
        sql += "'" + reg.sEtapa.trim() + "', ";
        sql += "'" + reg.sPrivacidad.trim() + "', ";
        sql += "'" + reg.sUrgencia.trim() + "', ";
        sql += "'" + reg.sEncriptado.trim() + "', ";
        sql += "'" + reg.sReferencia.trim() + "', ";
        sql += "'" + reg.sRolCon.trim() + "', ";
        sql += "'" + reg.sRolOrg.trim() + "', ";
        sql += "'" + reg.sRolDst.trim() + "', ";
        sql += reg.iEmpCon + ", ";
        sql += reg.iEmpOrg + ", ";
        sql += reg.iEmpDst + ", ";
        sql += "'" + reg.sTexto.trim() +"') "; 
		
		//sql = "EXECUTE PROCEDURE xpro_enviar(?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		
		return sql;
	}
	
	private String query7() {
		String sql;
		
		sql = "UPDATE secuen SET "; 
		sql += "valor = ? ";
		sql += "WHERE codigo = 'SOLSUM' "; 
		
		return sql;
	}
	
	private String query8() {
		String sql;
		
		sql = "SELECT caso, ";
		sql += "nro_orden, ";
		sql += "data_in, ";
		sql += "tipo_sol ";
		sql += "FROM sfc_interface ";
		sql += "WHERE estado = 0 ";
		sql += "AND tarifa = 'T1' ";
		sql += "AND tipo_sol IN ('INCORPORACION', 'MANSER', 'RETCLI') ";
		
		return sql;
	}

	private String query9() {
		String sql;
		
		sql = "UPDATE sfc_interface SET "; 
		sql += "estado = ?, ";
		sql += "descri_estado = 'Solicitud Iniciada', ";
		sql += "fecha_estado = CURRENT ";
		sql += "WHERE caso = ? ";
		sql += "AND nro_orden = ? ";
		sql += "AND tarifa = 'T1' ";
		sql += "AND tipo_sol = ? ";
		
		return sql;
	}

	private String query10() {
		String sql;
		
		sql = "INSERT INTO retcli (numero_cliente, codigo ";
		sql += ")VALUES(?, ?) ";		
		
		return sql;
	}
	
	private String query11() {
		String sql;
		
		sql = "SELECT numero ";
		sql += "FROM numao ";
		sql += "WHERE tipo_orden = ? ";
		sql += "AND area = ? ";
		sql += "FOR UPDATE ";
		
		return sql;
	}
	
	private String query12() {
		String sql;
		
		sql = "UPDATE numao SET ";
		sql += "numero = ? ";
		sql += "WHERE tipo_orden = ? ";
		sql += "AND area = ? ";
		
		return sql;
	}
	
	private String query13() {
		String sql;

		sql = "INSERT INTO orden ( ";
		sql += "tipo_orden, ";
		sql += "numero_orden, ";
		sql += "mensaje_xnear, ";
		sql += "servidor, ";
		sql += "sucursal, ";
		sql += "area_emisora, ";
		sql += "fecha_inicio, ";
		sql += "ident_etapa, ";
		sql += "term_dir, ";
		sql += "area_ejecutora, ";
		sql += "rol_usuario, ";
		sql += "tema, ";
		sql += "trabajo, ";
		sql += "numero_cliente, ";
		sql += "sfc_caso, ";
		sql += "sfc_nro_orden ";  
		sql += ")VALUES ( ";
		sql += "?, ?, ?, 1, ?, ?, CURRENT, 'RQ', 'SALEFORCE', ?, ?, ?, ?, ?, ?, ?) ";
		
		return sql;
	}
	
	private String query14() {
		String sql;
		
		sql = "SELECT sucursal, dv_numero_cliente FROM cliente ";
		sql += "WHERE numero_cliente = ? ";
		
		return sql;
	}
	
	private String query15() {
		String sql;
		
		sql = "SELECT cod_mac ";
		sql += "FROM sap_transforma ";
		sql += "WHERE clave = 'CARDTYPE' ";
		sql += "AND acronimo_sap = ? ";
		
		return sql;
	}

	private String query16() {
		String sql;
		
		sql = "INSERT into solicitud_adhpag ( ";
		sql += "numero_cliente, ";
		sql += "dv_cliente, ";
		sql += "codigo_movimiento, ";
		sql += "fp_banco, ";
		sql += "fp_tipocuenta, ";
		sql += "fp_nrocuenta, ";
		sql += "fecha_solicitud, ";
		sql += "rol_creacion, ";
		sql += "sucursal_ede, ";
		sql += "codigo_extraccion ";
		sql += ")VALUES( ";
		sql += "?, ?, 42, ?, 1, ";
		sql += "?, TODAY, 'SFC_CONTRATA', ";
		sql += "'0100', 'N') ";
		
		return sql;
	}

	private String query17(String sClave, String sTipo) {
		String sql;
		
		sql = "SELECT cod_mac ";
		sql += "FROM sap_transforma ";
		sql += "WHERE clave = '" + sClave.trim() + "' ";
		if(sTipo.trim()== "ACRO") {
			sql += "AND acronimo_sap = ? ";
		}else {
			sql += "AND cod_sap = ? ";
		}
		
		return sql;
	}

	private String query18() {
		String sql;
		
		sql = "SELECT oficina, ";
		sql += "autoriza_adhesion, ";
		sql += "confirma_adhesion, ";
		sql += "tipo, ";
		sql += "dig_nro_cuenta, "; 
		sql += "debito_directo, ";
		sql += "cod_ofi_bcra ";
		sql += "FROM entidades_debito "; 
		sql += "WHERE oficina = ? ";
		sql += "AND fecha_activacion <= TODAY "; 
		sql += "AND (fecha_desactivac > TODAY or fecha_desactivac is NULL) ";
		
		return sql;
	}
	
}
