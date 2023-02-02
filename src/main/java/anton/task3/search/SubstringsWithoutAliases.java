package anton.task3.search;

import org.springframework.stereotype.Component;

import java.util.List;


// Класс который проверяет есть или нет у подзапроса alias
// Возвращает массив подстрок без alias если withoutAlias = true
// Возвращает массив подстрок с alias если withoutAlias = false
// Например: (select AVG(p.address) as adr from dperson_dbt p where p.personid = t.t_partyid),
//           t.t_partyid as id, t2.t_name1, t.t_shortname as Name, concat(name, surname),
//           (select AVG(sum) from dperson_dbt p where p.personid = t.t_partyid) as avgSum
//
// Возвращает: if(withoutAlias){
//                  [(select AVG(p.address) as adr from dperson_dbt p where p.personid = t.t_partyid),
//                  (name, surname)]
//              }
//
//             [select AVG(sum) from dperson_dbt p where p.personid = t.t_partyid) as avgSum]



@Component
public class SubstringsWithoutAliases {
    SearchBrackets searchBrackets = new SearchBrackets();

    public String[] getSubstrWithoutAliases(String sql, boolean withoutAlias){

        sql = sql.trim();
        List<Integer> openBrackets = searchBrackets.getBrackets(sql,true);
        List<Integer> closeBrackets = searchBrackets.getBrackets(sql,false);

        String[] sabQueryWithoutAliases = new String[closeBrackets.size()];
        String[] sabQueryWithAliases = new String[closeBrackets.size()];
        int counterForWithout = 0;
        int counterForAliases = 0;

        for(int i = 0; i < closeBrackets.size(); i++){
            if(closeBrackets.get(i) == sql.length()-1){
                sabQueryWithoutAliases[counterForWithout] = sql.substring(openBrackets.get(i),
                        closeBrackets.get(i));
                break;
            }
            for(int j = closeBrackets.get(i); j < sql.length(); j++){

                if(sql.charAt(j) == 'a' & sql.charAt(j) != ' '){
                    sabQueryWithAliases[counterForAliases] = sql.substring(openBrackets.get(i),
                            closeBrackets.get(i)+1);
                    counterForAliases++;
                    break;
                }
                if(sql.charAt(j) == ',' & sql.charAt(j) != ' '){
                    sabQueryWithoutAliases[counterForWithout] = sql.substring(openBrackets.get(i),
                            closeBrackets.get(i)+1);
                    counterForWithout++;
                    break;
                }
            }
        }

        if(withoutAlias){
            return sabQueryWithoutAliases;
        }
        return  sabQueryWithAliases;
    }

}
