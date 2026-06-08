package src.usuarios;

import src.infraestrutura.ArquivoIndexado;
import src.infraestrutura.HashExtensivel;
import src.infraestrutura.ParIdEndereco;

public class ArquivoUsuario extends ArquivoIndexado<Usuario> {

    HashExtensivel<ParEmailId> indiceIndiretoEmail;

    public ArquivoUsuario() throws Exception {
        super("clientes", Usuario.class.getConstructor());
        indiceIndiretoEmail = new HashExtensivel<>(
            ParEmailId.class.getConstructor(), 
            4, 
            ".\\dados\\clientes\\indiceEmail.d.db",   // diretório
            ".\\dados\\clientes\\indiceEmail.c.db"    // cestos 
        );
    }

    @Override
    public int create(Usuario c) throws Exception {
        int id = super.create(c);
        indiceIndiretoEmail.create(new ParEmailId(c.getEmail(), id));
        return id;
    }

    public Usuario read(String email) throws Exception {
        ParEmailId pci = indiceIndiretoEmail.read(ParEmailId.hash(email));
        if(pci == null)
            return null;
        return read(pci.getId());
    }
    
    public boolean delete(String email) throws Exception {
        ParEmailId pci = indiceIndiretoEmail.read(ParEmailId.hash(email));
        if(pci != null) 
            if(delete(pci.getId())) 
                return indiceIndiretoEmail.delete(ParEmailId.hash(email));
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Usuario c = super.read(id);
        if(c != null) {
            if(super.delete(id))
                return indiceIndiretoEmail.delete(ParEmailId.hash(c.getEmail()));
        }
        return false;
    }

    @Override
    public boolean update(Usuario novoCliente) throws Exception {
        Usuario clienteVelho = read(novoCliente.getEmail());
        if(super.update(novoCliente)) {
            if(novoCliente.getEmail().compareTo(clienteVelho.getEmail())!=0) {
                indiceIndiretoEmail.delete(ParEmailId.hash(clienteVelho.getEmail()));
                indiceIndiretoEmail.create(new ParEmailId(novoCliente.getEmail(), novoCliente.getId()));
            }
            return true;
        }
        return false;
    }
}

