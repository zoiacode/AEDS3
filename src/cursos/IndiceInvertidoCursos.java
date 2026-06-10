package src.cursos;

import java.io.File;
import src.infraestrutura.ElementoLista;
import src.infraestrutura.ListaInvertida;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndiceInvertidoCursos {

    private static final String PASTA_DADOS = ".\\dados\\indiceinvertido\\";
    private static final String ARQ_DICIONARIO = PASTA_DADOS + "dicionario.li.db";
    private static final String ARQ_BLOCOS     = PASTA_DADOS + "blocos.li.db";
    private static final int    ITENS_POR_BLOCO = 10;

    private ListaInvertida listaInvertida;

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "a", "o", "as", "os", "um", "uma", "uns", "umas",
        "de", "do", "da", "dos", "das", "em", "no", "na", "nos", "nas",
        "ao", "aos", "a", "e", "ou", "que", "se", "por", "para",
        "com", "sem", "sob", "sobre", "ate", "apos", "desde",
        "entre", "contra", "perante", "durante", "mediante",
        "ja", "nao", "nem", "mas", "porem", "contudo", "todavia",
        "the", "and", "of", "in", "to", "for", "is", "on"
    ));

    public IndiceInvertidoCursos() throws Exception {
        File pasta = new File(PASTA_DADOS);
        if (!pasta.exists()) pasta.mkdirs();
        listaInvertida = new ListaInvertida(ITENS_POR_BLOCO, ARQ_DICIONARIO, ARQ_BLOCOS);
    }

    public void indexarCurso(int idCurso, String nomeCurso) throws Exception {
        listaInvertida.incrementaEntidades();
        String[] termos = extrairTermos(nomeCurso);
        Map<String, Integer> contagem = contarOcorrencias(termos);
        for (Map.Entry<String, Integer> entrada : contagem.entrySet()) {
            float tf = (float) entrada.getValue() / termos.length;
            listaInvertida.create(entrada.getKey(), new ElementoLista(idCurso, tf));
        }
    }

    public void removerCurso(int idCurso, String nomeCurso) throws Exception {
        listaInvertida.decrementaEntidades();
        String[] termos = extrairTermos(nomeCurso);
        Set<String> unicos = new HashSet<>(Arrays.asList(termos));
        for (String termo : unicos) {
            listaInvertida.delete(termo, idCurso);
        }
    }

    public void atualizarCurso(int idCurso, String nomeAntigo, String nomeNovo) throws Exception {
        String[] termosAntigos = extrairTermos(nomeAntigo);
        Set<String> unicosAntigos = new HashSet<>(Arrays.asList(termosAntigos));
        for (String termo : unicosAntigos) {
            listaInvertida.delete(termo, idCurso);
        }

        String[] termosNovos = extrairTermos(nomeNovo);
        Map<String, Integer> contagem = contarOcorrencias(termosNovos);
        for (Map.Entry<String, Integer> entrada : contagem.entrySet()) {
            float tf = (float) entrada.getValue() / termosNovos.length;
            ElementoLista elemento = new ElementoLista(idCurso, tf);
            if (!listaInvertida.update(entrada.getKey(), elemento)) {
                listaInvertida.create(entrada.getKey(), elemento);
            }
        }
    }

    public List<ResultadoBusca> buscar(String consulta) throws Exception {
        String[] termosBusca = extrairTermos(consulta);
        if (termosBusca.length == 0) return Collections.emptyList();

        int totalCursos = listaInvertida.numeroEntidades();
        if (totalCursos == 0) return Collections.emptyList();

        Map<Integer, Float> pontuacao = new HashMap<>();

        for (String termo : termosBusca) {
            ElementoLista[] lista = listaInvertida.read(termo);
            if (lista.length == 0) continue;

            int df = lista.length;
            double idf = Math.log10((double) totalCursos / df) + 1.0;

            for (ElementoLista el : lista) {
                float tfidf = el.getFrequencia() * (float) idf;
                pontuacao.merge(el.getId(), tfidf, Float::sum);
            }
        }

        List<ResultadoBusca> resultados = new ArrayList<>();
        for (Map.Entry<Integer, Float> entry : pontuacao.entrySet()) {
            resultados.add(new ResultadoBusca(entry.getKey(), entry.getValue()));
        }
        resultados.sort(Comparator.comparingDouble(ResultadoBusca::getPontuacao).reversed());
        return resultados;
    }

    public int numeroEntidades() throws Exception {
        return listaInvertida.numeroEntidades();
    }

    public void close() throws Exception {
        listaInvertida.close();
    }

    public String[] extrairTermos(String texto) {
        if (texto == null || texto.isEmpty()) return new String[0];

        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ")
                .trim();

        String[] tokens = normalizado.split("\\s+");
        List<String> validos = new ArrayList<>();
        for (String token : tokens) {
            if (!token.isEmpty() && !STOP_WORDS.contains(token)) {
                validos.add(token);
            }
        }
        return validos.toArray(new String[0]);
    }

    private Map<String, Integer> contarOcorrencias(String[] termos) {
        Map<String, Integer> mapa = new HashMap<>();
        for (String t : termos) {
            mapa.merge(t, 1, Integer::sum);
        }
        return mapa;
    }

    public static class ResultadoBusca {
        private final int idCurso;
        private final float pontuacao;

        public ResultadoBusca(int idCurso, float pontuacao) {
            this.idCurso = idCurso;
            this.pontuacao = pontuacao;
        }

        public int getIdCurso() { return idCurso; }
        public float getPontuacao() { return pontuacao; }

        @Override
        public String toString() {
            return "(id=" + idCurso + ", score=" + String.format("%.3f", pontuacao) + ")";
        }
    }

    public void limpar() throws Exception {
        listaInvertida.close();
        new java.io.File(ARQ_DICIONARIO).delete();
        new java.io.File(ARQ_BLOCOS).delete();
        listaInvertida = new ListaInvertida(ITENS_POR_BLOCO, ARQ_DICIONARIO, ARQ_BLOCOS);
    }
}