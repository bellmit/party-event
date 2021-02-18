<table class="zeus-table">
<tr class="table_th">
 {{if tableColumn.rownumbers===true}}<th width="65">{{ tableColumn.rownumberslabel }}</th>{{/if}}
 {{ each tableColumn.columns as column }}
    <th field="{{ column.field }}"
        class="{{ column.className }} {{ column.sortType }} {{if $index+1==tableColumn.columns.length}}bg_while_last{{/if}}"
        {{if column.width!=null}}width="{{ column.width }}"{{/if}}
    >{{$outputLabel column}}<span></span></th>
 {{ /each }}
 </tr>

 {{ each list as item idx}}
    <tr style="{{$doFunc tableColumn.rowStyler item idx}}" class="{{if ((idx%2) != 0)}}even-tr{{/if}}">
    {{if tableColumn.rownumbers===true}}<td class="td_common td_color_dark">{{$doFunc tableColumn.rownumbersTrans idx+startRowNum+1}}</td>{{/if}}
    {{ each tableColumn.columns as column }}
        {{if column.cz_tip}}
        <td  class="td_common {{if $index%2==(tableColumn.rownumbers===true?0:1)}}td_color_light{{else}}td_color_dark{{/if}}" style="{{ column.style }}">
            <span class="td_val"  overflowview="{{column.overflowview}}">{{$outputContent item column currentObj idx}}</span>
        </td>
        {{else}}
        <td  class="td_common {{if $index%2==(tableColumn.rownumbers===true?0:1)}}td_color_light{{else}}td_color_dark{{/if}}" style="{{ column.style }}" title="{{$outputContentForTitle item column currentObj idx}}">
            <span class="td_val"  overflowview="{{column.overflowview}}">{{$outputContent item column currentObj idx}}</span>
        </td>
        {{/if}}
     {{ /each }}
      </tr>
 {{ /each }}
</table>