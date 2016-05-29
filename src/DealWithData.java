/*
 * ������Է��
 * �༭ʱ�� 16/05/2015
 * */

import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;


public class DealWithData {

	
	public void Start(){
		int i;
		/*for ( i = 0;i<synnet.dnsize;++i)//����data �е� ����
		{	
			String str = ReadFile.data_noun[i];
			if (str == null)
				break;
			synnet tepSynnet = new synnet(str);
			synnet.dn[i] = tepSynnet;
		}

		for ( i = 0;i<synnet.dadjsize;++i)//���� data�е����ݴ� 
		{
			String str = ReadFile.data_adj[i];
			if (str == null)
				break;
			synnet.dadj[i] = new synnet(str);
		}

		for ( i = 0;i<synnet.dadvsize;++i)//����data�еĸ���
		{
			String str = ReadFile.data_adv[i];
			if (str == null)
				break;
			synnet.dadv[i] = new synnet(str);
		}

		for ( i = 0;i<synnet.dvsize;++i)//����data�еĶ���
		{
			String str = ReadFile.data_verb[i];
			if (str == null)
				break;
			synnet.dv[i] =new  synnet(str);
		}*/

		
		for ( i = 0;i<word.InSize;++i)//����index �е� ����
		{	
			String str = ReadFile.index_noun[i];
			if (str == null)
				break;
			word.In[i] = new word(str);
		}

		
		for ( i = 0;i<word.IadjSize;++i)//���� index�е����ݴ� 
		{
			String str = ReadFile.index_adj[i];
			if (str == null)
				break;
			word.Iadj[i] = new word(str);
		}

		for ( i = 0;i<word.IadvSize;++i)//����index�еĸ���
		{
		
			String str = ReadFile.index_adv[i];
			if (str == null)
				break;
			word.Iadv[i] = new word(str);
		}
		for ( i = 0;i<word.IvSize;++i)//����index�еĶ���
		{
		
			String str = ReadFile.index_verb[i];
			if (str == null)
				break;
			word.Iv[i] = new word(str);
		}
		
		//ͳһ��Ŀ
		int number = 0;
		while (word.In[number]!=null)
			number ++;
		word.InSize = number-1;
		
		number = 0;
		while (word.Iadj[number]!=null)
			number++;
		word.IadjSize = number-1;
		
		number = 0;
		while (word.Iadv[number]!=null)
			number ++;
		word.IadvSize = number-1;
		
		number = 0;
		while (word.Iv[number]!=null)
			number++;
		word.IvSize = number-1;
		
		word.NotFound.SetName("NotFound");
		word.NotFound.Setsynnetnum(0);
		

		//SetUp();
	}
	
	public void PutSynnetIntoWord(word cur, Vector<synnet> syn,String charac){
		//����
		
			int synsize = syn.size();
			for (int j = 0;j<synsize;++j)//��ÿ����ϵ���뵽�õ�����ȥ
			{
				String tepString = cur.items[j];
				synnet add = syn.elementAt(j);
				relation[] tepRelations  = add.GetRelation();
				int siz = tepRelations.length;
				for (int k = 0;k<siz;++k)
				{
					relation tp = tepRelations[k];
					
					if (tp.rela.equals("@")) //��λ��ϵ 
					{
						synnet tep = FindSynnetAccordOffset(tp.offset,charac);
						cur.hypermymy.add(tep);
					}
					if (tp.rela.equals("!"))//�����ϵ
					{
						synnet tep = FindSynnetAccordOffset(tp.offset,charac);
						cur.antonymy.add(tep);
					}
					if (tp.rela.equals("~"))//��λ��ϵ
					{
						synnet tep = FindSynnetAccordOffset(tp.offset,charac);
						cur.hyponymy.add(tep);
					}
				}
				//ͬ���ϵ
				String[] synt   = add.GetWords();
				int size = synt.length;
				for (int k = 0 ;k<size;++k)
				{
						String tep = synt[k];
						if (tep.compareTo(cur.Getname()) != 0)
						cur.synsets.add(tep);
				}
				
			}
		
	}
	
	public void SetUp(){
			StartSearch();
			return ;
	}
	public void printall(word tep){
		System.out.println("�����");
		if (tep.Hasanto()){
			int sz = tep.antonymy.size();
			for (int i = 0;i<sz;++i)
			{
				synnet retep = tep.antonymy.get(i);
				//System.out.println(retep.type);
				String[] words = retep.GetWords();
				String out = "";
				for (int j = 0;j<words.length;++j)
					out += " " + words[j];
				System.out.println(out);
			}	
		}
		
		System.out.println("�����");
		if (tep.Hashyper()){
			int sz = tep.hypermymy.size();
			for (int i = 0;i<sz;++i)
			{
				synnet retep = tep.hypermymy.get(i);
				//System.out.println(retep.type);
				String[] words = retep.GetWords();
				String out = "";
				for (int j = 0;j<words.length;++j)
					out += " " + words[j];
				System.out.println(out);
			}	
		}
		
		System.out.println("�����");
		if (tep.Hashyponymy()){
			int sz = tep.hyponymy.size();
			for (int i = 0;i<sz;++i)
			{
				synnet retep = tep.hyponymy.get(i);
				//System.out.println(retep.type);
				String[] words = retep.GetWords();
				String out = "";
				for (int j = 0;j<words.length;++j)
					out +=" " + words[j];
				System.out.println(out);
			}	
		}
		
		System.out.println("ͬ���");
		if (tep.Hassyn()){
			int sz = tep.synsets.size();
			for (int i = 0;i<sz;++i)
			{
				String retep = tep.synsets.get(i);
				System.out.println(retep);
			}	
		}
		
	}
	public word getwordaccordname(String name, String charac)//������������ӵĵط�����������
	{
			word res = FindAccordName(name,charac);
			if (res.Getname().compareTo(name) !=0 )
			{
					return word.NotFound;
				
			}
			if(res.isvisit)
			{
				return res;
			}
			else 
			{
				res.isvisit = true;
				Vector<synnet> tp = FindTheSynnet(res,charac);
				PutSynnetIntoWord(res,tp,charac);
				//printall(res);
			}
			
		return res;
		
	}
	
