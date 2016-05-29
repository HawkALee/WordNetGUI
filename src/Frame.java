/* �����ͼ�ν��棬�����ֵ����Ӻͼ�����
 * ��������Ψͯ
 * �༭ʱ�� 2/06/2015
 * */

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;

public class Frame
{
//----------------------   ���������ڼ���������  -----------------------
	private JFrame f = new JFrame( "WordNet" ) ;
	private JSplitPane  mainSplitPane;//�ָ��
	//���������������������
	private JPanel pTop = new JPanel() ;    //������������
	private JPanel pBottom = new JPanel() ;	//��װ�ť
	private JPanel pTree = new JPanel() ;	//��״ͼ
    private GraphView netplace; //##��ʾ�����࣬��  GraphView ʵ��
	public JPanel background = new JPanel();//������ര�ڱ�����Panel����δ���ñ���
	private String contest ;//���뵽�ı��������

	//-----------------------  ������ּ����벿��	  -----------------------	
	private JLabel  lb = new JLabel( "���뵥��:" ) ; //��ʾ�����ǩ	
	private JTextField tf = new JTextField( 15 ) ;//���뵥���ı���	
	private JButton ok = new JButton( "��ѯ" ) ;   //ȷ�ϰ�ť	
	private JTextArea ta = new JTextArea( 16,20 );//һ���ı���������ʾ���ֽ��
	                                              //�ĸ�����ѡ�ť
	private JButton bNoun = new JButton( "����" ) ;
	private JButton bAdv = new JButton( "����" ) ;
	private JButton bAdj = new JButton( "���ݴ�" ) ;
	private JButton bVerb = new JButton( "����" );
	private JButton bAll = new JButton("ȫ��");
	
//----------------------   �����˵���    ------------------------------
	private JMenuBar mb = new JMenuBar() ;		// �������˵���   �ļ�����ʷ������
	private JMenu mFile = new JMenu( "�ļ�" ) ;
	private JMenu mHistory = new JMenu( "��ʷ" ) ;
	private JMenu mHelp = new JMenu( "����" ) ;	
	
	//�ļ��˵�������ע���������˳����˵���	
	private JMenuItem saveItem = new JMenuItem( "������״ͼ" ) ;
	private JMenuItem exitItem = new JMenuItem( "�˳�" ) ;
	
	//��ʷ�˵�
	private HashSet<JMenuItem> historyList = new HashSet<JMenuItem>();
	
	//���������˵��С�˵���ĵ����˵���
	private JMenuItem aboutItem = new JMenuItem( "˵���ĵ�" ) ;
	
//---------------    ˵���ĵ�������     ----------------------------------------------
	private JFrame d = new JFrame("˵���ĵ�" ) ;	//��	
	private JPanel bp = new JPanel() ;//��������
	private JTextArea bta = new JTextArea( 8 , 15 ) ;//�ı���		
	private JButton bok = new JButton( "ȷ��" ) ;//ȷ����ť

	
	//###################################   ��ʼ������       ################################
	public void init()throws IOException   //����������ӵ�����λ�ò��ṩ��ؼ���
	{
		background.setLayout(new BorderLayout());
		background.setBackground(Color.gray);
		pTree.setLayout(new BorderLayout());
		ReadFile rf = new ReadFile();
		DealWithData run = new DealWithData();//�������ݵ���## DealWithData
		run.Start();//������д��
		
		//��ʼ��GraphView
		netplace = new GraphView(this);
//--------------------------------   ��ඥ�������벿����������  ----------------------------		
		//��ඥ����������װ����ʾ��ǩ�������ı����ȷ�ϰ�ť
		pTop.add( lb ) ;
		pTop.add( tf ) ;
		pTop.add( ok ) ;		
		//����Action������ʵ�������ı�����Ϣ
		Action sendMSG = new AbstractAction() 
		{
			private static final long serialVersionUID = 5765576602055012931L;
			//��������ӵ���ʷ��¼
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e)
			{								
				ta.setText("");	//��ʼ���ı���Ϊ��	
				Font ziti = new Font("����",Font.PLAIN,20);
				ta.setFont(ziti);  
				ta.setForeground(Color.white);
		 		contest = tf.getText(); //�õ�����ĵ���		 			
		 		if (contest.equals("")) return; //�������Ϊ�գ�ֱ�ӷ���		 			
				contest = contest.toLowerCase(); //��д��Сд				
				boolean flag = true;
				int count = 0; //ͳ�Ƶ�ǰ�м���Item
				Iterator<JMenuItem> i = historyList.iterator();
				while (i.hasNext())//ѭ���鿴�Ƿ����ظ�
				{
					count ++;
					JMenuItem m = i.next();
					if (m.getLabel().equals(contest)) //����Ѿ��м�¼�����ٱ���
					{
						flag = false;
						break;
					}
				}				
				if (flag == true) //���û�м�¼
				{
					final JMenuItem m = new JMenuItem(contest);
					historyList.add(m); //������ʷ����
					
					if (count == 5) //����Ѿ����������Ŀ���Ƴ���5�����嵽��0��λ��
					{
						mHistory.remove(4);
						mHistory.insert(m, 0);
					}
					else //����ֱ�Ӳ���0λ��
					{
						mHistory.insert(m, 0);
					}
					
					//���mHistory�˵��б��¼�����
					m.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							tf.setText(m.getLabel()); //��ʾ���ı�����
							ta.setText("");//��ս������
							try{
								displayText(contest,0);//````````````````````��ʾ��ѯ���
								ChangeGraph(contest,0);
								}catch (Exception a)
								{
									a.printStackTrace();
								}
						}
					});
				}
				
				//########################################��ʾ��ѯ�������������Ӳ�ѯ����
				try{
				displayText(contest,0);//````````````````````��ʾ��ѯ���
				ChangeGraph(contest,0);
				}catch (Exception b)
				{
					//e.printStackTrace();
				}	
			}
		} ;
		
		//��ӵ�����ȷ������ť���¼�����
		ok.addActionListener( sendMSG ) ;		
		//�ѻس����͡�ȷ������ť��������
		tf.getInputMap().put( KeyStroke.getKeyStroke( '\n' ) , "send" ) ;
		tf.getActionMap().put( "send" , sendMSG ) ;		
		
		//����������װ�봰�ڶ���
		background.add( pTop , BorderLayout.NORTH ) ;
