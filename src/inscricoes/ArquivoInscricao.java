package src.inscricoes;

import java.util.ArrayList;
import src.core.ArvoreBMais;
import src.infraestrutura.ArquivoIndexado;

import java.util.ArrayList;

public class ArquivoInscricao extends ArquivoIndexado<Inscricao> {

    private ArvoreBMais<ParIntInt> arvoreUsuarioInscricao;
    private ArvoreBMais<ParIntInt> arvoreCursoInscricao;

    public ArquivoInscricao() throws Exception {
        super("cursoUsuario", Inscricao.class.getConstructor());

        arvoreUsuarioInscricao = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            4,
            ".\\dados\\cursoUsuario\\usuarioInscricao.b.db"
        );

        arvoreCursoInscricao = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            4,
            ".\\dados\\cursoUsuario\\cursoInscricao.b.db"
        );
    }

    @Override
    public int create(Inscricao cu) throws Exception {
        int id = super.create(cu);
        arvoreUsuarioInscricao.create(new ParIntInt(cu.getIdUsuario(), id));
        arvoreCursoInscricao.create(new ParIntInt(cu.getIdCurso(), id));
        return id;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Inscricao cu = super.read(id);
        if (cu != null) {
            if (super.delete(id)) {
                arvoreUsuarioInscricao.delete(new ParIntInt(cu.getIdUsuario(), id));
                arvoreCursoInscricao.delete(new ParIntInt(cu.getIdCurso(), id));
                return true;
            }
        }
        return false;
    }

    public int[] readByUsuario(int idUsuario) throws Exception {
        ArrayList<ParIntInt> pares = arvoreUsuarioInscricao.read(null);
        if (pares == null || pares.isEmpty()) {
            return null;
        }

        ArrayList<Integer> inscricoesIds = new ArrayList<>();
        for (ParIntInt par : pares) {
            if (par.getChave1() == idUsuario) {
                inscricoesIds.add(par.getChave2());
            }
        }

        if (inscricoesIds.isEmpty()) {
            return null;
        }

        int[] resultado = new int[inscricoesIds.size()];
        for (int i = 0; i < inscricoesIds.size(); i++) {
            resultado[i] = inscricoesIds.get(i);
        }
        return resultado;
    }

    public int[] readByCurso(int idCurso) throws Exception {
        ArrayList<ParIntInt> pares = arvoreCursoInscricao.read(null);
        if (pares == null || pares.isEmpty()) {
            return null;
        }

        ArrayList<Integer> inscricoesIds = new ArrayList<>();
        for (ParIntInt par : pares) {
            if (par.getChave1() == idCurso) {
                inscricoesIds.add(par.getChave2());
            }
        }

        if (inscricoesIds.isEmpty()) {
            return null;
        }

        int[] resultado = new int[inscricoesIds.size()];
        for (int i = 0; i < inscricoesIds.size(); i++) {
            resultado[i] = inscricoesIds.get(i);
        }
        return resultado;
    }

    public Inscricao readByUsuarioAndCurso(int idUsuario, int idCurso) throws Exception {
        int[] inscricoes = readByUsuario(idUsuario);
        if (inscricoes != null) {
            for (int idInscricao : inscricoes) {
                Inscricao cu = read(idInscricao);
                if (cu != null && cu.getIdCurso() == idCurso) {
                    return cu;
                }
            }
        }
        return null;
    }

    public boolean deleteByUsuario(int idUsuario) throws Exception {
        int[] inscricoes = readByUsuario(idUsuario);
        if (inscricoes != null) {
            for (int idInscricao : inscricoes) {
                delete(idInscricao);
            }
        }
        return true;
    }

    public boolean deleteByCurso(int idCurso) throws Exception {
        int[] inscricoes = readByCurso(idCurso);
        if (inscricoes != null) {
            for (int idInscricao : inscricoes) {
                delete(idInscricao);
            }
        }
        return true;
    }

    @Override
    public void close() throws Exception {
        super.close();
        arvoreUsuarioInscricao.close();
        arvoreCursoInscricao.close();
    }
}


