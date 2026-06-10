package src.cursos;

import java.util.ArrayList;
import java.util.List;
import src.core.ArvoreBMais;
import src.core.NanoId;
import src.infraestrutura.ArquivoIndexado;

/**
 * Gerencia operações CRUD de Curso com índices indiretos.
 * A partir do TP3, também mantém o índice invertido para
 * busca por palavras-chave (TF×IDF).
 */
public class ArquivoCurso extends ArquivoIndexado<Curso> {

    private ArvoreBMais<ParUsuarioCurso> arvoreCursoUsuario;
    private IndiceInvertidoCursos indiceInvertido;

    public ArquivoCurso() throws Exception {
        super("cursos", Curso.class.getConstructor());

        arvoreCursoUsuario = new ArvoreBMais<>(
            ParUsuarioCurso.class.getConstructor(),
            4,
            ".\\dados\\cursos\\relacao.b.db"
        );

        indiceInvertido = new IndiceInvertidoCursos();
        reindexarSeNecessario();
    }

    private void reindexarSeNecessario() throws Exception {
        List<Curso> todos = super.readAll();
        int totalArquivo = (int) todos.stream().filter(c -> c != null).count();
        int totalIndice = indiceInvertido.numeroEntidades();
        if (totalIndice < totalArquivo) {
            // Reindexa tudo do zero para garantir consistência
            indiceInvertido.limpar();
            for (Curso c : todos) {
                if (c != null) {
                    indiceInvertido.indexarCurso(c.getId(), c.nome);
                }
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  CRUD com integração ao índice invertido                            //
    // ------------------------------------------------------------------ //

    @Override
    public int create(Curso c) throws Exception {
        c.codigoNanoID = NanoId.generate();
        int id = super.create(c);
        arvoreCursoUsuario.create(new ParUsuarioCurso(c.getIdUsuario(), id));
        indiceInvertido.indexarCurso(id, c.nome);
        return id;
    }

    @Override
    public Curso read(int id) throws Exception {
        return super.read(id);
    }

    /**
     * Lê um curso pelo código compartilhável (NanoId).
     */
    public Curso readByCodigo(String codigo) throws Exception {
        for (Curso c : readAll()) {
            if (c != null && c.codigoNanoID != null && c.codigoNanoID.equals(codigo)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Retorna todos os IDs dos cursos de um usuário específico.
     */
    public int[] readByUsuario(int idUsuario) throws Exception {
        ArrayList<ParUsuarioCurso> pares = arvoreCursoUsuario.read(null);
        if (pares == null || pares.isEmpty()) return null;

        ArrayList<Integer> cursosIds = new ArrayList<>();
        for (ParUsuarioCurso par : pares) {
            if (par.getIdUsuario() == idUsuario) {
                cursosIds.add(par.getIdCurso());
            }
        }
        if (cursosIds.isEmpty()) return null;

        int[] resultado = new int[cursosIds.size()];
        for (int i = 0; i < cursosIds.size(); i++)
            resultado[i] = cursosIds.get(i);
        return resultado;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Curso c = super.read(id);
        if (c != null) {
            if (super.delete(id)) {
                arvoreCursoUsuario.delete(new ParUsuarioCurso(c.getIdUsuario(), id));
                indiceInvertido.removerCurso(id, c.nome);
                return true;
            }
        }
        return false;
    }

    /**
     * Atualiza os dados de um curso.
     * Se o nome foi alterado, o índice invertido é atualizado.
     */
    @Override
    public boolean update(Curso novoCliente) throws Exception {
        Curso antigo = super.read(novoCliente.getId());
        boolean ok = super.update(novoCliente);
        if (ok && antigo != null && !antigo.nome.equals(novoCliente.nome)) {
            indiceInvertido.atualizarCurso(novoCliente.getId(), antigo.nome, novoCliente.nome);
        }
        return ok;
    }

    /**
     * Busca cursos por palavras-chave usando TF×IDF.
     *
     * @param consulta String digitada pelo usuário
     * @return Lista de cursos ordenada por relevância (maior primeiro)
     */
    public List<Curso> buscarPorPalavras(String consulta) throws Exception {
        List<IndiceInvertidoCursos.ResultadoBusca> resultados = indiceInvertido.buscar(consulta);
        List<Curso> cursos = new ArrayList<>();
        for (IndiceInvertidoCursos.ResultadoBusca r : resultados) {
            Curso c = super.read(r.getIdCurso());
            if (c != null) cursos.add(c);
        }
        return cursos;
    }

    /**
     * Deleta todos os cursos de um usuário.
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

    @Override
    public void close() throws Exception {
        super.close();
        arvoreCursoUsuario.close();
        indiceInvertido.close();
    }
}