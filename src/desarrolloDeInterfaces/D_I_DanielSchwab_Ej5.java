package desarrolloDeInterfaces;

import java.awt.BorderLayout;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class D_I_DanielSchwab_Ej5 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					D_I_DanielSchwab_Ej5 frame = new D_I_DanielSchwab_Ej5();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public D_I_DanielSchwab_Ej5() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JMenuBar menus = new JMenuBar();
		setJMenuBar(menus);
		
		JMenu archivo = new JMenu("Archivo");
		menus.add(archivo);
		JMenuItem nuevo = new JMenuItem("Nuevo");
		archivo.add(nuevo);
		
		nuevo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JTextArea tA_archivo = new JTextArea(10, 30);
					
					// Se inicializa el JScrollPane pasándole el área de texto por constructor
					JScrollPane scroll = new JScrollPane(tA_archivo);
					
					// Limpia el panel antes de añadir uno nuevo para no amontonarlos
					contentPane.removeAll(); 
					
					// Se añade al centro del BorderLayout
					contentPane.add(scroll, BorderLayout.CENTER);
					
					// Refresca la interfaz para que se muestren los nuevos elementos
					contentPane.revalidate();
					contentPane.repaint();
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
}