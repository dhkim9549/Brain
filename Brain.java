

/*
javac -cp .;bitcoinj-0.8.jar;guava-15.0.jar;log4j-1.2.15.jar;sc-light-jdk15on-1.47.0.2.jar;slf4j-log4j12-1.0.jar Brain.java
java -cp .;bitcoinj-0.8.jar;guava-15.0.jar;log4j-1.2.15.jar;sc-light-jdk15on-1.47.0.2.jar;slf4j-log4j12-1.0.jar Brain
*/

import java.io.*;
import java.util.*;
import java.math.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.google.bitcoin.core.*;
import com.google.common.base.*;
import com.google.common.primitives.*;
import org.spongycastle.math.ec.*;


public class Brain {
  
  static NetworkParameters np = NetworkParameters.prodNet();
  static MessageDigest md = null;

  public static void main(String [] args) throws Exception {
    
    test2();
    
  }
  
  public static void main2(String [] args) throws Exception {
    
    
		BufferedWriter out = new BufferedWriter(new FileWriter("d:/down/out.txt"));
		
		md = MessageDigest.getInstance("SHA-256");
    //test();
    
    Set address_set = readAddressList();
    System.out.println("1 address_set.size() = " + address_set.size());
    
    ArrayList dictionary_list = readDictionary();
    System.out.println("dictionary_list = " + dictionary_list);
    
    
    
    String addr_str = "";
    Random r = new Random();
    int match_cnt = 0;
    
    for(int i = 0; i < 1000000; i++) {
      
      String phrase = "";
      phrase += "" + dictionary_list.get(r.nextInt(dictionary_list.size()));
      phrase += "" + dictionary_list.get(r.nextInt(dictionary_list.size()));
      phrase += "" + dictionary_list.get(r.nextInt(dictionary_list.size()));
      phrase += "dktkfkdgo";
      
      //System.out.println(phrase);
      
      addr_str = getAddress(phrase);
      if(address_set.contains(addr_str)) {
        System.out.println(phrase + " " + addr_str);
        out.write(phrase + " " + addr_str + "\n");
        out.flush();
        match_cnt++;
      }
      
      if(i % 100 == 0) {
        System.out.println("i = " + i);
        System.out.println("match_cnt = " + match_cnt);
        System.out.println("phrase = " + phrase);
      }
    }
    
    System.out.println("match_cnt = " + match_cnt);
  
  }
  
  public static String getAddress(String phrase) throws NoSuchAlgorithmException{
    
    BigInteger ttt2 = makeSHA2(phrase);
    ECKey eckey = new ECKey(ttt2);
    Address addr = eckey.toAddress(np);
    String addr_str = addr.toString();
    
    return addr_str;
    
  }
  
  public static void test() throws NoSuchAlgorithmException {
    
    md = MessageDigest.getInstance("SHA-256");
    
    BigInteger bi = makeSHA2("qwer1234qwer1234qwer1234qwer1234");
    System.out.println("\n bi");
    System.out.println(bi);
    
    ECKey eckey = new ECKey(bi);
    System.out.println("\n eckey");
    System.out.println(eckey);
    
    System.out.println("\n toAddress");
    System.out.println(eckey.toAddress(NetworkParameters.prodNet()));
    
    System.out.println("\n toStringWithPrivate");
    System.out.println(eckey.toStringWithPrivate());
  
 }
 
  public static void test2() throws NoSuchAlgorithmException {
    
    md = MessageDigest.getInstance("SHA-256");
    
    BigInteger bi = new BigInteger("19012221580289683623909923137735195896757850210465903656839796037017902859946");
    BigInteger bi2 = new BigInteger("1");
    
    long i = 0;
    
    while(true) {
      
      i++;
      bi = bi.add(bi2);
    
      ECKey eckey = new ECKey(bi);
      
      String addr = eckey.toAddress(NetworkParameters.prodNet()).toString();;
      
      if(i % 1000 == 0) {
        System.out.println("i = " + i);
        System.out.println("bi = " + bi);
        System.out.println("addr = " + addr);
      }
      
      /*
      System.out.println("\n toAddress");
      System.out.println(eckey.toAddress(NetworkParameters.prodNet()));
      
      System.out.println("\n toStringWithPrivate");
      System.out.println(eckey.toStringWithPrivate());
      */
    }
  
 }
 
 public static String makeSHA(String inputText) throws NoSuchAlgorithmException{
  String test = inputText;
  MessageDigest md = MessageDigest.getInstance("SHA-256");
  md.update(test.getBytes());
  byte[] digest = md.digest();
  
  
  System.out.println(md.getAlgorithm());
  System.out.println(digest.length);
  
  StringBuffer sb = new StringBuffer();
  //for(byte b : digest) {
  // System.out.print(Integer.toHexString(b & 0xff) + "");
  // sb.append(Integer.toHexString(b & 0xff));
  //}
  
  System.out.println("\n\nReturn String : " + sb.toString());
  return sb.toString();
 }



  public static BigInteger makeSHA2(String inputText) throws NoSuchAlgorithmException{
    
    String test = inputText;
    md.update(test.getBytes());
    
    BigInteger bi = new BigInteger(1, md.digest());
    //System.out.println("\n bi");
    //System.out.println(bi);
    
    return bi;
  }
  
  
  
	public static Set readAddressList() throws Exception {

	  HashSet set = new HashSet();
		LineNumberReader in = new LineNumberReader(new FileReader("d:/down/AllAdresses/AllAdresses.txt"));
		
		String header = "";
		String s;
		long cnt = 0;

		while((s = in.readLine()) != null) {
		  
		  cnt++;
		  if(cnt % 100000 == 0) {
		    System.out.println(cnt);
		  }
		  
		  set.add(s);
		  
		  if(cnt >= 400000) {
		    break;
		  }
      
		}
		
		return set;
  }


	public static ArrayList readDictionary() throws Exception {

	  ArrayList list = new ArrayList();
		LineNumberReader in = new LineNumberReader(new FileReader("d:/down/Brain/names_100.txt"));
		
		String header = "";
		String s;
		long cnt = 0;

		while((s = in.readLine()) != null) {
		  
		  cnt++;
		  if(cnt % 100000 == 0) {
		    System.out.println(cnt);
		  }
		  
      String word = getToken(s, 2, "\t");
		  
		  list.add(word);
		  
		  if(cnt >= 10000) {
		    break;
		  }
		  
		}
		
    list.add("");
		
		return list;
  }


	public static String getToken(String s, int x) {
		return getToken(s, x, " \t\n\r\f");
	}

	public static String getToken(String s, int x, String delim) {
		StringTokenizer st = new StringTokenizer(s, delim);
		int counter = 0;
		String answer = null;
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if(counter == x) {
				answer = token;
			}
			counter++;
		}
		return answer;
	}
}