import java.util.ArrayList;

public class process  {
    String processName = "";
    int arrivalTime = 0;
    int exeTime = 0;
    int waitingTime = 0;
    int responseTime = 0;
    int turnAroundTime = 0;
    int compTime = 0;
    int priority=0;
    int quantum=0;
    int done=0;
    int flag=0;
    ArrayList<Integer>quantumHistory=new ArrayList<Integer>();

    public void setcompTime(int ct){
        compTime=ct;
        turnAroundTime=compTime-arrivalTime;
        waitingTime=turnAroundTime-exeTime;
    }
}
