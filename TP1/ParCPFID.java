package TP1;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParCPFID implements TP1.aed3.RegistroHashExtensivel<ParCPFID> {
    
    private String emailString; // chave
    private int id;     // valor
    private final short TAMANHO = 15;  // tamanho em bytes

    public ParCPFID() {
        this.emailString = "";
        this.id = -1;
    }

    public ParCPFID(String emailString, int id) throws Exception {
        // Certifique-se de que o email contém um formato válido
        if (!emailString.contains("@") || !emailString.contains(".")) {
            throw new IllegalArgumentException("Email deve conter '@' e '.com'.");
        }
        this.emailString = emailString;
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
        dos.write(this.emailString.getBytes());
        dos.writeInt(this.id);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] b = new byte[11];
        dis.read(b);
        this.emailString = new String(b);
        this.id = dis.readInt();
    }

    public static int hash(String email) throws IllegalArgumentException {
        // Certifique-se de que o email contém um formato válido
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email deve conter '@' e '.com'.");
        }

        // Converter o email para um número inteiro longo
        long emailLong = Long.parseLong(email.replace("@", "").replace(".", ""));

        // Aplicar uma função de hash usando um número primo grande
        int hashValue = (int) (emailLong % (int)(1e9 + 7));

        // Retornar um valor positivo
        return Math.abs(hashValue);
    }


}