	public class relation extends Object{
		
		public		String offset;//地址偏移
		public		String type;//词性
		public		String rela;//关系
		public		String reindex;//关系类型
		public 		String[] name;//名字
		
		public	relation(String off,String ty,String relaName,String index){
					offset = off;
					type = ty;
					rela = relaName;
					reindex = index;
				}
		public relation(){
				offset = null;
				type = null;
				rela = null;
				reindex = null;
			}
	}
