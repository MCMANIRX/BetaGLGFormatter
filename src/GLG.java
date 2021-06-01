import java.io.File;
import java.io.IOException;

public class GLG {
	
	  public static long matOff;
	  public static long meshOff;
	  public static long modelOff;
	  public static long indexOff;
	  public static long vertexOff;
	  public static long vertexAttOff;
	  public static long mateOff;
	  public static long spookOff;
	  public static long boneOff;
	  public static long bHashOff;
	  
	  public static int modelCount;
	  
	  public static boolean isOld;
	  public static File f ;
	  
	  
	  public GLG() {
		  
	  }
	  
	  public GLG(boolean isOld, File f)  {
		  
		  this.isOld = isOld;
		  this.f = f;
		  
	  }
	  
	
	  

}
