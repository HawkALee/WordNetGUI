/*
 * 姓名：苑斌
 * 编辑时间： 05/15/2015 19:06
 * 词义关系集合类
 * */
import java.util.StringTokenizer;
public class synnet extends Object{//关系集合
	static public synnet[] dn = new synnet[ReadFile.DATA_NOUN_LINES];
	static public	synnet[] dadj = new synnet[ReadFile.DATA_ADJ_LINES];
	static public	synnet[] dadv = new synnet[ReadFile.DATA_ADV_LINES];
	static public	synnet[] dv = new synnet[ReadFile.DATA_VERB_LINES];
	static public int dadjsize = ReadFile.data_adj.length;
	static public int dadvsize = ReadFile.data_adv.length;
	static public int dvsize = ReadFile.data_verb.length;
	static public int dnsize =  ReadFile.data_noun.length;
	
	private 	String offset;//位置
	private 	String fnum;//文件标号？
	
	private 	String type;//类型
	private		int w_cnt;//词语个数
	private 	String[] words;//词语
	
	private		String[] words_num;//词语的十六进制
	private 	int RelaNum;//关系数目
	
	relation[] Rela  ;//关系列表
	
	String frame;//句型
	String gloss;//注释或者例句
	public synnet() {
		// TODO Auto-generated constructor stub
	}
	public void Setoffset(String off){//设置偏移量
			this.offset = off;
	}
	public void Setfnum(String num){//设置文件位置
			fnum = num;
	}
	public void Settype(String rhs){//设置类型
			type = rhs;
	}
	public void SetWordNum(int num){//设置词语个数
			w_cnt = num;
			words = new String[num];
			words_num = new String[num];
	}
	public void SetRelationNum(int num){//关系个数
		RelaNum = num;
		Rela = new relation[num];
	}
	public void Setgloss(String rhs){//添加注释，解释
		gloss = rhs;
	}
	
	public String Gettype(){//返回类型
			return type;
	}
	public String Getoffset(){//返回偏移量
			return offset;
	}
	public String GetFileNum(){//返回文件编号
			return fnum;
	}
	public int GetWordNum(){//返回单词个数
		return w_cnt;
	}
	public String[] GetWords(){//返回单词们
		return words;
	}
	public String[] GetWordshex(){//返回单词的十六进制
		return words_num;
	}
	public int GetRelaNum(){//返回关系个数
		return RelaNum;
	}
	public 	relation[]  GetRelation(){//返回关系
		return Rela;
	}
	public synnet(String str)
	{//构造函数 利用一行的信息来构造synnet集合
		StringTokenizer tokenizer = new StringTokenizer(str,"|");
		StringTokenizer firstTokenizer = new StringTokenizer(tokenizer.nextToken()," ");
		this.gloss = tokenizer.nextToken();
		this.offset = firstTokenizer.nextToken();
		this.fnum = firstTokenizer.nextToken();
		this.type = firstTokenizer.nextToken();
		
		String tepString = firstTokenizer.nextToken();
		int num = Integer.parseInt(tepString,16);
		this.SetWordNum(num);
		
		int sz = this.w_cnt;
		for (int i = 0 ;i<sz;++i){
				String s1 = firstTokenizer.nextToken();
				String s2 = firstTokenizer.nextToken();
				words[i] = s1;
				words_num[i] = s2;
		}
		this.SetRelationNum(Integer.parseInt(firstTokenizer.nextToken()));//设置关系数目
		
		int size = this.RelaNum;
		for (int i = 0;i<size;++i){//添加关系
			String name = firstTokenizer.nextToken();
			String off = firstTokenizer.nextToken();
			String ty = firstTokenizer.nextToken();
			String index = firstTokenizer.nextToken();
			relation tep = new relation(off, ty, name, index);
			Rela[i] = tep;
		}
		
	}

	
}
