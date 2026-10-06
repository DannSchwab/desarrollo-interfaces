package desarrolloDeInterfaces;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class D_I_DanielSchwab_Ej3 extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JCheckBox toyota;
    private JCheckBox honda;
    private JCheckBox tesla;
    private JLabel texto;
    private JButton comprobar;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                D_I_DanielSchwab_Ej3 frame = new D_I_DanielSchwab_Ej3();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public D_I_DanielSchwab_Ej3() {
    	// Se define la cruceta de la esquina superior derecha de la ventana como la salida por defecto.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);

        // Uso el GridBagLayout como Layout porque sin usar la pestaña dising me lio muchisimo para sacar las distancias
        contentPane = new JPanel(new GridBagLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        // Esta clase ayuda a distribuir mejor el panel proporcionando mejores herramientas para el manejo de las distancias
        GridBagConstraints gbc = new GridBagConstraints();
        // Aquí se determina la distancia entre componentes en el GripBagLayout
        gbc.insets = new Insets(5, 5, 5, 10);
        // Se evita que los componentes crezcan en vertical a la hora de modificarse el tamaño de otros componentes u añadirse otros durante la ejecución del programa.
        gbc.fill = GridBagConstraints.HORIZONTAL;


        // Con GripBagLayout el panel se divide en secciones, las cuales son formadas por filas y columnas. La posición de los componentes se determinarán por estas. Siendo gridx la columna y grid y la fila
        gbc.gridx = 0;
        gbc.gridy = 0;
        toyota = new JCheckBox("Toyota");
        contentPane.add(toyota, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        honda = new JCheckBox("Honda");
        contentPane.add(honda, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        tesla = new JCheckBox("Tesla");
        contentPane.add(tesla, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        comprobar = new JButton("Comprobar");
        contentPane.add(comprobar, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        texto = new JLabel("");
        contentPane.add(texto, gbc);

        comprobar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    StringBuilder seleccion = new StringBuilder();

                    if (toyota.isSelected()) {
                        seleccion.append("Toyota ");
                    }
                    if (honda.isSelected()) {
                        seleccion.append("Honda ");
                    }
                    if (tesla.isSelected()) {
                        seleccion.append("Tesla ");
                    }

                    if (seleccion.length() == 0) {
                        texto.setText("No has seleccionado nada");
                    } else {
                        texto.setText("Has seleccionado: " + seleccion.toString().trim());
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        D_I_DanielSchwab_Ej3.this,
                        "Ocurrió un error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }
}