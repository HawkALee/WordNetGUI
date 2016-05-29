/*          从一个String中，得到词性、指针、频度等参数       */
/*                                                      */
/*        刘自然         第一版         2015.5.15         */

public class GetInfo {
	public String lemma = "";
	public String[] ptr_symbol = new String[10];  
	            //指针符号可能为2个字符，所以声明为String, 文件中一个词条的指针符号数不超过10个（目测）
	            //所以开了10个String的数组，此处可在后续版本中改用动态数组 arraylist . 
	public char pos;
	public int poly_cnt, p_cnt, sense_cnt, tagsense_cnt;
	public int[] synset_offset = new int[15];
	            //此处开15个大的数组与上面想法一样，后续版本可以改用 arraylist。

    //得到字符串 sr 从第 index 位开始，到 index 后面第一个空格之间的字符串。
    public String nextString(String sr, int index){
    	String ans = "";
    	while(sr.charAt(index) != ' '){
    		ans = ans + sr.charAt(index);
    		index++;
    	}
    	return ans;
    }

    //把 sr 分解成一个个参数。 此处假定每个字符串都符合输入规则，故没有写异常处理。后续版本可加上。
	public void obtainInfo(String sr){         
		String tmp = "";
		for(int i = 0; ; ){

			lemma = nextString(sr, i);    //第一个字符串为单词 lemma
			i = i + lemma.length() + 1;
		
            
			pos = sr.charAt(i);           //得到词性 pos
			i = i + 2;
		


			tmp = nextString(sr, i);
			poly_cnt = Integer.parseInt(tmp);  //义项个数，为一个十进制数
			i = i + tmp.length() + 1;


			tmp = nextString(sr, i);
			p_cnt = Integer.parseInt(tmp);     //关系指针个数,一个十进制数
			i = i + tmp.length() + 1;


			for(int j = 0; j < p_cnt; j++){    //得到 p_cnt 个指针符号
				ptr_symbol[j] = nextString(sr, i);
				i = i + ptr_symbol[j].length() + 1;
			}


			tmp = nextString(sr, i);           //义项个数，与前面义项一样，为多余的值，设置两个义项个数是为了...（不用管）	
			sense_cnt = Integer.parseInt(tmp);
			i = i + tmp.length() + 1;


			tmp = nextString(sr, i);           //该单词的频度
			tagsense_cnt = Integer.parseInt(tmp);
			i = i + tmp.length() + 1;


			for(int j = 0; j < poly_cnt; j++){  //得到poly_cnt个数的偏移地址
				tmp = nextString(sr, i);
				synset_offset[j] = Integer.parseInt(tmp);  //每个偏移地址为8位（前面补0）十进制数。
				i = i + tmp.length() + 1;
			}

			break;
		}   //for循环结束
	}    //obtainInfo() 结束

} //GetInfo
