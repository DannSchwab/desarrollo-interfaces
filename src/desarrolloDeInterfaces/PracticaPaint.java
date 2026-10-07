package desarrolloDeInterfaces;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import java.math.*;

import java.awt.Graphics2D;

public class PracticaPaint extends JFrame{
	
	private JPanel contentPane;
	
	public static void main(String[]args) {
		 EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
					PracticaPaint frame = new PracticaPaint();
					frame.setVisible(true);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	
	public class Lienzo extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
		}
	}

	
	public PracticaPaint(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		setContentPane(contentPane);

		Lienzo lienzo = new Lienzo();
		contentPane.add(lienzo, BorderLayout.CENTER);
		
		JMenuBar jmb = new JMenuBar();
		
		JMenu mBrocha = new JMenu("Brocha");
		JMenu mColor = new JMenu("Color");
		JMenu mLienzo= new JMenu("Lienzo");
		
		JMenuItem colores= new JMenuItem("Elegir color...");
		
		JMenuItem tamañoBrocha1= new JMenuItem("1px");
		JMenuItem tamañoBrocha2= new JMenuItem("3px");
		JMenuItem tamañoBrocha3= new JMenuItem("5px");
		
		JMenuItem limpiarLienzo= new JMenuItem("Limpiar lienzo");
		
		limpiarLienzo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				lienzo.repaint();
			}
		});
		
		setJMenuBar(jmb);
		
		jmb.add(mLienzo);
		jmb.add(mColor);
		jmb.add(mBrocha);
		
		mLienzo.add(limpiarLienzo);
		mColor.add(colores);
		mBrocha.add(tamañoBrocha1);
		mBrocha.add(tamañoBrocha2);
		mBrocha.add(tamañoBrocha3);
		
		
	}

}
