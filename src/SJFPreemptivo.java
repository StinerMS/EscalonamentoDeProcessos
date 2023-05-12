public class SJFPreemptivo {

    public static void main(String[] args) {

        int n_processos = 4;

        int[] tempo_execucao = {1, 5, 4, 9};
        int[] tempo_chegada = {3, 9, 1, 8};
        int[] prioridade = {11, 9, 9, 4};
        int[] tempo_espera = new int[n_processos];
        int[] tempo_restante = new int[n_processos];

        for (int i = 0; i < n_processos; i++) {
            tempo_restante[i] = tempo_execucao[i];
        }

        int tempo_atual = 0;
        int processos_concluidos = 0;

        while (processos_concluidos < n_processos) {

            int indice_menor_tempo_restante = -1;
            int menor_tempo_restante = Integer.MAX_VALUE;

            for (int i = 0; i < n_processos; i++) {
                if (tempo_chegada[i] <= tempo_atual && tempo_restante[i] < menor_tempo_restante && tempo_restante[i] > 0) {
                    menor_tempo_restante = tempo_restante[i];
                    indice_menor_tempo_restante = i;
                }
            }

            if (indice_menor_tempo_restante == -1) {
                tempo_atual++;

            } else {
                tempo_restante[indice_menor_tempo_restante]--;
                tempo_atual++;

                if (tempo_restante[indice_menor_tempo_restante] == 0) {
                    processos_concluidos++;
                    int indice_processo_concluido = indice_menor_tempo_restante;
                    tempo_espera[indice_processo_concluido] = tempo_atual - tempo_execucao[indice_processo_concluido] - tempo_chegada[indice_processo_concluido];
                }
            }

        }

        double tempo_medio_espera = 0;
        for (int i = 0; i < n_processos; i++) {
            tempo_medio_espera += tempo_espera[i];
        }
        tempo_medio_espera /= n_processos;

        System.out.println("Tempo médio de espera: " + tempo_medio_espera);

    }

}