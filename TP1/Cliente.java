package TP1;
import java.time.LocalDate;

import TP1.aed3.Registro;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class Cliente implements Registro {

    public int id;
    public String nome;
    public String cpf; // nao vai existir
    public float salario; // nao vai existir
    public LocalDate nascimento; // nao vai existir
    public int idCategoria;

    public String email;
    public String senha;
    public String perguntaSecreta;
    public String respostaSecreta;

    public Cliente() {
        this(-1, "", "", 0F, LocalDate.now(), "", "", "", "");
    }
    public Cliente(String n, String c, float s, LocalDate d, String email, String senha, String perguntaSecreta, String respostaSecreta) {
        this(-1, n, c, s, d, email, senha, perguntaSecreta, respostaSecreta);
    }

    public Cliente(int i, String n, String c, float s, LocalDate d, String email, String senha, String perguntaSecreta, String respostaSecreta) {
        this.id = i;
        this.nome = n;
        this.cpf = c;
        this.salario = s;
        this.nascimento = d;

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

    public String getCpf() {
        return cpf;
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
        return "\nNome......: " + this.nome +
               "\nEmail.....: " + this.email 
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

        byte[] cpf = new byte[11];
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.email = dis.readUTF();
        this.perguntaSecreta = dis.readUTF();
        this.respostaSecreta = dis.readUTF();
    }
}