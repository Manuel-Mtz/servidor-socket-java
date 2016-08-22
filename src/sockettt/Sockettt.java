/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockettt;

import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 *
 * @author Manuel
 */
public class Sockettt {

    final static int PUERTO=5000;
    static ServerSocket sc;
    static Socket so;
    static DataOutputStream salida;
    static String mensajeRecibido;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){ //throws IOException {
        // TODO code application logic here
        initServer();
       
    }
    

    
    public static void initServer(){ //throws IOException{
        BufferedReader entrada;
        Boolean ciclo=true;
        
        //while(ciclo){
        try{
            
            sc = new ServerSocket(PUERTO );/* crea socket servidor que escuchara en puerto 5000*/
            so=new Socket();
            System.out.println("Esperando una conexión:");
            so = sc.accept();
//Inicia el socket, ahora esta esperando una conexión por parte del cliente
            System.out.println("Un cliente se ha conectado.");
//Canales de entrada y salida de datos

            entrada = new BufferedReader(new InputStreamReader(so.getInputStream()));
            salida = new DataOutputStream(so.getOutputStream());
            System.out.println("Confirmando conexion al cliente....");
            salida.writeUTF("Conexion exitosa...\r\n");
//Recepcion de mensaje
            
            while (ciclo){
            salida.writeUTF("Envia peticion \r\n");    
            mensajeRecibido = entrada.readLine();
            System.out.println(mensajeRecibido);
            salida.writeUTF("Se recibio tu peticion \r\n");
            
            if(mensajeRecibido.equals("help"))
                salida.writeUTF("Posibles comandos \r\n"+"all \r\n"+"so \r\n"+ "user \r\n"+ "mac \r\n"+"disc \r\n"+"date \r\n"+"name \r\n"+"process \r\n"+"processor \r\n"+ "exit \r\n");
            else if(mensajeRecibido.equals("all")){
                salida.writeUTF(SO());
                salida.writeUTF("Usuario: "+Username());
                salida.writeUTF("Direccion MAC: "+ MAC()+"\r\n");
                salida.writeUTF("Discos: "+ Discos()+"\r\n");
                salida.writeUTF(imprimirInfoCPU()+"\r\n");
                salida.writeUTF(Fecha().toString()+"\r\n");
                salida.writeUTF(nombEquipo());
                String salidas=Procesos();
                salida.writeUTF(salidas);
                
            }
            else if(mensajeRecibido.equals("so"))
                salida.writeUTF(SO());
            else if(mensajeRecibido.equals("user"))
                salida.writeUTF("Usuario: "+Username());
            else if(mensajeRecibido.equals("mac"))
                salida.writeUTF("Direccion MAC: "+ MAC()+"\r\n");
            else if(mensajeRecibido.equals("disc"))
                salida.writeUTF("Discos: "+ Discos()+"\r\n");
            else if(mensajeRecibido.equals("date"))
                salida.writeUTF(Fecha().toString()+"\r\n");
            else if(mensajeRecibido.equals("name"))
                salida.writeUTF(nombEquipo());
            else if(mensajeRecibido.equals("process")){
                String salidas=Procesos();
                salida.writeUTF(salidas);
            }
                
            else if(mensajeRecibido.equals("processor"))   
                salida.writeUTF(imprimirInfoCPU()+"\r\n");
            //
            //salida.writeUTF(Fecha().toString());
            //String salidas=Procesos();
            //salida.writeUTF(Procesos());
            //System.out.println(salidas);
            //salida.writeUTF(salidas);
            //salida.writeUTF(SO());
            //salida.writeUTF("Usuario: "+Username());
            //salida.writeUTF("Direccion MAC: "+ MAC()+"\r\n");
            //salida.writeUTF("Discos: "+ Discos()+"\r\n");
            //salida.writeUTF(imprimirInfoCPU()+"\r\n");
            //
            else if(mensajeRecibido.equals("exit")){
            salida.writeUTF("Gracias por conectarte, adios!");
            System.out.println("Cerrando conexión...");
            ciclo=false;
            sc.close();//Aqui se cierra la conexión con el cliente
            }
            else  salida.writeUTF("Comando invalido..\r\n");
        }
}catch(Exception e ){
    System.out.println("Error: "+e.getMessage());
    //sc.close();
    }
        
  //      }
}
    
    public static String red(){
        String red="";
        try {

            InetAddress addr = InetAddress.getLocalHost();

            String hostname = addr.getHostName();

            red="IP: " + addr.getHostAddress()+"\r\n";

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return red;
    }
    
    public static String nombEquipo(){
        String nombre="";
        
        try {

            InetAddress addr = InetAddress.getLocalHost();

            String hostname = addr.getHostName();

            nombre= "Host: " + hostname+"\r\n";

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return nombre;
    }
    
    public static String SO(){
        String So=null;
        So= System.getProperty("os.name");
        So= So+ " architectura: "+System.getProperty("os.arch");
        So= So+ " version: "+System.getProperty("os.version")+"\r\n";
        return So;
    }
    
    /*public static String Procesador(){
        String procesador=null;
        Sigar sig= new Sigar();
        
        
        procesador= System.getenv("PROCESSOR_IDENTIFIER");
        
        return procesador;
    } */
    
    public static String imprimirInfoCPU() {
        Sigar sigar = new Sigar();
        CpuInfo[] infos = null;
        CpuPerc[] cpus = null;
        String resultado="";
        try {
            infos = sigar.getCpuInfoList();
            cpus = sigar.getCpuPercList();
        } catch (SigarException e) {
            e.printStackTrace();
        }
 
        CpuInfo info = infos[0];
        //long tamanioCache = info.getCacheSize();
        resultado= "\r\n Fabricante:" + info.getVendor()+"\r\n"+"Modelo:" + info.getModel()+"\r\n"+"Mhz: " + info.getMhz()+"\r\n"+
                "Total CPUs: " + info.getTotalCores();
      /*  System.out.println("Fabricante:tt" + info.getVendor());
        System.out.println("Modelottt" + info.getModel());
        System.out.println("Mhzttt" + info.getMhz());
        System.out.println("Total CPUstt" + info.getTotalCores());
        if ((info.getTotalCores() != info.getTotalSockets())
                || (info.getCoresPerSocket() > info.getTotalCores())) {
            System.out.println("CPUs fisiscastt" + info.getTotalSockets());
            System.out.println("Nucleos por CPUtt" + info.getCoresPerSocket());
        }*/
        return resultado;
    }    
    
    public static String Username(){
        String user=null;
        user= System.getProperty("user.name")+"\r\n";
       
        return user;
    }

    public static Date Fecha(){
        Date fecha = new Date();
        return fecha;
    }
    
    public static String Procesos(){
        String lista="";
        
        try{
            //Process proc = Runtime.getRuntime().exec("wmic.exe");
            Process proc = Runtime.getRuntime().exec("cmd /C tasklist");
           // Process proc = Runtime.getRuntime().exec("cmd /C systeminfo");
            BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            //OutputStreamWriter oStream = new OutputStreamWriter(proc.getOutputStream());
            //oStream .write("process where name='nuestroProceso.exe'");
            String line=null;
            while ((line = input.readLine()) != null) 
            { 
            //if (line.contains("nuestroProceso.exe")) 
                //return true;
                lista=lista+"\r\n"+line;
            } 
            input.close(); 
            } 
            catch (Exception e) 
            { 
        // handle error 
                 e.printStackTrace();
            }
        return lista;
    }
    
    public static String MAC(){
        InetAddress ip;
        String Mac="";
	try {

		ip = InetAddress.getLocalHost();
		System.out.println("Current IP address : " + ip.getHostAddress());

		NetworkInterface network = NetworkInterface.getByInetAddress(ip);

		byte[] mac = network.getHardwareAddress();

		System.out.print("Current MAC address : ");

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
		}
                Mac=sb.toString();
		System.out.println(sb.toString());

	} catch (UnknownHostException e) {

		e.printStackTrace();

	} catch (SocketException e){

		e.printStackTrace();

	}
        return Mac;
   
    }
    
    public static String Discos(){
         List files = Arrays.asList(File.listRoots());
         String disco="";
        for (Object f : files) {
            String elemento = FileSystemView.getFileSystemView().getSystemDisplayName((File) f);
            if (!elemento.isEmpty()) //No admite elementos vacios
            {
                System.out.println("getSystemDisplayName : " + elemento); //Me manda el nombre de la unidad
                disco=disco+ elemento+ "\r\n" ;
            }
        }
        
        return disco;
    }
    
}
