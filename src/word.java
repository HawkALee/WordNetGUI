/*姓	名：苑斌
 * 学院：信科
 * 完成时间：05/15/2015 19:45
 * 存储
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
	private String name;//单词名字
	//private String explanation;//单词解释
	private String type;//类型 四种 n,adj,adv,v
	private int relationnum;//关系数目
	private int  synnetnum;//义项数目
	private int frequence ;//频次
	Vector<String> Relations = new Vector<String>();
	String items[] ;
	ArrayList<synnet> antonymy = new ArrayList<synnet>();//反义词指针
	ArrayList<synnet> hypermymy = new ArrayList<synnet>();//上位词指针
	ArrayList<synnet> hyponymy = new ArrayList<synnet>();//下位词指针
	ArrayList<String> synsets = new ArrayList<String>();//同义词集合
	public boolean isvisit;
	
	//封装
	public boolean Hassyn(){
		return synsets.size() > 0;
	}//返回是否有同义词
	public boolean Hasanto(){
		return antonymy.size()>0;
	}//是否有反义词
	
	
	public boolean Hashyper(){
		return hypermymy.size() > 0;
	}//返回是否有上位词
	public boolean Hashyponymy(){
		return hyponymy.size()>0;		
	}//返回是否有下义词
	
	
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
			f.nextToken();//略去语料库中的义项个数
			frequence = Integer.parseInt(f.nextToken());
			items = new String[synnetnum];
			for (int i = 0;i<synnetnum;++i)
				items[i] = f.nextToken();
			f= null;
	}
	
}
