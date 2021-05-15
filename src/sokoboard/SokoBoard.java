/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoboard;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author hansschaa
 */
public class SokoBoard 
{
    public static int width, height;
    public static TemplateFactory templateFactory;
    public static Random random = new Random();
    public static FloodFill floodFill = new FloodFill();
    public static char WhiteSpace = '0';
    public static char StandarSpace = '9';
    public static int levelCount = 5;
    public static ArrayList<char[][]> levels = new ArrayList<>();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException 
    {
        //Get templates
        templateFactory = new TemplateFactory();

        //BuildLevel
        for(int i = 0 ; i < levelCount; i++)
        {
            //Setup Board Dimensions 
            SetupDimension();
            char[][] levelGenerated = null;
            
            do
            {
                levelGenerated = GenerateLevel();
            }while(levelGenerated == null);

            levels.add(levelGenerated);
        }
        
        PrintLevels();
    }
    
    private static char[][] GenerateLevel() 
    {
        char[][] level = new char[height][width];
        FillWEmpty(level);

        ArrayList<char[][]> templatesCandidates = new ArrayList<>();
        
        //Hay que ser precavido con los limites
        for(int i = 0; i+5 <= height; i+=3)
        {
            for(int j = 0 ; j+5 <= width ; j+=3)
            {
                for(Template template : templateFactory.templates)
                {
                    //Sacamos una copia del primer template
                    var templateTemp = template.Clone();
                    
                    //Get tiles en el borde
                    var bounds = new char[4][5];
                    bounds[0] = GetBound(level, i, j, Dir.TOP);
                    bounds[1] = GetBound(level, i, j, Dir.RIGHT);
                    bounds[2] = GetBound(level, i, j, Dir.BOTTOM);
                    bounds[3] = GetBound(level, i, j, Dir.LEFT);
                   
                    for(int r = 0 ; r < 4 ; r++)
                    {
                        //Rotamos el template
                        templateTemp.Rotate(r);
                        
                        if(TryPut(templateTemp.template, bounds))
                            templatesCandidates.add(templateTemp.template);

                        //Flipx
                        templateTemp.FlipX();
                        if(TryPut(templateTemp.template, bounds))
                            templatesCandidates.add(templateTemp.template);
                        
                        //Flipy
                        templateTemp.FlipY();
                        if(TryPut(templateTemp.template, bounds))
                            templatesCandidates.add(templateTemp.template);
                    }
                }
                
                //No hay ningún candidato :(
                if(templatesCandidates.size() == 0)
                    return null;
                
                else
                {
                    var randomTemplateIndex = random.nextInt(templatesCandidates.size());
                    var chosenCandidate = templatesCandidates.get(randomTemplateIndex);
                
                    PutCandidate(chosenCandidate, level, i, j);

                    templatesCandidates.clear();
                }
            }
        }
        
        RepairBounds(level);

        if(IsAllConnected(level))
            return level;
        
        else
            return null;
    }
    
    private static void RepairBounds(char[][] level) 
    {
        //Top
        for(int j = 0 ; j < width; j++)
            level[0][j] = '#';
        
        //Bottom
        for(int j = 0 ; j < width; j++)
            level[height-1][j] = '#';
        
        //left
        for(int i = 0 ; i < height; i++)
            level[i][0] = '#';
        
        //right
        for(int i = 0 ; i < height; i++)
            level[i][width-1] = '#';
    }
    
    private static boolean IsAllConnected(char[][] level) 
    {
        var result = floodFill.TryConnected(level);
        var maxWhiteSpaces = floodFill.GetWhiteSpaces(level);
        
        return maxWhiteSpaces == result.size();
    }

    private static boolean TryPut(char[][] template,char[][] bounds) 
    {
        var lenght = template.length;
           
        //Check top
        for(int j = 0 ; j < lenght; j++)
        {
            if(template[0][j] == ' ' && bounds[0][j] != ' ')
                return false;
        }
        
        //Check bottom
        for(int j = 0 ; j < lenght; j++)
        {
            if(template[lenght-1][j] == ' ' && bounds[2][j] != ' ')
                return false;
        }
        
        //Check left
        for(int i = 0 ; i < lenght; i++)
        {
            if(template[i][0] == ' ' && bounds[3][i] != ' ')
                return false;
        }
        
        //Check right
        for(int i = 0 ; i < lenght; i++)
        {
            if(template[i][lenght-1] == ' ' && bounds[1][i] != ' ')
                return false;
        }
 
        return true;
    }
    
    private static void PutCandidate(char[][] chosenCandidate, char[][] level, int i , int j) 
    {
        int maxI = i + chosenCandidate.length;
        int maxJ = j + chosenCandidate.length;
        
        int contI=0 ,contJ = 0;
        for(int ii = i ; ii < maxI ; ii++)
        {
            for(int jj = j ; jj < maxJ ; jj++)
            {   
                if(level[ii][jj] == ' ' && chosenCandidate[contI][contJ]== '0' || level[ii][jj] == '#' && chosenCandidate[contI][contJ]== '0')
                {
                    contJ++;
                    continue;
                }
                    
                
                level[ii][jj] = chosenCandidate[contI][contJ];
                contJ++;
            }
            contJ = 0;
            contI++;
        }
    }
    
    private static char[] GetBound(char[][] level, int i, int j, Dir dir) 
    {
        char[] bound = new char[5];
        int maxJ = j + 5;
        int maxI = i + 5;
        int cont = 0;

        switch(dir)
        {
            case TOP:
                if(i == 0)
                    return new char[]{'9','9','9','9','9'};
                
                for(int jj = j ; jj < maxJ; jj++)
                {
                    bound[cont] = level[i-1][jj];
                    cont++;
                }
                    
                
                break;
                
            case BOTTOM:
                if(i == height - 5)
                    return new char[]{'9','9','9','9','9'};
                
                for(int jj = j ; jj < maxJ; jj++)
                {
                    bound[cont] = level[i+1][jj];
                    cont++;
                }
                break;
                
            case LEFT:
                if(j == 0)
                    return new char[]{'9','9','9','9','9'};
                
                for(int ii = i ; ii < maxI; ii++)
                {
                    bound[cont] = level[ii][j-1];
                    cont++;
                }
                
                break;
            
            case RIGHT:
                if(j == level[0].length-5)
                    return new char[]{'9','9','9','9','9'};
                
                for(int ii = i ; ii < maxI; ii++)
                {
                    bound[cont] = level[ii][j+1];
                    cont++;
                }
                
                break;           
        }
        
        return bound;
    }

    private static void ShowLevel(char[][] level) 
    {
        for(int i = 0; i < level.length;i++)
        {
            for(int j = 0 ; j < level[0].length;j++)
                System.out.print(level[i][j]);
            
            System.out.println();
        }
        
        System.out.println();
    }    

    private static void FillWEmpty(char[][] level)
    {
        for(int i = 0; i < height;i++)
        {
            for(int j = 0 ; j < width;j++)
               level[i][j] = StandarSpace;
        }
    }    
    
    public static void PrintLevels()
    {
        for(char[][] level : levels)
            ShowLevel(level);
    }
    
    private static void SetupDimension() 
    {
        Random r = new Random();
        width = r.nextInt(2)+2;
        height = r.nextInt(2)+2;
        
        width*=3;
        height*=3;
        
        width+=2;
        height+=2;       
    }
}