//-------------------------------     �ı���         --------------------------------------
				
		ta.setLineWrap(true);
		ta.setEditable(false);                  //�ı�������༭			
		JScrollPane sp = new JScrollPane( ta ) ;//���ı���������������������	
		background.add( sp , BorderLayout.CENTER) ;	
//-------------------------------    ����²���ť   -------------------------------------
		//����²�������BoxLayout���ֹ�������������
		pBottom.setLayout( new GridLayout( 1 , 5 , 0 , 0 ) ) ;
		
		//���μ���������������ť
		pBottom.add( bNoun ) ;
		pBottom.add( bVerb ) ;
		pBottom.add( bAdj ) ;
		pBottom.add( bAdv ) ;				
		pBottom.add( bAll );//����ڵ���״ͼ�򿪰�ť

		//Ϊ�����ν����ṩ�¼�����
		bAll.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		
		//��pBottom���ڴ�����࣬pRight���ڴ����Ҳ�
		background.add( pBottom , BorderLayout.SOUTH ) ;


//***--------------------   ������    ------------------------------	
		//��ӵ��bNoun��ť����¼�����
		bNoun.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent e )
			{				
				ta.setText("");	//��ʼ���ı���Ϊ��
		 		try
		 		{
		 			displayText(contest,0);
		 			ChangeGraph(contest,0);
				}catch(Exception e1){}
	 		}				
		} ) ;
		bVerb.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent e )
			{				
				ta.setText("");	//��ʼ���ı���Ϊ��
		 		try
		 		{
		 			displayText(contest,1);
		 			ChangeGraph(contest,1);
				}catch(Exception e1){}
	 		}				
		} ) ;
		bAdj.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent e )
			{				
				ta.setText("");	//��ʼ���ı���Ϊ��
		 		try
		 		{
		 			displayText(contest,2);
		 			ChangeGraph(contest,2);
				}catch(Exception e1){}
	 		}				
		} ) ;
		bAdv.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent e )
			{				
				ta.setText("");	//��ʼ���ı���Ϊ��
		 		try
		 		{
		 			displayText(contest,3);
		 			ChangeGraph(contest,3);
				}catch(Exception e1){}
	 		}				
		} ) ;
		bAll.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent e )
			{				
				ta.setText("");	//��ʼ���ı���Ϊ��
		 		try
		 		{
		 			ta.append("����������������������������  ����    ����������������������������\n");
		 			displayText(contest,0);
		 			ta.append("����������������������������  ����    ����������������������������\n");
		 			displayText(contest,1);
		 			ta.append("���������������������������� ���ݴ�   ����������������������������\n");
		 			displayText(contest,2);
		 			ta.append("����������������������������  ����   ����������������������������\n");
		 			displayText(contest,3);
				}catch(Exception e1){}
	 		}				
		} ) ;

