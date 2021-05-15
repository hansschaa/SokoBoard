/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoboard;

import java.util.ArrayList;

/**
 *
 * @author hansschaa
 */
public class FloodFill 
{
    public ArrayList<Coordenate> visited;
    public int[][] visitedAux;
            

    public FloodFill() 
    {
        visited = new ArrayList<Coordenate>();
        
    }
    
    public ArrayList<Coordenate> TryConnected(char[][] level)
    {
        if(visited.size() > 0)
            visited.clear();
        
        visitedAux = new int[level.length][level[0].length];
        
        var iMax = level.length;
        var jMax = level[0].length;
        
        Fill(level, GetAnyWhiteSpace(level));
       
        
        return visited;
    }
    
    private void Fill(char[][] level, Coordenate coordenate)
    {
        if(coordenate.x < 0 || coordenate.x > level.length || coordenate.y < 0 || coordenate.y > level[0].length)
            return;
        
        if(visitedAux[coordenate.x][coordenate.y] == 1)
            return;

        int i = coordenate.x;
        int j = coordenate.y;
        
        visitedAux[i][j] = 1;
        
        
        if (level[i][j] == ' ')
        {
            visited.add(coordenate);
            
            Fill(level, new Coordenate(i + 1, j));
            Fill(level, new Coordenate(i - 1, j));
            Fill(level, new Coordenate(i, j + 1));
            Fill(level, new Coordenate(i, j - 1));
        }
    }
    
    public int GetWhiteSpaces(char[][] level)
    {
        int count = 0;
        for(int i = 0 ; i < level.length; i++)
        {
            for(int j = 0 ; j < level[0].length; j++)
            {
                if(level[i][j] == ' ')
                    count++;
            }
        }
        
        return count;
    }
    
    public Coordenate GetAnyWhiteSpace(char[][] level)
    {
        var iMax = level.length;
        var jMax = level[0].length;
        
        for(int i = 0 ; i < iMax; i++)
        {
            for(int j = 0 ; j < jMax; j++)
            {
                if(level[i][j] == ' ')
                    return new Coordenate(i,j);
            }
        }
        return null;
    }
    
}
