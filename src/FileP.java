
import java.io.Serializable;

/* @author Isaac */
public class FileP implements Serializable
{            
    int maxReadBufferSize = 240 * 1024; //240KB
    private String piece;
    private byte[] data = new byte[maxReadBufferSize];
    private  String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public FileP(){};
    
    public FileP(String piece, byte[] data, String fileName){       
        this.piece = piece;
        this.data = data;
        this.fileName = fileName;
    }
    
    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    
    
}