	public void StartSearch()
	{
		while(true)
		{
			System.out.println("������Ҫ��ѯ�Ĵ� �Լ� ���� ");
			String charac  ;
			String name;
			Scanner tscan = new Scanner(System.in);
			name = tscan.next();
			charac = tscan.next();
			word res = getwordaccordname(name,charac);////��������������Եõ� word
			printall(res);
		}
	}
	
	public Vector<synnet> FindTheSynnet(word root,String characteristic){//
			Vector<synnet> find = new Vector<synnet>();
			int size = root.GetSynnetNum();
			for (int i = 0 ;i<size;++i){
				String cur = root.items[i];
				synnet tp = FindSynnetAccordOffset(cur,characteristic);
				find.add(tp);
			}
			return find;
	}
	public synnet FindSynnetAccordOffset (String offset ,String BinarySearchLine){
		if (BinarySearchLine.compareTo("n") == 0){
			int l = 0,r = ReadFile.data_noun.length;
			while (l<r)
			{
				int mid = (l+r)/2;
				if (compareStringLine(offset,ReadFile.data_noun[mid]))
					l = mid + 1;		
				else 
					r = mid;
			}
				return new synnet(ReadFile.data_noun[l]);
		}
		
		if (BinarySearchLine.compareTo("v") == 0){
			int l = 0,r = ReadFile.data_verb.length;
			while (l<r)
			{
				int mid = (l+r)/2;
				if (compareStringLine(offset,ReadFile.data_verb[mid]))
					l = mid +1 ;		
				else 
					r = mid;
			}
			return new synnet( ReadFile.data_verb[l]);
		}
		
		if (BinarySearchLine.compareTo("adj") == 0){
			int l = 0,r = synnet.dadjsize;
			while (l<r){
				int mid = (l+r)/2;
				if (compareStringLine(offset,ReadFile.data_adj[mid]))
					l = mid + 1 ;		
				else 
					r = mid;
			}
			return new synnet(ReadFile.data_adj[l]);
		}
		
		if (BinarySearchLine.compareTo("adv") == 0){
			int l = 0,r = synnet.dadvsize;
			while (l<r){
				int mid = (l+r)/2;
				if (compareStringLine(offset,ReadFile.data_adv[mid]))
					l = mid+1;	
				else 
					r = mid;
			}
			return new synnet(ReadFile.data_adv[l]);
		}
		
		return null;
	}
	public boolean compareStringLine(String cur ,String line){
		StringTokenizer f = new StringTokenizer(line);
		return (cur.compareTo(f.nextToken()) > 0);
	}
	
	
	public void Gethypermymy(word root,int depth){//�õ�����root����λ��  ����Ϊdepth 
		int dep  = depth;
		word tp  = root;
		while (tp.Hashyper()){
			
		return ;	
		}
		
	}
	
	public word FindAccordName(String name,String characteristic ){//�������ֺʹ��Բ��ҵ���
		if(characteristic.equals("n")){//�������в�ѯ
			int l = 0;
			int r = word.InSize;
			while (l<r){
				int mid = (l+r +1 )/2;
				if (word.In[mid].Getname().compareTo(name)>0 )
					r = mid - 1;
				else 
					l = mid;
			}
			return word.In[l];
			
		}
		
		if(characteristic.equals("adj")){//�����ݴ��в�ѯ
			int l = 0;
			int r = word.IadjSize;
			while (l<r){
				int mid = (l+r+1)/2;
				if (word.Iadj[mid].Getname().compareTo(name) >0)
					r = mid -1;
				else 
					l = mid;
			}
			return word.Iadj[l];
			
		}
		
		if(characteristic.equals("v")){//�ڶ����в�ѯ
			int l = 0;
			int r = word.IvSize;
			while (l<r){
				int mid = (l+r+1)/2;
				if (word.Iv[mid].Getname().compareTo(name) >0)
					r = mid -1;
				else 
					l = mid;
			}
			return word.Iv[l];
			
		}
		
		if(characteristic.equals("adv")){//�ڸ����в�ѯ
			int l = 0;
			int r = word.IadvSize;
			while (l<r){
				int mid = (l+r+1)/2;
				if (word.Iadv[mid].Getname().compareTo(name) >0)
					r = mid -1;
				else 
					l = mid;
			}
			return word.Iadv[l];
			
		}
		return null; //û���ҵ�
	}
	
	
}