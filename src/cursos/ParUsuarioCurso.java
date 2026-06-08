package src.cursos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import src.core.RegistroArvoreBMais;

/**
 * Representa um par de IDs (idUsuario, idCurso) para a relação 1:N
 * Implementa RegistroArvoreBMais para ser armazenado em uma Árvore B+
 */
public class ParUsuarioCurso implements RegistroArvoreBMais<ParUsuarioCurso> {
    
    private int idUsuario;
    private int idCurso;
    private final short TAMANHO = 8; // 4 bytes para idUsuario + 4 bytes para idCurso
    
    public ParUsuarioCurso() {
        this.idUsuario = -1;
        this.idCurso = -1;
    }
    
    public ParUsuarioCurso(int idUsuario, int idCurso) {
        this.idUsuario = idUsuario;
        this.idCurso = idCurso;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    
    public int getIdCurso() {
        return idCurso;
    }
    
    @Override
    public short size() {
        return TAMANHO;
    }
    
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.idUsuario);
        dos.writeInt(this.idCurso);
        return baos.toByteArray();
    }
    
    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idUsuario = dis.readInt();
        this.idCurso = dis.readInt();
    }
    
    @Override
    public int compareTo(ParUsuarioCurso obj) {
        // Primeiro compara pelo idUsuario
        if (this.idUsuario != obj.idUsuario) {
            return this.idUsuario - obj.idUsuario;
        }
        // Se idUsuario for igual, compara pelo idCurso
        return this.idCurso - obj.idCurso;
    }
    
    @Override
    public ParUsuarioCurso clone() {
        return new ParUsuarioCurso(this.idUsuario, this.idCurso);
    }
    
    @Override
    public String toString() {
        return "(" + idUsuario + ";" + idCurso + ")";
    }
}


