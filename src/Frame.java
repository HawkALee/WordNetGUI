/* 程序的图形界面，各部分的连接和监听器
 * 姓名：孙唯童
 * 编辑时间 2/06/2015
 * */

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;

public class Frame
{
//----------------------   程序主窗口及顶级容器  -----------------------
	private JFrame f = new JFrame( "WordNet" ) ;
	private JSplitPane  mainSplitPane;//分割出
	//顶部、左侧两个顶级容器
	private JPanel pTop = new JPanel() ;    //左顶文字输入栏
	private JPanel pBottom = new JPanel() ;	//左底按钮
	private JPanel pTree = new JPanel() ;	//树状图
    private GraphView netplace; //##显示树的类，由  GraphView 实现
	public JPanel background = new JPanel();//绘制左侧窗口背景的Panel，暂未设置背景
	private String contest ;//输入到文本框的内容

	//-----------------------  左半文字及输入部分	  -----------------------	
	private JLabel  lb = new JLabel( "输入单词:" ) ; //提示输入标签	
	private JTextField tf = new JTextField( 15 ) ;//输入单词文本框	
	private JButton ok = new JButton( "查询" ) ;   //确认按钮	
	private JTextArea ta = new JTextArea( 16,20 );//一个文本域，用于显示文字结果
	                                              //四个词性选项按钮
	private JButton bNoun = new JButton( "名词" ) ;
	private JButton bAdv = new JButton( "副词" ) ;
	private JButton bAdj = new JButton( "形容词" ) ;
	private JButton bVerb = new JButton( "动词" );
	private JButton bAll = new JButton("全部");
	
//----------------------   顶部菜单栏    ------------------------------
	private JMenuBar mb = new JMenuBar() ;		// 设置主菜单：   文件、历史、帮助
	private JMenu mFile = new JMenu( "文件" ) ;
	private JMenu mHistory = new JMenu( "历史" ) ;
	private JMenu mHelp = new JMenu( "帮助" ) ;	
	
	//文件菜单——”注销“，”退出“菜单项	
	private JMenuItem saveItem = new JMenuItem( "保存树状图" ) ;
	private JMenuItem exitItem = new JMenuItem( "退出" ) ;
	
	//历史菜单
	private HashSet<JMenuItem> historyList = new HashSet<JMenuItem>();
	
	//帮助——菜单中”说明文档“菜单项
	private JMenuItem aboutItem = new JMenuItem( "说明文档" ) ;
	
//---------------    说明文档弹出框     ----------------------------------------------
	private JFrame d = new JFrame("说明文档" ) ;	//框	
	private JPanel bp = new JPanel() ;//顶级容器
	private JTextArea bta = new JTextArea( 8 , 15 ) ;//文本域		
	private JButton bok = new JButton( "确定" ) ;//确定按钮

	
	//###################################   初始化方法       ################################
	public void init()throws IOException   //将各部件添加到所属位置并提供相关监听
	{
		background.setLayout(new BorderLayout());
		background.setBackground(Color.gray);
		pTree.setLayout(new BorderLayout());
		ReadFile rf = new ReadFile();
		DealWithData run = new DealWithData();//处理数据的类## DealWithData
		run.Start();//将数据写入
		
		//初始化GraphView
		netplace = new GraphView(this);
//--------------------------------   左侧顶部的输入部件及监听器  ----------------------------		
		//左侧顶部容器依次装入提示标签、单词文本框和确认按钮
		pTop.add( lb ) ;
		pTop.add( tf ) ;
		pTop.add( ok ) ;		
		//创建Action监听器实例传递文本框信息
		Action sendMSG = new AbstractAction() 
		{
			private static final long serialVersionUID = 5765576602055012931L;
			//√用于添加到历史记录
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e)
			{								
				ta.setText("");	//初始化文本域为空	
				Font ziti = new Font("楷体",Font.PLAIN,20);
				ta.setFont(ziti);  
				ta.setForeground(Color.white);
		 		contest = tf.getText(); //得到输入的单词		 			
		 		if (contest.equals("")) return; //如果输入为空，直接返回		 			
				contest = contest.toLowerCase(); //大写变小写				
				boolean flag = true;
				int count = 0; //统计当前有几个Item
				Iterator<JMenuItem> i = historyList.iterator();
				while (i.hasNext())//循环查看是否有重复
				{
					count ++;
					JMenuItem m = i.next();
					if (m.getLabel().equals(contest)) //如果已经有纪录，不再保存
					{
						flag = false;
						break;
					}
				}				
				if (flag == true) //如果没有纪录
				{
					final JMenuItem m = new JMenuItem(contest);
					historyList.add(m); //存入历史集合
					
					if (count == 5) //如果已经到达最大数目，移除第5个，插到第0个位置
					{
						mHistory.remove(4);
						mHistory.insert(m, 0);
					}
					else //否则直接插入0位置
					{
						mHistory.insert(m, 0);
					}
					
					//添加mHistory菜单列表事件监听
					m.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							tf.setText(m.getLabel()); //显示在文本框中
							ta.setText("");//清空结果文字
							try{
								displayText(contest,0);//````````````````````显示查询结果
								ChangeGraph(contest,0);
								}catch (Exception a)
								{
									a.printStackTrace();
								}
						}
					});
				}
				
				//########################################显示查询结果，在这里添加查询动作
				try{
				displayText(contest,0);//````````````````````显示查询结果
				ChangeGraph(contest,0);
				}catch (Exception b)
				{
					//e.printStackTrace();
				}	
			}
		} ;
		
		//添加单击“确定”按钮的事件处理
		ok.addActionListener( sendMSG ) ;		
		//把回车键和“确定”按钮关联起来
		tf.getInputMap().put( KeyStroke.getKeyStroke( '\n' ) , "send" ) ;
		tf.getActionMap().put( "send" , sendMSG ) ;		
		
		//将输入容器装入窗口顶部
		background.add( pTop , BorderLayout.NORTH ) ;
