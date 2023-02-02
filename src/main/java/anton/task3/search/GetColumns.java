package anton.task3.search;

import anton.task3.selectcut.SelectCut;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Возвращает колонки из выражения со скобками если нет alias
// Если alias есть возвращает alias
// Например: (select p.address as ard from dperson_dbt p where p.personid = t.t_partyid)
// Вернёт: ard

@Component
public class GetColumns {
    public String getColumn(String sqlQueryWithBracket){
        SelectCut selectCut = new SelectCut();
        Pattern selectPattern = Pattern.compile("\\(select |\\( select ");
        Matcher selectMatcher = selectPattern.matcher(sqlQueryWithBracket);

        List<String> stringsQuery = new ArrayList<>();
        StringBuilder columnsSubQuery = new StringBuilder();
        if (selectMatcher.find()){
            columnsSubQuery.append(selectCut.cutSelectQuery(sqlQueryWithBracket).trim());
        }else {
            columnsSubQuery.append(sqlQueryWithBracket);
        }


        Pattern aliasPattern = Pattern.compile("as\\s*\\w*");
        Matcher aliasMatcher = aliasPattern.matcher(columnsSubQuery);
        if (aliasMatcher.find()){
            columnsSubQuery = new StringBuilder(aliasMatcher.group().substring(3));
            return columnsSubQuery.toString();
        }
        int startIndex = 1;
        for(int i = 0; i < columnsSubQuery.length()-1; i++){
            if(columnsSubQuery.charAt(i) == '(' || columnsSubQuery.charAt(i) == '.'){
                startIndex = i+1;
            }
            if(columnsSubQuery.charAt(i+1) == ')'){
                stringsQuery.add(columnsSubQuery.substring(startIndex, i+1).trim());
                startIndex = i;
            }
        }
        columnsSubQuery = new StringBuilder();
        for(String s: stringsQuery){
            columnsSubQuery.append(" " + s);
        }

        return columnsSubQuery.toString();
    }
}
