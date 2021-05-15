/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoboard;

import java.util.Arrays;

/**
 *
 * @author hansschaa
 */
public class Template 
{
    public char[][] template;

    public Template(char[][] template) {
        this.template = template;
    }

    void Print(char[][] template) 
    {
        for(int i = 0 ; i < template.length;i++)
        {
            for(int j = 0 ; j < template.length;j++)
            {
                System.out.print(template[i][j]);
            }
            
            System.out.println();
        }
    }
    
    public Template Clone()
    {
        var clone = new Template(Arrays.stream(template).map(char[]::clone).toArray(char[][]::new));
        return clone;
    }
    
    //Rota 90° en sentido horario
    public void Rotate(int count) 
    {
        for(int k = 0 ; k < count; k++)
        {
            int n = template.length;
            for (int i = 0; i < (n + 1) / 2; i++) 
            {
                for (int j = 0; j < n / 2; j++) 
                {
                    char temp = template[n - 1 - j][i];
                    template[n - 1 - j][i] = template[n - 1 - i][n - j - 1];
                    template[n - 1 - i][n - j - 1] = template[j][n - 1 - i];
                    template[j][n - 1 - i] = template[i][j];
                    template[i][j] = temp;
                }
            }
        }
    }

    public void FlipX() 
    {
        var length = template.length;
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length/2; y++) {
                char tmp = template[x][length - y - 1];
                template[x][length - y - 1] = template[x][y];
                template[x][y]= tmp;
            }
        }
}   
    
    public void FlipY() 
    {
        var length = template.length;
        for (int y = 0; y < length; y++) {
            for (int x = 0; x < length/2; x++) {
                char tmp = template[length-x-1][y];
                template[length-x-1][y] = template[x][y];
                template[x][y]= tmp;
            }
        }
    }
}
