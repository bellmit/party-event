<div class="ssair_notice_table">
<ul class="snotice_ul">
{{ each list as item }}
  <li class="fnt14" style="{{$doFunc tableColumn.rowStyler item $index}}">
   	<i class="fl">»</i>
   	{{ each tableColumn.columns as column }}
   		<span class="{{ column.className }}" style="{{ column.style }}" title="{{$outputContentForTitle item column}}">
   		{{$outputContent item column currentObj}}
   		</span>
   	{{ /each }}
  </li>
{{ /each }}
</ul>
</div>