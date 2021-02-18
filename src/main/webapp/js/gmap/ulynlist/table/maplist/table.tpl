<ul>
{{ each list as item }}
	<li class="gmap-list">
		<dl>
		{{ each tableColumn.columns as column }}
			{{if $index == 0}}
       		<dt><i class="icon"><img src="{{column.label}}/p{{$outputContent item column currentObj}}.png"></i>
       		{{else if $index == 1}}
       		<a href="#">{{$outputContent item column currentObj}}</a>
       		</dt>
       		{{else}}
       		<dd class="{{ column.className }}">{{$outputContent item column currentObj}}</dd>
       		{{/if}}
     {{ /each }}     		
         </dl>
    </li>

 {{ /each }}
                            
                            
</ul>
