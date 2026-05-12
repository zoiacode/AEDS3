package TP1;

import java.util.ArrayList;

public class ArquivoCursoUsuario extends aed3.Arquivo<CursoUsuario> {

    private ArvoreBMais<ParInteiroInteiro> arvoreUsuarioInscricao;
    private ArvoreBMais<ParInteiroInteiro> arvoreCursoInscricao;

    public ArquivoCursoUsuario() throws Exception {
        super("cursoUsuario", CursoUsuario.class.getConstructor());

        arvoreUsuarioInscricao = new ArvoreBMais<>(
            ParInteiroInteiro.class.getConstructor(),
            4,
            ".\\dados\\cursoUsuario\\usuarioInscricao.b.db"
        );

        arvoreCursoInscricao = new ArvoreBMais<>(
            ParInteiroInteiro.class.getConstructor(),
            4,
            ".\\dados\\cursoUsuario\\cursoInscricao.b.db"
        );
    }

    @Override
    public int create(CursoUsuario cu) throws Exception {
        int id = super.create(cu);
        arvoreUsuarioInscricao.create(new ParInteiroInteiro(cu.getIdUsuario(), id));
        arvoreCursoInscricao.create(new ParInteiroInteiro(cu.getIdCurso(), id));
        return id;
    }

    @Override
    public boolean delete(int id) throws Exception {
        CursoUsuario cu = super.read(id);
        if (cu != null) {
            if (super.delete(id)) {
                arvoreUsuarioInscricao.delete(new ParInteiroInteiro(cu.getIdUsuario(), id));
                arvoreCursoInscricao.delete(new ParInteiroInteiro(cu.getIdCurso(), id));
                return true;
            }
        }
        return false;
    }

    public int[] readByUsuario(int idUsuario) throws Exception {
        ArrayList<ParInteiroInteiro> pares = arvoreUsuarioInscricao.read(null);
        if (pares == null || pares.isEmpty()) {
            return null;
        }

        ArrayList<Integer> inscricoesIds = new ArrayList<>();
        for (ParInteiroInteiro par : pares) {
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
        ArrayList<ParInteiroInteiro> pares = arvoreCursoInscricao.read(null);
        if (pares == null || pares.isEmpty()) {
            return null;
        }

        ArrayList<Integer> inscricoesIds = new ArrayList<>();
        for (ParInteiroInteiro par : pares) {
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

    public CursoUsuario readByUsuarioAndCurso(int idUsuario, int idCurso) throws Exception {
        int[] inscricoes = readByUsuario(idUsuario);
        if (inscricoes != null) {
            for (int idInscricao : inscricoes) {
                CursoUsuario cu = read(idInscricao);
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
