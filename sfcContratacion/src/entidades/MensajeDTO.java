package entidades;

public class MensajeDTO {

	public long	lMensaje;
	public String	sProced;
	public String	sEtapa;
	public String	sPrivacidad;
	public String	sUrgencia;
	public String	sEncriptado;
	public String	sReferencia;
	public String	sRolCon;
	public String	sRolOrg;
	public String	sRolDst;
	public int	iEmpCon;
	public int	iEmpOrg;
	public int	iEmpDst;
	public String	sTexto;

	public MensajeDTO(long lNroMensaje, long lNroSol, String sDV, String sOrigen, String sDestino, String sProcedimiento) {
		this.lMensaje = lNroMensaje;
		this.sProced = sProcedimiento;
		this.sEtapa = "INICIO";
		this.sPrivacidad = "1";
		this.sUrgencia = "4";
		this.sEncriptado = "N";
		switch(sProcedimiento) {
			case "INCORPORACION":
				this.sReferencia = "Solicitud Sum.Nº: " + String.format("%08d", lNroSol) + "-" + sDV;
				break;
			case "MANSER":
				this.sReferencia = "(Manser) Cliente: " + String.format("%08d", lNroSol) + "-" + sDV;
				break;
			case "RETCLI":
				this.sReferencia = "(Retcli) Cliente: " + String.format("%08d", lNroSol) + "-" + sDV;
				break;
				
		}
		
		this.sRolCon = sDestino;
		this.sRolOrg = sOrigen;
		this.sRolDst = sDestino;
		this.iEmpCon = 1;
		this.iEmpOrg =1;
		this.iEmpDst = 1;
		this.sTexto = String.format("%08d", lNroSol) + "þ";
		
	}
}
