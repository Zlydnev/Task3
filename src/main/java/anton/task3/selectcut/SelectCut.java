package anton.task3.selectcut;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Класс который удаляет из запроса select и from
//
// select distinct (select AVG(p.address) from dperson_dbt p where p.personid = t.t_partyid), t.t_partyid, t2.t_name1, t.t_shortname
//        from dparty_dbt t join  dpersn_dbt t2 on t2.t_personid = t.t_partyid where t.t_partyid = 77
//
// Возвращает: (select AVG(p.address) from dperson_dbt p where p.personid = t.t_partyid), t.t_partyid, t2.t_name1, t.t_shortname


@Component
public class SelectCut {
    public String cutSelectQuery(String sql){

        int counter = 0;
        Pattern patternForSelect = Pattern.compile("select distinct|select");
        Pattern patternForFrom = Pattern.compile("from ");
        Matcher selectMatcher = patternForSelect.matcher(sql);
        Matcher fromMatcher = patternForFrom.matcher(sql);
        String subSql;
        int firstSelectIndex = 0;
        if(selectMatcher.find()){
            firstSelectIndex = selectMatcher.end();
            counter++;
        }
        while (selectMatcher.find()){
            counter++;
        }
        while (fromMatcher.find()){
            counter--;
            if(counter==0){
                counter = fromMatcher.start();
            }
        }
        subSql = sql.substring(firstSelectIndex, counter);

        return subSql;
    }
}
