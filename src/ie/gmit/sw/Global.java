package ie.gmit.sw;
/**
 * Called from tge ServiceHandler, this class sets up variables from the web.xml. They are now Global
 * and can be used anywhere in the applicatoion.
 * 
 * @author Patrick Moran
 */
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
