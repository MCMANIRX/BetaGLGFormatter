import java.io.*;
import java.nio.ByteBuffer;

public class main {
	  
	  
	public static void main(String[] args) throws IOException{
		
		File inputFile;
		GLGReader gl;
		
		if(args.length > 0 ) {
			
		 inputFile  = new File(args[0]);
			gl = new GLGReader(inputFile);
			gl.readGLG();
			gl.writeGLG();
		}
		else System.out.println("usage: java - jar BGF.jar input_file");
		
		
	}
	
	}