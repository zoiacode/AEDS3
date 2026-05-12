package TP1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParInteiroInteiro implements RegistroArvoreBMais<ParInteiroInteiro> {

    private int chave1;
    private int chave2;
    private final short TAMANHO = 8;

    public ParInteiroInteiro() {
        this.chave1 = -1;
        this.chave2 = -1;
    }

    public ParInteiroInteiro(int chave1, int chave2) {
        this.chave1 = chave1;
        this.chave2 = chave2;
    }

    public int getChave1() {
        return chave1;
    }

    public int getChave2() {
        return chave2;
    }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.chave1);
        dos.writeInt(this.chave2);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.chave1 = dis.readInt();
        this.chave2 = dis.readInt();
    }

    @Override
    public int compareTo(ParInteiroInteiro obj) {
        if (this.chave1 != obj.chave1) {
            return this.chave1 - obj.chave1;
        }
        return this.chave2 - obj.chave2;
    }

    @Override
    public ParInteiroInteiro clone() {
        return new ParInteiroInteiro(this.chave1, this.chave2);
    }

    @Override
    public String toString() {
        return "(" + chave1 + ";" + chave2 + ")";
    }
}
