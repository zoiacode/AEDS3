package TP1;

import aed3.Registro;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CursoUsuario implements Registro {

    public int id;
    public int idCurso;
    public int idUsuario;
    public String dataInscricao;

    public CursoUsuario() {
        this(-1, -1, -1, "");
    }

    public CursoUsuario(int id, int idCurso, int idUsuario, String dataInscricao) {
        this.id = id;
        this.idCurso = idCurso;
        this.idUsuario = idUsuario;
        this.dataInscricao = dataInscricao;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeInt(this.idCurso);
        dos.writeInt(this.idUsuario);
        dos.writeUTF(this.dataInscricao);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.idCurso = dis.readInt();
        this.idUsuario = dis.readInt();
        this.dataInscricao = dis.readUTF();
    }

    @Override
    public String toString() {
        return "ID Inscrição: " + id +
               "\nID Curso: " + idCurso +
               "\nID Usuário: " + idUsuario +
               "\nData da inscrição: " + dataInscricao;
    }
}
