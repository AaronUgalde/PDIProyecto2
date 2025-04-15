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
                                               List<ButtonConfig> extraButtons) {
        new GeneralOperationInterface2(operationName, argumentDefinitions, imageMatrix, extraButtons).setVisible(true);
    }

    /**
     * Registro de operaciones.
     * Ahora, cada operación se registra invocando al método auxiliar, lo que facilita la adición
     * de nuevas operaciones sin tocar la estructura principal de la interfaz.
     */


    private void registerOperations() {
        // --- Convertir a YIQ ---
        ArrayList<ButtonConfig> buttonsRGB_YIQ = new ArrayList<>();
        buttonsRGB_YIQ.add(new ButtonConfig("Convertir de RGB a YIQ", new RGB_YIQ(), Color.CYAN));
        buttonsRGB_YIQ.add(new ButtonConfig("Convertir de YIQ a RGB", new YIQ_RGB(), Color.CYAN));
        operations.add(new OperationInfo("Convertir a YIQ", () -> {
            openGeneralOperationInterface("Convertir a YIQ",
                    new LinkedHashMap<>(),  // No requiere parámetros
                    buttonsRGB_YIQ
            );
        }, OperationCategory.COLOR_SPACES));

        // --- Convertir a CMY ---
        ArrayList<ButtonConfig> buttonsRGB_CMY = new ArrayList<>();
        buttonsRGB_CMY.add(new ButtonConfig("Convertir de RGB a CMY", new RGB_CMY(), Color.CYAN));
        buttonsRGB_CMY.add(new ButtonConfig("Convertir de CMY a RGB", new CMY_RGB(), Color.CYAN));
        operations.add(new OperationInfo("Convertir a CMY", () -> {
            openGeneralOperationInterface("Convertir a CMY",
                    new LinkedHashMap<>(),  // No requiere parámetros
                    buttonsRGB_CMY
            );
        }, OperationCategory.COLOR_SPACES));

        // --- Convertir a CMYK ---
        ArrayList<ButtonConfig> buttonsRGB_CMYK = new ArrayList<>();
        buttonsRGB_CMYK.add(new ButtonConfig("Convertir de RGB a CMYK", new RGB_CMYK(), Color.CYAN));
        buttonsRGB_CMYK.add(new ButtonConfig("Convertir de CMYK a RGB", new CMYK_RGB(), Color.CYAN));
        operations.add(new OperationInfo("Convertir a CMYK", () -> {
            openGeneralOperationInterface("Convertir a CMYK",
                    new LinkedHashMap<>(),  // No requiere parámetros
                    buttonsRGB_CMYK
            );
        }, OperationCategory.COLOR_SPACES));

        // --- Convertir a HSI ---
        ArrayList<ButtonConfig> buttonsRGB_HSI = new ArrayList<>();
        buttonsRGB_HSI.add(new ButtonConfig("Convertir de RGB a HSI", new RGB_HSI(), Color.CYAN));
        buttonsRGB_HSI.add(new ButtonConfig("Convertir de HSI a RGB", new HSI_RGB(), Color.CYAN));
        operations.add(new OperationInfo("Convertir a HSI", () -> {
            openGeneralOperationInterface("Convertir a HSI",
                    new LinkedHashMap<>(),  // No requiere parámetros
                    buttonsRGB_HSI
            );
        }, OperationCategory.COLOR_SPACES));

        // --- Convertir a HSV ---
        ArrayList<ButtonConfig> buttonsRGB_HSV = new ArrayList<>();
        buttonsRGB_HSV.add(new ButtonConfig("Convertir de RGB a HSV", new RGB_HSV(), Color.CYAN));
        buttonsRGB_HSV.add(new ButtonConfig("Convertir de HSV a RGB", new HSV_RGB(), Color.CYAN));
        operations.add(new OperationInfo("Convertir a HSV", () -> {
            openGeneralOperationInterface("Convertir a HSV",
                    new LinkedHashMap<>(),  // No requiere parámetros
                    buttonsRGB_HSV
            );
        }, OperationCategory.COLOR_SPACES));

        // --- Convertir a LAB ---
        ArrayList<ButtonConfig> buttonsRGB_LAB = new ArrayList<>();
        buttonsRGB_LAB.add(new ButtonConfig("Convertir de RGB a LAB", new RGB_LAB(), Color.CYAN));
        buttonsRGB_LAB.add(new ButtonConfig("Convertir de LAB a RGB", new LAB_RGB(), Color.CYAN));
        operations.add(new OperationInfo("Convertir a LAB", () -> {
            openGeneralOperationInterface("Convertir a LAB",
                    new LinkedHashMap<>(),  // No requiere parámetros
                    buttonsRGB_LAB
            );
        }, OperationCategory.COLOR_SPACES));

        // --- Convertir a LMS ---
        ArrayList<ButtonConfig> buttonsRGB_LMS = new ArrayList<>();
        buttonsRGB_LMS.add(new ButtonConfig("Convertir de RGB a LMS", new RGB_LMS(), Color.CYAN));
        buttonsRGB_LMS.add(new ButtonConfig("Convertir de LMS a RGB", new LMS_RGB(), Color.CYAN));
        operations.add(new OperationInfo("Convertir a LMS", () -> {
            openGeneralOperationInterface("Convertir a LMS",
                    new LinkedHashMap<>(),  // No requiere parámetros
                    buttonsRGB_LMS
            );
        }, OperationCategory.COLOR_SPACES));

        // Aquí puedes seguir agregando más operaciones si así lo deseas...
        // -------------- Operaciones de Binarización --------------

// Binarizar con 1 umbral
        LinkedHashMap<String, Class<?>> binOneParams = new LinkedHashMap<>();
        binOneParams.put("threshold", Double.class);

        ArrayList<ButtonConfig> binOneButtons = new ArrayList<>();
        binOneButtons.add(new ButtonConfig(
                "Binarizar (1 umbral)",
                new BinarizeOneThreshold(),
                Color.MAGENTA // Elige el color que desees
        ));
        binOneButtons.add(new ButtonConfig("Invertir Binarizacion", new InvertBinarization(), Color.CYAN));
        binOneButtons.add(new ButtonConfig("Regresar a RGB", new YIQ_RGB(), Color.CYAN));

        operations.add(new OperationInfo("Binarizar (1 umbral)", () -> {
            openGeneralOperationInterface(
                    "Binarizar (1 umbral)",
                    binOneParams,       // Aquí definimos el tipo de los parámetros
                    binOneButtons       // Botón que ejecuta la operación
            );
        }, OperationCategory.BINARIZATION));

// Binarizar con 2 umbrales
        LinkedHashMap<String, Class<?>> binTwoParams = new LinkedHashMap<>();
        binTwoParams.put("threshold1", Double.class);
        binTwoParams.put("threshold2", Double.class);

        ArrayList<ButtonConfig> binTwoButtons = new ArrayList<>();
        binTwoButtons.add(new ButtonConfig(
                "Binarizar (2 umbrales)",
                new BinarizeTwoThresholds(),
                Color.MAGENTA
        ));
        binTwoButtons.add(new ButtonConfig("Invertir Binarizacion", new InvertBinarization(), Color.CYAN));
        binTwoButtons.add(new ButtonConfig("Regresar a RGB", new YIQ_RGB(), Color.CYAN));

        operations.add(new OperationInfo("Binarizar (2 umbrales)", () -> {
            openGeneralOperationInterface(
                    "Binarizar (2 umbrales)",
                    binTwoParams,
                    binTwoButtons
            );
        }, OperationCategory.BINARIZATION));

// Binarizar con 3 umbrales
        LinkedHashMap<String, Class<?>> binThreeParams = new LinkedHashMap<>();
        binThreeParams.put("threshold1", Double.class);
        binThreeParams.put("threshold2", Double.class);
        binThreeParams.put("threshold3", Double.class);

        ArrayList<ButtonConfig> binThreeButtons = new ArrayList<>();
        binThreeButtons.add(new ButtonConfig(
                "Binarizar (3 umbrales)",
                new BinarizeThreeThresholds(),
                Color.MAGENTA
        ));
        binThreeButtons.add(new ButtonConfig("Invertir Binarizacion", new InvertBinarization(), Color.CYAN));
        binThreeButtons.add(new ButtonConfig("Regresar a RGB", new YIQ_RGB(), Color.CYAN));

        operations.add(new OperationInfo("Binarizar (3 umbrales)", () -> {
            openGeneralOperationInterface(
                    "Binarizar (3 umbrales)",
                    binThreeParams,
                    binThreeButtons
            );
        }, OperationCategory.BINARIZATION));


        LinkedHashMap<String, Class<?>> interpolationParams = new LinkedHashMap<>();
        interpolationParams.put("scale", Double.class);

        ArrayList<ButtonConfig> interpolationButtons = new ArrayList<>();
        interpolationButtons.add(new ButtonConfig(
                "Interpolación",
                new Interpolation(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Interpolación", () -> {
            openGeneralOperationInterface(
                    "Interpolación",
                    interpolationParams,
                    interpolationButtons
            );
        }, OperationCategory.GEOMETRICOPERATIONS ));


        LinkedHashMap<String, Class<?>> rotationParams = new LinkedHashMap<>();
        rotationParams.put("angle", Double.class);

        ArrayList<ButtonConfig> rotationButtons = new ArrayList<>();
        rotationButtons.add(new ButtonConfig(
                "Rotation",
                new Rotation(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Rotación", () -> {
            openGeneralOperationInterface(
                    "Rotación",
                    rotationParams,
                    rotationButtons
            );
        }, OperationCategory.GEOMETRICOPERATIONS ));


        LinkedHashMap<String, Class<?>> translatioParams = new LinkedHashMap<>();
        translatioParams.put("tx", Integer.class);
        translatioParams.put("ty", Integer.class);

        ArrayList<ButtonConfig> translationButtons = new ArrayList<>();
        translationButtons.add(new ButtonConfig(
                "Translación",
                new Translation(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Translación", () -> {
            openGeneralOperationInterface(
                    "Translación",
                    translatioParams,
                    translationButtons
            );
        }, OperationCategory.GEOMETRICOPERATIONS ));


        LinkedHashMap<String, Class<?>> multiplicationParams = new LinkedHashMap<>();
        multiplicationParams.put("image", String.class);

        ArrayList<ButtonConfig> multiplicationButtons = new ArrayList<>();
        multiplicationButtons.add(new ButtonConfig(
                "Multiplicación",
                new Multiplication(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Multiplicación", () -> {
            openGeneralOperationInterface(
                    "Multiplicación",
                    multiplicationParams,
                    multiplicationButtons
            );
        }, OperationCategory.ARITHMETICOPERATIONS ));


        LinkedHashMap<String, Class<?>> divisionParams = new LinkedHashMap<>();
        divisionParams.put("image", String.class);

        ArrayList<ButtonConfig> divisionButtons = new ArrayList<>();
        divisionButtons.add(new ButtonConfig(
                "Division",
                new Division(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Division", () -> {
            openGeneralOperationInterface(
                    "Division",
                    divisionParams,
                    divisionButtons
            );
        }, OperationCategory.ARITHMETICOPERATIONS ));


        LinkedHashMap<String, Class<?>> substractionParams = new LinkedHashMap<>();
        substractionParams.put("image", String.class);

        ArrayList<ButtonConfig> substractionButtons = new ArrayList<>();
        substractionButtons.add(new ButtonConfig(
                "Resta",
                new Substraction(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Resta", () -> {
            openGeneralOperationInterface(
                    "Resta",
                    substractionParams,
                    substractionButtons
            );
        }, OperationCategory.ARITHMETICOPERATIONS));


        LinkedHashMap<String, Class<?>> sumParams = new LinkedHashMap<>();
        sumParams.put("image", String.class);

        ArrayList<ButtonConfig> sumButtons = new ArrayList<>();
        sumButtons.add(new ButtonConfig(
                "Suma",
                new Sum(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Suma", () -> {
            openGeneralOperationInterface(
                    "Suma",
                    sumParams,
                    sumButtons
            );
        }, OperationCategory.ARITHMETICOPERATIONS));

        LinkedHashMap<String, Class<?>> andParams = new LinkedHashMap<>();
        andParams.put("image", String.class);

        ArrayList<ButtonConfig> andButtons = new ArrayList<>();
        andButtons.add(new ButtonConfig(
                "AND",
                new And(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("AND", () -> {
            openGeneralOperationInterface(
                    "AND",
                    andParams,
                    andButtons
            );
        }, OperationCategory.LOGICOPERATIONS));


        LinkedHashMap<String, Class<?>> notParams = new LinkedHashMap<>();

        ArrayList<ButtonConfig> notButtons = new ArrayList<>();
        notButtons.add(new ButtonConfig(
                "NOT",
                new Not(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("NOT", () -> {
            openGeneralOperationInterface(
                    "NOT",
                    notParams,
                    notButtons
            );
        }, OperationCategory.LOGICOPERATIONS));



        LinkedHashMap<String, Class<?>> orParams = new LinkedHashMap<>();
        orParams.put("image", String.class);

        ArrayList<ButtonConfig> orButtons = new ArrayList<>();
        orButtons.add(new ButtonConfig(
                "OR",
                new Or(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("OR", () -> {
            openGeneralOperationInterface(
                    "OR",
                    orParams,
                    orButtons
            );
        }, OperationCategory.LOGICOPERATIONS));


        LinkedHashMap<String, Class<?>> xorParams = new LinkedHashMap<>();
        xorParams.put("image", String.class);

        ArrayList<ButtonConfig> xorButtons = new ArrayList<>();
        xorButtons.add(new ButtonConfig(
                "XOR",
                new Xor(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("XOR", () -> {
            openGeneralOperationInterface(
                    "XOR",
                    xorParams,
                    xorButtons
            );
        }, OperationCategory.LOGICOPERATIONS));


        LinkedHashMap<String, Class<?>> equalParams = new LinkedHashMap<>();
        equalParams.put("image", String.class);

        ArrayList<ButtonConfig> equalButtons = new ArrayList<>();
        equalButtons.add(new ButtonConfig(
                "Equal",
                new Equal(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Equal", () -> {
            openGeneralOperationInterface(
                    "Equal",
                    equalParams,
                    equalButtons
            );
        }, OperationCategory.RELATIONALOPERATIONS));

        LinkedHashMap<String, Class<?>> greaterOrEqualParams = new LinkedHashMap<>();
        greaterOrEqualParams.put("image", String.class);

        ArrayList<ButtonConfig> greaterOrEqualButtons = new ArrayList<>();
        greaterOrEqualButtons.add(new ButtonConfig(
                "Greater or equal",
                new GreaterOrEqual(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Greater or equal", () -> {
            openGeneralOperationInterface(
                    "Greater or equal",
                    greaterOrEqualParams,
                    greaterOrEqualButtons
            );
        }, OperationCategory.RELATIONALOPERATIONS));


        LinkedHashMap<String, Class<?>> greatherThanParams = new LinkedHashMap<>();
        greatherThanParams.put("image", String.class);

        ArrayList<ButtonConfig> greatherThanButtons = new ArrayList<>();
        greatherThanButtons.add(new ButtonConfig(
                "Greather than",
                new GreaterThan(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Greather than", () -> {
            openGeneralOperationInterface(
                    "Greather than",
                    greatherThanParams,
                    greatherThanButtons
            );
        }, OperationCategory.RELATIONALOPERATIONS));


        LinkedHashMap<String, Class<?>> lessOrEqualParams = new LinkedHashMap<>();
        lessOrEqualParams.put("image", String.class);

        ArrayList<ButtonConfig> lessOrEqualButtons = new ArrayList<>();
        lessOrEqualButtons.add(new ButtonConfig(
                "Less or equal",
                new LessOrEqual(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Less or equal", () -> {
            openGeneralOperationInterface(
                    "Less or equal",
                    lessOrEqualParams,
                    lessOrEqualButtons
            );
        }, OperationCategory.RELATIONALOPERATIONS));


        LinkedHashMap<String, Class<?>> lessThanParams = new LinkedHashMap<>();
        lessThanParams.put("image", String.class);

        ArrayList<ButtonConfig> lessThanButtons = new ArrayList<>();
        lessThanButtons.add(new ButtonConfig(
                "Less than",
                new LessThan(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Less than", () -> {
            openGeneralOperationInterface(
                    "Less than",
                    lessThanParams,
                    lessThanButtons
            );
        }, OperationCategory.RELATIONALOPERATIONS));


        LinkedHashMap<String, Class<?>> notEqualParams = new LinkedHashMap<>();
        notEqualParams.put("image", String.class);

        ArrayList<ButtonConfig> notEqualButtons = new ArrayList<>();
        notEqualButtons.add(new ButtonConfig(
                "Not equal",
                new NotEqual(),
                Color.MAGENTA
        ));

        operations.add(new OperationInfo("Not equal", () -> {
            openGeneralOperationInterface(
                    "Not equal",
                    notEqualParams,
                    notEqualButtons
            );
        }, OperationCategory.RELATIONALOPERATIONS));

        // Registro para ShiftOperation
        LinkedHashMap<String, Class<?>> shiftParams = new LinkedHashMap<>();
        shiftParams.put("shift", Double.class);

        ArrayList<ButtonConfig> shiftButtons = new ArrayList<>();
        shiftButtons.add(new ButtonConfig(
                "Shift",
                new Shift(),
                Color.CYAN
        ));
        operations.add(new OperationInfo("Shift", () -> {
            openGeneralOperationInterface("Shift", shiftParams, shiftButtons);
        }, OperationCategory.HISTOGRAMOPERATIONS));

// Registro para ScaleOperation
        LinkedHashMap<String, Class<?>> scaleParams = new LinkedHashMap<>();
        scaleParams.put("factor", Double.class);

        ArrayList<ButtonConfig> scaleButtons = new ArrayList<>();
        scaleButtons.add(new ButtonConfig(
                "Scale",
                new Scale(),
                Color.ORANGE
        ));
        operations.add(new OperationInfo("Scale", () -> {
            openGeneralOperationInterface("Scale", scaleParams, scaleButtons);
        }, OperationCategory.HISTOGRAMOPERATIONS));

// Registro para HistogramMatching
        LinkedHashMap<String, Class<?>> matchParams = new LinkedHashMap<>();
        matchParams.put("image", String.class);

        ArrayList<ButtonConfig> matchButtons = new ArrayList<>();
        matchButtons.add(new ButtonConfig(
                "Histogram Matching",
                new Matching(),
                Color.MAGENTA
        ));
        operations.add(new OperationInfo("Histogram Matching", () -> {
            openGeneralOperationInterface("Histogram Matching", matchParams, matchButtons);
        }, OperationCategory.HISTOGRAMOPERATIONS));

        // Registro para UniformEqualization
        LinkedHashMap<String, Class<?>> uniformParams = new LinkedHashMap<>();

        ArrayList<ButtonConfig> uniformButtons = new ArrayList<>();
        uniformButtons.add(new ButtonConfig(
                "Uniform Equalization",
                new UniformEqualization(),
                Color.BLUE
        ));
        operations.add(new OperationInfo("Uniform Equalization", () -> {
            openGeneralOperationInterface("Uniform Equalization", uniformParams, uniformButtons);
        }, OperationCategory.HISTOGRAMOPERATIONS));

        // Registro para ExponentialEqualization
        LinkedHashMap<String, Class<?>> exponentialParams = new LinkedHashMap<>();
        exponentialParams.put("alpha", Double.class);

        ArrayList<ButtonConfig> exponentialButtons = new ArrayList<>();
        exponentialButtons.add(new ButtonConfig(
                "Exponential Equalization",
                new ExponentialEqualization(),
                Color.CYAN
        ));
        operations.add(new OperationInfo("Exponential Equalization", () -> {
            openGeneralOperationInterface("Exponential Equalization", exponentialParams, exponentialButtons);
        }, OperationCategory.HISTOGRAMOPERATIONS));

        // Registro para RayleighEqualization
        LinkedHashMap<String, Class<?>> rayleighParams = new LinkedHashMap<>();
        rayleighParams.put("alpha", Double.class);

        ArrayList<ButtonConfig> rayleighButtons = new ArrayList<>();
        rayleighButtons.add(new ButtonConfig(
                "Rayleigh Equalization",
                new RayleighEqualization(),
                Color.MAGENTA
        ));
        operations.add(new OperationInfo("Rayleigh Equalization", () -> {
            openGeneralOperationInterface("Rayleigh Equalization", rayleighParams, rayleighButtons);
        }, OperationCategory.HISTOGRAMOPERATIONS));

        // Registro para HyperbolicRaicesEqualization
        LinkedHashMap<String, Class<?>> hyperRaicesParams = new LinkedHashMap<>();
        hyperRaicesParams.put("pot", Double.class);

        ArrayList<ButtonConfig> hyperRaicesButtons = new ArrayList<>();
        hyperRaicesButtons.add(new ButtonConfig(
                "Hyperbolic Raices Equalization",
                new HyperbolicRaicesEqualization(),
                Color.ORANGE
        ));
        operations.add(new OperationInfo("Hyperbolic Raices Equalization", () -> {
            openGeneralOperationInterface("Hyperbolic Raices Equalization", hyperRaicesParams, hyperRaicesButtons);
        }, OperationCategory.HISTOGRAMOPERATIONS));

        // Registro para HyperbolicLogEqualization
        LinkedHashMap<String, Class<?>> hyperLogParams = new LinkedHashMap<>();

        ArrayList<ButtonConfig> hyperLogButtons = new ArrayList<>();
        hyperLogButtons.add(new ButtonConfig(
                "Hyperbolic Log Equalization",
                new HyperbolicLogEqualization(),
                Color.GREEN
        ));
        operations.add(new OperationInfo("Hyperbolic Log Equalization", () -> {
            openGeneralOperationInterface("Hyperbolic Log Equalization", hyperLogParams, hyperLogButtons);
        }, OperationCategory.HISTOGRAMOPERATIONS));

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
