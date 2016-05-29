import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.GroupAction;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataColorAction;
import prefuse.action.filter.VisibilityFilter;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.expression.AbstractPredicate;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;

public class GraphView extends JPanel{
	//����Prefuse�е��ֶ�����
	private static final String graph = "graph";
	private static final String nodes = "graph.nodes";
	private static final String edges = "graph.edges";
	private static final String label1 = "name";
	private static final String label2 = "id";
	//Relationship��ʾ���ֹ�ϵ����ʾ���
	private boolean[] Relationship = {true, true, true, true, true};
	
	//��ʾ���ֹ�ϵ������
	private String[] RelationshipName = {"�����", "�����", "�����", "ͬ���"};
	
	//������ʾ����
	private Graph g;
	private Display display;
	protected Visualization m_vis = new Visualization();
	
	//������չ���ʲ�ѯ
	private DealWithData SearchWord = new DealWithData();
	
	//����Ĭ����ɫ
	private final int[] FillColor = new int[] {
		    ColorLib.rgba(242, 242, 242, 127), ColorLib.rgba(20, 162, 212, 127), 
		    ColorLib.rgba(0, 177, 106, 127), ColorLib.rgba(242, 94, 61, 127), 
		    ColorLib.rgba(242, 246, 252, 127)};
	
	//����ά����߹�ϵ
	private HashMap<String,Node> OriginNode = new HashMap<String,Node>();
	private HashMap<String,Node> AntonymyNode = new HashMap<String,Node>();
	private HashMap<String,Node> HypermymyNode = new HashMap<String,Node>();
	private HashMap<String,Node> HyponymyNode = new HashMap<String,Node>();
	private HashMap<String,Node> SynsetsNode = new HashMap<String,Node>();
	
	//��¼��ǰ��ѯ�Ĵ�����Ϣ
	private String Parts;
	
	private Frame frame;
	//Ĭ�Ϲ��캯��
	public GraphView(Frame f){
		super(new BorderLayout());
		
		frame = f;
		//�����ǩ�������������ó�Բ�ǣ�ˮƽ�߽�5px
		LabelRenderer tr = new LabelRenderer();
		tr.setRoundedCorner(8, 8);
		tr.setHorizontalPadding(5);	
		
		//����������������ñ�����ΪԲ�ߣ����Ϊ1.5
		EdgeRenderer eg = new EdgeRenderer();
		eg.setEdgeType(1);
		eg.setDefaultLineWidth(1.5f);
		
		//����Visualization��������
		m_vis.setRendererFactory(new DefaultRendererFactory(tr, eg));
		
		
		//Ĭ��ʹ��wordnet��Ϊ��ѯ��
		setWord(SearchWord.getwordaccordname("wordnet", "n"));
		
		//������ͣ�����¼�
		TupleSet focusGroup = m_vis.getGroup(Visualization.FOCUS_ITEMS);
		focusGroup.addTupleSetListener(new TupleSetListener() {
			public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem) {
				for (int i = 0; i < rem.length; ++i) {
					((VisualItem) rem[i]).setFixed(false);
				}
				for (int i = 0; i < add.length; ++i) {
					((VisualItem) add[i]).setFixed(false);
					((VisualItem) add[i]).setFixed(true);
				}
				if (ts.getTupleCount() == 0) {
					ts.addTuple(rem[0]);
					((VisualItem) rem[0]).setFixed(false);
				}
				m_vis.run("draw");
			}
		});
		
		//����ͼ��ߵ������
		VisibilityFilter vsFilter = new VisibilityFilter(graph, new RelationsPredicate());
		
		//��ɫ�¼�
		DataColorAction fill = new DataColorAction(nodes, "id", Constants.NOMINAL, VisualItem.FILLCOLOR, FillColor);
		fill.add(VisualItem.FIXED, ColorLib.rgba(255, 100, 100, 240));
		fill.add(VisualItem.HIGHLIGHT, ColorLib.rgba(255, 200, 125, 240));

