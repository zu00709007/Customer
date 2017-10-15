import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.CSVReader;
 
@SuppressWarnings("serial")
public class MAINWINDOW extends JFrame implements ActionListener
{ 
	public static void main(String[] args) throws Exception 
	{
		MAINWINDOW main = new MAINWINDOW();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setIconImage(ImageIO.read(MAINWINDOW.class.getResource("/img/logo.png")));
		main.pack();
		main.setVisible(true);
	}
	
	private int limit = 5;
	private boolean value_money = true;
	private JButton value_money_btn, decoding_btn;
	private File[] files = null;
	private PlaceholderTextField count;
	private JLabel copyright;
	private class node
	{
		public int value = 1;
		public int money = 0;
	}
	
    public MAINWINDOW()
    { 	  	
    	super("CEO小幫手");
        JButton button;
	    setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    c.weightx = 1;
	    c.weighty = 1;
	    c.fill = GridBagConstraints.BOTH;
	    c.ipady = 10;
	    c.ipadx = 15;

	    button = new JButton("選取檔案");	 
	    button.setFocusPainted(false);  
	    button.setActionCommand("1");
        button.addActionListener(this);
	    c.gridx = 0;
	    c.gridy = 0;
	    add(button, c);
	    
	    button = new JButton("開始計算");
	    button.setFocusPainted(false);  
	    button.setActionCommand("2");
        button.addActionListener(this);
	    c.gridx = 1;
	    c.gridy = 0;
	    add(button, c);	
	    
	    decoding_btn = new JButton("UTF-8");
	    decoding_btn.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
	    decoding_btn.setFocusPainted(false);  
	    decoding_btn.setActionCommand("4");
	    decoding_btn.addActionListener(this);
	    c.gridx = 0;
	    c.gridy = 1;
	    c.gridwidth = 2;
	    add(decoding_btn, c);	
	    
	    value_money_btn = new JButton("消費總次數排序");
	    value_money_btn.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
	    value_money_btn.setForeground(Color.BLUE);
	    value_money_btn.setFocusPainted(false);  
	    value_money_btn.setActionCommand("3");
	    value_money_btn.addActionListener(this);
	    c.gridx = 0;
	    c.gridy = 2;	    
	    add(value_money_btn, c);	 
	    
	    count = new PlaceholderTextField();
	    count.setHorizontalAlignment(SwingConstants.CENTER);
	    count.setOpaque(false);
	    count.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
	    count.setForeground(Color.BLACK);
	    count.setBorder(null); 
	    count.setPlaceholder("預設購買次數為 5次");
	    count.getDocument().addDocumentListener(new DocumentListener() 
	    {
			public void changedUpdate(DocumentEvent arg0){check();}
			public void insertUpdate(DocumentEvent arg0){check();}				
			public void removeUpdate(DocumentEvent arg0){check();}			
			public void check()
			{
				if(count.getText().length() == 0)
				{
					if(value_money)
						limit = 5;
					else
						limit = 500;
				}
				try 
				{
					if(Integer.parseInt(count.getText()) < 0)
						count.setForeground(Color.RED);
					else
					{
						limit = Integer.parseInt(count.getText());
						count.setForeground(Color.BLACK);
					}
				} 
				catch(Exception e) 
				{
					count.setForeground(Color.RED);
				}
			}
	    });
	    c.gridx = 0;
	    c.gridy = 3; 
	    c.gridwidth = 2;
	    c.ipady = 16;
	    add(count, c);
	    
	    button = new JButton("");
	    //button.setFocusPainted(false);  
	    c.gridx = 0;
	    c.gridy = 3;
	    add(button, c);	
	    
	    copyright = new JLabel("Copyright © 2017 JiYen. All rights reserved");
	    copyright.setHorizontalAlignment(SwingConstants.CENTER);
	    c.gridx = 0;
	    c.gridy = 4; 
	    c.gridwidth = 2;
	    add(copyright, c);
    }
    
