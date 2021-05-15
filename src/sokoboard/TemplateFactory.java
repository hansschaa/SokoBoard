/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author hansschaa
 */
public class TemplateFactory 
{
    public ArrayList<Template> templates;
    public int templateDim = 5;
    
    public TemplateFactory() throws FileNotFoundException
    {
        LoadTemplates();
    }
    
    public void LoadTemplates() throws FileNotFoundException
    {
        templates = new ArrayList<Template>();

        File file = new File(System.getProperty("user.dir")+"\\templates.txt");
        Scanner sc = new Scanner(file);

        char[][] template = new char[templateDim][templateDim];
        String line;
        int cont =0;
        while (sc.hasNextLine())
        {
            line = sc.nextLine();

            if(line.length() == 0) continue;
            
            for(int j = 0 ; j < line.length(); j++)
                template[cont][j] = line.charAt(j);
            
            cont++;
            
            if(cont == templateDim)
            {
                cont=0;
                templates.add(new Template(template));
                template = new char[templateDim][templateDim];
            }            
        }
    }
}