//-------------------------------     文本域         --------------------------------------
				
		ta.setLineWrap(true);
		ta.setEditable(false);                  //文本域不允许编辑			
		JScrollPane sp = new JScrollPane( ta ) ;//将文本域放入带滚动条的容器中	
		background.add( sp , BorderLayout.CENTER) ;	
//-------------------------------    左侧下部按钮   -------------------------------------
		//左侧下部容器用BoxLayout布局管理器纵向排列
		pBottom.setLayout( new GridLayout( 1 , 5 , 0 , 0 ) ) ;
		
		//依次加入左侧下排五个按钮
		pBottom.add( bNoun ) ;
		pBottom.add( bVerb ) ;
		pBottom.add( bAdj ) ;
		pBottom.add( bAdv ) ;				
		pBottom.add( bAll );//加入节点树状图打开按钮

		//为打开树形界面提供事件监听
		bAll.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		
		//将pBottom放在窗口左侧，pRight放在窗口右侧
		background.add( pBottom , BorderLayout.SOUTH ) ;


//***--------------------   监听器    ------------------------------	
		//添加点击bNoun按钮后的事件处理
		bNoun.addActionListener( new ActionListener() 
		{
			public void actionPerformed( ActionEvent e )
			{				
				ta.setText("");	//初始化文本域为空
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
				ta.setText("");	//初始化文本域为空
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
				ta.setText("");	//初始化文本域为空
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
				ta.setText("");	//初始化文本域为空
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
				ta.setText("");	//初始化文本域为空
		 		try
		 		{
		 			ta.append("——————————————  名词    ——————————————\n");
		 			displayText(contest,0);
		 			ta.append("——————————————  动词    ——————————————\n");
		 			displayText(contest,1);
		 			ta.append("—————————————— 形容词   ——————————————\n");
		 			displayText(contest,2);
		 			ta.append("——————————————  副词   ——————————————\n");
		 			displayText(contest,3);
				}catch(Exception e1){}
	 		}				
		} ) ;

//-------------- 顶部菜单 ------------------------
		mFile.add(exitItem) ;//为”文件“菜单添加菜单项
		mFile.add(saveItem);		
		mHelp.add(aboutItem) ;//为”帮助“菜单添加菜单项				
		mb.add(mFile) ;//将各菜单加入菜单栏
		mb.add(mHistory) ;
		mb.add(mHelp) ;		
		f.setJMenuBar( mb ) ;//为窗口设置菜单栏
		
		//为帮助菜单项添加事件处理
		aboutItem.addActionListener( new ActionListener ()
		{
			public void actionPerformed( ActionEvent e )
			{				
				d.setLocationRelativeTo(null);//在桌面中心显示对话框								
				d.setVisible(true) ;//设置对话框为可见
			}
		} ) ;
		
//----------------------------设置文件菜单弹出框-------------------------------
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
					 Times = Integer.parseInt(JOptionPane.showInputDialog("请输入放大倍数（值越大，生成图像越大，清晰度高）：",1));
				}
				catch(Exception ex){
					Times = 1;
				}
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
				    "PNG", "png");
				chooser.setFileFilter(filter);
				chooser.setDialogTitle("保存树状图");
				chooser.setDialogType(JFileChooser.SAVE_DIALOG);
				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						FileOutputStream FOS = new FileOutputStream(chooser.getSelectedFile() + ".png");
						netplace.getDisplay().saveImage(FOS, "PNG", Times);
						FOS.close();
						JOptionPane.showMessageDialog(null, "保存成功！");

					} catch (Exception ex) {
						//Nothing to Do
					}
				}
			}
		});

