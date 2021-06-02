import java.io.IOException;

public class Mesh {
	
	
	public int unk0;
	public int faceFormat;
	public int faceOff;
	public int faceCnt;
	public int faceType;
	public int attributeCount;
	public int unk1;
	public int matHash;
	public int unk2;
	public int unk3;
	public int unk4;
	public int matOff;
	public int unk5;
	public int texHash;
	public int texUnk;
	public int unk6;
	public int unk7;
	public int FFunk0;
	public int FFunk1;
	public int FFunk2;
	public int FFunk3;
	
	public byte[] mesh;
	private int index;
	
	
	
	public Mesh()  {
		index = 0;
		

		
		
	}
	
	public byte[] getMeshT3() throws IOException { //9 0's after 3, 6 0's start
		
		mesh = new byte[0x4a];
		
		putInt(0);
		putShort(faceFormat);
		putInt(faceOff);
		putShort(faceCnt);
		putByte(faceType);
		putByte(attributeCount);
		putInt(unk1);
		putInt(matHash);
		putInt(unk2);
		putInt(0);
		putInt(0x01892562); //const
		putInt(0x001D0007); //tmp const 		//putInt(matOff);
		putInt(unk5);
		putInt(texHash);
		putInt(texUnk);
		
		putInt(FFunk0);
		putInt(FFunk1);
		putInt(FFunk2);
		putInt(FFunk3);
		/*putInt(0xFFFFFFFF);
		putInt(0xFFFFFFFF);
		putInt(0xFFFFFFFF);
		putInt(0xFFFFFFFF);*/
		
		
		
	

		putByte(unk7);
		putInt(0);
		putByte(0);
		
		
		
		
		
		
		return mesh;
		
	}
	
	private void putInt(int i) {
		
		for(int j = 0; j < 4; ++j)
			mesh[index++] = (byte) ((i >> (24-(j*8))) &0xFF);
	}

	private void putShort(int i) {
		
		
		for(int j = 0; j < 2; ++j)
			mesh[index++] = (byte) ((i >> (8-(j*8))) &0xFF);
	}
	
	private void putByte(int i) throws IOException {
		
			mesh[index++] = (byte) (i &0xFF);


	}
}

/*//index start offset
		meshData[i][2] = (int)read.readShort(); //index count
		meshData[i][3] = (int)read.readByte(); //face type
		meshData[i][4] = (int)read.readByte(); //attribute count
		read.readInt(); //unk
		meshData[i][5] = read.readInt(); //Material hash ID
		read.readInt();
		read.readInt();
		read.readInt();//unk
		meshData[i][6] = read.readInt(); //Material offset
		read.readInt(); //unk
		meshData[i][7] = read.readInt(); //texture hash id
		read.seek(read.getFilePointer()+0x16); //1e*/
