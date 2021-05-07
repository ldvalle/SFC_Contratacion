package servicios;
import entidades.pruebaDTO;
import dao.PruebasDAO;
import java.util.Collection;

public class ProcesaManual {

    public boolean SetTrabajo(String sProcedimiento, String sMotivo, long lNroCliente){
        boolean resu=false;

        PruebasDAO miDao = new PruebasDAO();
        pruebaDTO miPrueba =null;

        System.out.println(String.format("ProcesaManual.SetTrabajo recibio %s %s %d", sProcedimiento, sMotivo, lNroCliente));

        miPrueba=miDao.lanzaGral(sProcedimiento, sMotivo, lNroCliente);

        System.out.println("Cod Retorno " + miPrueba.iCodRetorno);
        System.out.println("Desc Retorno " + miPrueba.sDescRetorno);
        System.out.println("Nro.Orden " + miPrueba.sNroOrden);
        System.out.println("Nro.OT " + miPrueba.sNroOt);

        if(miPrueba.iCodRetorno==0)
            resu=true;

        return resu;
    }
}
