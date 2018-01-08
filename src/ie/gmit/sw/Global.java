package ie.gmit.sw;

public class Global {
	
	private static int blockingQueueSize;
	private static int shingleSize;
	private static int maxHashes;
	
	public static int getBlockingQueueSize() {
		return blockingQueueSize;
	}
	
	public static void setBlockingQueueSize(int blockingQueueSize) {
		Global.blockingQueueSize = blockingQueueSize;
	}
	
	public static int getShingleSize() {
		return shingleSize;
	}
	
	public static void setShingleSize(int shingleSize) {
		Global.shingleSize = shingleSize;
	}

	public static int getMaxHashes() {
		return maxHashes;
	}

	public static void setMaxHashes(int maxHashes) {
		Global.maxHashes = maxHashes;
	}
	
	
	
}
