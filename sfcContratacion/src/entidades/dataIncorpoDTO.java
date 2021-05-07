package entidades;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class dataIncorpoDTO {
    /* Lo que viene del json 2020 */
    public long Caso;
    public long NroOrden;
    public String TipoSum; // Levantarlo de tipoSuministro
    public String Motivo;  //Levantarlo de la Categoria del Json
    public String Submotivo; //sin utilidad para t1
    public String Solicitante;
    public String ClaseCliente1; // es una parte del tipo de cliente despues vemos que utilidad tiene
    public String Nombre;  /* Nombre del cliente */
    public String TipoDoc;
    public long	  NroDoc;
    public String TipoDocContacto;
    public String   NroDocContacto;
    public String NombreContacto;
    public String TelefonoContacto;
    public String TipoIva;
    public long   PotenciaContratada;
    public String Electrodependiente;
    public long   NumeroDCI;
    public String EnteEmisor;
    public String certificadoDCI; //"Si/No"
    public String ConexionTransitoria;
    public String Observaciones;
    public String SucursalTecnica;
    public long   PodID;
    public String CodProvincia;
    public String CodPartido;
    public String CodLocalidad;
    public String CodCalle;
    public String NomCalle;
    public String NroDir;
    public String Piso;
    public String Depto;
    public long codPost;
    public int Sucursal;
    public String ZonaTecnica;
    public String TipoTarifa; //T1 T2 T3
    public String ValorTension; //
    public String voltaje_solicitado; // Levantarlo de Valor Tension
    public String nivelTension; //Alta Media o Baja
    public String ClaseServicio;  //Es el Tipo de Cliente
    public String Tarifa;
    public long NroCliente; // levantarlo de numeroSuministro
    public String telefono; // levantarlo de telefonoPpal
    public String telefono2; // levantarlo de telefonoAlternativo
    public String eMail;
    public String Peaje; // Sin utilidad en T1
    public String codEntreCalle1;
    public String nomEntreCalle1;
    public String codEntreCalle2;
    public String nomEntreCalle2;
    public String manzana;
    public String CodPropiedad;
    public String Ciiu; // el ramo levantarlo de "codigoCIIU"
    public String VarLibre1;
    public String VarLibre2;
    public String VarLibre3;
    public String VarLibre4;
    public String VarLibre5;
    public String VarLibre6;
    public String VarLibre7;
    public String VarLibre8;
    public String VarLibre9;
    public String VarLibre10;

    public String nro_cuit;
    public String CentroOperativo;

    public String TipoReparto;	//No está en el json

    /* Version 2017 sin pareja */

	public String nomLocalidad;
	public String TipoVenc;	//No está en el json

	public String Estado;
	public String CentroTrans;	//No está en el json
	//public String Procedimiento;

	public long   Potencia;
	public String TipoPersona;
	public String codCreditCard;
	public String NroTarjetaCredito;
	public String	Region;
	public String	consumoEstimado;
	public String	nroDeFases;
	public long	CostoInstalacion;
	public String	RolInicio;
	public String	NomCallePostal;
	public String	NroDirPostal;
	public String	codPostPostal;
	public String	localidadPostal;
	public String	provinciaPostal;
    public long     nroCuentaAnterior;
    public String   tipoAlta;
	
   public dataIncorpoDTO(interfaceDTO regIn) {
	   String s = regIn.data_in;
	   JSONParser parser = new JSONParser();
	   String sDenominacion="";
	   String sCadenaAux="";
	   
      try{
    	  
          Object obj = parser.parse(s);
          JSONArray array1 = (JSONArray)obj;

          JSONObject obj1 = (JSONObject)array1.get(0);
          
          //Generales
          sCadenaAux = (String) obj1.get("numeroCasoSFDC");
          this.Caso = Long.parseLong(sCadenaAux);
          //this.Caso = (Long) obj1.get("numeroCasoSFDC");
          sCadenaAux = (String) obj1.get("numeroOrden");
          this.NroOrden = Long.parseLong(sCadenaAux);
          //this.NroOrden = (Long) obj1.get("numeroOrden");
          sCadenaAux = (String) obj1.get("categoria");
          if(sCadenaAux.trim().equals("CAT201")){
              this.Motivo ="S01"; // Pedido del cliente
          }else{
              this.Motivo ="S16"; // Normalización
          }

          this.Tarifa = (String) obj1.get("tarifa");

          sCadenaAux = (String) obj1.get("tipoIdentificacion");
          if(sCadenaAux.trim().equals("K")){
              this.nro_cuit = (String) obj1.get("numeroIdentificacion");
              this.TipoDoc= (String) obj1.get("tipoIdentificacionContacto");
              sCadenaAux= (String) obj1.get("numeroIdentificacionContacto");
              this.NroDoc= Long.parseLong(sCadenaAux);

          }else{
              this.TipoDoc= (String) obj1.get("tipoIdentificacion");
              sCadenaAux= (String) obj1.get("numeroIdentificacion");
              this.NroDoc= Long.parseLong(sCadenaAux);
          }

          this.TipoIva = (String) obj1.get("condicionImpositiva");
/*
          sDenominacion = (String) obj1.get("razonSocial");

          if(sDenominacion.trim() == null || sDenominacion.equals("")) {
        	  
        	  sDenominacion = (String) obj1.get("apellido") + " " + (String) obj1.get("nombre");
        	  sDenominacion.trim();
          }
*/
          this.Nombre = (String) obj1.get("titular");
          
          this.TipoSum = (String) obj1.get("tipoSuministro");
          this.Ciiu = (String) obj1.get("codigoCIIU");
          this.voltaje_solicitado = (String) obj1.get("valorTension");
          this.telefono = (String) obj1.get("telefonoPrincipal");
          this.telefono2 = (String) obj1.get("telefonoAlternativo");
          this.CentroOperativo = (String) obj1.get("centroOperativo");
          this.CodPropiedad = (String) obj1.get("propietario");

          //Direccion Suministro
          this.CodPartido = (String) obj1.get("partido");
          //this.Region = (String) obj1.get("region");

          this.CodCalle = (String) obj1.get("codigoCalle");
          this.NomCalle = (String) obj1.get("calle");

          this.NroDir = (String) obj1.get("numeroCalle");
          this.Piso = (String) obj1.get("piso");
          this.Depto = (String) obj1.get("departamento");
          this.codPost = Long.parseLong( (String) obj1.get("codigoPostal"));
          this.CodLocalidad = (String) obj1.get("localidad");
          //this.nomLocalidad = (String) obj1.get("localidad");
          sCadenaAux= (String) obj1.get("provincia");
          if(sCadenaAux.trim().equals("00")) {
              this.CodProvincia = "C";
          }else{
              this.CodProvincia = "B";
          }
          this.codEntreCalle1 = (String) obj1.get("codigoEntreCalle");
          this.nomEntreCalle1 = (String) obj1.get("entreCalle");
          this.codEntreCalle2 = (String) obj1.get("codigoEntreOtraCalle");
          this.nomEntreCalle2 = (String) obj1.get("entreOtraCalle");
          this.manzana = (String) obj1.get("manzana");

          //Datos Nuevos
          sCadenaAux = (String) obj1.get("mail");
          if(sCadenaAux.trim()!=null && !sCadenaAux.trim().equals(""))
              if(ValidaEmail(sCadenaAux))
                this.eMail = sCadenaAux;

          sCadenaAux = (String) obj1.get("numeroSuministro");
          this.NroCliente = Long.parseLong(sCadenaAux);

          this.ClaseServicio = (String) obj1.get("claseServicio"); //tipo Cliente

          this.TipoDocContacto = (String) obj1.get("tipoIdentificacionContacto");
          this.NroDocContacto = (String) obj1.get("numeroIdentificacionContacto");
          this.NombreContacto = (String) obj1.get("clienteContacto");
          this.TelefonoContacto = (String) obj1.get("telefonoContacto");

          sCadenaAux = (String) obj1.get("potencia");
          this.PotenciaContratada= Long.parseLong(sCadenaAux);

          this.Electrodependiente = (String) obj1.get("electrodependiente");

          sCadenaAux = (String) obj1.get("numeroDCI");
          if(sCadenaAux.trim().equals("")){
              this.NumeroDCI= 0;
          }else{
              this.NumeroDCI= Long.parseLong(sCadenaAux);
          }


          this.EnteEmisor = (String) obj1.get("enteEmisor");
          this.certificadoDCI = (String) obj1.get("certificadoDCI");; //"Si/No"
          this.ConexionTransitoria = (String) obj1.get("conexionTransitoria");
          this.Observaciones = (String) obj1.get("observaciones");
          this.SucursalTecnica = (String) obj1.get("sucursal");

          sCadenaAux = (String) obj1.get("podId");
          this.PodID= Long.parseLong(sCadenaAux);

          this.Solicitante = (String) obj1.get("solicitante");

          this.VarLibre1 = (String) obj1.get("varios1");
          this.VarLibre2 = (String) obj1.get("varios2");
          this.VarLibre3 = (String) obj1.get("varios3");
          this.VarLibre4 = (String) obj1.get("varios4");
          this.VarLibre5 = (String) obj1.get("varios5");
          this.VarLibre6 = (String) obj1.get("varios6");
          this.VarLibre7 = (String) obj1.get("varios7");
          this.VarLibre8 = (String) obj1.get("varios8");
          this.VarLibre9 = (String) obj1.get("varios9");
          this.VarLibre10 = (String) obj1.get("varios10");

          sCadenaAux = (String) obj1.get("nroCuentaAnterior");
          if(sCadenaAux.trim().equals("")){
              this.tipoAlta="INIC";
          }else {
              this.nroCuentaAnterior = Long.parseLong(sCadenaAux);
              this.tipoAlta="CTIT";
          }

/*

          this.TipoPersona = (String) obj1.get("categoria");
          this.codCreditCard = (String) obj1.get("typeC");
          this.NroTarjetaCredito = (String) obj1.get("CNTB2B");

          this.consumoEstimado = (String) obj1.get("consumoEstimado");
          this.nroDeFases = (String) obj1.get("numeroDeFases");
          this.CostoInstalacion = (Long) obj1.get("costoInstalacion");
          this.RolInicio = (String) obj1.get("executiveC");
          sCadenaAux = (String) obj1.get("codigoFiscal");
          this.NroDoc = Long.parseLong(sCadenaAux);
          //this.NroDoc = (Long) obj1.get("codigoFiscal");
          this.TipoTarifa = (String) obj1.get("tarifaCliente");
*/
          //Direccion Postal
/*
          this.NomCallePostal = (String) obj1.get("calleAux");
          this.NroDirPostal = (String) obj1.get("numeroCalleAux");
          this.codPostPostal = (String) obj1.get("codigoPostalAux");
          this.localidadPostal = (String) obj1.get("localidadAux");
          this.provinciaPostal = (String) obj1.get("provinciaAux");
*/
/*          
          JSONObject obj2 = (JSONObject)obj1.get("DireccionPrincipal");

          this.CodCalle = (String) obj2.get("Calle");
          this.NroDir = String.valueOf((Long) obj2.get("NumeroCalle"));
          this.Piso = (String) obj2.get("Piso");
          this.Depto = (String) obj2.get("Departamento");
          this.codPost = Long.parseLong( (String) obj2.get("CodigoPostal"));
          this.CodLocalidad = (String) obj2.get("Localidad");
          this.CodProvincia = (String) obj2.get("Provincia");
*/          
          
      }catch(ParseException pe){
          System.out.println("position: " + pe.getPosition());
          System.out.println(pe);
       }		
   }

    private boolean ValidaEmail(String eMail){
        // Que no sea vacio
        if(eMail.trim()==""){
            return false;
        }

        int i=0;
        int iCantArroba=0;
        int iCantPunto=0;

        for(i=0; i < eMail.length(); i++){
            char c = eMail.charAt(i);
            int iCode = (int) c;

            if(iCode >=1 && iCode <= 45)
                return false;

            if(iCode == 47)
                return false;

            if(iCode >=58 && iCode <= 63)
                return false;

            if(iCode >=91 && iCode <= 96 && iCode != 95)
                return false;

            if(iCode >=126 && iCode <= 255)
                return false;

            if(c=='@')
                iCantArroba++;

            if(c=='.')
                iCantPunto++;
        }

        int largo=eMail.length();

        //Que no termine en punto
        if(eMail.substring(largo,largo)==".")
            return false;
        //que tenga una arroba
        if(iCantArroba != 1)
            return false;
        // que tenga al menos un punto
        if(iCantPunto == 0)
            return false;
        // que not tenga '..'
        if(eMail.indexOf("..") !=-1)
            return false;
        // que not tenga '.@'
        if(eMail.indexOf(".@") !=-1)
            return false;
        // que not tenga '@.'
        if(eMail.indexOf("@.") !=-1)
            return false;
        // que not tenga '@.'
        if(eMail.indexOf("@.") !=-1)
            return false;
        // que not tenga '@_'
        if(eMail.indexOf("@_") !=-1)
            return false;
        // que not tenga '@-'
        if(eMail.indexOf("@-") !=-1)
            return false;

        return true;
    }


}