//-------------- �����˵� ------------------------
		mFile.add(exitItem) ;//Ϊ���ļ����˵���Ӳ˵���
		mFile.add(saveItem);		
		mHelp.add(aboutItem) ;//Ϊ���������˵���Ӳ˵���				
		mb.add(mFile) ;//�����˵�����˵���
		mb.add(mHistory) ;
		mb.add(mHelp) ;		
		f.setJMenuBar( mb ) ;//Ϊ�������ò˵���
		
		//Ϊ�����˵�������¼�����
		aboutItem.addActionListener( new ActionListener ()
		{
			public void actionPerformed( ActionEvent e )
			{				
				d.setLocationRelativeTo(null);//������������ʾ�Ի���								
				d.setVisible(true) ;//���öԻ���Ϊ�ɼ�
			}
		} ) ;
		
//----------------------------�����ļ��˵�������-------------------------------
		exitItem.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{
				sureExit() ;
			}
		} ) ; 
		
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int Times;
				try{
					 Times = Integer.parseInt(JOptionPane.showInputDialog("������Ŵ�����ֵԽ������ͼ��Խ�������ȸߣ���",1));
				}
				catch(Exception ex){
					Times = 1;
				}
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				    "PNG", "png");
				chooser.setFileFilter(filter);
				chooser.setDialogTitle("������״ͼ");
				chooser.setDialogType(JFileChooser.SAVE_DIALOG);
				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						FileOutputStream FOS = new FileOutputStream(chooser.getSelectedFile() + ".png");
						netplace.getDisplay().saveImage(FOS, "PNG", Times);
						FOS.close();
						JOptionPane.showMessageDialog(null, "����ɹ���");

					} catch (Exception ex) {
						//Nothing to Do
					}
				}
			}
		});

//----------------------------���ð����˵�������-------------------------------
		
		//Ϊ�Ի�������¼�����
		bok.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{				
				d.setVisible(false) ;//���öԻ���Ϊ����
			}
		} ) ;

		//����ı���������
		bta.append( "             WordNetС����ҵ\n\n" ) ;
		bta.append( "               �������\n" ) ;
		
		bta.setEditable(false);
		bok.setBackground(Color.black);
		bta.setBackground(Color.black);
		bta.setForeground(Color.white);
		bok.setForeground(Color.white);
		bp.setBackground(Color.black);
		bp.setForeground(Color.white);		

		//���ı����ȷ�ϰ�ť���붥������
		bp.add(bta) ;
		bp.add(bok) ;		
		bta.setOpaque(false);
		bok.setContentAreaFilled(false);
		
		//��������������Ի���
		d.add(bp) ;
		
		//���öԻ����С
		d.setSize( 200 , 220 ) ;
		
		//��������Ĵ�С
		d.setResizable(false);
		
		//�رմ����¼�����
		d.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				d.dispose();
			}
		});
//---------------------------   �Ҳ���״ͼ����ʼ��      -----------------
		//netplace = new GraphView(null);
		netplace.m_vis.run("layout");
		//netplace = new JPanel();
		//JScrollPane sptree = new JScrollPane( netplace ) ;//���ı���������������������	
		//sptree.setPreferredSize(new Dimension(200,200));
		pTree.add( netplace , BorderLayout.CENTER) ;	
		pTree.setPreferredSize(new Dimension(1000,600));//������ʾ��״ͼ���Ĵ�С
		
		
		
		
//---------------------------    �������͸��      -------------------------------
		ta.setOpaque(false);
		lb.setOpaque(false);
		pBottom.setOpaque(false);
		pTop.setOpaque(false);
		sp.setOpaque(false);
		sp.getViewport().setOpaque(false);
		sp.getVerticalScrollBar().setOpaque(false);
		sp.getHorizontalScrollBar().setOpaque(false);

		ok.setContentAreaFilled(false);
		bNoun.setContentAreaFilled(false);
		bAdj.setContentAreaFilled(false);
		bAdv.setContentAreaFilled(false);
		bVerb.setContentAreaFilled(false);
		bAll.setContentAreaFilled(false);
		
		lb.setForeground(Color.black);
		ok.setForeground(new Color(0, 0, 0));
		ta.setForeground(Color.black);
		
//------------------------    ������ֽ��棬�ұ�ͼ�ν���         -----------------------------

		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, background,pTree);
		mainSplitPane.setDividerLocation(600); //���÷ָ�����λ��
		mainSplitPane.setDividerSize(3); //���÷ָ����Ĵ�ϸ
		
//		horizontal.add(background);
//		horizontal.add(Box.createHorizontalGlue());
//		horizontal.add(pTree);
		f.add(mainSplitPane);
		
//------------------------    �������         -----------------------------		
		//����رհ�ť���˳�
		f.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);		
		//�رմ����¼�����
		f.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				sureExit(); //��رմ��ں����sureExit�ж��Ƿ�ȷ���˳�
			}
		});
		f.getContentPane().setBackground(Color.black);
		mainSplitPane.setBackground(Color.green);
		f.pack() ;//�����ڵ�������Ѵ�С
		f.setLocationRelativeTo(null);//������������ʾ����				
		f.setVisible( true ) ;//��������ʾ����
		//f.setUndecorated(true);
	}
	
