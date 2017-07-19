import java.util.Arrays;

public class SecondHash<K>{

	class Node<S>{
		
		S data;
		
		Node(S data){
			this.data = data;
		}
		
	}
	
	
	int capacity = 100;
	double threshold = 0.75;
	int maxRoundLoops = 100;
	int percentFull = 10;
	private int size = 0;
	Node<K> hashArr[][];
	int numbers[] = {11,23,37,61,123};
	int append = 0;
	long memory = 0;
	long cumelativeMemory = 0;
	int degree = 0;
	K incomingElement = null;

	public int getCapacity() {
		return capacity;
	}

	SecondHash(int capacity, int maxRoundLoops, int percentFull, int degree){
		this.capacity = capacity;
		this.percentFull = percentFull;
		this.maxRoundLoops = maxRoundLoops;
		this.degree = degree;
		hashArr = new Node[degree][capacity];
		for(int i = 0 ; i< hashArr.length; i++){
			Arrays.fill(hashArr[i],null);
		}
	}
	
	public boolean put(K data){
		incomingElement = data;
		memory = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		Node<K> newNode = new Node<K>(data);
		int key = 0;
		for(int i = 0 ; i < degree; i++){
			key = hash(data,i);
			if(hashArr[i][key] != null && hashArr[i][key].data.equals(data)){
				return false;	
			}
		}
		if((size/(capacity*1.0*degree))*100 > percentFull){
			maxLoopsStuck();
		}
		if(insertToTable(hash(data,0), newNode, 0, newNode.data, 0)){
			size++;
			cumelativeMemory += memory;
			return true;
		}
		return false;	
		
	}
	
	private boolean insertToTable(int key, Node<K> n, int count, K data, int D){
		if(count > maxRoundLoops){
			int hash = hash(incomingElement,0);
			hashArr[0][hash] = null;
			size--;
			put(data);
			return false;
		}else if(hashArr[D][key] == null){
			hashArr[D][key] = n;
			memory = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory() - memory; 
			return true;
		}else{
			Node<K> temp = hashArr[D][key];
			hashArr[D][key] = n;
			D++;
			if((D >= degree))
				D = 0;
			return insertToTable(hash(temp.data,D), temp, ++count, data, D);
		}
	}
	
	private void maxLoopsStuck(){
		Node<K> temp1[][] = hashArr;
		capacity = capacity + numbers[append];
		if(append < 4){
			append++;
		}
		size = 0;
		hashArr = new Node[degree][capacity];
		for(int i = 0 ; i< hashArr.length; i++){
			Arrays.fill(hashArr[i],null);
		}
		for(int i = 0 ; i < temp1.length; i++){
			for(int j = 0 ; j < temp1[i].length; j++){
				if(temp1[i][j] != null)
					put(temp1[i][j].data);
			}
		}
	}
	
	private int  hash(K data, int degree){
		int key = 0;
		switch(degree%3){
		case 0:
			key = ((data.hashCode() & 0xf7777777)*7 + 29*degree + 7) % (capacity/2);
			break;
		case 1:
			int hash = data.hashCode();
			hash = hash &  0xf7777777;
			int modified = 0;
			int count = 0;
			while (hash != 0){
				count ++;
				int remainder = hash%7;
				hash = hash/7;
				remainder *= (count*7 + 11*degree);
				//remainder += (7* (count/2)); 
				modified += (remainder * 127);
			}
			key = ((modified))*degree % (capacity);
			break;
		case 2:
			hash = data.hashCode();
			hash = hash & 0xf7777777;
			modified = 0;
			count = 0;
			while (hash != 0){
				count ++;
				int remainder = hash%7;
				hash = hash/7;
				remainder *= (degree^count + degree*29);
				//remainder += (7* (count/2)); 
				modified += (remainder * 371);
			}
			key = ((modified)) % (capacity);
			break;
		}
		return key;
	}
	
	public boolean delete(K data){
		for(int i = 0 ; i < degree; i++){
			int key = hash(data,i);
			if(hashArr[i][key] != null && hashArr[i][key].data.equals(data)){
				hashArr[i][key] = null;
				size--;
				return true;
			}
		}
		System.out.println(data+" is not on the table");
		return false;
	}
	
	public int size(){
		return size;
	}
	
	public double loadFactor(){
		return (1.0*size)/capacity;
	}
	
	public long avgMemoryUsagePerInsertion(){
		return cumelativeMemory/size;
	}
	
}
