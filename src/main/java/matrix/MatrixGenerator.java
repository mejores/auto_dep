package matrix;

import java.util.Random;

/**
 * @ClassName Matrix
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/17/2020 2:47 PM
 * @Version 1.0
 **/
public class MatrixGenerator {
    /**
     * 随机生成一个double值类型的矩阵
     * @param rows
     * @param columns
     * @return
     */
    public static double[][] generate (int rows, int columns) {
        double[][] ret=new double[rows][columns];
        Random random=new Random();
        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {
                //生成0-1的浮点数
                ret[i][j]=random.nextDouble()*10;
            }
        }
        return ret;
    }
}
