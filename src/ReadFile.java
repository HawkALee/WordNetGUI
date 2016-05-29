/*    从index.pos和data.pos文件读入数据（pos为noun,verb,adj,adv)到8个String数组    */
/*                                                                             */
/*    刘自然                  第一版                 2015.5.15                   */

import java.io.*;

public class ReadFile {
	 final static int INDEX_NOUN_LINES = 117200;      //原文件有 117127 行，直接开 117200 大的静态数组。
	 final static int DATA_NOUN_LINES = 81500;        //原文件有 81456 行， 下同
	 final  static int INDEX_VERB_LINES = 11520;       // 11518
	 final static int DATA_VERB_LINES = 13680;        // 13680
	 final static int INDEX_ADJ_LINES = 22180;        // 22170
	 final static int DATA_ADJ_LINES = 18910;         // 18906
	 final static int INDEX_ADV_LINES = 4640;         // 4630
	 final static int DATA_ADV_LINES = 3680;          // 3673

	 public static  String[] index_noun = new String[INDEX_NOUN_LINES];
	static public String[] data_noun = new String[DATA_NOUN_LINES];
	static public  String[] index_verb = new String[INDEX_VERB_LINES];
	static public String[] data_verb = new String[DATA_VERB_LINES];
	static public String[] index_adj = new String[INDEX_ADJ_LINES];
	static public String[] data_adj = new String[DATA_ADJ_LINES];
	static public String[] index_adv = new String[INDEX_ADV_LINES];
	static public String[] data_adv = new String[DATA_ADV_LINES];

	public ReadFile() throws IOException{           //构造函数，在实例化该对象时，读取文件
		getNoun();
		getVerb();
		getAdj();
		getAdv();
	}      //Constructor


	public void getNoun() throws IOException{
		
		int i = 0;
		BufferedReader br = new BufferedReader(new FileReader("data/index.noun"));

		for(int j = 0; j < 29; j++)                //原文件前29行是说明，此处直接过滤前29行,下同。
			br.readLine();

		while((index_noun[i] = br.readLine()) != null)  //index.noun的读取
			i++;

		br = new BufferedReader(new FileReader("data/data.noun"));
		i = 0;
		for(int j = 0; j < 29; j++)
			br.readLine();
		while((data_noun[i] = br.readLine()) != null)    //data.noun的读取
			i++;
		br.close();
	}  //getNoun()


	public void getVerb() throws IOException{
		
		int i = 0;
		BufferedReader br = new BufferedReader(new FileReader("data/index.verb"));
		for(int j = 0; j < 29; j++)                      //原文件前29行是说明，此处直接过滤前29行,下同。
			br.readLine();
		while((index_verb[i] = br.readLine()) != null)
			i++;

		br = new BufferedReader(new FileReader("data/data.verb"));
		i = 0;
		for(int j = 0; j < 29; j++)
			br.readLine();
		while((data_verb[i] = br.readLine()) != null)
			i++;
		br.close();
	}  //getVerb()


	public void getAdj() throws IOException{
		
		int i = 0;
		BufferedReader br = new BufferedReader(new FileReader("data/index.adj"));
		for(int j = 0; j < 29; j++)                       //原文件前29行是说明，此处直接过滤前29行,下同。
			br.readLine();
		while((index_adj[i] = br.readLine()) != null)
			i++;

		br = new BufferedReader(new FileReader("data/data.adj"));
		i = 0;
		for(int j = 0; j < 29; j++)
			br.readLine();
		while((data_adj[i] = br.readLine()) != null)
			i++;
		br.close();
	}  //getAdj()


	public void getAdv() throws IOException{
		
		int i = 0;
		BufferedReader br = new BufferedReader(new FileReader("data/index.adv"));
		for(int j = 0; j < 29; j++)                        //原文件前29行是说明，此处直接过滤前29行,下同。
			br.readLine();
		while((index_adv[i] = br.readLine()) != null)
			i++;

		br = new BufferedReader(new FileReader("data/data.adv"));
		i = 0;
		for(int j = 0; j < 29; j++)
			br.readLine();
		while((data_adv[i] = br.readLine()) != null)
			i++;
		br.close();
	}  //getAdv()


}