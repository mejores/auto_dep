package matrix;

/**
 * @ClassName IndividualMultiplierTask
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/17/2020 3:34 PM
 * @Version 1.0
 **/
public class IndividualMultiplierTask implements Runnable {
    private final double[][] result;
    private final double[][] matrix1;
    private final double[][] matrix2;

    //计算第几行第几列个数值
    private final int row;
    private final int column;

    public IndividualMultiplierTask(double[][] result, double[][] matrix1, double[][] matrix2,
                                    int i, int j){
        this.result = result;
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.row = i;
        this.column = j;
    }

    @Override
    public void run() {
        for (int i = 0; i <matrix2.length; i++) {
            result[row][column] = matrix1[row][i]*matrix2[i][column];
        }
    }
}
