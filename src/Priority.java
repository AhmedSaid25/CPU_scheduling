import java.util.Arrays;
import java.util.Scanner;
import java.util.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import static java.lang.Integer.parseInt;

/*
        process 1 --> process name =  , process arrival =  , process bT =  , process priority =
        process 2 --> process name =  , process arrival =  , process bT =  , process priority =
        process 3 --> process name =  , process arrival =  , process bT =  , process priority =
        process 4 --> process name =  , process arrival =  , process bT =  , process priority =
        process 5 --> process name =  , process arrival =  , process bT =  , process priority =
*/
public class Priority {
    Priority() {
        Scanner cin = new Scanner(System.in);
        System.out.println("Enter the number of process: ");
        int n = cin.nextInt();              //Number of processes
        String[][] scheduler = new String[n][4];     //2D array to get the data
        int[] arrival1 = new int[n];           //array to store arrival time to be accessed
        String[] name = new String[n];         //array to store processes name to be accessed
        int[] execution = new int[n];          //array to store burst time to be accessed
        int[] OldExecution = new int[n];          //array to store old burst time to be compared
        int[] priority = new int[n+1];         //array to store priority  to be accessed
        int[] burst = new int[n];              //array to store arrival time to be compared
        int [] WaitingList = new int [n];      //array to store Waiting time to be calculated
        String [] OrderedWaitingList = new String[n];     //array to store order of finished processes
        int pos = 0 ;
        int end=0;
        int mn = 2000 ;


        //Queue<Integer> waitingList = new LinkedList<>();
        //Queue<Integer> turnaroundList = new LinkedList<>();

        LinkedList<Integer> waitingList = new LinkedList<Integer>();         // LinkedList to store waiting times for finished processes
        LinkedList<Integer> turnaroundList = new LinkedList<Integer>();      // LinkedList to store turnaround times for finished processes

        Queue<String> executionOrder = new LinkedList<>();                   // queue to store order of processes in execution

        int totalWaiting = 0;                 // variable to calculate average waiting time
        int totalTurnaround = 0;               // variable to calculate average turnaround

        // get data
        for (int i = 0; i < n; i++) {
            System.out.println("Enter the info of process number " + (i + 1) + " (Process Name , Arrival Time , Execution Time , priority) " + " : \n");
            for (int j = 0; j < 4; j++) {
                scheduler[i][j] = cin.next();
                if (j == 0 )
                {
                    name[i] = scheduler[i][j];
                }
                if (j == 1) {
                    arrival1[i] = parseInt(scheduler[i][j]);
                    mn = Math.min(mn,parseInt(scheduler[i][j])) ;

                }
                if (j == 3) {
                    priority[i] = parseInt(scheduler[i][j]);

                }
                if (j == 2) {
                    execution[i] = parseInt(scheduler[i][j]);
                    burst[i]= parseInt(scheduler[i][j]);
                }
            }
        }

        priority[n]=1111111;
        int time = mn;
        int starvation = 0 ;
        int counter = 0;
        boolean bo = true;
        while (bo)
        {
            int bestPriority = n;
            for (int i = 0; i < n; i++)
            {
                // which process will be executed ?
                if (arrival1[i] <= time && priority[i] < priority[bestPriority] && execution[i] > 0)
                {
                    bestPriority = i;
                    executionOrder.add(name[i]) ;
                }
            }
            if (starvation == mn)
            {
                OldExecution[bestPriority] = execution[bestPriority];
            }
            execution[bestPriority]--;
            if ( execution[bestPriority] == 0 )
            {
                end = time + 1;
                counter++;

                WaitingList[bestPriority]=(end - arrival1[bestPriority]-burst[bestPriority]);
                totalWaiting+= (end-arrival1[bestPriority]-burst[bestPriority]);
                waitingList.add(WaitingList[bestPriority]);

                turnaroundList.add(WaitingList[bestPriority] + burst[bestPriority]);
                totalTurnaround+= WaitingList[bestPriority] + burst[bestPriority] ;

                OrderedWaitingList[pos] = name[bestPriority] ;
                pos++ ;
            }

            if(counter==n){
                bo=false;
            }
            time++;
            starvation++ ;
            // this part for starvation
//            if (starvation == 10)
//            {
//                for (int j = 0 ; j < n ; j++)
//                {
//                    if (OldExecution[j] == burst[j])
//                    {
//                        priority[j]-=2 ;
//                    }
//                }
//                starvation = 0 ;
//            }

        }
        System.out.println("Processes execution order");
        System.out.println(executionOrder);
        System.out.println("");
        System.out.println("process "+"   Execution Time " +"   Arrival Time "+"   Finished processes "+"   Waiting Time "+"   Turnaround Time ");
        for (int i=0;i<n;i++){
            System.out.println(name[i]+"\t\t\t\t"+burst[i]+"\t\t\t\t "+arrival1[i]+"\t\t\t\t "+OrderedWaitingList[i]+"\t\t\t\t\t "+waitingList.get(i)+"\t\t\t\t\t "+turnaroundList.get(i));
        }
        System.out.println("Average of Waiting List = "+totalWaiting/n);
        System.out.println("Average of Turnaround List = "+totalTurnaround/n);
    }

}