		ActionList draw = new ActionList();
		draw.add(vsFilter);
		//draw.add(fill);
		draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, ColorLib.gray(175)));
		draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0, 0, 0)));
		draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.rgba(0, 0, 0, 15)));
		draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, ColorLib.gray(150)));
		
		//�����¼�
		RadialTreeLayout rtl = new RadialTreeLayout(graph);
		//rtl.setAutoScale(true);
		//rtl.setAngularBounds(0, 1);
		draw.add(rtl);
		
		//����ת�¼�
		ActionList treeRoot = new ActionList();
		treeRoot.add(new TreeRootAction(graph));
		
		//˫���¼�
		ActionList doubleClicked = new ActionList();
		doubleClicked.add(new DoubleClicked(graph));
		
		//�ػ��¼�
		ActionList animate = new ActionList(Activity.INFINITY);
		animate.add(new RepaintAction());
		animate.add(fill);
		
		//��m_vis����¼�
		m_vis.putAction("draw", draw);
		m_vis.putAction("layout", animate);
		m_vis.putAction("treeRoot", treeRoot);
		m_vis.putAction("DoubleClicked", doubleClicked);
		m_vis.runAfter("draw", "layout");
		
		//����Display��ʾ����
		display = new Display(m_vis);
		display.setHighQuality(true);
		display.setDamageRedraw(true);
		display.setSize(500, 700);
		display.pan(250,350);
		display.setBackground(Color.GRAY);
		display.setForeground(Color.WHITE);
		
		//����Display����¼�
		display.addControlListener(new FocusControl(1, "treeRoot"));
		display.addControlListener(new FocusControl(2, "DoubleClicked"));
		display.addControlListener(new DragControl());
		display.addControlListener(new PanControl());
		display.addControlListener(new ZoomControl());
		display.addControlListener(new WheelZoomControl());
		display.addControlListener(new ZoomToFitControl());
		display.addControlListener(new NeighborHighlightControl());
		
		//���л�ͼ����
		m_vis.run("draw");
		
		//����JSplitPane�������Ϊ����������
		JSplitPane split = new JSplitPane();
		split.setResizeWeight(1);
		split.setMinimumSize(new Dimension(700,500));
		split.setContinuousLayout(true);
		split.setOneTouchExpandable(true);
		split.setLeftComponent(display);
		JPanel right = new JPanel();
		right.setLayout(new GridLayout(5, 1, 0, 0));
		for (int i = 1; i < 5; i++) {
			Checkbox ckbox = new Checkbox();
			ckbox.setLabel(RelationshipName[i - 1]);
			ckbox.setState(true);
			final int finalI = i;
			ckbox.setBackground(new Color(FillColor[i],true));
			ckbox.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent itemEvent) {
					Checkbox tmp = (Checkbox) itemEvent.getSource();
					Relationship[finalI] = tmp.getState();
					m_vis.run("draw");
					m_vis.cancel("layout");
					m_vis.run("layout");
				}
			});
			right.add(ckbox);
		}
		JButton save = new JButton("Save to File");
		save.setContentAreaFilled(false);
		save.addActionListener(new ActionListener() {
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
						display.saveImage(FOS, "PNG", Times);
						FOS.close();
						JOptionPane.showMessageDialog(null, "����ɹ���");

					} catch (Exception ex) {
						//Nothing to Do
					}
				}
			}
		});
		right.add(save);
		split.setRightComponent(right);
		add(split);
		addComponentListener(new ComponentListener(){
			@Override
			public void componentHidden(ComponentEvent e){
				m_vis.cancel("layout");
			}
			public void componentMoved(ComponentEvent e){
				m_vis.run("draw");
				m_vis.cancel("layout");
				m_vis.run("layout");
			}
			public void componentResized(ComponentEvent e){
				m_vis.run("draw");
				m_vis.cancel("layout");
				m_vis.run("layout");				
			}
			public void componentShown(ComponentEvent e){
				m_vis.run("draw");
				m_vis.run("layout");				
			}
		});
	}
	
	private void initGraph(){
		// update labeling
		DefaultRendererFactory drf =
		    (DefaultRendererFactory)m_vis.getRendererFactory();
		((LabelRenderer)drf.getDefaultRenderer()).setTextField(label1);

		// update graph
		m_vis.removeGroup(graph);
		VisualGraph vg = m_vis.addGraph(graph, g);
		m_vis.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.FALSE);
		if (vg.getNodeCount() != 0) {
			VisualItem f = (VisualItem) vg.getNode(0);
			m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);
			f.setFixed(false);
		}
	}
		
	public Display getDisplay(){
    	return display;
    }
		
	private Node getFromMap(String s,int n){
		switch(n){
		case 0:return OriginNode.get(s); 
		case 1:return AntonymyNode.get(s); 
		case 2:return HypermymyNode.get(s); 
		case 3:return HyponymyNode.get(s);
		case 4:return SynsetsNode.get(s); 
		}
		return null;
	}
	
	private void putToMap(String s,int n,Node nd){
		switch(n){
		case 0:OriginNode.put(s,nd); return;
		case 1:AntonymyNode.put(s,nd); return;
		case 2:HypermymyNode.put(s,nd); return;
		case 3:HyponymyNode.put(s,nd);return;
		case 4:SynsetsNode.put(s,nd); return;
		}
	}
	
	private void removeFromMap(String s,int n){
		switch(n){
		case 0:OriginNode.remove(s); return;
		case 1:AntonymyNode.remove(s); return;
		case 2:HypermymyNode.remove(s); return;
		case 3:HyponymyNode.remove(s);return;
		case 4:SynsetsNode.remove(s); return;
		}
	}
	
	private void clearAllMap(){
		OriginNode.clear();
		AntonymyNode.clear();
		HypermymyNode.clear();
		HyponymyNode.clear();
		SynsetsNode.clear();
	}

	public void setWord(word wd){
		//�ÿ�ԭ�е�Node�����ͼ
		Parts = wd.GetType();
		clearAllMap();
		g = new Graph();
		g.addColumn(label1, String.class);
		g.addColumn(label2,int.class);
		addWord(wd,0);
		initGraph();
		m_vis.cancel("layout");
		m_vis.run("draw");
		m_vis.run("layout");
	}
	
	private void addWord(word wd,int n){
		//�Ȳ�ѯ�Ƿ���ڣ�������ڣ����ٽ�����Ӳ���
		Tuple root;
		if(n == 0){
			Node tmp = getFromMap(wd.Getname(),0);
			if(tmp != null){
				return;
			}
			else{
				root = (Tuple)g.addNode();
				root.setString(label1, wd.Getname());
				root.setInt(label2, 0);
				putToMap(wd.Getname(), 0, (Node)root);
			}
		}
		else{
			root = (Tuple)getFromMap(wd.Getname(),0);
			if(root != null){
				return;
			}
			try{
				root = (Tuple)getFromMap(wd.Getname(),n);
				removeFromMap(wd.Getname(),n);
				root.setInt(label2, 0);
				putToMap(wd.Getname(),0,(Node)root);
			}
			catch(Exception e){
				root = null;
			}
		}
		
		//���ʲ����ڽ�����Ӳ���������������������ȡ��ת��Ϊ����
		synnet[] antonymy = wd.antonymy.toArray(new synnet[1]);
		synnet[] hypermymy = wd.hypermymy.toArray(new synnet[1]);
		synnet[] hyponymy = wd.hyponymy.toArray(new synnet[1]);
		String[] synsets = wd.synsets.toArray(new String[1]);



		for (int i = 0; i < antonymy.length; ++i) {
			if (antonymy[i] != null)
				for(int j = 0; j < antonymy[i].GetWords().length;++j){
					Tuple tmpNode = (Tuple)getFromMap(antonymy[i].GetWords()[j],1);
					if(tmpNode == null){
						tmpNode = (Tuple)g.addNode();
						tmpNode.setString(label1, antonymy[i].GetWords()[j]);
						tmpNode.setInt(label2, 1);
						putToMap(antonymy[i].GetWords()[j],1,(Node)tmpNode);
					}
					try{
						g.addEdge((Node)root, (Node)tmpNode);
					}
					catch(Exception e){
						
					}
				}
		}

		for (int i = 0; i < hypermymy.length; ++i) {
			if (hypermymy[i] != null)
			for(int j = 0; j < hypermymy[i].GetWords().length;++j){
				Tuple tmpNode = (Tuple)getFromMap(hypermymy[i].GetWords()[j],2);
				if(tmpNode == null){
					tmpNode = (Tuple)g.addNode();
					tmpNode.setString(label1, hypermymy[i].GetWords()[j]);
					tmpNode.setInt(label2, 2);
					putToMap(hypermymy[i].GetWords()[j],2,(Node)tmpNode);
				}
				try{
					g.addEdge((Node)root, (Node)tmpNode);
				}
				catch(Exception e){
					
				}
			}
		}

		for (int i = 0; i < hyponymy.length; ++i) {
			if (hyponymy[i] != null)
			for(int j = 0; j < hyponymy[i].GetWords().length;++j){
				Tuple tmpNode = (Tuple)getFromMap(hyponymy[i].GetWords()[j],3);
				if(tmpNode == null){
					tmpNode = (Tuple)g.addNode();
					tmpNode.setString(label1, hyponymy[i].GetWords()[j]);
					tmpNode.setInt(label2, 3);
					putToMap(hyponymy[i].GetWords()[j],3,(Node)tmpNode);
				}
				try{
					g.addEdge((Node)root, (Node)tmpNode);
				}
				catch(Exception e){
					
				}
			}
		}

		for (int i = 0; i < synsets.length; ++i) {
			if (synsets[i] == null || synsets[i] == "")
				continue;
			Tuple tmpNode = (Tuple)getFromMap(synsets[i],4);
			if(tmpNode == null){
				tmpNode = (Tuple)g.addNode();
				tmpNode.setString(label1, synsets[i]);
				tmpNode.setInt(label2, 4);
				putToMap(synsets[i],4,(Node)tmpNode);
			}
			try{
				g.addEdge((Node)root, (Node)tmpNode);
			}
			catch(Exception e){
				
			}
		}
	}
	private int WordParts(){
		if(Parts.equals("n"))
			return 0;
		if(Parts.equals("v"))
			return 1;
		if(Parts.equals("adj"))
			return 2;
		if(Parts.equals("adv"))
			return 3;
		return -1;
	}
	private class TreeRootAction extends GroupAction {
		public TreeRootAction(String graphGroup) {
			super(graphGroup);
		}
		public void run(double frac) {
			TupleSet focus = m_vis.getGroup(Visualization.FOCUS_ITEMS);
			if ( focus == null || focus.getTupleCount() == 0 ) return;

			Graph g = (Graph)m_vis.getGroup(m_group);
			Node f = null;
			Iterator tuples = focus.tuples();
			while (tuples.hasNext() && !g.containsTuple(f = (Node)tuples.next())) {
				f = null;
			}
			if ( f == null ) return;
			g.getSpanningTree(f);
			frame.setNewText("");
			try{
				frame.displayText(((Tuple)f).getString(label1),WordParts());
				frame.setJTextFile(((Tuple)f).getString(label1));
			}
			catch(Exception e){
				frame.setJTextFile("");
			}
			((VisualItem)f).setFixed(false);
			m_vis.run("draw");
			m_vis.cancel("layout");
			m_vis.run("layout");
		}
	}
	
	private class DoubleClicked extends GroupAction {
		public DoubleClicked(String graphGroup) {
			super(graphGroup);
		}
		public void run(double frac) {
			TupleSet focus = m_vis.getGroup(Visualization.FOCUS_ITEMS);
			if ( focus == null || focus.getTupleCount() == 0 ) return;

			Graph g = (Graph)m_vis.getGroup(m_group);
			Node f = null;
			Iterator tuples = focus.tuples();
			while (tuples.hasNext() && !g.containsTuple(f = (Node)tuples.next())) {
				f = null;
			}
			if ( f == null ) return;
			addWord(SearchWord.getwordaccordname(((Tuple)f).getString(label1), Parts),((Tuple)f).getInt(label2));
			//g.getSpanningTree(f);
			frame.setNewText("");
			try{
				frame.displayText(((Tuple)f).getString(label1),WordParts());
				frame.setJTextFile(((Tuple)f).getString(label1));
			}
			catch(Exception e){
				frame.setJTextFile("");
			}
			((VisualItem)f).setFixed(false);
			m_vis.run("draw");
			m_vis.cancel("layout");
			m_vis.run("layout");
		}
	}
	
	private class RelationsPredicate extends AbstractPredicate {
		@Override
		public Class getType(Schema schema) {
			return Boolean.TYPE;
		}

		@Override
		public boolean getBoolean(Tuple t) {
			InGroupPredicate inNodes = new InGroupPredicate(nodes);
			if (inNodes.getBoolean(t)) {
				((VisualItem)t).setExpanded(Relationship[t.getInt("id")]);
				return Relationship[t.getInt("id")];
			} else {
				Edge tmp = (Edge)t;
				((VisualItem)t).setExpanded(Relationship[tmp.getSourceNode().getInt("id")] && Relationship[tmp.getTargetNode().getInt("id")]);
				return Relationship[tmp.getSourceNode().getInt("id")] && Relationship[tmp.getTargetNode().getInt("id")];
			}
		}
	}
} // end of class GraphView