package GUI;

import Model.ImageMatrix;
import Operations.ArithmeticOperation.Division;
import Operations.ArithmeticOperation.Multiplication;
import Operations.ArithmeticOperation.Substraction;
import Operations.ArithmeticOperation.Sum;
import Operations.Binarization.BinarizeOneThreshold;
import Operations.Binarization.BinarizeThreeThresholds;
import Operations.Binarization.BinarizeTwoThresholds;
import Operations.Binarization.InvertBinarization;
import Operations.EdgeDetection.CannyOperation;
import Operations.GeometricOperations.Interpolation;
import Operations.GeometricOperations.Rotation;
import Operations.GeometricOperations.Translation;
import Operations.HistogramOperations.*;
import Operations.LogicOperations.And;
import Operations.LogicOperations.Not;
import Operations.LogicOperations.Or;
import Operations.LogicOperations.Xor;
import Operations.ModelColors.RGB_CMY.CMY_RGB;
import Operations.ModelColors.RGB_CMY.RGB_CMY;
import Operations.ModelColors.RGB_CMYK.CMYK_RGB;
import Operations.ModelColors.RGB_CMYK.RGB_CMYK;
import Operations.ModelColors.RGB_HSI.HSI_RGB;
import Operations.ModelColors.RGB_HSI.RGB_HSI;
import Operations.ModelColors.RGB_HSV.HSV_RGB;
import Operations.ModelColors.RGB_HSV.RGB_HSV;
import Operations.ModelColors.RGB_LAB.LAB_RGB;
import Operations.ModelColors.RGB_LAB.RGB_LAB;
import Operations.ModelColors.RGB_LMS.LMS_RGB;
import Operations.ModelColors.RGB_LMS.RGB_LMS;
import Operations.ModelColors.RGB_YIQ.RGB_YIQ;
import Operations.ModelColors.RGB_YIQ.YIQ_RGB;
import Operations.OperationFunction;
import Operations.RelationalOperation.*;
import Operations.ConvolutionOperations.*;
import Model.MaskType;
import Operations.NonLinearFilters.*;

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

    /**
     * Método auxiliar que centraliza la creación de la ventana de operación.
     * Permite registrar operaciones pasando:
     * - El nombre de la operación.
     * - Un mapa de definiciones de argumentos (puede ser vacío).
     * - La implementación de la operación (OperationFunction).
     * - Lista de configuraciones adicionales para botones.
     */
    private void openGeneralOperationInterface(String operationName,
                                               LinkedHashMap<String, Class<?>> argumentDefinitions,
                                               List<ButtonConfig> extraButtons, Map<String,Object[]> comboOptions) {
        new GeneralOperationInterface2(operationName, argumentDefinitions, imageMatrix, extraButtons, comboOptions).setVisible(true);
    }

    /**
     * Registro de operaciones.
     * Ahora, cada operación se registra invocando al método auxiliar, lo que facilita la adición
     * de nuevas operaciones sin tocar la estructura principal de la interfaz.
     */


    private void registerOperations() {
        // --- Color Spaces ---
        registerColorSpace("YIQ", new RGB_YIQ(), new YIQ_RGB());
        registerColorSpace("CMY", new RGB_CMY(), new CMY_RGB());
        registerColorSpace("CMYK", new RGB_CMYK(), new CMYK_RGB());
        registerColorSpace("HSI", new RGB_HSI(), new HSI_RGB());
        registerColorSpace("HSV", new RGB_HSV(), new HSV_RGB());
        registerColorSpace("LAB", new RGB_LAB(), new LAB_RGB());
        registerColorSpace("LMS", new RGB_LMS(), new LMS_RGB());

        // --- Binarization ---
        addOperation("Binarizar (1 umbral)", Collections.singletonMap("threshold", Double.class),
                List.of(new ButtonConfig("Binarizar", new BinarizeOneThreshold(), Color.MAGENTA),
                        new ButtonConfig("Invertir", new InvertBinarization(), Color.CYAN),
                        new ButtonConfig("RGB", new YIQ_RGB(), Color.CYAN)),
                OperationCategory.BINARIZATION);
        addOperation("Binarizar (2 umbrales)", Map.of("threshold1", Double.class, "threshold2", Double.class),
                List.of(new ButtonConfig("Binarizar", new BinarizeTwoThresholds(), Color.MAGENTA),
                        new ButtonConfig("Invertir", new InvertBinarization(), Color.CYAN),
                        new ButtonConfig("RGB", new YIQ_RGB(), Color.CYAN)),
                OperationCategory.BINARIZATION);
        addOperation("Binarizar (3 umbrales)", Map.of("threshold1", Double.class, "threshold2", Double.class, "threshold3", Double.class),
                List.of(new ButtonConfig("Binarizar", new BinarizeThreeThresholds(), Color.MAGENTA),
                        new ButtonConfig("Invertir", new InvertBinarization(), Color.CYAN),
                        new ButtonConfig("RGB", new YIQ_RGB(), Color.CYAN)),
                OperationCategory.BINARIZATION);

        // --- Geometric Operations ---
        addOperation("Interpolación", Map.of("scale", Double.class),
                List.of(new ButtonConfig("Interpolar", new Interpolation(), Color.MAGENTA)),
                OperationCategory.GEOMETRICOPERATIONS);
        addOperation("Rotación", Map.of("angle", Double.class),
                List.of(new ButtonConfig("Rotar", new Rotation(), Color.MAGENTA)),
                OperationCategory.GEOMETRICOPERATIONS);
        addOperation("Translación", Map.of("tx", Integer.class, "ty", Integer.class),
                List.of(new ButtonConfig("Trasladar", new Translation(), Color.MAGENTA)),
                OperationCategory.GEOMETRICOPERATIONS);

        // --- Arithmetic Operations ---
        addOperation("Suma", Map.of("image", String.class),
                List.of(new ButtonConfig("Sumar", new Sum(), Color.MAGENTA)),
                OperationCategory.ARITHMETICOPERATIONS);
        addOperation("Resta", Map.of("image", String.class),
                List.of(new ButtonConfig("Restar", new Substraction(), Color.MAGENTA)),
                OperationCategory.ARITHMETICOPERATIONS);
        addOperation("Multiplicación", Map.of("image", String.class),
                List.of(new ButtonConfig("Multiplicar", new Multiplication(), Color.MAGENTA)),
                OperationCategory.ARITHMETICOPERATIONS);
        addOperation("División", Map.of("image", String.class),
                List.of(new ButtonConfig("Dividir", new Division(), Color.MAGENTA)),
                OperationCategory.ARITHMETICOPERATIONS);

        // --- Logic Operations ---
        for (Map.Entry<String, OperationFunction> e : Map.of(
                        "AND", new And(), "OR", new Or(), "XOR", new Xor(), "NOT", new Not())
                .entrySet()) {
            addOperation(e.getKey(), Collections.emptyMap(),
                    List.of(new ButtonConfig(e.getKey(), e.getValue(), Color.MAGENTA)),
                    OperationCategory.LOGICOPERATIONS);
        }

        // --- Relational Operations ---
        for (Map.Entry<String, OperationFunction> e : Map.of(
                        "Equal", new Equal(), "Not equal", new NotEqual(),
                        "Greater than", new GreaterThan(), "Greater or equal", new GreaterOrEqual(),
                        "Less than", new LessThan(), "Less or equal", new LessOrEqual())
                .entrySet()) {
            addOperation(e.getKey(), Map.of("image", String.class),
                    List.of(new ButtonConfig(e.getKey(), e.getValue(), Color.MAGENTA)),
                    OperationCategory.RELATIONALOPERATIONS);
        }

        // --- Histogram Operations ---
        addOperation("Shift", Map.of("shift", Double.class),
                List.of(new ButtonConfig("Shift", new Shift(), Color.CYAN)),
                OperationCategory.HISTOGRAMOPERATIONS);
        addOperation("Scale", Map.of("factor", Double.class),
                List.of(new ButtonConfig("Scale", new Scale(), Color.ORANGE)),
                OperationCategory.HISTOGRAMOPERATIONS);
        addOperation("Histogram Matching", Map.of("image", String.class),
                List.of(new ButtonConfig("Match", new Matching(), Color.MAGENTA)),
                OperationCategory.HISTOGRAMOPERATIONS);
        addOperation("Uniform Equalization", Collections.emptyMap(),
                List.of(new ButtonConfig("Uniform Eq", new UniformEqualization(), Color.BLUE)),
                OperationCategory.HISTOGRAMOPERATIONS);
        addOperation("Exponential Equalization", Map.of("alpha", Double.class),
                List.of(new ButtonConfig("Exponencial Eq", new ExponentialEqualization(), Color.CYAN)),
                OperationCategory.HISTOGRAMOPERATIONS);
        addOperation("Rayleigh Equalization", Map.of("alpha", Double.class),
                List.of(new ButtonConfig("Rayleigh Eq", new RayleighEqualization(), Color.MAGENTA)),
                OperationCategory.HISTOGRAMOPERATIONS);
        addOperation("Hyperbolic Raices Equalization", Map.of("pot", Double.class),
                List.of(new ButtonConfig("Hyper Raices", new HyperbolicRaicesEqualization(), Color.ORANGE)),
                OperationCategory.HISTOGRAMOPERATIONS);
        addOperation("Hyperbolic Log Equalization", Collections.emptyMap(),
                List.of(new ButtonConfig("Hyper Log", new HyperbolicLogEqualization(), Color.GREEN)),
                OperationCategory.HISTOGRAMOPERATIONS);

        // --- Edge Detection ---
        addOperation("Canny", Map.of(
                        "sigma", Double.class,
                        "kernelSize", Integer.class,
                        "lowThreshold", Double.class,
                        "highThreshold", Double.class),
                List.of(new ButtonConfig("Canny", new CannyOperation(), Color.RED)),
                OperationCategory.EDGEDETECTION);


        // --- Convolution Filters ---
        // Low-pass filters
        Map<String,Class<?>> lowDefs = Map.of(
                "type", String.class,
                "size", Integer.class,
                "sigma", Double.class,
                "normalize", Boolean.class
        );
        Map<String,Object[]> lowCombos = new HashMap<>();
        lowCombos.put("type", new String[]{"box","gauss","suavizado7","suavizado9","suavizado11"});
        lowCombos.put("size", new Integer[]{3,5,7,9,11});
        ArrayList<ButtonConfig> lowButtons = new ArrayList<>();
        lowButtons.add(new ButtonConfig("Box Blur", new LowPassConvolution(), Color.CYAN));
        lowButtons.add(new ButtonConfig("Gaussian Blur", new LowPassConvolution(), Color.CYAN));
        lowButtons.add(new ButtonConfig("Suavizado 7×7", new LowPassConvolution(), Color.CYAN));
        lowButtons.add(new ButtonConfig("Suavizado 9×9", new LowPassConvolution(), Color.CYAN));
        lowButtons.add(new ButtonConfig("Suavizado 11×11", new LowPassConvolution(), Color.CYAN));
        operations.add(new OperationInfo(
                "Filtros Pasa-Bajas",
                () -> openGeneralOperationInterface("Filtros Pasa-Bajas", new LinkedHashMap<>(lowDefs), lowButtons, lowCombos),
                OperationCategory.CONVOLUTIONFILTERS,
                new LinkedHashMap<>(lowDefs),
                lowCombos
        ));

        // High-pass & Edge detection filters
        Map<String,Class<?>> highDefs = Map.of(
                "type", String.class,
                "direction", String.class,
                "normalize", Boolean.class
        );
        Map<String,Object[]> highCombos = new HashMap<>();
        highCombos.put("type", new String[]{
                "sharpen1","sharpen2","sharpen3",
                "sobelx","sobely",
                "prewittx","prewitty",
                "robertsx","robertsy",
                "freichenx","freicheny",
                "homogeneidad","diferencia",
                "prewitt_compass","robinson3","robinson5","kirsch"
        });
        highCombos.put("direction", new String[]{"E","NE","N","NO","O","SO","S","SE"});
        ArrayList<ButtonConfig> highButtons = new ArrayList<>();
        String[] highLabels = {"Sharpen 1","Sharpen 2","Sharpen 3","Roberts X","Roberts Y",
                "Prewitt X","Prewitt Y","Sobel X","Sobel Y","Frei-Chen X","Frei-Chen Y",
                "Prewitt Compass","Robinson 3","Robinson 5","Kirsch"};
        for (String lbl : highLabels) {
            highButtons.add(new ButtonConfig(lbl, new HighPassConvolution(), Color.ORANGE));
        }
        operations.add(new OperationInfo(
                "Filtros Pasa-Altas y Borde",
                () -> openGeneralOperationInterface("Filtros Pasa-Altas y Borde", new LinkedHashMap<>(highDefs), highButtons, highCombos),
                OperationCategory.CONVOLUTIONFILTERS,
                new LinkedHashMap<>(highDefs),
                highCombos
        ));

// === Registro de Filtros No Lineales por separado ===

// Arithmetic Mean Filter
        {
            LinkedHashMap<String, Class<?>> defs = new LinkedHashMap<>();
            defs.put("maskType", MaskType.class);
            defs.put("maskSize", Integer.class);

            Map<String, Object[]> combos = Map.of(
                    "maskType", MaskType.values(),
                    "maskSize", new Integer[]{3, 5, 7, 9, 11}
            );

            ArrayList<ButtonConfig> buttons = new ArrayList<>();
            buttons.add(new ButtonConfig("Arithmetic Mean", new ArithmeticMeanFilter(), Color.LIGHT_GRAY));

            operations.add(new OperationInfo("Arithmetic Mean Filter",
                    () -> openGeneralOperationInterface("Arithmetic Mean Filter", defs, buttons, combos),
                    OperationCategory.NONLINEARFILTERS, defs, combos));
        }

// Median Filter
        {
            LinkedHashMap<String, Class<?>> defs = new LinkedHashMap<>();
            defs.put("maskType", MaskType.class);
            defs.put("maskSize", Integer.class);

            Map<String, Object[]> combos = Map.of(
                    "maskType", MaskType.values(),
                    "maskSize", new Integer[]{3, 5, 7, 9, 11}
            );

            ArrayList<ButtonConfig> buttons = new ArrayList<>();
            buttons.add(new ButtonConfig("Median", new MedianFilter(), Color.GRAY));

            operations.add(new OperationInfo("Median Filter",
                    () -> openGeneralOperationInterface("Median Filter", defs, buttons, combos),
                    OperationCategory.NONLINEARFILTERS, defs, combos));
        }

// Max Filter
        {
            LinkedHashMap<String, Class<?>> defs = new LinkedHashMap<>();
            defs.put("maskType", MaskType.class);
            defs.put("maskSize", Integer.class);

            Map<String, Object[]> combos = Map.of(
                    "maskType", MaskType.values(),
                    "maskSize", new Integer[]{3, 5, 7, 9, 11}
            );

            ArrayList<ButtonConfig> buttons = new ArrayList<>();
            buttons.add(new ButtonConfig("Maximum", new MaxFilter(), Color.BLUE));

            operations.add(new OperationInfo("Max Filter",
                    () -> openGeneralOperationInterface("Max Filter", defs, buttons, combos),
                    OperationCategory.NONLINEARFILTERS, defs, combos));
        }

// Min Filter
        {
            LinkedHashMap<String, Class<?>> defs = new LinkedHashMap<>();
            defs.put("maskType", MaskType.class);
            defs.put("maskSize", Integer.class);

            Map<String, Object[]> combos = Map.of(
                    "maskType", MaskType.values(),
                    "maskSize", new Integer[]{3, 5, 7, 9, 11}
            );

            ArrayList<ButtonConfig> buttons = new ArrayList<>();
            buttons.add(new ButtonConfig("Minimum", new MinFilter(), Color.MAGENTA));

            operations.add(new OperationInfo("Min Filter",
                    () -> openGeneralOperationInterface("Min Filter", defs, buttons, combos),
                    OperationCategory.NONLINEARFILTERS, defs, combos));
        }

// Mid-Point Filter
        {
            LinkedHashMap<String, Class<?>> defs = new LinkedHashMap<>();
            defs.put("maskType", MaskType.class);
            defs.put("maskSize", Integer.class);

            Map<String, Object[]> combos = Map.of(
                    "maskType", MaskType.values(),
                    "maskSize", new Integer[]{3, 5, 7, 9, 11}
            );

            ArrayList<ButtonConfig> buttons = new ArrayList<>();
            buttons.add(new ButtonConfig("Mid-Point", new MidPointFilter(), Color.CYAN));

            operations.add(new OperationInfo("Mid-Point Filter",
                    () -> openGeneralOperationInterface("Mid-Point Filter", defs, buttons, combos),
                    OperationCategory.NONLINEARFILTERS, defs, combos));
        }

// Alpha-Trimmed Filter
        {
            LinkedHashMap<String, Class<?>> defs = new LinkedHashMap<>();
            defs.put("maskType", MaskType.class);
            defs.put("maskSize", Integer.class);
            defs.put("p", Integer.class);

            Map<String, Object[]> combos = Map.of(
                    "maskType", MaskType.values(),
                    "maskSize", new Integer[]{3, 5, 7, 9, 11}
            );

            ArrayList<ButtonConfig> buttons = new ArrayList<>();
            buttons.add(new ButtonConfig("Alpha-Trimmed", new AlphaTrimmedFilter(), Color.ORANGE));

            operations.add(new OperationInfo("Alpha-Trimmed Filter",
                    () -> openGeneralOperationInterface("Alpha-Trimmed Filter", defs, buttons, combos),
                    OperationCategory.NONLINEARFILTERS, defs, combos));
        }

// Harmonic Mean Filter
        {
            LinkedHashMap<String, Class<?>> defs = new LinkedHashMap<>();
            defs.put("maskType", MaskType.class);
            defs.put("maskSize", Integer.class);

            Map<String, Object[]> combos = Map.of(
                    "maskType", MaskType.values(),
                    "maskSize", new Integer[]{3, 5, 7, 9, 11}
            );

            ArrayList<ButtonConfig> buttons = new ArrayList<>();
            buttons.add(new ButtonConfig("Harmonic Mean", new HarmonicMeanFilter(), Color.DARK_GRAY));

            operations.add(new OperationInfo("Harmonic Mean Filter",
                    () -> openGeneralOperationInterface("Harmonic Mean Filter", defs, buttons, combos),
                    OperationCategory.NONLINEARFILTERS, defs, combos));
        }

// Contra-Harmonic Filter
        {
            LinkedHashMap<String, Class<?>> defs = new LinkedHashMap<>();
            defs.put("maskType", MaskType.class);
            defs.put("maskSize", Integer.class);
            defs.put("q", Double.class);

            Map<String, Object[]> combos = Map.of(
                    "maskType", MaskType.values(),
                    "maskSize", new Integer[]{3, 5, 7, 9, 11}
            );

            ArrayList<ButtonConfig> buttons = new ArrayList<>();
            buttons.add(new ButtonConfig("Contra-Harmonic", new ContraHarmonicFilter(), Color.PINK));

            operations.add(new OperationInfo("Contra-Harmonic Filter",
                    () -> openGeneralOperationInterface("Contra-Harmonic Filter", defs, buttons, combos),
                    OperationCategory.NONLINEARFILTERS, defs, combos));
        }

// Geometric Mean Filter
        {
            LinkedHashMap<String, Class<?>> defs = new LinkedHashMap<>();
            defs.put("maskType", MaskType.class);
            defs.put("maskSize", Integer.class);

            Map<String, Object[]> combos = Map.of(
                    "maskType", MaskType.values(),
                    "maskSize", new Integer[]{3, 5, 7, 9, 11}
            );

            ArrayList<ButtonConfig> buttons = new ArrayList<>();
            buttons.add(new ButtonConfig("Geometric Mean", new GeometricMeanFilter(), Color.GREEN));

            operations.add(new OperationInfo("Geometric Mean Filter",
                    () -> openGeneralOperationInterface("Geometric Mean Filter", defs, buttons, combos),
                    OperationCategory.NONLINEARFILTERS, defs, combos));
        }

// Max-Min Filter
        {
            LinkedHashMap<String, Class<?>> defs = new LinkedHashMap<>();
            defs.put("maskType", MaskType.class);
            defs.put("maskSize", Integer.class);

            Map<String, Object[]> combos = Map.of(
                    "maskType", MaskType.values(),
                    "maskSize", new Integer[]{3, 5, 7, 9, 11}
            );

            ArrayList<ButtonConfig> buttons = new ArrayList<>();
            buttons.add(new ButtonConfig("Max-Min", new MaxMinFilter(), Color.RED));

            operations.add(new OperationInfo("Max-Min Filter",
                    () -> openGeneralOperationInterface("Max-Min Filter", defs, buttons, combos),
                    OperationCategory.NONLINEARFILTERS, defs, combos));
        }
    }

    // Helper to reduce boilerplate
    private void registerColorSpace(String name, OperationFunction toSpace, OperationFunction fromSpace) {
        ArrayList<ButtonConfig> buttons = new ArrayList<>();
        buttons.add(new ButtonConfig("RGB → " + name, toSpace, Color.CYAN));
        buttons.add(new ButtonConfig(name + " → RGB", fromSpace, Color.CYAN));
        operations.add(new OperationInfo(
                "Convertir a " + name,
                () -> openGeneralOperationInterface("Convertir a " + name,
                        new LinkedHashMap<>(), buttons, Collections.emptyMap()),
                OperationCategory.COLOR_SPACES,
                new LinkedHashMap<>(), Collections.emptyMap()
        ));
    }

    private void addOperation(String name,
                              Map<String,Class<?>> argDefs,
                              List<ButtonConfig> buttons,
                              OperationCategory category) {
        operations.add(new OperationInfo(
                name,
                () -> openGeneralOperationInterface(name,
                        new LinkedHashMap<>(argDefs),
                        new ArrayList<>(buttons),
                        Collections.emptyMap()),
                category,
                new LinkedHashMap<>(argDefs),
                Collections.emptyMap()
        ));
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

        button.addActionListener(_ -> {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                action.run();
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        });
        return button;
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
        // Lógica para guardar la imagen (similar a la existente)
    }
}
