package entidades;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;


public class dataRetcliDTO {
	public long	Caso;
	public long NroOrden;
	public long	nroCliente;
	public String sMotivo;
	public String	RetiroMedidor;
	public int	Sucursal;
	public String sCentroOperativo;
	public String	Dv;
	
	public dataRetcliDTO(interfaceDTO regIn) {
		
		String s = regIn.data_in;
		JSONParser parser = new JSONParser();
	
	      try{
	    	  
	          Object obj = parser.parse(s);
	          JSONArray array1 = (JSONArray)obj;

	          JSONObject obj1 = (JSONObject)array1.get(0);

	          this.nroCliente = (Long) obj1.get("numeroCliente");
	          this.sMotivo = (String) obj1.get("motivo");
	          this.RetiroMedidor = (String) obj1.get("retiroMedidor");
	          
	      }catch(ParseException pe){
	          System.out.println("position: " + pe.getPosition());
	          System.out.println(pe);
	       }	   
	          
	}
}
