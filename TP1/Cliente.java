package TP1;

import TP1.aed3.Registro;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class Cliente implements Registro {

    public int id;
    public String nome;
    public String email;
    public String senha;
    public String perguntaSecreta;
    public String respostaSecreta;

    public Cliente() {
        this(-1, "", "", "", "", "");
    }
    public Cliente(String nome, String email, String senha, String pergunta, String resposta) {
        this(-1, nome, email, senha, pergunta, resposta);
    }

    public Cliente(int i, String n, String email, String senha, String perguntaSecreta, String respostaSecreta) {
        this.id = i;
        this.nome = n;
        this.email = email;
        this.senha = senha;
        this.perguntaSecreta = perguntaSecreta;
        this.respostaSecreta = respostaSecreta;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPerguntaSecreta() {
        return perguntaSecreta;
    }


    public String toString() {
        return "\nID........: " + this.id +
               "\nNome......: " + this.nome +
               "\nEmail.....: " + this.email +
               "\nPergunta..: " + this.perguntaSecreta
               ;     
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.email);
        dos.writeUTF(this.senha);
        dos.writeUTF(this.perguntaSecreta);
        dos.writeUTF(this.respostaSecreta);
        return baos.toByteArray();
    }


    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.email = dis.readUTF();
        this.senha = dis.readUTF();
        this.perguntaSecreta = dis.readUTF();
        this.respostaSecreta = dis.readUTF();
    }
}