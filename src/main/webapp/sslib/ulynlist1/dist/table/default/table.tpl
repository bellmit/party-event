<table class="listpackage">
<tr class="listpackage_first_tr">
 {{if tableColumn.rownumbers===true}}<th width="40">{{ tableColumn.rownumberslabel }}</th>{{/if}}
 {{ each tableColumn.columns as column }}
    <th field="{{ column.field }}"
        class="{{ column.className }} {{ column.sortType }}"
        {{if column.width!=null}}width="{{ column.width }}"{{/if}}
    >
         <span>
         {{$outputLabel column}}
         </span>
    </th>
 {{ /each }}
 </tr>

 {{ each list as item idx}}
    <tr style="{{$doFunc tableColumn.rowStyler item idx}}">
    {{if tableColumn.rownumbers===true}}<td>{{if item!=null}}{{$doFunc tableColumn.rownumbersTrans idx+startRowNum+1}}{{/if}}</td>{{/if}}
    {{ each tableColumn.columns as column }}
        <td style="{{ column.style }}" title="{{$outputContentForTitle item column currentObj idx}}">
        {{$outputContent item column currentObj idx}}
        </td>
     {{ /each }}
      </tr>
 {{ /each }}
</table>