//----------------------------设置帮助菜单弹出框-------------------------------
		
		//为对话框添加事件处理
		bok.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent e )
			{				
				d.setVisible(false) ;//设置对话框为不见
			}
		} ) ;

		//添加文本框里内容
		bta.append( "             WordNet小组作业\n\n" ) ;
		bta.append( "               火神等人\n" ) ;
		
		bta.setEditable(false);
		bok.setBackground(Color.black);
		bta.setBackground(Color.black);
		bta.setForeground(Color.white);
		bok.setForeground(Color.white);
		bp.setBackground(Color.black);
		bp.setForeground(Color.white);		

		//将文本框和确认按钮加入顶级容器
		bp.add(bta) ;
		bp.add(bok) ;		
		bta.setOpaque(false);
		bok.setContentAreaFilled(false);
		
		//将顶级容器加入对话框
		d.add(bp) ;
		
		//设置对话框大小
		d.setSize( 200 , 220 ) ;
		
		//不允许更改大小
		d.setResizable(false);
		
		//关闭窗口事件监听
		d.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				d.dispose();
			}
		});
//---------------------------   右侧树状图区初始化      -----------------
		//netplace = new GraphView(null);
		netplace.m_vis.run("layout");
		//netplace = new JPanel();
		//JScrollPane sptree = new JScrollPane( netplace ) ;//将文本域放入带滚动条的容器中	
		//sptree.setPreferredSize(new Dimension(200,200));
		pTree.add( netplace , BorderLayout.CENTER) ;	
		pTree.setPreferredSize(new Dimension(1000,600));//设置显示树状图区的大小
		
		
		
		
//---------------------------    设置组件透明      -------------------------------
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
		
//------------------------    左边文字界面，右边图形界面         -----------------------------

		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, background,pTree);
		mainSplitPane.setDividerLocation(600); //设置分隔条的位置
		mainSplitPane.setDividerSize(3); //设置分隔条的粗细
		
//		horizontal.add(background);
//		horizontal.add(Box.createHorizontalGlue());
//		horizontal.add(pTree);
		f.add(mainSplitPane);
		
//------------------------    窗口情况         -----------------------------		
		//点击关闭按钮后退出
		f.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);		
		//关闭窗口事件监听
		f.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				sureExit(); //点关闭窗口后进入sureExit判断是否确定退出
			}
		});
		f.getContentPane().setBackground(Color.black);
		mainSplitPane.setBackground(Color.green);
		f.pack() ;//将窗口调整到最佳大小
		f.setLocationRelativeTo(null);//在桌面中心显示窗口				
		f.setVisible( true ) ;//将窗口显示出来
		//f.setUndecorated(true);
	}
	
//------------------  其他函数    ----------------------------------------------------------------
	public void setNewText(String ss){
		ta.setText(ss);
	}
	public void setJTextFile(String s){
		tf.setText(s);
		contest = s;
	}
	public static void main( String [] args )throws IOException//主函数，运行init（）
	{
		new Frame().init() ;
	}
//------------------    文字输出显示      -----------------	
	//根据contest内容查询，并显示结果
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
				
			antonymytext(tep);//反
			hypermymytext(tep);//上
			hyponymytext(tep);//下
			synsetstext(tep);//同
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
			netplace.setWord(tep);//画图，只能画出其中的一种词性
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		
	}
    //不同词义输出效果
	public void  antonymytext(word tep)throws IOException//反义词打印
	{
		 try
		{if(0==tep.antonymy.size()) return ;
		    ta.append("反义词:\n");
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
					out += "   <释义>:"+tep.antonymy.get(i).gloss;
					ta.append(out+"\n");
				}	
			}			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		
	}
	public void  hypermymytext(word tep)throws IOException//上义词打印
	{
		 try
		{	
			
			if(0==tep.hypermymy.size()) return ;
			ta.append("上义词:\n");
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
					out += "   <释义>:"+tep.hypermymy.get(i).gloss;
					ta.append(out+"\n");
				}	
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		
	}
	public void  hyponymytext(word tep)throws IOException//下义词打印
	{
		 try
		{
			if(0==tep.hyponymy.size()) return ;
			ta.append("下义词:\n");
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
					out += "   <释义>:"+tep.hyponymy.get(i).gloss;
					ta.append(out+"\n");
				}	
			}
					}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
		
	}
	public void  synsetstext(word tep)throws IOException//同义词打印
	{
		 try
		{	
			 if(0==tep.synsets.size()) return ;
			ta.append("同义词:\n");
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
	//√判断是否要退出
	private void sureExit()
	{
		  int result = JOptionPane.showConfirmDialog(f, "确定要退出吗？", "退出", JOptionPane.YES_NO_OPTION);
		  if(result == JOptionPane.NO_OPTION) //点"否"，直接返回
		  {
			  return;
		  }
		  else //点"是"，退出
		  {
			  System.exit(0);
		  }

	}

}
