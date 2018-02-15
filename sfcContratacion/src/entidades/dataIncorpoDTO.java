package entidades;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class dataIncorpoDTO {

	public String Nombre;
	public String TipoDoc;	//No está en el json
	public long	  NroDoc;	//No está en el json - está como codigoFiscal
	public String CodProvincia;
	public String CodPartido;
	public String CodLocalidad;
	public String nomLocalidad;
	public String CodCalle;
	public String NomCalle;
	public String NroDir;
	public String Piso;
	public String Depto;
	public long codPost;
	public String TipoIva;
	public String TipoVenc;	//No está en el json
	public String CodPropiedad;	//No está en el json
	public String Ciiu;
	public String Sucursal;
	public String TipoReparto;	//No está en el json
	public String Estado;
	public String CentroTrans;	//No está en el json
	public String TipoSum;
	//public String Procedimiento;
	
	public long Caso;
	public long NroOrden;
	public String Motivo;
	public String Tarifa;
	public String nro_cuit;
	public String voltaje_solicitado;
	public String telefono;
	
	public String TipoPersona;
	public String eMail;
	public String codCreditCard;
	public String NroTarjetaCredito;
	public long NroCliente;
	public String	ClaseServicio;
	public String	Region;
	public String	TipoTarifa;
	public String	consumoEstimado;
	public String	nroDeFases;
	public long	CostoInstalacion;
	public String	RolInicio;
	
	public String	NomCallePostal;
	public String	NroDirPostal;
	public String	codPostPostal;
	public String	localidadPostal;
	public String	provinciaPostal;
	
	
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
          this.Motivo = (String) obj1.get("motivo");
          this.Tarifa = (String) obj1.get("tarifa");
          this.nro_cuit = (String) obj1.get("cuitCuil");
          this.TipoIva = (String) obj1.get("tax");
          
          sDenominacion = (String) obj1.get("razonSocial");

          if(sDenominacion.trim() == null || sDenominacion.equals("")) {
        	  
        	  sDenominacion = (String) obj1.get("apellido") + " " + (String) obj1.get("nombre");
        	  sDenominacion.trim();
          }
         
          this.Nombre = sDenominacion.trim();
          
          this.TipoSum = (String) obj1.get("tipoDeSuministro");
          this.Ciiu = (String) obj1.get("codigoCIU");
          this.voltaje_solicitado = (String) obj1.get("nivelTension");
          this.telefono = (String) obj1.get("telefono");
          this.Sucursal = (String) obj1.get("sucursal");
          this.CodPartido = (String) obj1.get("partido");
          this.Region = (String) obj1.get("region");
          
          //Direccion Suministro
          //this.CodCalle = (String) obj1.get("calle");
          this.NomCalle = (String) obj1.get("calle");
          //this.NroDir = String.valueOf((Long) obj1.get("numeroCalle"));
          this.NroDir = (String) obj1.get("numeroCalle");
          this.Piso = (String) obj1.get("piso");
          this.Depto = (String) obj1.get("departamento");
          this.codPost = Long.parseLong( (String) obj1.get("codigoPostal"));
          //this.CodLocalidad = (String) obj1.get("localidad");
          this.nomLocalidad = (String) obj1.get("localidad");
          this.CodProvincia = (String) obj1.get("provincia");

          //Datos Nuevos
          this.TipoPersona = (String) obj1.get("categoria");
          this.eMail = (String) obj1.get("mail");
          this.codCreditCard = (String) obj1.get("typeC");
          this.NroTarjetaCredito = (String) obj1.get("CNTB2B");

          sCadenaAux = (String) obj1.get("contractID");
          this.NroCliente = Long.parseLong(sCadenaAux);
          //this.NroCliente = (Long) obj1.get("contractID");
          this.ClaseServicio = (String) obj1.get("codigoServicio");
          this.TipoTarifa = (String) obj1.get("tarifaCliente");
          this.consumoEstimado = (String) obj1.get("consumoEstimado");
          this.nroDeFases = (String) obj1.get("numeroDeFases");
          this.CostoInstalacion = (Long) obj1.get("costoInstalacion");
          this.RolInicio = (String) obj1.get("executiveC");
          sCadenaAux = (String) obj1.get("codigoFiscal");
          this.NroDoc = Long.parseLong(sCadenaAux);
          //this.NroDoc = (Long) obj1.get("codigoFiscal");
          
          //Direccion Postal
          this.NomCallePostal = (String) obj1.get("calleAux");
          this.NroDirPostal = (String) obj1.get("numeroCalleAux");
          this.codPostPostal = (String) obj1.get("codigoPostalAux");
          this.localidadPostal = (String) obj1.get("localidadAux");
          this.provinciaPostal = (String) obj1.get("provinciaAux");
          
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
	
}
