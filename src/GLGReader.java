import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.stream.Stream;

public class GLGReader {
	
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
	  public static long glOff;
	  
	  public static int MAT_KEY = 0x16;
	  public static int MESH_KEY = 0x04;
	  public static int MODEL_KEY = 0x03;
	  public static int IND_KEY = 0x07;
	  public static int VERT_KEY = 0x06;
	  public static int VAPD_KEY = 0x05;
	  public static int MATRIX_KEY = 0x02;
	  public static int SPOOK_KEY = 0x08; //haha
	  public static int BONE_KEY = 0x0A;
	  public static int BHASH_KEY = 0x0b;
	  public static int GL_KEY;
	  
	  public static int modelCount;
	  public static int meshCount;
	  public static int attCnt;
	  
	  public static boolean isOld;
	  public static boolean isMap;
	  
	  public static boolean missed;
	  public static boolean missed1;
	
	  public static long ipos0;
	  public static long ipos1;
	  public static long ipos2;
	  
	  public static Mesh[] meshData;
	
	  public static File f ;
	  public static File o ;
	  public static File log ;
	  public static FileWriter logStrm;
	  public static RandomAccessFile read; 
	  public static RandomAccessFile w; 
	
	public GLGReader (File f) throws IOException {

		 this.f = f;
		 o = new File(f.getParent()+"\\_"+f.getName());
		 log = new File(f.getParent()+"\\convlog.txt");

	

		 logStrm = new FileWriter(log);
		 
		isOld = false;
		isMap = false;
		 
		read = new RandomAccessFile(f, "r");
		w = new RandomAccessFile(o, "rw");
		
		log.delete();
		
		p(f.getName());
		
		
	}
	
	
	public void readGLG() throws IOException {
		
		getModels();
		getMeshData();		
		
	}
	
	public static long find(int key) throws IOException {
			
		while(read.getFilePointer() <= f.length()-4) {
					
					byte b=0;
					int j = 0;
					
					if(read.readByte() == 0x01) 
						if((read.read()&0xFF) == 0xb0) {
							
							b=read.readByte();
							j= Math.abs(read.readInt());
							read.seek(read.getFilePointer()-0x4);
							
							if( j < 0xffff)
								if(b==(byte)key) 
									return read.getFilePointer();
									
								
				
			} //end if
		}//end while
		return -1;
		}
	
	
	static void found(String type) throws IOException {
			
			pd(type);
			
			
			
		long off = read.getFilePointer();
			
			switch(type) {
			
			case "mat":
				matOff = off;
				break;
			case "mesh":
				meshOff = off;
				break;
			case "model":
				modelOff = off;
				break;
			case "index":
				indexOff = off;
				break;
			case "verts":
				vertexOff = off;
				break;
			case "vapd":
				vertexAttOff = off;
				break;
			case "matrix":
				mateOff = off;
				break;
			case "spooky":
				spookOff = off;
				break;
			case "bhash":
				bHashOff = off;
				break;
			case "bone":
				boneOff = off;
				break;
			default:
				p("lol");
			}
			
			p(String.format("%s (0x%x)",type,off));

		}
	
