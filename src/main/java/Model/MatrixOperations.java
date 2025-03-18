package Model;

public class MatrixOperations {

    /**
     * Suma dos matrices.
     * @param A Primera matriz.
     * @param B Segunda matriz.
     * @return La matriz resultante de la suma.
     * @throws IllegalArgumentException si las dimensiones no coinciden.
     */
    public static double[][] add(double[][] A, double[][] B) {
        int rows = A.length;
        int cols = A[0].length;
        if (B.length != rows || B[0].length != cols) {
            throw new IllegalArgumentException("Las matrices deben tener las mismas dimensiones.");
        }
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }
        return result;
    }

    /**
     * Resta la matriz B de la matriz A.
     * @param A Primera matriz.
     * @param B Segunda matriz.
     * @return La matriz resultante de la resta.
     * @throws IllegalArgumentException si las dimensiones no coinciden.
     */
    public static double[][] subtract(double[][] A, double[][] B) {
        int rows = A.length;
        int cols = A[0].length;
        if (B.length != rows || B[0].length != cols) {
            throw new IllegalArgumentException("Las matrices deben tener las mismas dimensiones.");
        }
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }
        return result;
    }

    /**
     * Multiplicación matricial.
     * @param A Matriz A.
     * @param B Matriz B.
     * @return El producto matricial de A y B.
     * @throws IllegalArgumentException si el número de columnas de A no coincide con el número de filas de B.
     */
    public static double[][] multiply(double[][] A, double[][] B) {
        int aRows = A.length;
        int aCols = A[0].length;
        int bRows = B.length;
        int bCols = B[0].length;
        if (aCols != bRows) {
            throw new IllegalArgumentException("El número de columnas de A debe ser igual al número de filas de B.");
        }
        double[][] result = new double[aRows][bCols];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bCols; j++) {
                for (int k = 0; k < aCols; k++) {
                    result[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return result;
    }

    /**
     * Multiplicación de una matriz por un escalar.
     * @param A Matriz a multiplicar.
     * @param scalar Valor escalar.
     * @return La matriz resultante.
     */
    public static double[][] scalarMultiply(double[][] A, double scalar) {
        int rows = A.length;
        int cols = A[0].length;
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = A[i][j] * scalar;
            }
        }
        return result;
    }

    /**
     * Transpone una matriz.
     * @param A Matriz a transponer.
     * @return La matriz transpuesta.
     */
    public static double[][] transpose(double[][] A) {
        int rows = A.length;
        int cols = A[0].length;
        double[][] result = new double[cols][rows];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                result[j][i] = A[i][j];
            }
        }
        return result;
    }

    /**
     * Multiplicación elemento a elemento (Hadamard).
     * @param A Primera matriz.
     * @param B Segunda matriz.
     * @return La matriz resultante de la multiplicación elemento a elemento.
     * @throws IllegalArgumentException si las dimensiones no coinciden.
     */
    public static double[][] hadamardProduct(double[][] A, double[][] B) {
        int rows = A.length;
        int cols = A[0].length;
        if (B.length != rows || B[0].length != cols) {
            throw new IllegalArgumentException("Las matrices deben tener las mismas dimensiones para la multiplicación elemento a elemento.");
        }
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                result[i][j] = A[i][j] * B[i][j];
            }
        }
        return result;
    }

    /**
     * Aplica una convolución 2D a una imagen con un kernel dado.
     * Se utiliza relleno con ceros para los bordes.
     * @param image La matriz que representa la imagen.
     * @param kernel El kernel de convolución.
     * @return La imagen filtrada.
     */
    public static double[][] convolution(double[][] image, double[][] kernel) {
        int imageRows = image.length;
        int imageCols = image[0].length;
        int kernelRows = kernel.length;
        int kernelCols = kernel[0].length;

        int padRows = kernelRows / 2;
        int padCols = kernelCols / 2;

        double[][] result = new double[imageRows][imageCols];

        for (int i = 0; i < imageRows; i++){
            for (int j = 0; j < imageCols; j++){
                double sum = 0;
                for (int ki = 0; ki < kernelRows; ki++){
                    for (int kj = 0; kj < kernelCols; kj++){
                        int ii = i + ki - padRows;
                        int jj = j + kj - padCols;
                        if (ii >= 0 && ii < imageRows && jj >= 0 && jj < imageCols){
                            sum += image[ii][jj] * kernel[ki][kj];
                        }
                    }
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    /**
     * Calcula la inversa de una matriz cuadrada utilizando eliminación Gauss-Jordan.
     * @param matrix La matriz a invertir.
     * @return La matriz inversa.
     * @throws IllegalArgumentException si la matriz no es cuadrada.
     * @throws ArithmeticException si la matriz no es invertible.
     */
    public static double[][] invert(double[][] matrix) {
        int n = matrix.length;
        if (matrix[0].length != n) {
            throw new IllegalArgumentException("La matriz debe ser cuadrada para poder invertirla.");
        }

        // Crear matriz aumentada [matrix | I]
        double[][] augmented = new double[n][2 * n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                augmented[i][j] = matrix[i][j];
            }
            for (int j = n; j < 2 * n; j++){
                augmented[i][j] = (i == (j - n)) ? 1 : 0;
            }
        }

        // Eliminación Gauss-Jordan
        for (int i = 0; i < n; i++){
            // Buscar el pivote
            double pivot = augmented[i][i];
            if (pivot == 0){
                int swap = i + 1;
                while (swap < n && augmented[swap][i] == 0){
                    swap++;
                }
                if (swap == n) {
                    throw new ArithmeticException("La matriz no es invertible.");
                }
                double[] temp = augmented[i];
                augmented[i] = augmented[swap];
                augmented[swap] = temp;
                pivot = augmented[i][i];
            }

            // Normalizar la fila del pivote
            for (int j = 0; j < 2 * n; j++){
                augmented[i][j] /= pivot;
            }

            // Eliminar el resto
            for (int k = 0; k < n; k++){
                if (k != i){
                    double factor = augmented[k][i];
                    for (int j = 0; j < 2 * n; j++){
                        augmented[k][j] -= factor * augmented[i][j];
                    }
                }
            }
        }

        // Extraer la matriz inversa
        double[][] inverse = new double[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                inverse[i][j] = augmented[i][j + n];
            }
        }
        return inverse;
    }


    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[i].length; j++){
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
