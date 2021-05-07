package entidades;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class dataManserDTO {
	public long	Caso;
	public long NroOrden;
	public long	nroCliente;
	public String sMotivo;
	public long nroMedidorNvo;	/* No está en el Json*/
	public String sMarcaMedidorNvo;	/* No está en el Json*/
	public String sModeloMedidorNvo;	/* No está en el Json*/
	public String sObservaciones;
	public int Sucursal;
	public String sCentroOperativo;
	public String Dv;
	
	public dataManserDTO(interfaceDTO regIn) {
		String s = regIn.data_in;
		JSONParser parser = new JSONParser();

	      try{
	    	  
	          Object obj = parser.parse(s);
	          JSONArray array1 = (JSONArray)obj;

	          JSONObject obj1 = (JSONObject)array1.get(0);
	          
	          //Generales
	          /*
	          this.Caso = (Long) obj1.get("NumeroCasoSFDC");
	          this.NroOrden = (Long) obj1.get("NumeroOrden");
	          */
	          this.nroCliente = (Long) obj1.get("numeroCliente");
	          this.sMotivo = (String) obj1.get("motivo");
	          this.nroMedidorNvo = (Long) obj1.get("numeroMedidor");
	          this.sMarcaMedidorNvo = (String) obj1.get("marcaMedidor");
	          this.sModeloMedidorNvo = (String) obj1.get("modeloMedidor");
	          this.sObservaciones = (String) obj1.get("observaciones");
	          
	      }catch(ParseException pe){
	          System.out.println("position: " + pe.getPosition());
	          System.out.println(pe);
	       }	   
	   
	}
}
