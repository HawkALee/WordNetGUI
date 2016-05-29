/*
 * ������Է��
 * �༭ʱ�䣺 05/15/2015 19:06
 * �����ϵ������
 * */
import java.util.StringTokenizer;
public class synnet extends Object{//��ϵ����
	static public synnet[] dn = new synnet[ReadFile.DATA_NOUN_LINES];
	static public	synnet[] dadj = new synnet[ReadFile.DATA_ADJ_LINES];
	static public	synnet[] dadv = new synnet[ReadFile.DATA_ADV_LINES];
	static public	synnet[] dv = new synnet[ReadFile.DATA_VERB_LINES];
	static public int dadjsize = ReadFile.data_adj.length;
	static public int dadvsize = ReadFile.data_adv.length;
	static public int dvsize = ReadFile.data_verb.length;
	static public int dnsize =  ReadFile.data_noun.length;
	
	private 	String offset;//λ��
	private 	String fnum;//�ļ���ţ�
	
	private 	String type;//����
	private		int w_cnt;//�������
	private 	String[] words;//����
	
	private		String[] words_num;//�����ʮ������
	private 	int RelaNum;//��ϵ��Ŀ
	
	relation[] Rela  ;//��ϵ�б�
	
	String frame;//����
	String gloss;//ע�ͻ�������
	public synnet() {
		// TODO Auto-generated constructor stub
	}
	public void Setoffset(String off){//����ƫ����
			this.offset = off;
	}
	public void Setfnum(String num){//�����ļ�λ��
			fnum = num;
	}
	public void Settype(String rhs){//��������
			type = rhs;
	}
	public void SetWordNum(int num){//���ô������
			w_cnt = num;
			words = new String[num];
			words_num = new String[num];
	}
	public void SetRelationNum(int num){//��ϵ����
		RelaNum = num;
		Rela = new relation[num];
	}
	public void Setgloss(String rhs){//���ע�ͣ�����
		gloss = rhs;
	}
	
	public String Gettype(){//��������
			return type;
	}
	public String Getoffset(){//����ƫ����
			return offset;
	}
	public String GetFileNum(){//�����ļ����
			return fnum;
	}
	public int GetWordNum(){//���ص��ʸ���
		return w_cnt;
	}
	public String[] GetWords(){//���ص�����
		return words;
	}
	public String[] GetWordshex(){//���ص��ʵ�ʮ������
		return words_num;
	}
	public int GetRelaNum(){//���ع�ϵ����
		return RelaNum;
	}
	public 	relation[]  GetRelation(){//���ع�ϵ
		return Rela;
	}
	public synnet(String str)
	{//���캯�� ����һ�е���Ϣ������synnet����
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
		this.SetRelationNum(Integer.parseInt(firstTokenizer.nextToken()));//���ù�ϵ��Ŀ
		
		int size = this.RelaNum;
		for (int i = 0;i<size;++i){//��ӹ�ϵ
			String name = firstTokenizer.nextToken();
			String off = firstTokenizer.nextToken();
			String ty = firstTokenizer.nextToken();
			String index = firstTokenizer.nextToken();
			relation tep = new relation(off, ty, name, index);
			Rela[i] = tep;
		}
		
	}

	
}
