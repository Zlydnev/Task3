package anton.task3.search;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
// Класс который ищет в строке индексы скобок и добавляет их в массив
// Возвращает массив индексов открытых скобок если isOpenBrackets = true
// Возвращает массив индексов закрытых скобок если isOpenBrackets = false

@Component
public class SearchBrackets {

    public List<Integer> getBrackets(String sql , boolean isOpenBrackets){
        Pattern pattern = Pattern.compile("\\(");
        Pattern pattern1 = Pattern.compile("\\)");
        Matcher matcher = pattern.matcher(sql);
        Matcher matcher1 = pattern1.matcher(sql);
        List<Integer> repeatedCloseBrackets = new ArrayList<>();
        List<Integer> repeatedOpenBrackets = new ArrayList<>();
        while (matcher.find()){
            repeatedOpenBrackets.add(matcher.start());
        }
        while (matcher1.find()){
            repeatedCloseBrackets.add(matcher1.start());
        }
        List<Integer> openBrackets = new ArrayList<>();
        List<Integer> closeBrackets = new ArrayList<>();

        for(int i = 0; i < repeatedCloseBrackets.size()-1; i++){
            if(repeatedOpenBrackets.get(i+1)<repeatedCloseBrackets.get(i)){
                openBrackets.add(repeatedOpenBrackets.get(i));
                closeBrackets.add(repeatedCloseBrackets.get(i+1));
                i++;
            }else{
                openBrackets.add(repeatedOpenBrackets.get(i));
                closeBrackets.add(repeatedCloseBrackets.get(i));
            }
        }

        if(isOpenBrackets){
            return openBrackets;
        }
        return closeBrackets;
    }
}
