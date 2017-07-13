import java.util.Random;

public class CuckooTest {
	
	int size = 500;
	
	public void run(){
		int capacity = 10;
		int maxRoundLoops = 10;
		int arr[] = prepareRandomIntegerArray(size);
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
			System.out.println("____________________________________\nStart Capacity: "+ capacity+"\nFinal Capacity: "+hashCapacity/10+"\nMax Round-loops: "+maxRoundLoops+"\ntime for "+size+" insertions: "+(millitime/10.0)+" ms\nLoad Factor: "+loadFactor/10+"\nMemory cost per insertion: "+memoryCost/10+"\n____________________________________\n");
			//capacity += 10;
			maxRoundLoops += 2;
		}
	}
	
	public void percentBreakPointRun(){
		int arraySize = 5000;
		int insertions = 0;
		int capacity = 100;
		int maxRoundLoops = 101;
		long timeTaken[][] = new long[9][5];
		long memTaken[][] = new long[9][5];
		for(int i = 0; i < 5; i ++){
			int newArr[] = prepareRandomIntegerArray(arraySize);
			int percentage = 10;
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
				memTaken[percentage/10 - 1][i] = hash.avgMemoryUsagePerInsertion();
				timeTaken[percentage/10 - 1][i] = totalTime;
				percentage += 10;
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
			System.out.println("Avg Time at max Capacity "+(i+1)*10+": "+time/5000000.0+" ms");
			System.out.println("Avg Memory cost per Insertion : "+mem/5);
		}
	}
	
	public void DynamicPointRun(){
		int arraySize = 5000;
		int insertions = 0;
		int capacity = 100;
		int maxRoundLoops = 100;
		long timeTaken[][] = new long[9][5];
		long memTaken[][] = new long[9][5];
		for(int i = 0; i < 5; i ++){
			int newArr[] = prepareRandomIntegerArray(arraySize);
			int percentage = 10;
			while(percentage < 91){
				SecondHash<Integer> hash = new SecondHash<Integer>(capacity, maxRoundLoops, percentage, 9);
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
				percentage += 10;
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
			System.out.println("Avg Time at max Capacity "+(i+1)*10+": "+time/5000000.0+" ms");
			System.out.println("Avg Memory cost per Insertion : "+mem/5);
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
	
	
	
	public static void main(String args[]){
		CuckooTest test = new CuckooTest();
		test.run();
		test.percentBreakPointRun();
		test.DynamicPointRun();
	}
}
