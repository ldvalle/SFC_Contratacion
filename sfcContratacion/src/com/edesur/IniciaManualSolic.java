package com.edesur;
import servicios.ProcesaManual;

public class IniciaManualSolic {
    public static void main(String[] args) {
/*
        ProcesaSolSRV miSrv = new ProcesaSolSRV();


        if(! miSrv.LeeSolicitudes()) {
            System.out.println("Error al cargar la solicitud.");
            return;
        }
*/

        String Procedimiento;
        String Motivo;
        Long   nroCliente;

        ProcesaManual miSrv = new ProcesaManual();

        Procedimiento = args[0];
        Motivo = args[1];
        nroCliente=Long.parseLong(args[2]);

        System.out.println(String.format("procedimiento %s Motivo %s Nro.Cliente %d", Procedimiento, Motivo, nroCliente));

        if(miSrv.SetTrabajo(Procedimiento, Motivo,nroCliente)){
            System.out.println("Operacion Realizada OK");
        }else{
            System.out.println("Operacion Fallida");
        }




    }


}