	static void getModels() throws IOException {
		
		read.seek(0x0);
		
		modelOff=find(MODEL_KEY);
		
		read.seek(modelOff);
	
		int size = read.readInt();
		modelCount = size/16;
		int[] hashID = new int[modelCount];
		int lmeshCount = 0;
		
		for(int i = 0; i <modelCount; ++i) {
			lmeshCount= read.readInt();
			meshCount += lmeshCount;
			hashID[i] = read.readInt();
			
			read.readLong();
		 
		 
			p("model is at "+pdah((int)(modelOff+read.getFilePointer()))+" has a size of "+pdah(size)+", a mesh count of "+pdah(lmeshCount)+", and a hash of "+pint(hashID[i], 0)+". Model count is "+modelCount+".");
	
	}
		p("total mesh count is "+meshCount);
		
		if(modelCount > 50)
			isMap=true;
		
	}
	static void getMeshData() throws IOException {
		
		meshData = Stream.generate(() -> new Mesh()).limit(meshCount).toArray(Mesh[]::new);
	
	
		meshOff = find(MESH_KEY);
		
		read.seek(meshOff); //skip unk
		
		int size = read.readInt();
		
		p("size is "+pdah(size));
				
	
		 ipos0 = 0;
		 ipos1 = 0;
		 ipos2 = 0;
		 
	int	 unkPos = 0;
		
		
		for(int i = 0; i < meshCount; ++i) {
			
				missed = false;
				missed1 = false;
				ipos0 = read.getFilePointer();
				
				unkPos = 0;
				
				read.readShort();
				meshData[i].faceFormat = (int)read.readShort();
				meshData[i].faceOff= read.readInt(); 
				meshData[i].faceCnt = (int)read.readShort(); 
				meshData[i].faceType = (int)read.readByte(); //face type
				meshData[i].attributeCount = (int)read.readByte(); //attribute count
				
				errorCheck("att",i);
							
				meshData[i].unk1 = read.readInt(); //unk
				meshData[i].matHash = read.readInt(); //Material hash ID
				meshData[i].unk2 = read.readInt();
				meshData[i].unk3 = read.readInt(); //0x01892562
				
				errorCheck("unk3",i);		
							
				meshData[i].matOff = read.readInt(); //Material offset
				meshData[i].unk5 = read.readInt(); //unk
				meshData[i].texHash= read.readInt(); //texture hash id
				
				errorCheck("tex",i);
				
				meshData[i].texUnk= read.readInt();
				
				meshData[i].FFunk0 = read.readInt();
				meshData[i].FFunk1 = read.readInt();
				meshData[i].FFunk2 = read.readInt();
				meshData[i].FFunk3 = read.readInt();
				
				errorCheck("cleanup",i);
				
			//re-align and get unknown byte 'unk7'
				meshData[i].unk7 =0 ;		
				
				read.seek(ipos0+0x20); //arbitrary, should probably fix at some point
				
				while(read.read() !=0xff);
				while(read.readShort() != 0x0);
				
				read.seek(read.getFilePointer()-0x4);			
				while(!zeroFF(read.read()) && !zeroFF(read.read()));
				
			//	pl(read.getFilePointer());
				meshData[i].unk7 = read.readByte();
				//pl(meshData[i].unk7);
				
				if(meshData[i].unk7 ==0x0) {
					p("byte read error. Aborting at "+pdah((int) read.getFilePointer()));
					System.exit(0);
				}
			
			
			while(read.readByte()== 0);
			read.seek(read.getFilePointer() -0x7); //re-align
			

	
		
			p("==========Mesh "+(i)+"===========");
			p("index format: "+pint(meshData[i].faceFormat,0));
			p("index start offset: "+pint(meshData[i].faceOff,0));
			p("index count: "+pint(meshData[i].faceCnt,0));
			p("face type: "+pint(meshData[i].faceType,0));
			p("attribute count: "+pint(meshData[i].attributeCount,0));
			p("material Hash ID: "+pint(meshData[i].matHash,0));
			p("material offset: "+pint(meshData[i].matOff,0));
			p("texture hash ID: "+pint(meshData[i].texHash,0));
			p("===========================\n");
			
		}
		
	}

	
	public static boolean zeroFF(int b) {
		return b != 0 || b != 0xff;
		
	}

