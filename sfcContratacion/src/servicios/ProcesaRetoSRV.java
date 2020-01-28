package servicios;

import entidades.interfaceDTO;
import entidades.retoOT_DTO;
import entidades.periodosDTO;
import entidades.traceCarpetasDTO;
import entidades.costoOT_DTO;

import java.util.Collection;
import dao.RetornosDAO;

public class ProcesaRetoSRV {

	public Boolean LeeSolicitudes() {
		RetornosDAO miDao = new RetornosDAO();
		Collection<interfaceDTO>lstInterface = miDao.getLstCasos();
		
		for(interfaceDTO fila: lstInterface) {
			if(!ProcesaCaso(fila)) {
				System.out.println("Error al procesar Caso");
				return false;
			}
		}		
		
		return true;
	}
	
	private Boolean ProcesaCaso(interfaceDTO reg) {
		String sUbicaciones="";
		String sObservaciones="";
		String sJsonRta="";
		
		periodosDTO miPeriodo = null;
		
		RetornosDAO miSrv = new RetornosDAO();
		miPeriodo = miSrv.getPeriodoDT();
		
		switch(reg.tipo_sol.trim()) {
			case "INCORPORACION":
			case "MANSER":
			case "RETCLI":
				retoOT_DTO regSts = null;
				
				//Recupero Status Actual
				regSts=miSrv.getStsOT(reg.caso, reg.nro_orden);
				
				//Recupero Ubicaciones
				sUbicaciones = CargaUbicaciones(regSts.mensaje_xnear);
				
				//Recupero Observaciones
				sObservaciones = miSrv.getTextonMensaje(regSts.mensaje_xnear);
				
				//Armo Resultado
				if(regSts.ident_etapa=="FI") {
					regSts.iSts=5;
					regSts.DescriSts="Finalizado";
					
					switch(regSts.status) {
						case "NORE":
							regSts.iSts=6;
							regSts.DescriSts="Finalizado No Realizado";
	
							break;
						case "REAL":
							regSts.iSts=5;
							regSts.DescriSts="Finalizado OK";
	
							break;
					}
				}else {
					switch(regSts.status) {
						case "NOTI":
						case "ASOL":
						case "CTEC":
						case "RAUV":
						case "PLAN":
						case "CERR":
						case "NOTP":
							regSts.iSts=2;
							regSts.DescriSts="En Proceso";
							if(reg.tipo_sol.trim()=="INCORPORACION") {
								//Ver si tiene que facturar KIT
								costoOT_DTO regCosto = null;
								regCosto = miSrv.getCostoOT(regSts.mensaje_xnear);
								if(regCosto.bExiste) {
									sObservaciones += "| FACTURAR KIT INSTALACION Materiales: $" + regCosto.dCostoMat + " Mano de Obra: $" + regCosto.dCostoMo + " Total:$ " + regCosto.dCostoTotal + "|";
									regSts.iSts=4;
									regSts.DescriSts="Debe Facturar Kit Instalacion";
								}
							}

							break;
						case "NORE":
							regSts.iSts=6;
							regSts.DescriSts="Finalizado No Realizado";

							break;
						case "REAL":
							regSts.iSts=5;
							regSts.DescriSts="Finalizado OK";

							break;
					}
					
				}
				
				//Armo el Json de Respuesta
				sJsonRta=ArmaJsonOT(reg, regSts, sUbicaciones, sObservaciones);
				
				//Informo Resultado
				if(! miSrv.setStatus(regSts, reg.caso, reg.nro_orden, sJsonRta)) {
					System.out.println("Fallo actualizar Sts. para Caso " + reg.caso + " Nro.Orden " + reg.nro_orden);
					return false;
				}
				
				break;
			
			case "CONTACTO":
				retoOT_DTO regStsCto = null;
				
				//Recupero Status Actual
				regStsCto = miSrv.getCtoSegen(reg.caso, reg.nro_orden);
				
				//Recupero Ubicaciones
				sUbicaciones = CargaUbicaciones(regStsCto.mensaje_xnear);
				
				//Recupero Observaciones
				sObservaciones = miSrv.getTextonMensaje(regStsCto.mensaje_xnear);
				
				//Armo Resultado
				switch(regStsCto.status.trim()) {
					case "FINALIZADO":
						regStsCto.iSts=10;
						regStsCto.DescriSts="Finalizado";
						break;
					default:
						regStsCto.iSts=2;
						regStsCto.DescriSts="En Proceso";
						break;
				
				}
				
				//Armo el Json de Respuesta
				sJsonRta=ArmaJsonCTO(reg, regStsCto, sUbicaciones, sObservaciones);
				
				//Informo Resultado
				if(! miSrv.setStatus(regStsCto, reg.caso, reg.nro_orden, sJsonRta)) {
					System.out.println("Fallo actualizar Sts. para Caso " + reg.caso + " Nro.Orden " + reg.nro_orden);
					return false;
				}
				
				break;
		}		
		
		return true;
	}
	
