package GUI;

import Model.ImageMatrix;
import Model.TypeOfImage;
import Operations.OperationFunction;
import Operations.ModelColors.ModelColor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class GeneralOperationInterface extends JFrame {
    private final String operationName;
    // Mapa de definiciones: clave = nombre del argumento, valor = clase del tipo de dato esperado
    private final LinkedHashMap<String, Class<?>> argumentDefinitions;
    // Mapa para almacenar los campos de entrada
    private final LinkedHashMap<String, JTextField> inputFields;

    private final ImageMatrix originalImage;
    private final OperationFunction operationFunction;

    // Panel principal que contendrá tanto los campos de entrada como el resultado
    private final JPanel mainPanel;
    // Panel para mostrar la(s) imagen(es) resultante(s)
    private final JPanel resultPanel;
    // Panel de control para los botones
    private final JPanel controlPanel;

    // Variable para almacenar el resultado de la operación
    private ImageMatrix resultImage;

    // Botones de control
    private JButton btnExecute;
    private JButton btnSave;
    private JButton btnRevert;

    public GeneralOperationInterface(String operationName,
                                     LinkedHashMap<String, Class<?>> argumentDefinitions,
                                     ImageMatrix imageMatrix,
                                     OperationFunction operationFunction) {
        super("Interfaz de Operación: " + operationName);
        this.operationName = operationName;
        this.argumentDefinitions = argumentDefinitions;
        this.inputFields = new LinkedHashMap<>();
        this.originalImage = imageMatrix;
        this.operationFunction = operationFunction;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        // O si quieres que se adapte al contenido:
        // pack();
        // setLocationRelativeTo(null);

        // Panel principal con BorderLayout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));
        setContentPane(mainPanel);

        // Panel de título
        JPanel titlePanel = createTitlePanel("Operación: " + operationName);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // --- Panel de parámetros (arriba) ---
        JPanel inputPanel = new JPanel(new GridLayout(argumentDefinitions.size(), 2, 10, 10));
        inputPanel.setBackground(new Color(245, 245, 250));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Map.Entry<String, Class<?>> entry : argumentDefinitions.entrySet()) {
            JLabel label = new JLabel(entry.getKey() + " (" + entry.getValue().getSimpleName() + "):");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JTextField textField = new JTextField();
            inputPanel.add(label);
            inputPanel.add(textField);
            inputFields.put(entry.getKey(), textField);
        }

        // --- Panel de resultado (abajo) ---
        resultPanel = new JPanel();
        resultPanel.setBackground(new Color(245, 245, 250));
        resultPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        resultPanel.setBorder(BorderFactory.createTitledBorder("Resultado"));

        // Lo ponemos dentro de un scroll para manejar imágenes grandes
        JScrollPane resultScroll = new JScrollPane(resultPanel);

        // --- JSplitPane vertical (top = parámetros, bottom = resultado) ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, resultScroll);
        splitPane.setOneTouchExpandable(true);
        // Qué proporción darle inicialmente a la parte superior vs. inferior
        splitPane.setResizeWeight(0.3);
        // 0.3 = 30% de la ventana para parámetros, 70% para el resultado

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Panel de control (botones) abajo
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setBackground(new Color(240, 240, 245));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        // Botón "Ejecutar"
        btnExecute = createStyledButton("Ejecutar", this::performOperation, new Color(60, 179, 113));
        controlPanel.add(btnExecute);

        // Botones "Guardar" y "Volver a la original"
        btnSave = createStyledButton("Guardar", this::saveResult, new Color(70, 130, 180));
        btnRevert = createStyledButton("Volver a la original", this::revertOperation, new Color(70, 130, 180));
    }

    private JPanel createTitlePanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 70));
        panel.add(titleLabel);
        return panel;
    }

    private JButton createStyledButton(String text, Runnable action, Color baseColor) {
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
        button.addActionListener(e -> action.run());
        return button;
    }

    private void performOperation() {
        // Recupera y convierte los parámetros
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<>();
        for (Map.Entry<String, JTextField> entry : inputFields.entrySet()) {
            String key = entry.getKey();
            String valueStr = entry.getValue().getText();
            Class<?> type = argumentDefinitions.get(key);
            Object value;
            try {
                if (type == Integer.class) {
                    value = Integer.parseInt(valueStr);
                } else if (type == Double.class) {
                    value = Double.parseDouble(valueStr);
                } else if (type == Float.class) {
                    value = Float.parseFloat(valueStr);
                } else if (type == String.class) {
                    value = valueStr;
                } else {
                    value = valueStr; // Por defecto
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Error: el valor para \"" + key + "\" no es un " + type.getSimpleName(),
                        "Error de conversión",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            parameters.put(key, value);
        }

        try {
            // Ejecuta la operación y obtiene la imagen resultado
            resultImage = operationFunction.apply(originalImage, parameters);
            // Actualiza el panel de resultado
            updateResultPanel();
            // Agrega los botones "Guardar" y "Volver a la original" (si no se han agregado aún)
            if (btnSave.getParent() == null) {
                controlPanel.add(btnSave);
            }
            if (btnRevert.getParent() == null) {
                controlPanel.add(btnRevert);
            }
            controlPanel.revalidate();
            controlPanel.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al ejecutar la operación: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println(originalImage.getTypeOfImage());
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }

    /**
     * Actualiza el panel de resultado mostrando la imagen modificada.
     * Si la operación es de tipo ModelColor, se muestran los canales según la conversión:
     * - Para RGB: RED, GREEN y BLUE.
     * - Para CMY: CIAN, MAGENTA y YELLOW.
     * - Para CMYK: CIAN, MAGENTA, YELLOW y K.
     * - Para cualquier otro espacio, se asume que el sufijo de la operación (después de "Convertir a")
     *   contiene las letras de los canales a mostrar.
     */
    private void updateResultPanel() {
        resultPanel.removeAll();
        if (operationFunction instanceof ModelColor) {
            for(int i = 0; i<resultImage.getTypeOfImage().getNumberOfChannels(); i++){
                BufferedImage channelImg = extractChannelImage(i);
                JLabel channelLabel = new JLabel(new ImageIcon(channelImg));
                channelLabel.setBorder(BorderFactory.createTitledBorder(String.valueOf(resultImage.getTypeOfImage().getNameOfChannels().toCharArray()[i])));
                resultPanel.add(channelLabel);
            }
        } else {
            BufferedImage imgResult = resultImage.toBufferedImage();
            JLabel label = new JLabel(new ImageIcon(imgResult));
            resultPanel.add(label);
        }
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    /**
     * Extrae la imagen de un canal en escala de grises a partir de la imagen fuente.
     * En el caso de canales RGB se extrae el valor del componente correspondiente;
     * para otros canales se utiliza una fórmula de luminancia (o se puede ajustar según el modelo).
     */
    private BufferedImage extractChannelImage(int channel) {
        int width = resultImage.getWidth();
        int height = resultImage.getHeight();
        BufferedImage channelImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        double[][] iChannel = resultImage.getChannel(channel);
        double[][][] matrix = resultImage.getMatrix();
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                int pixel = (int) Math.round(iChannel[j][i]*255);
                int r = 0, g = 0, b = 0;
                if(resultImage.getTypeOfImage() == TypeOfImage.RGB) {
                    if(channel == 0) {
                        r = pixel;
                    }else if(channel == 1) {
                        g = pixel;
                    }else if(channel == 2) {
                        b = pixel;
                    }
                }else if(resultImage.getTypeOfImage() == TypeOfImage.CMY) {
                    int c = 0, m = 0, y = 0;
                    if(channel == 0) {
                        c = pixel;
                    }else if(channel == 1) {
                        m = pixel;
                    }else if(channel == 2) {
                        y = pixel;
                    }
                    r = 255 - c; g = 255 - m; b = 255 - y;
                }else if(resultImage.getTypeOfImage() == TypeOfImage.CMYK) {
                    int c = 0, m = 0, y = 0, k=0;
                    if(channel == 0) {
                        c = pixel;
                    }else if(channel == 1) {
                        m = pixel;
                    }else if(channel == 2) {
                        y = pixel;
                    }else if(channel == 3) {
                        k = pixel;
                    }
                    r = 255 - Math.min(255, c * (255 - k) + k);
                    g = 255 - Math.min(255, m * (255 - k) + k);
                    b = 255 - Math.min(255, y * (255 - k) + k);
                }else{
                    r = pixel; g = pixel; b = pixel;
                }
                int rgb = (r << 16) | (g << 8) | b;
                channelImg.setRGB(i, j, rgb);
            }
        }
        return channelImg;
    }

    /**
     * Guarda la imagen resultado en un archivo.
     * En el caso de ModelColor se guarda la imagen compuesta (la imagen original modificada).
     */
    private void saveResult() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Imagen");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes (*.jpg, *.jpeg, *.png, *.bmp)", "jpg", "jpeg", "png", "bmp"));

        int res = fileChooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage toSave = resultImage.toBufferedImage();
                File file = fileChooser.getSelectedFile();
                String format = "png"; // Formato predeterminado
                String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
                if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                    format = "JPEG";
                } else if (extension.equalsIgnoreCase("bmp")) {
                    format = "BMP";
                }
                javax.imageio.ImageIO.write(toSave, format, file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la imagen: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Restaura la imagen original.
     * En caso de operación ModelColor se utiliza el método deApply para revertir la conversión.
     */
    private void revertOperation() {
        ImageMatrix reverted;
        if (operationFunction instanceof ModelColor) {
            reverted = ((ModelColor) operationFunction).deApply(resultImage);
        } else {
            reverted = originalImage;
        }
        try {
            resultImage = reverted;
            updateResultPanel();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al revertir la operación: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}