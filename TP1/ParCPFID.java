package TP1;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParCPFID implements aed3.RegistroHashExtensivel<ParCPFID> {
    
    private String emailString; // chave
    private int id;     // valor
    private static final short EMAIL_BYTES = 64; // tamanho fixo em bytes para o email
    private final short TAMANHO = (short) (EMAIL_BYTES + 4);  // email + id

    public ParCPFID() {
        this.emailString = "";
        this.id = -1;
    }

    public ParCPFID(String emailString, int id) throws Exception {
        if (emailString == null || !emailString.contains("@") || !emailString.contains(".")) {
            throw new IllegalArgumentException("Email deve conter '@' e '.com'.");
        }
        this.emailString = emailString.trim();
        this.id = id;
    }

    public String getEmailString() {
        return emailString;
    }

    public int getId() {
        return id;
    }

 
    @Override
    public int hashCode() {
        return hash(this.emailString);
    }

    public short size() {
        return this.TAMANHO;
    }

    public String toString() {
        return "("+this.emailString + ";" + this.id+")";
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] emailBytes = this.emailString.getBytes("UTF-8");
        if (emailBytes.length > EMAIL_BYTES) {
            dos.write(emailBytes, 0, EMAIL_BYTES);
        } else {
            dos.write(emailBytes);
            dos.write(new byte[EMAIL_BYTES - emailBytes.length]);
        }
        dos.writeInt(this.id);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] b = new byte[EMAIL_BYTES];
        dis.readFully(b);
        this.emailString = new String(b, "UTF-8").trim();
        this.id = dis.readInt();
    }

    public static int hash(String email) throws IllegalArgumentException {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email deve conter '@' e '.com'.");
        }
        int h = email.trim().toLowerCase().hashCode();
        if (h == Integer.MIN_VALUE)
            return 0;
        return Math.abs(h);
    }


}