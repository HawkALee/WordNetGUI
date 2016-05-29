/*          ��һ��String�У��õ����ԡ�ָ�롢Ƶ�ȵȲ���       */
/*                                                      */
/*        ����Ȼ         ��һ��         2015.5.15         */

public class GetInfo {
	public String lemma = "";
	public String[] ptr_symbol = new String[10];  
	            //ָ����ſ���Ϊ2���ַ�����������ΪString, �ļ���һ��������ָ�������������10����Ŀ�⣩
	            //���Կ���10��String�����飬�˴����ں����汾�и��ö�̬���� arraylist . 
	public char pos;
	public int poly_cnt, p_cnt, sense_cnt, tagsense_cnt;
	public int[] synset_offset = new int[15];
	            //�˴���15����������������뷨һ���������汾���Ը��� arraylist��

    //�õ��ַ��� sr �ӵ� index λ��ʼ���� index �����һ���ո�֮����ַ�����
    public String nextString(String sr, int index){
    	String ans = "";
    	while(sr.charAt(index) != ' '){
    		ans = ans + sr.charAt(index);
    		index++;
    	}
    	return ans;
    }

    //�� sr �ֽ��һ���������� �˴��ٶ�ÿ���ַ���������������򣬹�û��д�쳣���������汾�ɼ��ϡ�
	public void obtainInfo(String sr){         
		String tmp = "";
		for(int i = 0; ; ){

			lemma = nextString(sr, i);    //��һ���ַ���Ϊ���� lemma
			i = i + lemma.length() + 1;
		
            
			pos = sr.charAt(i);           //�õ����� pos
			i = i + 2;
		


			tmp = nextString(sr, i);
			poly_cnt = Integer.parseInt(tmp);  //���������Ϊһ��ʮ������
			i = i + tmp.length() + 1;


			tmp = nextString(sr, i);
			p_cnt = Integer.parseInt(tmp);     //��ϵָ�����,һ��ʮ������
			i = i + tmp.length() + 1;


			for(int j = 0; j < p_cnt; j++){    //�õ� p_cnt ��ָ�����
				ptr_symbol[j] = nextString(sr, i);
				i = i + ptr_symbol[j].length() + 1;
			}


			tmp = nextString(sr, i);           //�����������ǰ������һ����Ϊ�����ֵ�������������������Ϊ��...�����ùܣ�	
			sense_cnt = Integer.parseInt(tmp);
			i = i + tmp.length() + 1;


			tmp = nextString(sr, i);           //�õ��ʵ�Ƶ��
			tagsense_cnt = Integer.parseInt(tmp);
			i = i + tmp.length() + 1;


			for(int j = 0; j < poly_cnt; j++){  //�õ�poly_cnt������ƫ�Ƶ�ַ
				tmp = nextString(sr, i);
				synset_offset[j] = Integer.parseInt(tmp);  //ÿ��ƫ�Ƶ�ַΪ8λ��ǰ�油0��ʮ��������
				i = i + tmp.length() + 1;
			}

			break;
		}   //forѭ������
	}    //obtainInfo() ����

} //GetInfo
