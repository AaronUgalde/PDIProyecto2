package GUI;

import Model.ImageMatrix;
import Operations.OperationFunction;
import Operations.Result;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GeneralOperationInterface2 extends JFrame {
    private final String operationName;
    private final LinkedHashMap<String, Class<?>> argumentDefinitions;
    private final Map<String,Object[]> comboOptions;
    private final LinkedHashMap<String, JComponent> inputComponents;
    private ImageMatrix imageMatrix;

    private JPanel mainPanel;
    private JPanel resultPanel;
    private JPanel controlPanel;
    private JLabel imageLabel;
    private JScrollPane resultScrollPane;

    // Tamaño mínimo para la ventana
    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 600;

    public GeneralOperationInterface2(String operationName,
                                      LinkedHashMap<String, Class<?>> argumentDefinitions,
                                      ImageMatrix imageMatrix,
                                      List<ButtonConfig> extraButtons,
                                      Map<String,Object[]> comboOptions) {
        super("Interfaz de Operación: " + operationName);
        this.operationName = operationName;
        this.argumentDefinitions = argumentDefinitions;
        this.inputComponents = new LinkedHashMap<>();
        this.imageMatrix = imageMatrix;
        this.comboOptions        = comboOptions;

        initComponents(extraButtons);
    }

    private void initComponents(List<ButtonConfig> extraButtons) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(MIN_WIDTH, MIN_HEIGHT);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));
        setContentPane(mainPanel);

        JPanel titlePanel = createTitlePanel("Operación: " + operationName);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel inputPanel = createInputPanel();
        JScrollPane inputScrollPane = new JScrollPane(inputPanel);
        inputScrollPane.setBorder(null);

        resultPanel = createResultPanel();
        resultScrollPane = new JScrollPane(resultPanel);
        resultScrollPane.setBorder(null);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputScrollPane, resultScrollPane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.3);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        controlPanel = createControlPanel(extraButtons);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                resizeImage();
            }
        });
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10,10,10,10), "Parámetros"));

        GridBagConstraints labelC = new GridBagConstraints();
        labelC.fill = GridBagConstraints.HORIZONTAL;
        labelC.anchor = GridBagConstraints.WEST;
        labelC.insets = new Insets(5,5,5,10);
        labelC.weightx = 0.3;

        GridBagConstraints compC = new GridBagConstraints();
        compC.fill = GridBagConstraints.HORIZONTAL;
        compC.gridwidth = GridBagConstraints.REMAINDER;
        compC.insets = new Insets(5,5,5,5);
        compC.weightx = 0.7;

        int row = 0;
        for (Map.Entry<String, Class<?>> entry : argumentDefinitions.entrySet()) {
            String key = entry.getKey();
            Class<?> type = entry.getValue();

            labelC.gridy = row;
            compC.gridy = row;

            JLabel label = new JLabel(key + " (" + type.getSimpleName() + "):");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panel.add(label, labelC);

            JComponent comp;
            if (comboOptions.containsKey(key)) {
                // genera JComboBox con esos valores
                Object[] opts = comboOptions.get(key);
                JComboBox<Object> combo = new JComboBox<>(opts);
                combo.setPreferredSize(new Dimension(120, combo.getPreferredSize().height));
                comp = combo;

            } else if (type == Integer.class) {
                JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
                spinner.setPreferredSize(new Dimension(80, spinner.getPreferredSize().height));
                comp = spinner;
            } else if (type == Double.class || type == Float.class) {
                JSpinner spinner = new JSpinner(new SpinnerNumberModel(0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 0.1));
                spinner.setPreferredSize(new Dimension(80, spinner.getPreferredSize().height));
                comp = spinner;
            } else if (type == Boolean.class) {
                comp = new JCheckBox();
            } else if (key.equalsIgnoreCase("image") || type == File.class) {
                JTextField fileField = new JTextField(10);
                fileField.setEditable(false);
                JButton browse = new JButton("Examinar");
                browse.addActionListener(e -> {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        fileField.setText(file.getAbsolutePath());
                    }
                });
                JPanel filePanel = new JPanel(new BorderLayout(5,0));
                filePanel.setBackground(panel.getBackground());
                filePanel.add(fileField, BorderLayout.CENTER);
                filePanel.add(browse, BorderLayout.EAST);
                comp = filePanel;
            } else {
                comp = new JTextField(10);
            }
            panel.add(comp, compC);
            inputComponents.put(key, comp);
            row++;
        }

        GridBagConstraints fillC = new GridBagConstraints();
        fillC.gridy = row;
        fillC.weighty = 1.0;
        fillC.fill = GridBagConstraints.BOTH;
        panel.add(Box.createVerticalGlue(), fillC);

        return panel;
    }

    private JPanel createTitlePanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(53, 124, 165));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);
        return panel;
    }


    private void performOperation(OperationFunction operation) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        for (Map.Entry<String, JComponent> entry : inputComponents.entrySet()) {
            String key = entry.getKey();
            JComponent comp = entry.getValue();
            Class<?> type = argumentDefinitions.get(key);
            Object value;
            if (comp instanceof JComboBox) {
                value = ((JComboBox<?>)comp).getSelectedItem();
            }
            else if (comp instanceof JSpinner) {
                value = ((JSpinner) comp).getValue();
            } else if (comp instanceof JCheckBox) {
                value = ((JCheckBox) comp).isSelected();
            } else if (comp instanceof JPanel) {
                // File chooser panel
                JTextField tf = (JTextField) ((JPanel) comp).getComponent(0);
                String path = tf.getText();
                value = path;
            } else {
                String text = ((JTextField) comp).getText();
                if (text.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "El campo \"" + key + "\" no puede estar vacío",
                            "Error de validación",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    if (type == Integer.class) {
                        value = Integer.parseInt(text);
                    } else if (type == Double.class) {
                        value = Double.parseDouble(text);
                    } else if (type == Float.class) {
                        value = Float.parseFloat(text);
                    } else if (type == Boolean.class) {
                        value = Boolean.parseBoolean(text);
                    } else {
                        value = text;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error: el valor para \"" + key + "\" no es un " + type.getSimpleName(),
                            "Error de conversión",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            parameters.put(key, value);
        }

        try {
            // Mostrar cursor de espera
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            // Se ejecuta la operación y se obtiene el nuevo panel de resultado.
            Result miResult = operation.apply(imageMatrix, parameters);

            // Actualizar la imagen
            imageMatrix = miResult.getImageMatrix();

            // Actualizar el contenido del scroll pane con el nuevo panel
            JPanel newResultPanel = miResult.getPanel();
            if (newResultPanel != null) {
                resultScrollPane.setViewportView(newResultPanel);
            } else {
                // Si no hay panel personalizado, recrear el panel de resultado con la nueva imagen
                JPanel defaultResultPanel = createResultPanel();
                resultScrollPane.setViewportView(defaultResultPanel);
            }

            // Redimensionar la imagen para que se ajuste
            resizeImage();

            mainPanel.revalidate();
            mainPanel.repaint();
            System.out.println("Panel de resultado actualizado");

            // Restaurar cursor normal
            setCursor(Cursor.getDefaultCursor());
        } catch (Exception ex) {
            // Restaurar cursor normal
            setCursor(Cursor.getDefaultCursor());

            JOptionPane.showMessageDialog(this,
                    "Error al ejecutar la operación: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void saveResult() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Imagen");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes (*.jpg, *.jpeg, *.png, *.bmp)", "jpg", "jpeg", "png", "bmp"));

        int res = fileChooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // Verificar la extensión del archivo
            String filePath = file.getAbsolutePath();
            String format = "png";

            if (!filePath.toLowerCase().endsWith(".jpg") &&
                    !filePath.toLowerCase().endsWith(".jpeg") &&
                    !filePath.toLowerCase().endsWith(".png") &&
                    !filePath.toLowerCase().endsWith(".bmp")) {
                file = new File(filePath + ".png");
            }

            String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1).toLowerCase();
            if (extension.equals("jpg") || extension.equals("jpeg")) {
                format = "JPEG";
            } else if (extension.equals("bmp")) {
                format = "BMP";
            }

            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                // Usar la imagen original de ImageMatrix para guardar con máxima calidad
                BufferedImage imageToSave = imageMatrix.toBufferedImage();
                javax.imageio.ImageIO.write(imageToSave, format, file);

                JOptionPane.showMessageDialog(this,
                        "Imagen guardada correctamente en:\n" + file.getAbsolutePath(),
                        "Guardar completado",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar la imagen: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }
    // ... resto de métodos (createResultPanel, resizeImage, createStyledButton, saveResult) igual que antes
    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createTitledBorder("Resultado")
        ));

        // Centro la imagen y la hago escalable
        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setBackground(panel.getBackground());

        BufferedImage img = imageMatrix.toBufferedImage();
        ImageIcon icon = new ImageIcon(img);
        imageLabel = new JLabel(icon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        imagePanel.add(imageLabel);
        panel.add(imagePanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Redimensiona la imagen para que se adapte al panel de resultados
     */
    private void resizeImage() {
        if (imageMatrix == null || resultScrollPane == null || imageLabel == null) return;

        BufferedImage originalImage = imageMatrix.toBufferedImage();
        if (originalImage == null) return;

        // Obtener dimensiones disponibles
        int panelWidth = resultScrollPane.getViewport().getWidth() - 30; // margen
        int panelHeight = resultScrollPane.getViewport().getHeight() - 30; // margen

        if (panelWidth <= 0 || panelHeight <= 0) return;

        // Calcular proporciones
        int imgWidth = originalImage.getWidth();
        int imgHeight = originalImage.getHeight();

        double widthRatio = (double) panelWidth / imgWidth;
        double heightRatio = (double) panelHeight / imgHeight;

        // Usar el factor más pequeño para mantener la proporción
        double scaleFactor = Math.min(widthRatio, heightRatio);

        // Si la imagen es más pequeña que el panel, no la agrandes
        if (scaleFactor > 1.0 && (imgWidth < panelWidth && imgHeight < panelHeight)) {
            scaleFactor = 1.0;
        }

        int scaledWidth = (int)(imgWidth * scaleFactor);
        int scaledHeight = (int)(imgHeight * scaleFactor);

        // Crear imagen redimensionada
        if (scaledWidth > 0 && scaledHeight > 0) {
            BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = scaledImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
            g.dispose();

            imageLabel.setIcon(new ImageIcon(scaledImage));
        }
    }

    /**
     * Crea un botón estilizado.
     */
    private JButton styleOfButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(baseColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
        return button;
    }

    private JButton createStyledButton(String text, OperationFunction action, Color baseColor) {
        JButton button = styleOfButton(text, baseColor);
        button.addActionListener(_ -> performOperation(action));
        return button;
    }

    private JButton createStyledButton(String text, Runnable action, Color baseColor) {
        JButton button = styleOfButton(text, baseColor);
        button.addActionListener(_ -> action.run());
        return button;
    }

    private JPanel createControlPanel(List<ButtonConfig> extraButtons) {
        // Panel principal de control
        JPanel outerControlPanel = new JPanel(new BorderLayout());
        outerControlPanel.setBackground(new Color(240, 240, 245));
        outerControlPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Panel interno para los botones con FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));

        // Botón savea
        JButton saveButton = createStyledButton("Guardar", this::saveResult, new Color(53, 124, 165));
        buttonPanel.add(saveButton);

        // Agregar botones pasados por parámetro
        if (extraButtons != null) {
            for (ButtonConfig config : extraButtons) {
                JButton extraButton = createStyledButton(config.getText(), config.getAction(), config.getBaseColor());
                buttonPanel.add(extraButton);
            }
        }

        // Añadir panel de botones a un scroll pane horizontal
        JScrollPane buttonScrollPane = new JScrollPane(buttonPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        buttonScrollPane.setBorder(null);
        buttonScrollPane.setPreferredSize(new Dimension(MIN_WIDTH, 70));

        outerControlPanel.add(buttonScrollPane, BorderLayout.CENTER);

        return outerControlPanel;
    }
}
