package servicios;


import entidades.dataIncorpoDTO;
import entidades.dataManserDTO;
import entidades.MensajeDTO;
import entidades.SolSumDTO;
import entidades.interfaceDTO;
import entidades.dataIncorpoDTO;
import entidades.dataRetcliDTO;
import entidades.ClienteDTO;

import java.util.Collection;

import dao.SuministroDAO;

public class ProcesaSolSRV {

	public Boolean LeeSolicitudes() {
		SuministroDAO miDao = new SuministroDAO();
		
		Collection<interfaceDTO>lstInterface = miDao.getLstSolicitudes();
		
		for(interfaceDTO fila: lstInterface) {
			
			if(!ProcesaSolicitud(fila)) {
				System.out.println("Error al procesar Solicitud");
				return false;
			}
		}		
		
		return true;
	}
	
	private Boolean ProcesaSolicitud(interfaceDTO reg) {
		
		switch(reg.tipo_sol.trim()) {
			case "INCORPORACION":
				
				dataIncorpoDTO regIN = new dataIncorpoDTO(reg);
				regIN.Caso = reg.caso;
				regIN.NroOrden = reg.nro_orden;

				if(!CargaSol("INC", regIN)) {
					System.out.println("Error al cargar la solicitud.");
					return false;
				}
				break;

			case "CAMTIT":

				dataIncorpoDTO regCamTit = new dataIncorpoDTO(reg);
				regCamTit.Caso = reg.caso;
				regCamTit.NroOrden = reg.nro_orden;

				if(!CargaSol("CTIT", regCamTit)) {
					System.out.println("Error al cargar la solicitud.");
					return false;
				}
				break;

			case "MANSER":
				dataManserDTO regMan = new dataManserDTO(reg);
				regMan.Caso = reg.caso;
				regMan.NroOrden = reg.nro_orden;

				if(!CargaManser(regMan)) {
					System.out.println("Error al cargar el Manser.");
					return false;
				}				
				
				break;
			case "RETCLI":
				dataRetcliDTO regRet = new dataRetcliDTO(reg);

				regRet.Caso = reg.caso;
				regRet.NroOrden = reg.nro_orden;

				if(!CargaRetcli(regRet)) {
					System.out.println("Error al cargar el Retcli.");
					return false;
				}				
				break;
		}		
		return true;
		
	}
	
	public Boolean CargaSol(String tipoSol, dataIncorpoDTO regSF) {
		long lNroMensaje=0;
		String	sRolOrigen="";
		String  sRolDestino="";

		SuministroDAO miDAO = new SuministroDAO();

		System.out.printf("Caso %d Nombre %s\n", regSF.Caso, regSF.Nombre);

		//Levantar Sucursal dado el Centro Operativo
		if(regSF.CentroOperativo.trim()!= null || (!regSF.CentroOperativo.equals(""))) {
			regSF.Sucursal = miDAO.getSucursal(regSF.CentroOperativo.trim());
		}

		//Transformar Tarifa -- voy a tener mas de un opcion MAC por una SFC
/*
		if(regSF.Tarifa.trim()!= null || (!regSF.Tarifa.equals(""))) {
			regSF.Tarifa = miDAO.getTrafoSapMac("TARIFTYP", "ACRO", regSF.Tarifa.trim());
		}
*/
		//Transformar Codigo Tarjeta Credito
/*
		if(regSF.codCreditCard.trim() != null || (!regSF.codCreditCard.equals(""))) {
			regSF.codCreditCard = miDAO.getTrafoSapMac("CARDTYPE", "ACRO", regSF.codCreditCard.trim());
			regSF.codCreditCard=regSF.codCreditCard.trim();
		}
 */
		//Transformar Codigo Servicio -- voy a tener mas de un opcion MAC por una SFC
/*
		if(regSF.ClaseServicio.trim()!= null || (!regSF.ClaseServicio.equals(""))) {
			//regSF.ClaseServicio = miDAO.getTrafoSapMac("TIPCLI", "ACRO", regSF.ClaseServicio.trim());
			regSF.ClaseServicio = miDAO.getTrafoSapMac("TIPCLI", "COD_SAP", regSF.ClaseServicio.trim());
		}
*/
		//Transformar Tipo IVA
		if(regSF.TipoIva.trim()!= null || (!regSF.TipoIva.equals(""))) {
			regSF.TipoIva = miDAO.getTrafoSapMac("TIPIVA", "COD_SAP", regSF.TipoIva.trim());
		}
		
		//Obtener Nro.Mensaje
		lNroMensaje = miDAO.getNroMensaje("INCORPORACION");
		if(lNroMensaje<=0) {
			System.out.println("No se recuperó Nro.de Mensaje");
			return false;
		}
		
		//Obtener Destino
		//sRolOrigen = "E17317";
		sRolOrigen = "SALESFORCE";
		sRolDestino =miDAO.getRolDestino("INC", regSF.Sucursal);
		sRolDestino = "EVDI";
		
		//Registrar Solicitud
		if(! miDAO.regSolSumin(tipoSol, regSF, lNroMensaje, sRolOrigen, sRolDestino)) {
			return false;
		}

		return true;
	}

	public Boolean CargaManser(dataManserDTO regSF) {
		long lNroMensaje=0;
		String	sRolOrigen="";
		String  sRolDestino="";
		String  sAreaOrigen="";
		ClienteDTO miCliente = new ClienteDTO();
		
		SuministroDAO miDAO = new SuministroDAO();

		//Obtener Sucursal Cliente
		miCliente = miDAO.getDataCliente(regSF.nroCliente);
		
		regSF.sCentroOperativo = miCliente.sSucursal;
		regSF.Dv = miCliente.sDV;
		
		//Obtener Nro.Mensaje		
		lNroMensaje = miDAO.getNroMensaje("MANSER");
		if(lNroMensaje<=0) {
			System.out.println("No se recuperó Nro.de Mensaje");
			return false;
		}
		
		//Obtener Destino
		sRolOrigen = "E17317";
		sAreaOrigen = "SICO";
		sRolDestino =miDAO.getRolDestino("MANSER", regSF.Sucursal);

		//Registrar Manser
		if(! miDAO.regManRet(regSF, lNroMensaje, sRolOrigen, sAreaOrigen, sRolDestino, "MANSER")) {
			return false;
		}
		
		return true;
	}	
	
	public Boolean CargaRetcli(dataRetcliDTO regSF) {
		long lNroMensaje=0;
		String	sRolOrigen="";
		String  sRolDestino="";
		String  sAreaOrigen="";
		ClienteDTO miCliente = new ClienteDTO();
		
		SuministroDAO miDAO = new SuministroDAO();

		//Obtener Sucursal Cliente
		miCliente = miDAO.getDataCliente(regSF.nroCliente);
		
		regSF.sCentroOperativo = miCliente.sSucursal;
		regSF.Dv = miCliente.sDV;
		
		//Obtener Nro.Mensaje		
		lNroMensaje = miDAO.getNroMensaje("RETCLI");
		if(lNroMensaje<=0) {
			System.out.println("No se recuperó Nro.de Mensaje");
			return false;
		}
		
		//Obtener Destino
		sRolOrigen = "E17317";
		sAreaOrigen = "SICO";
		sRolDestino =miDAO.getRolDestino("RETCLI", regSF.Sucursal);
		
		
		//Registrar Manser
		if(! miDAO.regRetcli(regSF, lNroMensaje, sRolOrigen, sAreaOrigen, sRolDestino, "RETCLI")) {
			return false;
		}
		
		return true;
	}
	
}
