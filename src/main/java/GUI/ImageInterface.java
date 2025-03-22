package GUI;

import Model.ImageMatrix;
import Operations.ModelColors.*;
import Operations.OperationFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Interfaz principal para el procesamiento de imágenes y para lanzar operaciones.
 */
public class ImageInterface extends JFrame {
    private final ImageMatrix imageMatrix;
    private JLabel imageLabel;
    private BufferedImage currentImage;
    private JPanel mainContentPanel;
    private JTabbedPane sidebarTabs;

    // Constantes de estilo
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color PANEL_COLOR = new Color(240, 240, 245);
    static final Color ACCENT_COLOR = new Color(70, 130, 180);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 18);
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 12);

    // Lista de operaciones disponibles
    private List<OperationInfo> operations = new ArrayList<>();

    public ImageInterface(BufferedImage image) throws IOException {
        super("Procesamiento de Imágenes");
        this.imageMatrix = new ImageMatrix(image);
        this.currentImage = image;

        initializeUI();
        registerOperations();
        createOperationPanels();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 700);
        setMinimumSize(new Dimension(800, 600));
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLocationRelativeTo(null);

        // Panel de título y barra de herramientas
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Panel principal que contiene la imagen y el panel lateral
        mainContentPanel = new JPanel(new BorderLayout(10, 0));
        mainContentPanel.setBackground(BACKGROUND_COLOR);
        add(mainContentPanel, BorderLayout.CENTER);

        // Área para mostrar la imagen
        JPanel imagePanel = createImagePanel(currentImage);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel lateral con pestañas para categorías
        sidebarTabs = new JTabbedPane(JTabbedPane.TOP);
        sidebarTabs.setFont(BUTTON_FONT);
        sidebarTabs.setPreferredSize(new Dimension(250, 0));
        mainContentPanel.add(sidebarTabs, BorderLayout.EAST);

        // Panel de barra de estado
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void registerOperations() {
        // Registrar operaciones de espacios de color utilizando la interfaz genérica.
        operations.add(new OperationInfo("Mostrar Canales RGB", this::showChannels, OperationCategory.COLOR_SPACES));
        operations.add(new OperationInfo("Convertir a YIQ", () -> openGeneralOperationInterface(
                "Convertir a YIQ", new LinkedHashMap<>(), new RGB_YIQ()), OperationCategory.COLOR_SPACES));
        operations.add(new OperationInfo("Convertir a CMY", () -> openGeneralOperationInterface(
                "Convertir a CMY", new LinkedHashMap<>(), new RGB_CMY()), OperationCategory.COLOR_SPACES));
        operations.add(new OperationInfo("Convertir a CMYK", () -> openGeneralOperationInterface(
                "Convertir a CMYK", new LinkedHashMap<>(), new RGB_CMYK()), OperationCategory.COLOR_SPACES));
        // Otras operaciones se pueden registrar aquí...
    }

    private void openGeneralOperationInterface(String opName, LinkedHashMap<String, Class<?>> argDefinitions, OperationFunction opFunction) {
        GeneralOperationInterface goi = new GeneralOperationInterface(opName, argDefinitions, imageMatrix, opFunction);
        goi.setVisible(true);
    }

    private void createOperationPanels() {
        Map<OperationCategory, JPanel> categoryPanels = new HashMap<>();

        for (OperationCategory category : OperationCategory.values()) {
            JPanel categoryPanel = new JPanel();
            categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
            categoryPanel.setBackground(PANEL_COLOR);
            categoryPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
            categoryPanels.put(category, categoryPanel);

            boolean hasOperationsInCategory = operations.stream()
                    .anyMatch(op -> op.category == category);

            if (hasOperationsInCategory) {
                sidebarTabs.addTab(category.getDisplayName(), categoryPanel);
            }
        }

        for (OperationInfo operation : operations) {
            JPanel categoryPanel = categoryPanels.get(operation.category);
            if (categoryPanel != null) {
                JButton button = createActionButton(operation.name, operation.action, operation.buttonColor);
                categoryPanel.add(button);
                categoryPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            }
        }

        for (JPanel panel : categoryPanels.values()) {
            panel.add(Box.createVerticalGlue());
        }
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Procesamiento de Imágenes");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(new Color(50, 50, 60));
        panel.add(titleLabel, BorderLayout.WEST);

        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolbarPanel.setOpaque(false);

        JButton loadButton = createToolbarButton("Cargar Imagen", this::loadImage);
        JButton saveButton = createToolbarButton("Guardar Imagen", this::saveImage);

        toolbarPanel.add(loadButton);
        toolbarPanel.add(saveButton);

        panel.add(toolbarPanel, BorderLayout.EAST);
        return panel;
    }

    private JButton createToolbarButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(new Color(50, 50, 60));
        button.setBackground(new Color(230, 230, 235));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(5, 10, 5, 10));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 225));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 230, 235));
            }
        });

        button.addActionListener(e -> action.run());
        return button;
    }

    private JPanel createImagePanel(BufferedImage image) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(240, 240, 245));
        infoPanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel dimensionsLabel = new JLabel(String.format("Dimensiones: %d × %d px", image.getWidth(), image.getHeight()));
        dimensionsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dimensionsLabel.setForeground(new Color(80, 80, 90));
        infoPanel.add(dimensionsLabel);

        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setBackground(Color.WHITE);
        imageContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageContainer.add(imageLabel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(infoPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(imageContainer, gbc);

        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(new EmptyBorder(5, 15, 5, 15));

        JLabel statusLabel = new JLabel("Listo");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(80, 80, 90));
        panel.add(statusLabel, BorderLayout.WEST);
        return panel;
    }

    private JButton createActionButton(String text, Runnable action, Color color) {
        JButton button = new JButton();
        button.setFont(BUTTON_FONT);
        button.setForeground(new Color(50, 50, 60));
        button.setBackground(new Color(230, 230, 235));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 210, 215), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setLayout(new BorderLayout());

        JPanel colorIndicator = new JPanel();
        colorIndicator.setPreferredSize(new Dimension(5, 0));
        colorIndicator.setBackground(color);
        button.add(colorIndicator, BorderLayout.WEST);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(BUTTON_FONT);
        textLabel.setForeground(new Color(50, 50, 60));
        textLabel.setBorder(new EmptyBorder(0, 5, 0, 0));
        button.add(textLabel, BorderLayout.CENTER);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 220, 225));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 230, 235));
            }
        });

        button.addActionListener(e -> {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                action.run();
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        });
        return button;
    }

    private void showChannels() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            ChannelsInterface channelsInterface = new ChannelsInterface(imageMatrix);
            channelsInterface.setVisible(true);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Imagen");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes (*.jpg, *.jpeg, *.png, *.bmp)", "jpg", "jpeg", "png", "bmp"));

        int res = fileChooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage newImage = ImageIO.read(file);
                if (newImage != null) {
                    ImageInterface newInterface = new ImageInterface(newImage);
                    newInterface.setVisible(true);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Imagen");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes (*.jpg, *.jpeg, *.png, *.bmp)", "jpg", "jpeg", "png", "bmp"));

        int res = fileChooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                String format = "png"; // Formato predeterminado
                String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
                if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                    format = "JPEG";
                } else if (extension.equalsIgnoreCase("bmp")) {
                    format = "BMP";
                }
                ImageIO.write(currentImage, format, file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la imagen: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}