package anton.task3.controller;

import org.springframework.stereotype.Component;

//  Класс который делит строку на подстроки и возвращает или alias или тело слова
//  Например:
//  Получает:  t.t_partyid as id, t2.t_name1, t.t_shortname
//  Возвращает: [id, t_name1, t_shortname]

@Component
public class ResultColumns {
    public String[] getResultColumns(String sql){
        String[] columns = sql.split(",");

        for(int i = 0; i < columns.length; i++){
            String column = columns[i].trim();
            if(column.charAt(column.length()-1) == ')'){
                for(int j = column.length()-1; j > 0; j--){
                    if(j == 1){
                        columns[i] = column.substring(0 ,column.length()-1);
                        break;
                    }
                    if(column.charAt(j) == '('){
                        columns[i] = column.substring(j+1,column.length()-1);
                        break;
                    }
                }
                break;
            }
            for(int j = column.length()-1; j > 0; j--){
                if(column.charAt(j) == ' ' || column.charAt(j) == '.'){
                    columns[i] = column.substring(j+1);
                    break;
                }

            }
            columns[i] = columns[i].trim();
        }

        return columns;
    }
}
