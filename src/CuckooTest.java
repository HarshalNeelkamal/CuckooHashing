import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class CuckooTest {
	
	int size = 400;
	
	public void run(){
		int capacity = 10;
		int maxRoundLoops = 10;
		int arr[] = prepareRandomIntegerArray(size);
		System.out.println("\n*************inserting "+size+" elements without skipping*************\n");
		System.out.println(String.format("%-20s%-20s%-20s%-20s","Deep","Insertion Time","Load Factor","Memory Cost"));
		while(maxRoundLoops < 101){
			double millitime = 0;
			double loadFactor = 0;
			int hashCapacity = 0;
			long memoryCost = 0;
			for(int j = 0; j < 10; j++){
				CuckooHash<Integer> hash = new CuckooHash<Integer>(capacity, maxRoundLoops);
				long time = System.nanoTime();
				for(int i = 0; i < arr.length; i++){
					hash.put((arr[i]));
				}
				time = System.nanoTime() - time;
				millitime = millitime + (1.0*time)/1000000;
				hashCapacity = hashCapacity + hash.getCapacity();
				loadFactor = loadFactor + hash.loadFactor();
				memoryCost = memoryCost + hash.avgMemoryUsagePerInsertion();
				hash = null;
			}
			//\nStart Capacity: "+ capacity+"\nFinal Capacity: "+hashCapacity/10+"
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(3);
			System.out.println(String.format("%-20s%-20s%-20s%-20s",""+maxRoundLoops,""+df.format(millitime/(10.0))+" ms ",""+df.format(loadFactor/10),""+df.format(memoryCost/(10*1024*1024.0))+" MBs"));
			maxRoundLoops += 10;
		}
	}
	
	public void percentBreakPointRun(){
		int arraySize = 5000;
		int insertions = 0;
		int capacity = 100;
		int maxRoundLoops = 101;
		int repetations = 5;
		int increment = 10;
		long timeTaken[][] = new long[90/increment][repetations];
		long memTaken[][] = new long[90/increment][repetations];
		System.out.println("\n*************Deep Assumed: "+maxRoundLoops+"*************\n");
		System.out.println(String.format("%-20s%-20s%-20s","Max Capacity","Insertion Time","Memory Cost"));
		for(int i = 0; i < repetations; i ++){
			int newArr[] = prepareRandomIntegerArray(arraySize);
			int percentage = increment;
			while(percentage < 91){
				PercentBasedCuckooHash<Integer> hash = new PercentBasedCuckooHash<Integer>(capacity, maxRoundLoops, percentage);
				long totalTime = 0;
				for(int j =0 ; j < newArr.length; j++){
					long time = System.nanoTime();
					if(hash.put(newArr[j])){
						time = System.nanoTime() - time;
						insertions++;
						totalTime += time;
					}
					if(insertions == 1000){
						//System.out.println("total: "+j);
						break;
					}else if(j == newArr.length - 1){
						System.out.println("insertions for"+percentage+"% :"+ hash.size()+"  "+j);
					}
				}
				memTaken[percentage/increment - 1][i] = hash.avgMemoryUsagePerInsertion();
				timeTaken[percentage/increment - 1][i] = totalTime;
				percentage += increment;
				insertions = 0;
			}
		}
		for(int i = 0; i < timeTaken.length; i++){
			int time = 0;
			int mem = 0;
			for(int j = 0; j < timeTaken[i].length; j++){
				time += timeTaken[i][j];
				mem += memTaken[i][j];
			}
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(3);
			System.out.println(String.format("%-20s%-20s%-20s",""+(i+1)*increment,""+df.format(time/(repetations*1000000.0))+" ms ",""+df.format(mem/(repetations*1024*1024.0))+" MBs"));
		}
	}
	
	public void DynamicPointRun(int degree){
		int arraySize = 5000;
		int insertions = 0;
		int capacity = 100;
		int maxRoundLoops = 100;
		int repetations = 5;
		int increment = 10;
		long timeTaken[][] = new long[90/increment][repetations];
		long memTaken[][] = new long[90/increment][repetations];
		System.out.println("\n*************Deep Assumed: "+maxRoundLoops+" & Degree = "+degree+"*************\n");
		System.out.println(String.format("%-20s%-20s%-20s","Max Capacity","Insertion Time","Memory Cost"));
		for(int i = 0; i < repetations; i ++){
			int newArr[] = prepareRandomIntegerArray(arraySize);
			int percentage = increment;
			while(percentage < 91){
				SecondHash<Integer> hash = new SecondHash<Integer>(capacity, maxRoundLoops, percentage, degree);
				long totalTime = 0;
				for(int j =0 ; j < newArr.length; j++){
					long time = System.nanoTime();
					if(hash.put(newArr[j])){
						time = System.nanoTime() - time;
						insertions++;
						totalTime += time;
					}
					if(insertions == 1000){
						//System.out.println("total: "+j);
						break;
					}else if(j == newArr.length - 1){
						System.out.println("insertions for"+percentage+"% :"+ hash.size()+"  "+j);
					}
				}
				memTaken[percentage/10 - 1][i] = hash.avgMemoryUsagePerInsertion();
				timeTaken[percentage/10 - 1][i] = totalTime;
				percentage += increment;
				hash = null;
				insertions = 0;
			}
		}
		for(int i = 0; i < timeTaken.length; i++){
			int time = 0;
			int mem = 0;
			for(int j = 0; j < timeTaken[i].length; j++){
				time += timeTaken[i][j];
				mem += memTaken[i][j];
			}
//			System.out.println("Avg Time at max Capacity "+(i+1)*10+": "+time/5000000.0+" ms");
//			System.out.println("Avg Memory cost per Insertion : "+mem/5);
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(3);
			System.out.println(String.format("%-20s%-20s%-20s",""+(i+1)*increment,""+df.format(time/(repetations*1000000.0))+" ms ",""+df.format(mem/(repetations*1024*1024.0))+" MBs"));
		}
	}
	
	public String[] prepareRandomStringArray(int size){
		String arr[] = new String[size];
		for(int i = 0; i < arr.length; i++){
			arr[i] = getRandomString();
		}
		return arr;
	}
	
	public int[] prepareRandomIntegerArray(int size){
		int arr[] = new int[size];
		Random rand = new Random();
		for(int i = 0; i < arr.length; i++){
			arr[i] = rand.nextInt(10)*1000 + rand.nextInt(2570) + rand.nextInt(33370) + rand.nextInt(190) +  rand.nextInt(250) + rand.nextInt(370) + rand.nextInt(190);
		}
		return arr;
	}
	
	
	
	public String getRandomString(){
		String returnable = "";
		Random rand = new Random();
		int stringSize = rand.nextInt(4) +2;
		for(int i = 0; i < stringSize; i++){
			returnable += (char)(rand.nextInt(26)+65);
		}
		return returnable;
	}
	
	public void userRunProgram(){
		while(true){
			System.out.println("Enter the No. of tables you want to implement cuckoo hash with(enter -1 to quit):");
			Scanner s = new Scanner(System.in);
			int degree = -1;
			try{
				degree = s.nextInt();
			}catch (InputMismatchException e) {
				System.out.println("Please enter a no greater then or equal to \"2\" ");
				continue;
			}
			if(degree < 2){
				if(degree == -1){
					break;
				}
				System.out.println("Please enter a no greater then or equal to \"2\" ");
				continue;
			}
			DynamicPointRun(degree);
			
		}
	}
	
	public static void main(String args[]){
		CuckooTest test = new CuckooTest();
		test.run();
		System.out.println(String.format("======================================================================="));
		test.percentBreakPointRun();
		System.out.println(String.format("======================================================================="));
		test.userRunProgram();
	}
}
