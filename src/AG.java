import java.util.*;
import java.io.*;
import java.lang.*;
import java.net.ProxySelector;

class MyComparator implements Comparator<process> {
    public int compare(process x, process y){
        return x.arrivalTime - y.arrivalTime;
    }
}
public class AG {
    Scanner cin=new Scanner(System.in);
    int totalTime=0;
    int complete=0;
    int numOfprocesses;
    int total_waiting_time=0,total_turnaroound=0;

    ArrayList<process>processes=new ArrayList<process>();
    ArrayList<String>chart=new ArrayList<String>();
    ArrayList<Integer>switchingTime=new ArrayList<Integer>();
    HashMap<String, process> processesmap = new HashMap<String, process>();

    public AG(){
        this.input();
        this.logic();
        this.output();

    }
    public void output(){
        //print chart
        for (int i=0; i<chart.size(); i++){
            System.out.print(chart.get(i)+" ");
        }

        System.out.println();
        System.out.print("0 ");
        for (int i=0; i<switchingTime.size(); i++){
            System.out.print(String.valueOf(switchingTime.get(i))+" ");
        }

        System.out.println();
        System.out.println("process "+"Executiom Time " +"Arrival Time "+"Completion Time  "+"Turnaround Time "+"Waiting Time "+" Response Time");
        processesmap.forEach((key,value)->{
            System.out.println(value.processName+"\t\t\t\t "+value.exeTime+"\t\t\t\t "+value.arrivalTime+"\t\t\t\t "+
                    value.compTime+"\t\t\t\t "+value.turnAroundTime+"\t\t\t\t "+value.waitingTime+"\t\t\t\t "+value.responseTime);
        });

        System.out.println();
        processesmap.forEach((key,value)->{
            System.out.print(value.processName+" "+" update of quantum : ");
            for(int j=0;j<value.quantumHistory.size();j++){
                System.out.print(value.quantumHistory.get(j));
                System.out.print(" ");
            }
            System.out.println();
        });
        System.out.println("Average of Waiting List = "+total_waiting_time/numOfprocesses);
        System.out.println("Average of Turnaround List = "+total_turnaroound/numOfprocesses);
    }
    public void input(){

        System.out.println("Enter number of process");
        numOfprocesses=cin.nextInt();

        for (int i=1; i<=numOfprocesses; i++){
            process process=new process();
            process process2=new process();

            process.processName="p"+String.valueOf(i);
            process2.processName="p"+String.valueOf(i);

            System.out.println("Enter arrival time of process "+ process.processName );
            int arrivalTime=cin.nextInt();
            process.arrivalTime=arrivalTime;    process2.arrivalTime=arrivalTime;

            System.out.println("Enter execution time of process "+ process.processName);
            int exeTime=cin.nextInt();
            process.exeTime=exeTime;    process2.exeTime=exeTime;

            System.out.println("Enter Priority time of process "+ process.processName);
            int priority=cin.nextInt();
            process.priority=priority;  process2.priority=priority;

            System.out.println("Enter Quantum time of process "+ process.processName);
            int quantum=cin.nextInt();
            process.quantum=quantum;    process2.quantum=quantum;
            process.quantumHistory.add(quantum);
            process2.quantumHistory.add(quantum);
            processesmap.put(process2.processName, process2);
            processes.add(process);
        }
        Collections.sort(processes, new MyComparator());
    }
    public void logic(){
        process curprocess=null;
        ArrayList<process>readyProcesses=new ArrayList<process>();
        int i=0,type=0,q=0;

        while(complete!=numOfprocesses){
            // adding the processes that arrived at the current time
            while(i<processes.size() && processes.get(i).arrivalTime<=totalTime){
                readyProcesses.add(processes.get(i));
                i++;
            }

            if(curprocess==null){
                if(readyProcesses.size()>0){
                    type=0;
                    curprocess=readyProcesses.get(0);
                    readyProcesses.remove(0);
                }
                else{
                    type=3;
                    totalTime++;
                }
            }

            if(type==0){
                process pr=processesmap.get(curprocess.processName);
                if(pr.flag==0){
                    pr.flag=1;
                    pr.responseTime=totalTime;
                    processesmap.put(pr.processName, pr);
                }

                q=calcQuarter((double)curprocess.quantum);
                totalTime+= Math.min(curprocess.exeTime, q);
                curprocess.quantum -= Math.min(curprocess.exeTime, q);
                curprocess.exeTime -= Math.min(curprocess.exeTime, q);

                type++;

                //Case i
                if(curprocess.quantum==0 && curprocess.exeTime>0 ) {
                    curprocess.quantum+=2;
                }

                if(curprocess.exeTime==0) {
                    chart.add(curprocess.processName);
                    switchingTime.add(totalTime);

                    process p =processesmap.get(curprocess.processName);
                    p.setcompTime(totalTime);
                    total_turnaroound+=p.turnAroundTime;
                    total_waiting_time+=p.waitingTime;
                    p.quantum=0;
                    p.quantumHistory.add(0);
                    processesmap.put(p.processName, p);

                    curprocess=null;
                    complete++; //increament the num of processes that finished
                }

            }
            else if(type==1){
                process hp=null;
                int h=-1;
                //check if there is a process with high priorty
                for(int j=0;j<readyProcesses.size();j++){
                    //choose all the processes which have prority greateer than the current
                    if(readyProcesses.get(j).priority < curprocess.priority){
                        //maximize hp
                        if(hp!=null && hp.priority>readyProcesses.get(j).priority){
                            hp=readyProcesses.get(j);
                            h=j;
                        }
                        else if (hp==null){
                            hp=readyProcesses.get(j);
                            h=j;
                        }
                    }
                }
                //check if it found process with high priorty && Case ii
                if(h!=-1){
                    chart.add(curprocess.processName);
                    switchingTime.add(totalTime);
                    curprocess.quantum/=2;

                    process p =processesmap.get(curprocess.processName);
                    p.quantum += curprocess.quantum;
                    curprocess.quantum = p.quantum;
                    p.quantumHistory.add(p.quantum);
                    processesmap.put(p.processName, p);

                    readyProcesses.remove(h);
                    readyProcesses.add(curprocess);
                    curprocess=hp;
                    type=0;
                }
                else{
                    int half=this.calcHalf(processesmap.get(curprocess.processName).quantum);
                    if(half<=q+q){
                        q=half-q;
                    }
                    totalTime += Math.min(curprocess.exeTime, q);
                    curprocess.quantum -= Math.min(curprocess.exeTime, q);
                    curprocess.exeTime -= Math.min(curprocess.exeTime, q);


                    //Case i
                    if(curprocess.quantum==0 && curprocess.exeTime>0 ) {
                        curprocess.quantum+=2;
                    }

                    //check if the process finish && case iv
                    if(curprocess.exeTime==0) {
                        chart.add(curprocess.processName);
                        switchingTime.add(totalTime);

                        process p =processesmap.get(curprocess.processName);
                        p.setcompTime(totalTime);
                        p.quantum=0;
                        p.quantumHistory.add(0);
                        processesmap.put(p.processName, p);
                        total_turnaroound+=p.turnAroundTime;
                        total_waiting_time+=p.waitingTime;

                        curprocess=null;
                        complete++; //increament the num of processes that finished
                    }

                    type++;
                }

            }
            else if(type ==2){
                process lowexe=null;
                int h=-1;
                //check if there is a process with low exe time
                for(int j=0;j<readyProcesses.size();j++){
                    //choose all the processes which have exe time lower than the current
                    if(readyProcesses.get(j).exeTime<curprocess.exeTime){
                        //minimize lowexe
                        if(lowexe!=null && lowexe.exeTime > readyProcesses.get(j).exeTime){
                            lowexe=readyProcesses.get(j);
                            h=j;
                        }
                        else if (lowexe==null){
                            lowexe=readyProcesses.get(j);
                            h=j;
                        }
                    }
                }
                //check if it found process with low exe time && Case ii
                if(h!=-1){
                    curprocess.quantum +=processesmap.get(curprocess.processName).quantum;
                    chart.add(curprocess.processName);
                    switchingTime.add(totalTime);

                    process p = processesmap.get(curprocess.processName);
                    p.quantum = curprocess.quantum;
                    p.quantumHistory.add(p.quantum);
                    processesmap.put(p.processName, p);

                    readyProcesses.remove(h);
                    readyProcesses.add(curprocess);
                    curprocess=lowexe;
                    type=0;
                }
                else{
                    curprocess.quantum -= 1;
                    curprocess.exeTime -= 1;
                    totalTime ++;

                    //Case i
                    if(curprocess.quantum==0 && curprocess.exeTime>0 ) {
                        curprocess.quantum+=2;
                    }

                    //check if the process finish && case iv
                    if(curprocess.exeTime==0) {
                        chart.add(curprocess.processName);
                        switchingTime.add(totalTime);

                        process p =processesmap.get(curprocess.processName);
                        p.setcompTime(totalTime);
                        p.quantum=0;
                        p.quantumHistory.add(0);
                        processesmap.put(p.processName, p);
                        total_turnaroound+=p.turnAroundTime;
                        total_waiting_time+=p.waitingTime;

                        curprocess=null;
                        complete++; //increament the num of processes that finished
                    }
                }
            }

        }

    }

    public int calcQuarter(double  num){
        return (int)Math.ceil(num/4);
    }
    public int calcHalf(double  num){
        return (int)Math.ceil(num/2);
    }
}
