/*��	����Է��
 * ѧԺ���ſ�
 * ���ʱ�䣺05/15/2015 19:45
 * �洢
 * */

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;


public class word {
	
	  static int InSize = ReadFile.INDEX_NOUN_LINES;      
	   static int IvSize = ReadFile.INDEX_VERB_LINES;     
	  static int IadjSize = ReadFile.INDEX_ADJ_LINES;       
	  static int IadvSize = ReadFile.INDEX_ADV_LINES;        

	public static  word[] In = new word[ReadFile.INDEX_NOUN_LINES];
	static public  word[] Iv = new word[ReadFile.INDEX_VERB_LINES];
	static public word[] Iadj = new word[ReadFile.INDEX_ADJ_LINES];
	static public word[] Iadv = new word[ReadFile.INDEX_ADV_LINES];
	public static word NotFound = new word();
	private String name;//��������
	//private String explanation;//���ʽ���
	private String type;//���� ���� n,adj,adv,v
	private int relationnum;//��ϵ��Ŀ
	private int  synnetnum;//������Ŀ
	private int frequence ;//Ƶ��
	Vector<String> Relations = new Vector<String>();
	String items[] ;
	ArrayList<synnet> antonymy = new ArrayList<synnet>();//�����ָ��
	ArrayList<synnet> hypermymy = new ArrayList<synnet>();//��λ��ָ��
	ArrayList<synnet> hyponymy = new ArrayList<synnet>();//��λ��ָ��
	ArrayList<String> synsets = new ArrayList<String>();//ͬ��ʼ���
	public boolean isvisit;
	
	//��װ
	public boolean Hassyn(){
		return synsets.size() > 0;
	}//�����Ƿ���ͬ���
	public boolean Hasanto(){
		return antonymy.size()>0;
	}//�Ƿ��з����
	
	
	public boolean Hashyper(){
		return hypermymy.size() > 0;
	}//�����Ƿ�����λ��
	public boolean Hashyponymy(){
		return hyponymy.size()>0;		
	}//�����Ƿ��������
	
	
	public int compareTo(Object o){
		word objWord  = (word)o;
			return name.compareTo(objWord.name);
	}
	
	public String Getname(){
		return name;
	}
	public String GetType(){
		return type;
	}
	public int GetSynnetNum(){
		return synnetnum;
	}
	
	public void SetType(String ty){
		type = ty;
	}
	public void SetName(String na){
		name = na;
	}
	public void Setsynnetnum(int num){
		synnetnum = num;
	}
	
	public word(){}
	public word(String str){
			StringTokenizer f = new StringTokenizer(str);
			name = f.nextToken();
			type = f.nextToken();
			synnetnum = Integer.parseInt(f.nextToken());
			relationnum = Integer.parseInt(f.nextToken());
			isvisit = false;
			for (int i = 0;i<relationnum;++i)
				Relations.add(f.nextToken());
			f.nextToken();//��ȥ���Ͽ��е��������
			frequence = Integer.parseInt(f.nextToken());
			items = new String[synnetnum];
			for (int i = 0;i<synnetnum;++i)
				items[i] = f.nextToken();
			f= null;
	}
	
}
