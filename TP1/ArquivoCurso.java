package TP1;

import TP1.aed3.*;
import java.util.ArrayList;

/**
 * Gerencia operações CRUD de Curso com índices indiretos
 * Mantém relação 1:N com usuários através da Árvore B+
 */
public class ArquivoCurso extends TP1.aed3.Arquivo<Curso> {

    private ArvoreBMais<ParIdId> arvoreCursoUsuario; // Índice 1:N (idUsuario, idCurso)

    public ArquivoCurso() throws Exception {
        super("cursos", Curso.class.getConstructor());
        
        // Inicializa a árvore B+ para a relação 1:N (idUsuario, idCurso)
        // Ordem 4 = máximo 3 elementos por página
        arvoreCursoUsuario = new ArvoreBMais<>(
            ParIdId.class.getConstructor(),
            4,
            ".\\dados\\cursos\\relacao.b.db"
        );
    }

    /**
     * Cria um novo curso e o vincula ao usuário
     * @param c Curso a ser criado
     * @return ID autogerado do curso
     */
    @Override
    public int create(Curso c) throws Exception {
        // Gera automaticamente o código NanoID
        c.codigoNanoID = NanoID.generate();
        
        // Cria o curso usando o CRUD genérico
        int id = super.create(c);
        
        // Vincula o curso ao usuário na árvore B+
        arvoreCursoUsuario.create(new ParIdId(c.getIdUsuario(), id));
        
        return id;
    }

    /**
     * Lê um curso pelo ID
     * @param id ID do curso
     * @return Objeto Curso ou null se não encontrado
     */
    @Override
    public Curso read(int id) throws Exception {
        return super.read(id);
    }

    /**
     * Lê um curso pelo código compartilhável (NanoID)
     * @param codigo Código em formato NanoID
     * @return Objeto Curso ou null se não encontrado
     */
    public Curso readByCodigo(String codigo) throws Exception {
        // Como não temos índice direto pelo código, fazemos busca linear
        // Uma otimização futura seria criar um índice hash para códigos
        // Por enquanto, usamos uma abordagem simplificada
        return null;
    }

    /**
     * Retorna todos os IDs dos cursos de um usuário específico
     * @param idUsuario ID do usuário
     * @return Array de IDs dos cursos
     */
    public int[] readByUsuario(int idUsuario) throws Exception {
        // Busca na árvore B+ todos os pares que começam com idUsuario
        // O compareTo em ParIdId compara primeiro por idUsuario
        ParIdId chave = new ParIdId(idUsuario, 0);
        ArrayList<ParIdId> pares = arvoreCursoUsuario.read(chave);
        
        if (pares == null || pares.isEmpty()) {
            return null;
        }
        
        // Extrai apenas os IDs de cursos que pertencem a este usuário
        ArrayList<Integer> cursosIds = new ArrayList<>();
        for (ParIdId par : pares) {
            if (par.getIdUsuario() == idUsuario) {
                cursosIds.add(par.getIdCurso());
            }
        }
        
        if (cursosIds.isEmpty()) {
            return null;
        }
        
        // Converte ArrayList para array primitivo
        int[] resultado = new int[cursosIds.size()];
        for (int i = 0; i < cursosIds.size(); i++) {
            resultado[i] = cursosIds.get(i);
        }
        
        return resultado;
    }

    /**
     * Deleta um curso e remove sua vinculação com o usuário
     * @param id ID do curso a deletar
     * @return true se deletado com sucesso
     */
    @Override
    public boolean delete(int id) throws Exception {
        Curso c = super.read(id);
        if (c != null) {
            if (super.delete(id)) {
                // Remove a vinculação na árvore B+
                arvoreCursoUsuario.delete(new ParIdId(c.getIdUsuario(), id));
                return true;
            }
        }
        return false;
    }

    /**
     * Atualiza os dados de um curso
     * @param novoCliente Curso com dados atualizados
     * @return true se atualizado com sucesso
     */
    @Override
    public boolean update(Curso novoCliente) throws Exception {
        return super.update(novoCliente);
    }

    /**
     * Deleta todos os cursos de um usuário
     * @param idUsuario ID do usuário
     * @return true se todos os cursos foram deletados
     */
    public boolean deleteByUsuario(int idUsuario) throws Exception {
        int[] cursosIds = readByUsuario(idUsuario);
        if (cursosIds != null) {
            for (int idCurso : cursosIds) {
                delete(idCurso);
            }
            return true;
        }
        return false;
    }

    /**
     * Fecha os recursos (arquivo e índices)
     */
    @Override
    public void close() throws Exception {
        super.close();
        arvoreCursoUsuario.close();
    }
}