	private String CargaUbicaciones(Long lNroMsg) {
		RetornosDAO miSrv = new RetornosDAO();
		String sLista="";
		
		Collection<traceCarpetasDTO>lstCarpeta = miSrv.getUbicaMensaje(lNroMsg);
		
		for(traceCarpetasDTO fila: lstCarpeta) {
			sLista = "{\"carpeta\":\"" + fila.sCarpeta.trim() + "\", \"fecha\":\"" + fila.dtFecha.trim() + "\"},";
		}		
		
		if(sLista.length()>0) { //Esto es para sacarle la ultima coma
			sLista = sLista.substring(0, sLista.length()-1);
		}
		
		return sLista;
	}
	
	private String ArmaJsonOT(interfaceDTO regInter, retoOT_DTO regOT, String sUbicaciones, String sObservaciones) {
		String sJson="";
		
		sJson = "{";
		sJson += "\"numeroCasoSFDC\":\"" + regInter.caso + "\", ";
		sJson += "\"numeroOrden\":\"" + regInter.nro_orden + "\", ";
		sJson += "\"codEstadoSolicitud\":\"" + regInter.estado + "\", ";
		sJson += "\"numeroOT\":\"" + regOT.numero_orden + "\", ";
		sJson += "\"codEstadoOT\":\"" + regOT.iSts + "\", ";
		sJson += "\"descripcionEstadoOT\":\"" + regOT.DescriSts.trim() + "\", ";
		sJson += "\"fechaEstadoOT\":\"" + regOT.fecha_proc + "\", ";
		sJson += "\"ubicacionesDelDia\":[" + sUbicaciones.trim() + "], ";
		sJson += "\"observaciones\":\"" + sObservaciones + "\"";
		sJson += "}";
		
		return sJson;
	}
	
	private String ArmaJsonCTO(interfaceDTO regInter, retoOT_DTO regOT, String sUbicaciones, String sObservaciones) {
		String sJson="";
		String sResuContacto="";
		
		if(regOT.sResultadoContacto.trim()=="022") {
			sResuContacto="FAVORABLE";
		}else if(regOT.sResultadoContacto.trim()=="023") {
			sResuContacto="DESFAVORABLE";
		}else {
			sResuContacto="Sin Respuesta";
		}
				
		sJson = "{";
		sJson += "\"numeroCasoSFDC\":\"" + regInter.caso + "\", ";
		sJson += "\"numeroOrden\":\"" + regInter.nro_orden + "\", ";
		sJson += "\"codEstadoSolicitud\":\"" + regInter.estado + "\", ";
		sJson += "\"numeroContacto\":\"" + regOT.co_numero + "\", ";
		sJson += "\"sucursalContacto\":\"" + regOT.co_sucursal + "\", ";
		sJson += "\"respuestaContacto\":\"" + sResuContacto + "\", ";
		sJson += "\"codEstadoSegen\":\"" + regOT.iSts + "\", ";
		sJson += "\"descripcionEstadoSegen\":\"" + regOT.DescriSts.trim() + "\", ";
		sJson += "\"fechaEstadoSegen\":\"" + regOT.fecha_proc + "\", ";
		sJson += "\"ubicacionesDelDia\":[" + sUbicaciones.trim() + "], ";
		sJson += "\"observaciones\":\"" + sObservaciones + "\"";
		sJson += "}";
		
		return sJson;
	}
	
}
