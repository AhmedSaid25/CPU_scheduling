import java.util.*;

public class RR {
    Scanner cin=new Scanner(System.in);
    RR(){
        System.out.println("Enter number of process");
        int num=cin.nextInt();
        System.out.println("Enter context Value ");
        int context=cin.nextInt();
        System.out.println("Enter quantum time:");
        int quantum=cin.nextInt();
        ArrayList<process> processes = new ArrayList<process>();
        HashMap<String,Integer> Exec=new HashMap<>();
        ArrayList<String>chart=new ArrayList<String>();
        int total_turnaroound=0,total_waiting_time=0;
        for (int i=0;i<num;i++){
            process obj=new process();
            int x=i+1;
            String process="p"+x;
            obj.processName=process;
            System.out.println("Enter arrival time of process "+process );
            int arrival=cin.nextInt();obj.arrivalTime=arrival;
            System.out.println("Enter execution time of process "+ process);
            int brust=cin.nextInt();obj.exeTime=brust;
            Exec.put(process,brust);
            processes.add(obj);
        }
        processes.sort(new Comp());
        // mark process as complete
        int completed[]=new int[num+1];
        for(int i=0;i<num;i++){
            completed[i]=0;
        }
        int complete=0;int cntr=0;int total=0;int process_time=0;
        Deque<process>readyProcesses=new LinkedList<process>();
        boolean busy=false;
        //get index of this process
        int indx=0;
        //time less than smallest
        total=processes.get(0).arrivalTime;

        while (complete!=num){
            total++;
            // adding the processes that arrived at the current time
            for (int i=cntr;i<num;i++){
                if(processes.get(i).arrivalTime<=total){
                    readyProcesses.add(processes.get(i));
                    cntr++;
                }
            }

            // process in work
            if(busy==false&&readyProcesses.size()>0){
                chart.add(readyProcesses.getFirst().processName);
                for (int j=0;j<num;j++){
                    if(processes.get(j).processName.equals(readyProcesses.getFirst().processName)){
                        indx=j;break;
                    }
                }

                busy=true;
                if(processes.get(indx).done==0){
                    processes.get(indx).done=1;
                    processes.get(indx).responseTime=total-1;
                }
                if(readyProcesses.getFirst().exeTime<=quantum){
                    process_time=readyProcesses.getFirst().exeTime;
                }
                else{
                    process_time=quantum;
                }

            }
            // decrease process time by 1
            if(process_time>0)process_time--;
            if(process_time==0&&busy){
                total+=context;
                busy=false;
                if(readyProcesses.getFirst().exeTime<=quantum){
                    completed[indx]=1;complete++;
                    processes.get(indx).compTime=total;
                    processes.get(indx).turnAroundTime=processes.get(indx).compTime-processes.get(indx).arrivalTime;
                    total_turnaroound+=processes.get(indx).turnAroundTime;
                    processes.get(indx).waitingTime=processes.get(indx).turnAroundTime-Exec.get(processes.get(indx).processName);
                    total_waiting_time+=processes.get(indx).waitingTime;
                    readyProcesses.removeFirst();
                }
                else{
                    readyProcesses.getFirst().exeTime-=quantum;
                    readyProcesses.add(readyProcesses.getFirst());
                    readyProcesses.removeFirst();
                }
            }

        }
        System.out.println("Processes execution order");
        for (int i=0;i<chart.size();i++){
            System.out.print(chart.get(i)+" ");
        }
        System.out.println("");
        System.out.println("process "+"Execution Time " +"Arrival Time "+"Completion Time  "+"Turnaround Time "+"Waiting Time "+" Response Time");
        for (int i=0;i<processes.size();i++){
            System.out.println(processes.get(i).processName+"\t\t\t\t"+Exec.get(processes.get(i).processName)+"\t\t\t\t "+processes.get(i).arrivalTime+"\t\t\t\t "+processes.get(i).compTime+"\t\t\t\t "+processes.get(i).turnAroundTime+"\t \t\t\t"+processes.get(i).waitingTime+"\t\t\t\t "+processes.get(i).responseTime);
        }
        System.out.println("Average of Waiting List = "+total_waiting_time/num);
        System.out.println("Average of Turnaround List = "+total_turnaroound/num);

    }
}
