/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.app.tarea2;

import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameter;
import edu.berkeley.compbio.jlibsvm.ImmutableSvmParameterGrid;
import edu.berkeley.compbio.jlibsvm.binary.BinaryModel;
import edu.berkeley.compbio.jlibsvm.binary.C_SVC;
import edu.berkeley.compbio.jlibsvm.binary.MutableBinaryClassificationProblemImpl;
import edu.berkeley.compbio.jlibsvm.kernel.GaussianRBFKernel;
import edu.berkeley.compbio.jlibsvm.kernel.LinearKernel;
import edu.berkeley.compbio.jlibsvm.util.SparseVector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author obi
 */
public class Tarea2 {

    public static  SparseVector generateFeatures(float[] floats) {
        SparseVector sparseVector = new SparseVector(floats.length);
        int[] indices = new int[2];
        for (int i = 0; i < floats.length; i++) {
            indices[i] = new Integer(i);
        }
        sparseVector.indexes = indices;
        sparseVector.values = floats;
        return sparseVector;
    }
    public static void main(String[] args) {
        
        double acc=0.0;
        
        parametros par = new parametros();
        ArrayList<Integer> features =new ArrayList<>();
        features.add(7);
        features.add(8);
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        
        float[][] dataset=new float[par.largoDataset][par.cantidadTotalFeatures+1];

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File (par.rutaDataset);
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea;
            int indiceLinea=0;
            while((linea=br.readLine())!=null){
                System.out.println(linea);
                indiceLinea++;
                String[] parts = linea.split(" ");
                for(int i=0;i<parts.length;i++){
                    dataset[indiceLinea-1][i]=Float.parseFloat(parts[i]);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try{                    
                if( null != fr ){   
                    fr.close();     
                }                  
            }catch (Exception e2){ 
                e2.printStackTrace();
            }
        }
        
        // create a new SVM implementation in the C SVC style.
        C_SVC svm = new C_SVC();
        // build parameters
        ImmutableSvmParameterGrid.Builder builder = ImmutableSvmParameterGrid.builder();
        
        // create training parameters ------------
        HashSet<Float> cSet;
        HashSet<LinearKernel> kernelSet;
        kernelSet = new HashSet<LinearKernel>();
        kernelSet.add(new LinearKernel());
        
        cSet = new HashSet<Float>();
        cSet.add(1.0f);
        
        
        // configure finetuning parameters
        builder.eps = 0.001f; // epsilon
        builder.Cset = cSet; // C values used
        builder.kernelSet = kernelSet; //Kernel used
        
        ImmutableSvmParameter params = builder.build();
        MutableBinaryClassificationProblemImpl problem= new MutableBinaryClassificationProblemImpl(String.class, par.largoDataset);
        //obtiene datos del dataset
        for(int i=0;i<par.largoDataset;i++){
            float[] lineaTemp=new float[features.size()];
            for(int j=0;j<features.size();j++){ //se toman solo las columnas seleccionadas
                lineaTemp[j]=dataset[i][features.get(j)];
            }
            SparseVector x1 = generateFeatures(lineaTemp);
            problem.addExample(x1, dataset[i][par.cantidadTotalFeatures]); //se le asigna una clasificacion a la tupla
        }
        
        BinaryModel model = svm.train(problem, params);
        int hits = 0;
        for(int i=0;i<par.largoDataset;i++){
            float[] lineaTemp=new float[features.size()];
            for(int j=0;j<features.size();j++){ //se toman solo las columnas seleccionadas
                lineaTemp[j]=dataset[i][features.get(j)];
            }
            SparseVector xTest = generateFeatures(lineaTemp);
            float predictedLabel =  (float) model.predictLabel(xTest);
            if(predictedLabel==dataset[i][par.cantidadTotalFeatures]){
                hits++;
            }
        }
        acc=hits*100/par.largoDataset;
        System.out.println("Precisión: "+acc+"%");
    }
}