	public void actionPerformed(ActionEvent e) 
	{
        String cmd = e.getActionCommand();
        if(cmd == "1") 
        {            
            JFileChooser choose = new JFileChooser(".");  
            choose.setAcceptAllFileFilterUsed(false);
            choose.addChoosableFileFilter(new FileNameExtensionFilter("CSV檔案", "csv"));
            choose.setMultiSelectionEnabled(true);
            if(JFileChooser.APPROVE_OPTION == choose.showOpenDialog(this))
            {
            	files = choose.getSelectedFiles();
            	copyright.setText("已選取" + files.length + "個檔案");
            }
        }
        if(cmd == "2")
        {
        	if(null != files && files.length > 0)
            { 
        		node a;
        		CSVReader reader;
        		Map<String, node> tm = new TreeMap<String, node>();
            	for(int i=0; i<files.length; ++i)
            	{            		
					try 
					{
						reader = new CSVReader(new InputStreamReader(new FileInputStream(files[i]), decoding_btn.getText()));
	            		String [] oneline;
	            		reader.readNext();
	            		while ((oneline = reader.readNext()) != null) 
	            		{
	            		    if(tm.containsKey(oneline[3]))
	            		    {
	            		    	a = tm.get(oneline[3]);
	            		    	a.money += Math.round(Float.parseFloat(oneline[6]));
	            		    	++a.value;
	            		    	tm.put(oneline[3], a);
	            		    }
	            		    else
	            		    {
	            		    	a = new node();
	            		    	a.money = Math.round(Float.parseFloat(oneline[6]));
	            		    	tm.put(oneline[3], a);
	            		    }	    
	            		}
					} 
					catch (Exception e1) 
					{
						e1.printStackTrace();
					}
            	}
            	//sort by node.value
            	LinkedList<Map.Entry<String, node>> list = new LinkedList<Map.Entry<String, node>>(tm.entrySet());
        		Collections.sort(list, new Comparator<Map.Entry<String, node>>()
        		{
        			public int compare(Map.Entry<String, node> o1, Map.Entry<String, node> o2) 
        			{
        				if(value_money)
        				{
        					return o2.getValue().value - o1.getValue().value;
        				}
        				else
        				{
        					return o2.getValue().money - o1.getValue().money;
        				}   		
        			}
        		});       
				try 
				{
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.csv", false), decoding_btn.getText()));
					writer.write("買家帳號,消費總次數,消費總金額");
					writer.newLine();
					for(int i=0; i<list.size(); ++i)
	        		{
	        			if(value_money && list.get(i).getValue().value >= limit)
	        			{       
	        				writer.write(list.get(i).getKey() + "," + list.get(i).getValue().value + "," + list.get(i).getValue().money);
	        				writer.newLine();
	        			}
	        			else if(false == value_money && list.get(i).getValue().money >= limit)
	        			{
	        				writer.write(list.get(i).getKey() + "," + list.get(i).getValue().value + "," + list.get(i).getValue().money);
	        				writer.newLine();
	        			}
	        			else
	        				break;
	        		} 
					writer.close();  
					copyright.setText("處理完成");
				} 
				catch (Exception e1) 
				{
					e1.printStackTrace();
				}				     			
            } 
        }
        if(cmd == "3")
        {
        	if(value_money)
        	{
        		value_money_btn.setText("消費總金額排序");
        		count.setText("");
        		value_money_btn.setForeground(Color.RED);
        		count.setPlaceholder("預設消費金額為 500元");
        		count.repaint();
        		limit = 500;
        		value_money = false;
        	}
        	else
        	{
        		value_money_btn.setText("消費總次數排序");
        		count.setText("");
        		value_money_btn.setForeground(Color.BLUE);
        		count.setPlaceholder("預設購買次數為 5次");
        		count.repaint();
        		limit = 5;
        		value_money = true;
        	}
        }
        if(cmd == "4")
        {
        	if(decoding_btn.getText().equals("UTF-8"))
        	{
        		decoding_btn.setText("BIG5");
        	}
        	else
        	{
        		decoding_btn.setText("UTF-8");
        	}
        }
	}
}
