package matrix;

/**
 * @ClassName RowMultiplierTask
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/17/2020 3:57 PM
 * @Version 1.0
 **/
public class RowMultiplierTask implements Runnable {

    private final double[][] matrix1;
    private final double[][] matrix2;

    private final int row;

    private double[][] matrixRes;

    public RowMultiplierTask(double[][] matrix1, double[][] matrix2,double[][] matrixRes, int row ) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.row = row;
        this.matrixRes = matrixRes;
    }

    @Override
    public void run() {
        for (int j = 0; j <matrix2[0].length; j++) {
            matrixRes[row][j] = 0;
            for (int k = 0; k <matrix2.length; k++) {
                matrixRes[row][j]+= matrix1[row][k]*matrix2[k][j];
            }

        }
    }
}
