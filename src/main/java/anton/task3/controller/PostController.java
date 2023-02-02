package anton.task3.controller;

import anton.task3.search.GetColumns;
import anton.task3.search.SubstringsWithoutAliases;
import anton.task3.selectcut.SelectCut;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Принцип работы алгоритма :
// 1. Убрать select, from и всё что после from
// 2. Найти построки и функции у которых нет alias
// 3. Получить значения в кобках
// 4. Заменить скобки на значения в основной строке
// 5. Найти подстроку с alias и исключить её из строки
// 6. Разделить скобки по запятой
// 7. Запустить цикл работающий с конца.


@RestController()
@RequestMapping(value = "/api")
@Api(value = "String", description = "Контроллер для парсинга sql-запроса")
public class PostController {
    private final SelectCut selectCut;
    private final SubstringsWithoutAliases withoutAliases;
    private final GetColumns getColumns;
    private final ResultColumns resultColumns;

    @Autowired
    public PostController(SelectCut selectCut, SubstringsWithoutAliases withoutAliases, GetColumns getColumns, ResultColumns resultColumns) {
        this.selectCut = selectCut;
        this.withoutAliases = withoutAliases;
        this.getColumns = getColumns;
        this.resultColumns = resultColumns;
    }

    @PostMapping("/sql")
    @ApiOperation(value = "Метод для парсинга строки", response = String.class)
    public String[] getSQLColumns(@RequestBody @ApiParam(value = "SQL запрос") String sql){
        sql = selectCut.cutSelectQuery(sql);
        String[] stringsWithoutAliases = withoutAliases.getSubstrWithoutAliases(sql, true);
        String[] stringsWithAliases = withoutAliases.getSubstrWithoutAliases(sql, false);

        //  Циклы получают результат без скобок
        //  и заменяют выражение со скобками на результат
        for(String s : stringsWithoutAliases){
            if(s != null) {
                String column = getColumns.getColumn(s);
                sql = sql.replace(s,column);
            }
        }
        for(String s: stringsWithAliases){
            if(s != null){
                sql = sql.replace(s,"");
            }
        }
        String[] result = resultColumns.getResultColumns(sql);

        return result;
    }

}
