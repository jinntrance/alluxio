package tachyon;

import tachyon.client.OutStream;
import tachyon.client.TachyonFS;
import tachyon.client.TachyonFile;
import tachyon.client.WriteType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public final class TestUtils {

  private static Set<Integer> masterPorts= new CopyOnWriteArraySet<Integer>();
  private static Set<Integer> workerPorts= new CopyOnWriteArraySet<Integer>();

  public static int getWorkerPort(){
   int port = 0;
   do {
      port = Constants.DEFAULT_WORKER_PORT - randomInt();
    }  while (workerPorts.contains(port));
   workerPorts.add(port);
    return port;
  }

  private static int randomInt() {
    return new Random().nextInt(2000)*2;
  }

  public static int getMasterPort(){
    int port = 0;
    do {
      port = Constants.DEFAULT_MASTER_PORT - randomInt();
    } while (masterPorts.contains(port));
    masterPorts.add(port);
    return port;
  }

  /**
   * Create a simple file with <code>len</code> bytes.
   * 
   * @param tfs
   * @param fileName
   * @param op
   * @param len
   * @return created file id.
   * @throws IOException
   */
  public static int createByteFile(TachyonFS tfs, String fileName, WriteType op, int len)
      throws IOException {
    int fileId = tfs.createFile(fileName);
    TachyonFile file = tfs.getFile(fileId);
    OutStream os = file.getOutStream(op);

    for (int k = 0; k < len; k ++) {
      os.write((byte) k);
    }
    os.close();

    return fileId;
  }

  /**
   * Create a simple file with <code>len</code> bytes.
   * 
   * @param tfs
   * @param fileName
   * @param op
   * @param len
   * @param blockCapacityByte
   * @return created file id.
   * @throws IOException
   */
  public static int createByteFile(TachyonFS tfs, String fileName, WriteType op, int len,
      long blockCapacityByte) throws IOException {
    int fileId = tfs.createFile(fileName, blockCapacityByte);
    TachyonFile file = tfs.getFile(fileId);
    OutStream os = file.getOutStream(op);

    for (int k = 0; k < len; k ++) {
      os.write((byte) k);
    }
    os.close();

    return fileId;
  }

  public static byte[] getIncreasingByteArray(int len) {
    return getIncreasingByteArray(0, len);
  }

  public static byte[] getIncreasingByteArray(int start, int len) {
    byte[] ret = new byte[len];
    for (int k = 0; k < len; k ++) {
      ret[k] = (byte) (k + start);
    }
    return ret;
  }

  public static boolean equalIncreasingByteArray(int len, byte[] arr) {
    return equalIncreasingByteArray(0, len, arr);
  }

  public static boolean equalIncreasingByteArray(int start, int len, byte[] arr) {
    if (arr == null || arr.length < len) {
      return false;
    }
    for (int k = 0; k < len; k ++) {
      if (arr[k] != (byte) (start + k)) {
        return false;
      }
    }
    return true;
  }

  public static ByteBuffer getIncreasingByteBuffer(int len) {
    return getIncreasingByteBuffer(0, len);
  }

  public static ByteBuffer getIncreasingByteBuffer(int start, int len) {
    return ByteBuffer.wrap(getIncreasingByteArray(start, len));
  }

  public static ByteBuffer getIncreasingIntBuffer(int len) {
    ByteBuffer ret = ByteBuffer.allocate(len * 4);
    for (int k = 0; k < len; k ++) {
      ret.putInt(k);
    }
    ret.flip();
    return ret;
  }
}
