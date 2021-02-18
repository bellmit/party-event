<table class="ulynlist_flat_table">
<tr class="table_th">
 {{if tableColumn.rownumbers===true}}<th width="40">{{ tableColumn.rownumberslabel }}</th>{{/if}}
 {{ each tableColumn.columns as column }}
    <th field="{{ column.field }}"
        class="{{ column.className }} {{ column.sortType }} {{if $index+1==tableColumn.columns.length}}bg_while_last{{/if}}"
        {{if column.width!=null}}width="{{ column.width }}"{{/if}}
    >
         {{$outputLabel column}}
         <span></span>
    </th>
 {{ /each }}
 </tr>

 {{ each list as item }}
    <tr style="{{$doFunc tableColumn.rowStyler item $index}}">
    {{if tableColumn.rownumbers===true}}<td class="td_common td_color_dark">{{$index+startRowNum+1}}</td>{{/if}}
    {{ each tableColumn.columns as column }}
        <td  class="td_common {{if $index%2==(tableColumn.rownumbers===true?0:1)}}td_color_light{{else}}td_color_dark{{/if}}" style="{{ column.style }}" title="{{$outputContentForTitle item column}}">
        {{$outputContent item column currentObj}}
        </td>
     {{ /each }}
      </tr>
 {{ /each }}
</table>