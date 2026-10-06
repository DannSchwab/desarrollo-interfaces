package desarrolloDeInterfaces;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class D_I_DanielSchwab_Ej6 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					D_I_DanielSchwab_Ej6 frame = new D_I_DanielSchwab_Ej6();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public D_I_DanielSchwab_Ej6() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		contentPane.setLayout(new BorderLayout());
		
		JTextArea tArea = new JTextArea(30, 15);		
		JScrollPane scroll = new JScrollPane(tArea);
		
		SpinnerNumberModel modeloNumeros = new SpinnerNumberModel(12, 4, 40, 1);
		JSpinner spinner = new JSpinner(modeloNumeros);
		
		JSlider slider = new JSlider(JSlider.HORIZONTAL, 4, 40, 12);

		contentPane.add(scroll, BorderLayout.CENTER);
		contentPane.add(spinner, BorderLayout.WEST);
		contentPane.add(slider, BorderLayout.NORTH);
		
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					tArea.setFont(new Font("Arial",Font.PLAIN, (int)spinner.getValue()));
				}catch (Exception ex){
					ex.printStackTrace();
					JOptionPane.showMessageDialog(contentPane, "Error desconocido", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				try {
					tArea.setFont(new Font("Arial",Font.PLAIN, (int)slider.getValue()));
				}catch (Exception ex){
					ex.printStackTrace();
					JOptionPane.showMessageDialog(contentPane, "Error desconocido", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		setContentPane(contentPane);
	}

}
