package TP1;

import aed3.Registro;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class Curso implements Registro {

    public int id;
    public int idUsuario; // Chave Estrangeira (FK)
    public String nome;
    public String dataInicio;
    public String descricao;
    public String codigoNanoID; // 10 caracteres alfanuméricos
    public byte estado; // 0-Ativo, 1-Encerrado, 2-Concluído, 3-Cancelado

    // Construtor vazio para o CRUD
    public Curso() {
        this(-1, -1, "", "", "", "", (byte)0);
    }

    // Construtor completo
    public Curso(int id, int idUsuario, String nome, String data, String desc, String codigo, byte estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.dataInicio = data;
        this.descricao = desc;
        this.codigoNanoID = codigo;
        this.estado = estado;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    // Getters úteis para os menus e filtros
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getStatusExtenso() {
        switch (this.estado) {
            case 0: return "Curso ativo e recebendo inscrições";
            case 1: return "Curso ativo, mas sem novas inscrições";
            case 2: return "Curso realizado e concluído";
            case 3: return "Curso cancelado";
            default: return "Estado desconhecido";
        }
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeInt(this.idUsuario);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.dataInicio);
        dos.writeUTF(this.descricao);
        dos.writeUTF(this.codigoNanoID);
        dos.writeByte(this.estado);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.idUsuario = dis.readInt();
        this.nome = dis.readUTF();
        this.dataInicio = dis.readUTF();
        this.descricao = dis.readUTF();
        this.codigoNanoID = dis.readUTF();
        this.estado = dis.readByte();
    }

    @Override
    public String toString() {
        return "\nID: " + id + 
               "\nCódigo: " + codigoNanoID +
               "\nNome: " + nome + 
               "\nInício: " + dataInicio + 
               "\nStatus: " + getStatusExtenso();
    }
}