package matrix;

/**
 * @ClassName SerialMultiplier
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/17/2020 2:49 PM
 * @Version 1.0
 **/
public class SerialMultiplier {
    /**
     * 矩阵乘法
     * @param matrix1
     * @param matrix2
     * @param result
     */
    public static void multiply (double[][] matrix1, double[][] matrix2,
                                 double[][] result) {
        //A有多少行--C有多少行
        int rows1=matrix1.length;
        //A有多少列（B有多少行）
        int columns1=matrix1[0].length;
        //B用多少列--C有多少列
        int columns2=matrix2[0].length;

        for (int i=0; i<rows1; i++) {
            for (int j=0; j<columns2; j++) {
                result[i][j]=0;
                for (int k=0; k<columns1; k++) {
                    result[i][j]+=matrix1[i][k]*matrix2[k][j];
                }
            }
        }
    }


    double[][] mul (double[][] matrixA, double[][] matrixB){
        //矩阵A有多少 行
        int matrixARows = matrixA.length;
        //矩阵A又少列，也就是矩阵B有多少行
        int matrixAcolumns = matrixA[0].length;
        int matrixBcolumns = matrixB[0].length;
        double[][] matrixC = new double[matrixARows][matrixBcolumns];
        //矩阵C就是矩阵A的行，和矩阵B的列数
        for (int i=0; i <= matrixARows;i++){
            for (int j=0;j<matrixBcolumns;j++){
                //初始化为0
                 matrixC[i][j] = 0;
                 for(int k=0;k<matrixAcolumns;k++){
                     matrixC[i][j] += matrixA[i][k]*matrixB[k][j];
                 }
            }
        }
        return matrixC;
    }
}
