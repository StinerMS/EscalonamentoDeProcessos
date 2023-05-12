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

        //implementando SJF Não Preemptivo
        int tempo = 0;

        while(true){
            break;
        }

        imprime_stats(tempo_espera);

    }

    public static void PRIORIDADE(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada, int[] prioridade){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();
        int[] tempo_chegada = chegada.clone();
        int[] prioridade_temp = prioridade.clone();

        //implementar código do Prioridade preemptivo e não preemptivo
        //...
        //

        imprime_stats(tempo_espera);

    }

    public static void Round_Robin(int[] execucao, int[] espera, int[] restante){
        int[] tempo_execucao = execucao.clone();
        int[] tempo_espera = espera.clone();
        int[] tempo_restante = restante.clone();


        //implementar código do Round-Robin
        //...
        //

        imprime_stats(tempo_espera);
    }
}