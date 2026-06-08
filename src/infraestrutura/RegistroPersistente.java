package src.infraestrutura;

import java.io.IOException;

public interface RegistroPersistente {
    void setId(int i);
    int getId();
    byte[] toByteArray() throws IOException;
    void fromByteArray(byte[] b) throws IOException;
}


