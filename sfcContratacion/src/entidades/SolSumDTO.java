package entidades;

public class SolSumDTO {
	public long	lNroSolicitud;
	public String	sDv;
	public String	sNombre;
	public String	sTipoDoc;
	public Long		lNroDoc;
	public String	sProvincia;
	public String	sPartido;
	public String	sLocalidad;
	public String	sCodCalle;
	public String	sNomCalle; 
	public String	sNroDir;
	public String	sPiso;
	public String	sDepto;
	public Long	codPost;
	//public long	lMensaje;
	public String	sTipoIva;
	public String	sTipoVenc;
	public String	sCodPropiedad;
	public String	sCiiu;
	public String	sSucursal;
	public String	sTipoReparto;
	public String	sEstado;
	public String	sCentroTrans;
	public String	sTipoSum;
	public long		lNroMensaje;
	public int		iServidor;

	public long 	Caso;
	public long 	NroOrden;
	public String 	Motivo;
	public String 	Tarifa;
	public String 	nro_cuit;
	public String 	voltaje_solicitado;
	public String 	telefono;
	public String	sTipoCliente;
	public long		lNroCliente;
	public String	sTipoFpago;
	public String	sClienteDV;
	public String	sCodTarjeta;
	public String	sNroTarjeta;

	public long		lPODid;
	public String	sRolSFC;

	public String   sCodEntreCalle1;
	public String	sNomEntreCalle1;
	public String	sCodEntreCalle2;
	public String	sNomEntreCalle2;

	//Datos Postales
	public String	dp_nom_provincia;
	public String	dp_nom_localidad;
	public String	dp_nom_calle;
	public String	dp_nro_dir;
	public String	dp_cod_postal;

	public Long		potenciaContratada;
	public String	sRstObraCliente;
	public Long		cta_ant;
	public String	camTit;
	
	public SolSumDTO(long NroSol, long NroMsg, dataIncorpoDTO regInt) {
		this.lNroSolicitud = NroSol;
		this.sDv = CalculaDV(NroSol);
		this.sTipoVenc = "1";
		if(regInt.tipoAlta.trim().equals("INC")){
			this.sEstado = "ET";
			this.camTit = "N";
		}else{
			this.sEstado = "FI";
			this.cta_ant = regInt.nroCuentaAnterior;
			this.camTit = "S";
		}

		this.sCentroTrans = "*****";
		this.sRstObraCliente = "N";

		this.sCodPropiedad = regInt.CodPropiedad;
		this.sCiiu = regInt.Ciiu;
		this.sNombre = regInt.Nombre;
		this.sTipoDoc = regInt.TipoDoc;
		this.lNroDoc = regInt.NroDoc;
		this.sProvincia = regInt.CodProvincia;
		this.sPartido = regInt.CodPartido;
		this.sLocalidad = regInt.CodLocalidad;
		this.sCodCalle = regInt.CodCalle;
		this.sNomCalle = regInt.NomCalle;
		this.sNroDir = regInt.NroDir;
		this.sPiso = regInt.Piso;
		this.sDepto = regInt.Depto;
		this.codPost = regInt.codPost;
		this.sTipoIva = regInt.TipoIva;
		this.sSucursal = regInt.CentroOperativo;

		if(regInt.TipoReparto == null) {
			this.sTipoReparto = "NORMAL";
		}else {
			this.sTipoReparto = regInt.TipoReparto.trim();
		}

		if(regInt.TipoSum == null || regInt.TipoSum.trim()=="" || regInt.TipoSum.length()==0) {
			this.sTipoSum = "0";
		}else {
			this.sTipoSum = regInt.TipoSum.trim();
		}
		
		this.Caso = regInt.Caso;
		this.NroOrden = regInt.NroOrden;
		this.Motivo = regInt.Motivo.trim();
		this.Tarifa = regInt.Tarifa.trim();
		this.nro_cuit = regInt.nro_cuit.trim();
		this.voltaje_solicitado = regInt.voltaje_solicitado;
		this.telefono = regInt.telefono.trim();
		this.lNroMensaje = NroMsg;
		this.iServidor = 1;

		this.sTipoCliente = regInt.ClaseServicio.trim();
		this.lNroCliente = regInt.NroCliente;

		this.sTipoFpago="N";
		/*
		if(regInt.NroTarjetaCredito.trim()== null || regInt.NroTarjetaCredito.equals("")) {
			this.sTipoFpago="N";
		}else {
			this.sTipoFpago="D";
			this.sCodTarjeta = regInt.codCreditCard.trim();
			this.sNroTarjeta = regInt.NroTarjetaCredito.trim();
		}
		*/

		
		if(this.lNroCliente>0) {
			this.sClienteDV = CalculaDV(this.lNroCliente);
		}

		this.lPODid = regInt.PodID;
		this.sRolSFC = regInt.Solicitante.trim();

		this.sCodEntreCalle1 = regInt.codEntreCalle1.trim();
		this.sNomEntreCalle1 = regInt.nomEntreCalle1.trim();
		this.sCodEntreCalle2 = regInt.codEntreCalle2.trim();
		this.sNomEntreCalle2 = regInt.nomEntreCalle2.trim();

		this.potenciaContratada = regInt.PotenciaContratada;

		/*
		this.dp_cod_postal = regInt.codPostPostal.trim();
		this.dp_nom_calle = regInt.NomCallePostal.trim();
		this.dp_nom_localidad = regInt.localidadPostal.trim();
		this.dp_nom_provincia = regInt.provinciaPostal.trim();
		this.dp_nro_dir = regInt.NroDirPostal.trim();

		 */
	}
	
	private String CalculaDV(long NroSol) {
		String	sDVlocal;
		String	ruta;
		String	factor;
		String	sNumero;
		long	suma;
		int   i, j;
		int   largo;
		int   Result;
		float factor2;
		int   miEntero;
		String	sDigito1;
		String	sDigito2;
		int	iDigito1;
		int	iDigito2;

		sDVlocal="";
		sNumero="";
		
		ruta = "1234567890K";
		factor = "234567234";
		sNumero = Long.toString(NroSol);
		
		suma = 0;
		j=1;
		
		largo = sNumero.length();
		largo = largo-1;
		
		for(i=largo; i>0; i-- ) {
			sDigito1="";
			sDigito2="";
			
			sDigito1 = sNumero.substring(i, i+1);
			sDigito2 = factor.substring(j, j+1);
			
			iDigito1 = Integer.parseInt(sDigito1);
			iDigito2 = Integer.parseInt(sDigito2);
			
			suma = suma + (iDigito1 * iDigito2);
			
			j++;
		}
		
		factor2 = suma / 11;
		
		miEntero = (int) (Math.round(factor2));
		Result = (int) (11 - (suma - miEntero * 11));
		sDVlocal = ruta.substring(Result-1, Result);
		
		return sDVlocal;
	}
}