	private static void errorCheck(String mode, int i) throws IOException {
		
		switch(mode) {
			case "att":
								//if attribute count is unreasonable (<=0, >=10)....
				if(i>0 && meshData[i].attributeCount == 0 || meshData[i].attributeCount >10 ) {
					ipos1 = read.getFilePointer();
					
					missed =true;
					p("re-aligning...");
									//...look for a new attribute count value, going backwards.
				while(i>0 && meshData[i].attributeCount == 0 || meshData[i].attributeCount >10 && read.getFilePointer() > ipos0) {
					read.seek(read.getFilePointer()-0x2);
					meshData[i].attributeCount = (int)read.readByte();
				}
				}
				//if attribute count is still unreasonable (<=0, >=10)....
				if(i>0 && meshData[i].attributeCount == 0 || meshData[i].attributeCount >10) {
					read.seek(ipos1);
					
					//...look for a new attribute count value, going forwards.
	
				while(i>0 && meshData[i].attributeCount == 0 || meshData[i].attributeCount >10 && read.getFilePointer() < ipos0+0x4a)  {
					meshData[i].attributeCount = (int)read.readByte();
				}
				}
				break;
		
			case "unk3":
					//if "unk3" is not this 4-byte constant, find it somewhere inside the mesh table entry's 0x4a bytes.
				if(meshData[i].unk3 != 0x01892562)
					meshData[i].unk3 = read.readInt();
				
				if(meshData[i].unk3 != 0x01892562) {
					
					missed1 = true;
					p("re-aligning...");
					
					ipos2 = read.getFilePointer()-0x4;
					while(meshData[i].unk3 != 0x01892562 && read.getFilePointer() < ipos0+0x4a) {
						read.seek(read.getFilePointer()-0x3);
						meshData[i].unk3 = (int)read.readInt();
					
					}//end while
					
					while(meshData[i].unk3 != 0x01892562 && read.getFilePointer() > ipos0) {
						read.seek(read.getFilePointer()-0x5);
						meshData[i].unk3 = (int)read.readInt();
					
					}//end while
					
					
				}

				break;
				
			case "tex":
				if(meshData[i].texHash==0x0)
					meshData[i].texHash = read.readInt();
				break;
				
			case "cleanup": //fix all alignment erros and clean up
				
				if(missed) 
					read.seek(ipos1-0xb);
				
				if(missed1) {
					read.seek(ipos0+3);
				}
				
				if(missed||missed1) {
					
					meshData[i].faceFormat = (int)read.readShort();
					meshData[i].faceOff= read.readInt(); 
					meshData[i].faceCnt = (int)read.readShort(); 
					meshData[i].faceType = (int)read.readByte(); 
					meshData[i].attributeCount = (int)read.readByte(); 
					
					meshData[i].unk1 = read.readInt(); //unk
					meshData[i].matHash = read.readInt(); //Material hash ID
					meshData[i].unk2 = read.readInt();
					meshData[i].unk3 = read.readInt(); //0x01892562
					
					errorCheck("unk3",i);
					
					meshData[i].matOff = read.readInt(); //Material offset
				}
				
				
	
					
				
			if(missed||missed1)
				read.seek(ipos0+0x32); //re-align
				
				break;
				
				

				
		}

		}


	public void writeGLG() throws IOException {
		
		o.delete();
		
		w.seek(0x0);
		read.seek(0x0);

		
		
		
		while(wp() < meshOff)
			w.write(read.read());
		
	
		w.writeShort(0x0);
		for(Mesh m : meshData) 
			w.write(m.getMeshT3()); //write mesh table
		
		w.seek(meshOff);
		w.writeInt(meshCount*0x4a); //write section size
		
		w.seek(wp() + (meshCount*0x4a)); //return to eof
		
		
		
		
		//copy the rest of the file's contents
		vertexAttOff = find(VAPD_KEY);
		read.seek(Math.abs(vertexAttOff -0x4));
		
		byte[] restOfFile = new byte[(int) ( f.length()- read.getFilePointer())];
		
		read.read(restOfFile);
		w.write(restOfFile);
		
		//write filesize
	
		w.seek(0x4);
		w.writeInt((int)(o.length()-0x8));
		
		//close log stream
		logStrm.close();
		
	}
	
	public static void p(String txt) throws IOException {
		
		System.out.println(txt);
		log(txt);
	}
		
	public static String pint(int txt, int mode) throws IOException {
		
		
		String text = Integer.toHexString((txt));
		if(mode ==1) 
			p(text); 
		else
			return text;
		return null;
			
		}
	
	public static void pl(long txt) throws IOException {
		
		
		String text = Long.toHexString((txt));
	p(text);
			
		}
		
	public static String pdah(int txt) throws IOException {
		
		
		
		return (txt+" (0x"+Integer.toHexString(txt)+")");
	}
	
	public static void pd(String type) throws IOException {
		
		p(type+" detected");
		
	}
	
	public static void pos() throws IOException {
		
		p(pdah((int)read.getFilePointer()));
	}
	
	public static long wp() throws IOException {
		
		return w.getFilePointer();
	}
	
	
	public static void log(String txt) throws IOException {
	
		logStrm.write(txt+"\n");
	}
	}