//------------------  ��������    ----------------------------------------------------------------
	public void setNewText(String ss){
		ta.setText(ss);
	}
	public void setJTextFile(String s){
		tf.setText(s);
		contest = s;
	}
	public static void main( String [] args )throws IOException//������������init����
	{
		new Frame().init() ;
	}
//------------------    ���������ʾ      -----------------	
	//����contest���ݲ�ѯ������ʾ���
	public void  displayText(String contest,int intcharac)throws IOException
	{
		if(contest == null)
			return;
		 try
		{
			String charac[]={"n","v","adj","adv"}  ;
			String name;
			name=contest;
			DealWithData run = new DealWithData();			
			word tep =	run.getwordaccordname(name,charac[intcharac]);				
				//run.printall(tep);
				
			antonymytext(tep);//��
			hypermymytext(tep);//��
			hyponymytext(tep);//��
			synsetstext(tep);//ͬ
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		
	}
	public void ChangeGraph(String contest,int intcharac) throws IOException{
		if(contest == null)
			return;
		 try
		{
			String charac[]={"n","v","adj","adv"}  ;
			String name;
			name=contest;
			DealWithData run = new DealWithData();			
			word tep =	run.getwordaccordname(name,charac[intcharac]);				
			netplace.setWord(tep);//��ͼ��ֻ�ܻ������е�һ�ִ���
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		
	}
    //��ͬ�������Ч��
	public void  antonymytext(word tep)throws IOException//����ʴ�ӡ
	{
		 try
		{if(0==tep.antonymy.size()) return ;
		    ta.append("�����:\n");
			if (tep.Hasanto()){
				int sz = tep.antonymy.size();
				for (int i = 0;i<sz;++i)
				{
					ta.append("        ");
					synnet retep = tep.antonymy.get(i);
					//System.out.println(retep.type);
					String[] words = retep.GetWords();
					String out = "";
					for (int j = 0;j<words.length;++j)
						out += " " + words[j];
					out += "   <����>:"+tep.antonymy.get(i).gloss;
					ta.append(out+"\n");
				}	
			}			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		
	}
	public void  hypermymytext(word tep)throws IOException//����ʴ�ӡ
	{
		 try
		{	
			
			if(0==tep.hypermymy.size()) return ;
			ta.append("�����:\n");
			if (tep.Hashyper()){
				int sz = tep.hypermymy.size();
				for (int i = 0;i<sz;++i)
				{	
					ta.append("        ");
					synnet retep = tep.hypermymy.get(i);
					//System.out.println(retep.type);
					String[] words = retep.GetWords();
					String out = "";
					for (int j = 0;j<words.length;++j)
						out += " " + words[j];
					out += "   <����>:"+tep.hypermymy.get(i).gloss;
					ta.append(out+"\n");
				}	
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		
	}
	public void  hyponymytext(word tep)throws IOException//����ʴ�ӡ
	{
		 try
		{
			if(0==tep.hyponymy.size()) return ;
			ta.append("�����:\n");
			if (tep.Hashyponymy()){
				int sz = tep.hyponymy.size();
				for (int i = 0;i<sz;++i)
				{
					ta.append("        ");
					synnet retep = tep.hyponymy.get(i);
					//System.out.println(retep.type);
					String[] words = retep.GetWords();
					String out = "";
					for (int j = 0;j<words.length;++j)
						out +=" " + words[j];
					out += "   <����>:"+tep.hyponymy.get(i).gloss;
					ta.append(out+"\n");
				}	
			}
					}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		
	}
	public void  synsetstext(word tep)throws IOException//ͬ��ʴ�ӡ
	{
		 try
		{	
			 if(0==tep.synsets.size()) return ;
			ta.append("ͬ���:\n");
			if (tep.Hassyn()){
				int sz = tep.synsets.size();
				for (int i = 0;i<sz;++i)
				{	ta.append("        ");
					String retep = tep.synsets.get(i);
					ta.append(retep+"\n");
				}	
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		
	}
	//���ж��Ƿ�Ҫ�˳�
	private void sureExit()
	{
		  int result = JOptionPane.showConfirmDialog(f, "ȷ��Ҫ�˳���", "�˳�", JOptionPane.YES_NO_OPTION);
		  if(result == JOptionPane.NO_OPTION) //��"��"��ֱ�ӷ���
		  {
			  return;
		  }
		  else //��"��"���˳�
		  {
			  System.exit(0);
		  }

	}

}
