import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SJF {
    Scanner cin=new Scanner(System.in);
    SJF(){
        System.out.println("Enter number of process");
        int num=cin.nextInt();int sum=0;
        System.out.println("Enter context Value ");
        int context=cin.nextInt();
        int total_waiting_time=0,total_turnaroound=0;
        ArrayList<process>processes=new ArrayList<process>();
        HashMap<String,Integer> Exec=new HashMap<>();
        ArrayList<String>chart=new ArrayList<String>();
        for (int i=1;i<=num;i++){
          process obj=new process();
          String process="p"+i;
          obj.processName=process;
          System.out.println("Enter arrival time of process "+process );
          int arrival=cin.nextInt();obj.arrivalTime=arrival;
            System.out.println("Enter execution time of process "+ process);
          int brust=cin.nextInt();obj.exeTime=brust;sum+=brust;
            Exec.put(process,brust);
          processes.add(obj);
        }
        // mark process as complete
        int complete=0;
        int completed[]=new int[num+1];
        for(int i=0;i<num;i++){
            completed[i]=0;
        }
        // mark first come of process
        int first[]=new int[num+1];
        for(int i=0;i<num;i++){
            first[i]=0;
        }
        int total=0;
        // loop over all execution time
        ArrayList<String>lst=new ArrayList<>();
        while (complete!=num){
            // min brust ,check arrival
            int indx=num;int mn=sum;
            for(int j=0;j<processes.size();j++){
                if(processes.get(j).arrivalTime<=total&&completed[j]==0&&processes.get(j).exeTime<mn){
                    indx=j;mn=processes.get(j).exeTime;
                }
            }
            // no process available
            if(indx==num){
                total+=context;
            }
            else{
                // add process to know if there change
                lst.add(processes.get(indx).processName);
                if(lst.size()>1){
                    if(!lst.get(lst.size()-1).equals(lst.get(lst.size()-2))){
                        total+=context;chart.add(lst.get(lst.size()-1));
                    }
                }
                else chart.add(lst.get(lst.size()-1));
                processes.get(indx).exeTime-=1;
                if(first[indx]==0){
                    first[indx]=total;processes.get(indx).responseTime=total;
                }
                if(processes.get(indx).exeTime==0){
                    completed[indx]=1;complete++;
                    processes.get(indx).compTime=total+1;
                    processes.get(indx).turnAroundTime=processes.get(indx).compTime-processes.get(indx).arrivalTime;
                    total_turnaroound+=processes.get(indx).turnAroundTime;
                    processes.get(indx).waitingTime=processes.get(indx).compTime-Exec.get(processes.get(indx).processName)-processes.get(indx).arrivalTime;
                    total_waiting_time+=processes.get(indx).waitingTime;
                }
            }
            total++;

        }
        System.out.println("Processes execution order");
        for (int i=0;i<chart.size();i++){
            System.out.print(chart.get(i)+" ");
        }
        System.out.println("");
        System.out.println("process "+"Execution Time " +"Arrival Time "+"Completion Time  "+"Turnaround Time "+"Waiting Time "+" Response Time");
        for (int i=0;i<processes.size();i++){
            System.out.println(processes.get(i).processName+"\t\t\t\t "+Exec.get(processes.get(i).processName)+"\t\t\t\t "+processes.get(i).arrivalTime+"\t\t\t\t "+processes.get(i).compTime+"\t\t\t\t "+processes.get(i).turnAroundTime+"\t\t\t\t "+processes.get(i).waitingTime+"\t\t\t\t "+processes.get(i).responseTime);
        }
        System.out.println("Average of Waiting List = "+total_waiting_time/num);
        System.out.println("Average of Turnaround List = "+total_turnaroound/num);
    }
}
