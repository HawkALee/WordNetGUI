	public class relation extends Object{
		
		public		String offset;//��ַƫ��
		public		String type;//����
		public		String rela;//��ϵ
		public		String reindex;//��ϵ����
		public 		String[] name;//����
		
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
