package TP1;
import aed3.*;

public class ArquivoCliente extends aed3.Arquivo<Cliente> {

    Arquivo<Cliente> arqClientes;
    HashExtensivel<ParCPFID> indiceIndiretoEmail;

    public ArquivoCliente() throws Exception {
        super("clientes", Cliente.class.getConstructor());
        indiceIndiretoEmail = new HashExtensivel<>(
            ParCPFID.class.getConstructor(), 
            4, 
            ".\\dados\\clientes\\indiceEmail.d.db",   // diretório
            ".\\dados\\clientes\\indiceEmail.c.db"    // cestos 
        );
    }

    @Override
    public int create(Cliente c) throws Exception {
        int id = super.create(c);
        indiceIndiretoEmail.create(new ParCPFID(c.getEmail(), id));
        return id;
    }

    public Cliente read(String email) throws Exception {
        ParCPFID pci = indiceIndiretoEmail.read(ParCPFID.hash(email));
        if(pci == null)
            return null;
        return read(pci.getId());
    }
    
    public boolean delete(String email) throws Exception {
        ParCPFID pci = indiceIndiretoEmail.read(ParCPFID.hash(email));
        if(pci != null) 
            if(delete(pci.getId())) 
                return indiceIndiretoEmail.delete(ParCPFID.hash(email));
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Cliente c = super.read(id);
        if(c != null) {
            if(super.delete(id))
                return indiceIndiretoEmail.delete(ParCPFID.hash(c.getEmail()));
        }
        return false;
    }

    @Override
    public boolean update(Cliente novoCliente) throws Exception {
        Cliente clienteVelho = read(novoCliente.getEmail());
        if(super.update(novoCliente)) {
            if(novoCliente.getEmail().compareTo(clienteVelho.getEmail())!=0) {
                indiceIndiretoEmail.delete(ParCPFID.hash(clienteVelho.getEmail()));
                indiceIndiretoEmail.create(new ParCPFID(novoCliente.getEmail(), novoCliente.getId()));
            }
            return true;
        }
        return false;
    }
}