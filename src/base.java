import java.util.Scanner;
import java.util.Random;

public class base {

    static int MAXIMO_TEMPO_EXECUCAO = 65535;
    static int n_processos = 3;
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

            System.out.print("\nEscolha o argoritmo?: \n1=FCFS \n2=SJF Preemptivo \n3=SJF Não Preemptivo  \n4=Prioridade Preemptivo \n5=Prioridade Não Preemptivo  \n6=Round_Robin  \n7=Imprime lista de processos \n8=Popular processos novamente \n9=Sair: ");
            alg =  teclado.nextInt();

            switch(alg){
                case 1 -> FCFS(tempo_execucao, tempo_espera, tempo_restante);
                case 2 -> SJF(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
                case 3 -> SJF(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
                case 4 -> PRIORIDADE(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                case 5 -> PRIORIDADE(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                case 6 -> Round_Robin(tempo_execucao, tempo_espera, tempo_restante);
                case 7 -> imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                case 8 -> {
                    popular_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                    imprime_processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada, prioridade);
                }
                case 9 -> {
                    break;
                }
            }
        }
    }

    public static void popular_processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,  int [] prioridade ){
        Random random = new Random();
        Scanner teclado = new Scanner (System.in);
        int aleatorio;

        try{
            System.out.print("Será aleatório?: sim: 1 não: qualquer outro número");
            aleatorio =  teclado.nextInt();
        }catch(Exception e){
            System.out.println("Opção inválida, processos serão executados randomicamente.");
            aleatorio = 1;
        }

        for (int i = 0; i < n_processos; i++) {
            //Popular Processos Aleatorio

            if (aleatorio == 1){
                tempo_execucao[i] = random.nextInt(10)+1;
                tempo_chegada[i] = random.nextInt(10)+1;
                prioridade[i] = random.nextInt(15)+1;
            }
            //Popular Processos Manual
            else {
                tempo_execucao[0] = 10;
                tempo_chegada[0] = 3;
                prioridade[0] = 9;

                tempo_execucao[1] = 7;
                tempo_chegada[1] = 7;
                prioridade[1] = 7;

                tempo_execucao[2] = 3;
                tempo_chegada[2] = 2;
                prioridade[2] = 15;
                /*
                System.out.print("Digite o tempo de execução do processo["+i+"]:  ");
                tempo_execucao[i] = teclado.nextInt();
                System.out.print("Digite o tempo de chegada do processo["+i+"]:  ");
                tempo_chegada[i] = teclado.nextInt();
                System.out.print("Digite a prioridade do processo["+i+"]:  ");
                prioridade[i] = teclado.nextInt();
                 */
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
            System.out.println("Processo[" + i + "]: tempo_espera = " + espera[i]);
        }
        System.out.println("\nTempo total: " + tempoTotal + " Média de tempo: " + ((double)tempoTotal / n_processos));
    }

    public static void list(int tempo, int processoEmExecucao, int[] tempo_restante){
        System.out.println("tempo[" + tempo +
                        "] processo[ " + processoEmExecucao +
                        "] restante: " + tempo_restante[processoEmExecucao]);
    }

    public static void FCFS(int[] execucao, int[] espera, int[] restante){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();

        int processoEmExecucao = 0;
        int tempo = 0;

        while(true){
            // Lista o processo atual e seus stats
            list(tempo, processoEmExecucao, tempo_restante);

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

    public static void SJF(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();

        // menor tempo de execução
        int menorTempo = MAXIMO_TEMPO_EXECUCAO;
        int processoEmExecucao = -1;

        int concluidos = 0;
        int tempo = 0;

        while(true) {
            tempo++;

            if (preemptivo || processoEmExecucao == -1) {
                for (int i=0; i < n_processos; i++) {
                    if ((tempo_restante[i] != 0) && (tempo_chegada[i] <= tempo)) {
                        if (tempo_restante[i] < menorTempo) {
                            menorTempo = tempo_restante[i];
                            processoEmExecucao = i;
                        }
                    }
                }
            }
            if (processoEmExecucao == -1)
                System.out.println("tempo["+tempo+"]: nenhum processo está pronto");
            else {

                if (tempo_execucao[processoEmExecucao] == tempo_restante[processoEmExecucao])
                    tempo_espera[processoEmExecucao] = tempo - tempo_chegada[processoEmExecucao];

                tempo_restante[processoEmExecucao]--;
                list(tempo, processoEmExecucao, tempo_restante);

                if (tempo_restante[processoEmExecucao] == 0) {
                    processoEmExecucao = -1;
                    menorTempo = MAXIMO_TEMPO_EXECUCAO;
                    concluidos++;

                    if (concluidos == n_processos)
                        break;
                }
            }
        }
        imprime_stats(tempo_espera);
    }

    public static void PRIORIDADE(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();
        int[] _prioridade = prioridade.clone();

        int maiorPrioridade = 0;

        // Ao invés de usar boolean, setar processoEmExecução para -1 enquanto nenhum processo chegar na fila de pronto
        int processoEmExecucao = -1;

        int concluidos = 0;
        int tempo = 0;

        while(true) {
            tempo++;

            if (preemptivo || processoEmExecucao == -1) {
                for (int proc=0; proc<n_processos; proc++) {
                    if ((tempo_restante[proc] != 0) && (tempo_chegada[proc] <= tempo)) {
                        if (_prioridade[proc] > maiorPrioridade) {
                            maiorPrioridade = _prioridade[proc];
                            processoEmExecucao = proc;
                        }
                    }
                }
            }
            if (processoEmExecucao == -1)
                System.out.println("tempo["+tempo+"]: nenhum processo está pronto");
            else {
                if (tempo_restante[processoEmExecucao] == tempo_execucao[processoEmExecucao])
                    tempo_espera[processoEmExecucao] = tempo - tempo_chegada[processoEmExecucao];

                tempo_restante[processoEmExecucao]--;
                list(tempo, processoEmExecucao, tempo_restante);

                if (tempo_restante[processoEmExecucao] == 0) {
                    processoEmExecucao = -1;
                    maiorPrioridade = 0;
                    concluidos++;

                    if (concluidos == n_processos)
                        break;
                }
            }
        }
        imprime_stats(tempo_espera);
    }

    public static void Round_Robin(int[] execucao, int[] espera, int[] restante) {
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();

        Scanner k = new Scanner(System.in);

        System.out.println("Escolha o time slice: ");
        int quantum = k.nextInt();

        int quantumCount = 0;
        int concluidos = 0;

        int processoEmExecucao = 0;
        int tempo = 0;

        while(true){
            tempo++;
            quantumCount++;
            list(tempo, processoEmExecucao, tempo_restante);

            if(tempo_restante[processoEmExecucao] == tempo_execucao[processoEmExecucao]) {
                tempo_espera[processoEmExecucao] = tempo;
            }
            if(tempo_restante[processoEmExecucao] <= 0){
                concluidos++;
            }
            if(tempo_restante[processoEmExecucao] > 0){
                tempo_restante[processoEmExecucao]--;
            }else{
                processoEmExecucao++;
                quantumCount = 0;
            }
            if(quantumCount >= quantum){
                processoEmExecucao++;
                quantumCount = 0;
            }
            if(processoEmExecucao >= n_processos){
                processoEmExecucao = 0;
            }
            if(concluidos == n_processos){
                break;
            }
        }
        imprime_stats(tempo_espera);
    }
}