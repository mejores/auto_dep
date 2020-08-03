package matrix;

import java.util.Arrays;
import java.util.Date;

/**
 * @ClassName SerialMain
 * @Description
 * @Author Fernando Juan(joven)
 * @Date 7/17/2020 2:52 PM
 * @Version 1.0
 **/
public class SerialMain {
    public static void main(String[] args) {

        double matrix1[][] = MatrixGenerator.generate(3, 5);
        double matrix2[][] = MatrixGenerator.generate(5, 6);
        System.out.println(Arrays.toString(matrix1));
        System.out.println(Arrays.toString(matrix2));
        double resultSerial[][]= new double[matrix1.length]
                [matrix2[0].length];

        Date start=new Date();
        System.out.println("开始计算");
        SerialMultiplier.multiply(matrix1, matrix2, resultSerial);
        Date end=new Date();
        System.out.printf("Serial: %d%n",end.getTime()-start.getTime());
        int rows = resultSerial.length;
        int columns = resultSerial[0].length;
        for(int i = 0; i<rows;i++){
            System.out.print("|");
            for (int j = 0; j < columns; j++) {
                System.out.print("***"+resultSerial[i][j]+"***");
            }
            System.out.println("|");
        }
    }
}
