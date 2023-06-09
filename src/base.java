import java.util.Scanner;

import java.util.Random;

public class base {

    static int MAXIMO_TEMPO_EXECUCAO = 65535;

    static int n_processos = 4;
    int[] id = new int[n_processos];


    public static void main(String[] args) {

        int[] tempo_execucao = new int[n_processos];
        int[] tempo_chegada = new int[n_processos];
        int[] prioridade = new int[n_processos];
        int[] tempo_espera = new int[n_processos];
        int[] tempo_restante = new int[n_processos];


        Scanner teclado = new Scanner (System.in);


        popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

        imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

        //Escolher algoritmo
        int alg;

        while(true) {

            System.out.print("\nEscolha o argoritmo?: \n1=FCFS \n2=SJF Preemptivo \n3=SJF Não Preemptivo  \n4=Prioridade Preemptivo \n5=Prioridade Não Preemptivo  \n6=Round_Robin  \n7=Imprime lista de processos \n8=Popular processos novamente \n9=Sair]: ");
            alg =  teclado.nextInt();

            if (alg == 1) { //FCFS
                FCFS(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
            }
            else if (alg == 2) { //SJF PREEMPTIVO
                SJF(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
            }
            else if (alg == 3) { //SJF NÃO PREEMPTIVO
                SJF(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);

            }
            else if (alg == 4) { //PRIORIDADE PREEMPTIVO
                PRIORIDADE(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 5) { //PRIORIDADE NÃO PREEMPTIVO
                PRIORIDADE(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);

            }
            else if (alg == 6) { //Round_Robin
                Round_Robin(tempo_execucao, tempo_espera, tempo_restante);

            }
            else if (alg == 7) { //IMPRIME CONTEÚDO INICIAL DOS PROCESSOS
                imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 8) { //REATRIBUI VALORES INICIAIS
                popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
            }
            else if (alg == 9) {
                break;
            }
        }

    }

    public static void popular_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,  int [] prioridade ){
        Random random = new Random();
        Scanner teclado = new Scanner (System.in);
        int aleatorio;

        System.out.print("Será aleatório?:  ");
        aleatorio =  teclado.nextInt();

        for (int i = 0; i < n_processos; i++) {
            //Popular Processos Aleatorio
            if (aleatorio == 1){
                tempo_execucao[i] = random.nextInt(10)+1;
                tempo_chegada[i] = random.nextInt(10)+1;
                prioridade[i] = random.nextInt(15)+1;
            }
            //Popular Processos Manual
            else {
                System.out.print("Digite o tempo de execução do processo["+i+"]:  ");
                tempo_execucao[i] = teclado.nextInt();
                System.out.print("Digite o tempo de chegada do processo["+i+"]:  ");
                tempo_chegada[i] = teclado.nextInt();
                System.out.print("Digite a prioridade do processo["+i+"]:  ");
                prioridade[i] = teclado.nextInt();
            }
            tempo_restante[i] = tempo_execucao[i];

        }
    }

    public static void imprime_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,  int []prioridade){
        //Imprime lista de processos
        for (int i = 0; i < n_processos; i++) {
            System.out.println("Processo["+i+"]: tempo_execucao="+ tempo_execucao[i] + " tempo_restante="+tempo_restante[i] + " tempo_chegada=" + tempo_chegada[i] + " prioridade =" +prioridade[i]);
        }
    }

    public static void imprime_stats (int[] espera) {
        int[] tempo_espera = espera.clone();
        int tempoTotal = 0;

        for(int i = 0; i < n_processos; i++){
            tempoTotal += tempo_espera[i];
        }
        System.out.println("Tempo total: " + tempoTotal + " Média de tempo: " + (tempoTotal / n_processos));
    }

    public static void FCFS(int[] execucao, int[] espera, int[] restante, int[] chegada){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        // int[] tempo_chegada = chegada.clone();
        // implementando FCFS *********************************************

        int tempo = 0;
        int processoEmExecucao = 0;

        while(true){
            System.out.println("tempo[" + tempo +
                             "] processo[ " + processoEmExecucao +
                             "] restante: " + tempo_restante[processoEmExecucao]);

            if(tempo_restante[processoEmExecucao] == tempo_execucao[processoEmExecucao])
                tempo_espera[processoEmExecucao] = tempo;

            if(tempo_restante[processoEmExecucao] == 1){
                if(processoEmExecucao >= n_processos -1){
                    break;
                }else{
                    tempo++;
                    processoEmExecucao++;
                }
            }else{
                tempo_restante[processoEmExecucao]--;
                tempo++;
            }
        }
        imprime_stats(tempo_espera);
    }

    public static void SJF(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();

        int tempo = 0;
        int processosConcluidos = 0;
        boolean[] processosConcluidosFlag = new boolean[n_processos];

        while (processosConcluidos < n_processos) {
            int menorTempoExecucao = MAXIMO_TEMPO_EXECUCAO;
            int proximoProcesso = -1;

            // Encontra o processo com o menor tempo de execução entre os processos que chegaram até o momento atual
            for (int i = 0; i < n_processos; i++) {
                if (!processosConcluidosFlag[i] && tempo_chegada[i] <= tempo && tempo_execucao[i] < menorTempoExecucao) {
                    menorTempoExecucao = tempo_execucao[i];
                    proximoProcesso = i;
                }
            }

            if (proximoProcesso == -1) {
                tempo++;
                continue;
            }

            if (preemptivo) {
                // Executa o processo por 1 unidade de tempo
                tempo_restante[proximoProcesso]--;

                if (tempo_restante[proximoProcesso] == 0) {
                    tempo_espera[proximoProcesso] = tempo - tempo_chegada[proximoProcesso] - tempo_execucao[proximoProcesso] + 1;
                    processosConcluidosFlag[proximoProcesso] = true;
                    processosConcluidos++;
                }
            } else {
                // Executa o processo até o final
                tempo += tempo_execucao[proximoProcesso];
                tempo_espera[proximoProcesso] = tempo - tempo_chegada[proximoProcesso] - tempo_execucao[proximoProcesso];
                processosConcluidosFlag[proximoProcesso] = true;
                processosConcluidos++;
            }

            tempo++;
        }

        imprime_stats(tempo_espera);
    }

    public static void PRIORIDADE(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();
        int[] prioridade_temp = prioridade.clone();

        int n = execucao.length; // Número de processos

        int tempo = 0; // Tempo atual
        int processosConcluidos = 0; // Número de processos concluídos até agora

        while (processosConcluidos < n) {
            int processoAtual = -1;
            int prioridadeMinima = Integer.MAX_VALUE;

            // Encontra o próximo processo com a maior prioridade entre os processos que chegaram até o momento atual
            for (int i = 0; i < n; i++) {
                if (tempo_chegada[i] <= tempo && tempo_restante[i] > 0 && prioridade_temp[i] < prioridadeMinima) {
                    processoAtual = i;
                    prioridadeMinima = prioridade_temp[i];
                }
            }

            if (processoAtual == -1) {
                tempo++; // Não há processos para executar, incrementa o tempo
                continue;
            }

            // Executa o processo atual
            if (preemptivo) {
                tempo_restante[processoAtual]--; // Executa uma unidade de tempo
            } else {
                tempo_restante[processoAtual] = 0; // Executa o processo até o fim
            }

            // Verifica se o processo foi concluído
            if (tempo_restante[processoAtual] == 0) {
                processosConcluidos++;

                // Calcula o tempo de espera do processo concluído
                tempo_espera[processoAtual] = tempo - tempo_chegada[processoAtual] - tempo_execucao[processoAtual];
            }

            tempo++; // Incrementa o tempo
        }

        imprime_stats(tempo_espera);
    }

    public static void Round_Robin(int[] execucao, int[] espera, int[] restante) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();

        int n = execucao.length; // Número de processos

        int quantum = 2; // Tamanho do quantum
        int tempo = 0; // Tempo atual
        int processosConcluidos = 0; // Número de processos concluídos até agora

        while (processosConcluidos < n) {
            boolean processoConcluido = true;

            for (int i = 0; i < n; i++) {
                if (tempo_restante[i] > 0) {
                    processoConcluido = false;

                    if (tempo_restante[i] > quantum) {
                        tempo += quantum; // Executa o quantum
                        tempo_restante[i] -= quantum; // Reduz o tempo restante do processo
                    } else {
                        tempo += tempo_restante[i]; // Executa o tempo restante do processo
                        tempo_espera[i] = tempo - tempo_execucao[i]; // Calcula o tempo de espera do processo concluído
                        tempo_restante[i] = 0; // Define o tempo restante como zero, pois o processo foi concluído
                        processosConcluidos++;
                    }
                }
            }

            if (processoConcluido)
                tempo++; // Incrementa o tempo se nenhum processo foi executado
        }

        imprime_stats(tempo_espera);
    }}