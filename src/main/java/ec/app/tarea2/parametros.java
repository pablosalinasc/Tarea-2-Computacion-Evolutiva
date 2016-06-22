/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.app.tarea2;

/**
 *
 * @author obi
 */
public class parametros {
    int cantidadTotalFeatures;
    int largoDataset;
    String rutaDataset;
    boolean printFunciones;
    public parametros(){
        //AUS
        printFunciones=false;
        rutaDataset="AUS.data";
        largoDataset=690;
        cantidadTotalFeatures=14;
    }
}
